package com.sg.findfood.model.remote

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * created by sandeep gupta on 17/3/19
 */

interface FourSquareService {

        @GET("/v2/venues/search?v=20180401&limit=20")
        fun getNearPlaces(@Query("client_id") client_id: String,
                           @Query("client_secret") client_secret: String,
                           @Query("near") near: String,
                           @Query("query") query: String): Observable<SearchResponse>

    }

