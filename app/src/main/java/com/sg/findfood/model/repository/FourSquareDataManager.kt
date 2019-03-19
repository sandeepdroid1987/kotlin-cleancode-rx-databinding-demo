package com.sg.findfood.model.repository

import com.sg.findfood.model.remote.SearchResponse
import io.reactivex.Observable

/**
 * created by sandeep gupta on 17/3/19
 */

interface FourSquareDataManager {

    fun getSearchResult(query: String): Observable<SearchResponse>

    fun isFavouriteVenue(venue : String) : Int

    fun toggleFavourite(venue : String, isEnabled:Boolean) :   Int
}
