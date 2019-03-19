package com.sg.findfood.model.repository

import android.content.Context
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.sg.findfood.model.local.room.AppDatabase
import com.sg.findfood.model.remote.FourSquareService
import com.sg.findfood.model.local.room.FavouriteVenue
import com.sg.findfood.model.remote.SearchResponse
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * created by sandeep gupta on 17/3/19
 */

class FourSquareDataManagerImpl : FourSquareDataManager {


    private val HTTPS_API_FORSQUARE_URL = "https://api.foursquare.com/"
    private val CLIENT_ID = "EIKJ5ZQLWQ5MYATP3SZDSDKCCZBLKFUAEU2PSY3N4G3AZSTG";
    private val CLIENT_SECRET = "NDPE5TM2VJLZH1OX3V3U0WWCEGSHM5XWBKB1O35O4HBSYBNF"
    private val KEY_NEAR_PLACE = "Seattle,+WA"

    private var fourSquareService: FourSquareService? = null


    init {

        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        val builder = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)

        val client: OkHttpClient = builder.build()

        val retrofit = Retrofit.Builder()
            .baseUrl(HTTPS_API_FORSQUARE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

        fourSquareService = retrofit.create(FourSquareService::class.java)
    }

    companion object {
        private var dataManager: FourSquareDataManager? = null
        private var db: AppDatabase? = null
        @Synchronized
        @JvmStatic
        fun getInstance(): FourSquareDataManager {
            if (dataManager == null) {
                dataManager = FourSquareDataManagerImpl()
            }
            return dataManager!!
        }

        @JvmStatic
        fun initializeDb(appContext: Context) {

            db = AppDatabase.getAppDataBase(appContext)
        }
    }

    override fun getSearchResult(query: String): Observable<SearchResponse> {
        return fourSquareService!!.getNearPlaces(CLIENT_ID, CLIENT_SECRET, KEY_NEAR_PLACE, query)
    }

    override fun toggleFavourite(venue: String, isEnabled: Boolean): Int {
        var favVenue = FavouriteVenue(venue);
        if (isEnabled) {
            return db!!.favouriteVenueDao().deleteFavouriteVenue(venue)
        } else {
            db!!.favouriteVenueDao().insertFavourite(favVenue)
            return 1
        }
    }

    override fun isFavouriteVenue(venue: String): Int {
        return db!!.favouriteVenueDao().getFavourite(venue)
    }
}