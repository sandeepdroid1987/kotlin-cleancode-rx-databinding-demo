package com.sg.findfood.model

import androidx.lifecycle.MutableLiveData
import com.sg.findfood.model.remote.Venue

/**
 * created by sandeep gupta on 17/3/19
 */

object SharedDataStore {

    var favouriteStateLiveData  : MutableLiveData<Boolean>? =  MutableLiveData()
    var selectedVenue : Venue? =null
}