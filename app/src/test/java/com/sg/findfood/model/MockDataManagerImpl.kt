package com.sg.findfood.model

import com.sg.findfood.model.remote.*
import com.sg.findfood.model.repository.FourSquareDataManager
import io.reactivex.Observable

class MockDataManagerImpl : FourSquareDataManager {

    private var mockedResponse : SearchDataResponse? =null
    private var mocFavSavedId = -1
    override fun toggleFavourite(venue: String, isEnabled: Boolean): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isFavouriteVenue(venue: String): Int {
        return mocFavSavedId;
      }

    override fun getSearchResult(query: String): Observable<SearchResponse> {
        if(mockedResponse!=null ){
             val response = SearchResponse(mockedResponse)
            return Observable.just(response)
        } else {
            return Observable.just(SearchResponse(null))
        }
    }

    fun setResponseForSuccess()
    {
        var category1 = Category("category", true, Icon("",".png"), "www.homeaway.com")
        var categories = ArrayList<Category>()
        categories.add(category1)

        var venue1  = Venue("1","name",Location("dummy address, seattle," , 44.5555,56.666), categories,false)

        var venueList = ArrayList<Venue>()
        venueList.add(venue1)
        this.mockedResponse = SearchDataResponse(venueList);
    }

    fun setResponseForFailure() {
       this.mockedResponse = null
    }

    fun setFavAsSaved() {
        mocFavSavedId =1
    }

    fun setFavAsNotSaved()
    {
        mocFavSavedId = -1
    }

}