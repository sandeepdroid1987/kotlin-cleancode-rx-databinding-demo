package com.sg.findfood.viewmodel


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sg.findfood.model.SharedDataStore
import com.sg.findfood.model.repository.FourSquareDataManager
import com.sg.findfood.model.repository.FourSquareDataManagerImpl
import com.sg.findfood.model.remote.Venue
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * created by sandeep gupta on 17/3/19
 */

class DetailViewModel(fourSquareDataManager: FourSquareDataManager) : ViewModel() {

    private var fourSquareDataManager: FourSquareDataManager? =null
    var venue : Venue? = null
    var fabState : MutableLiveData<Boolean> ? = null

    // dependencies are passed in constructor, so that it can mocked in tests
    init {
        this.fourSquareDataManager = fourSquareDataManager
        fabState = MutableLiveData()
    }

    fun setData(venue : Venue) {
        this.venue = venue
        fabState!!.value=false
    }

    fun isFavourite(venue_key : String) {
        Observable.fromCallable { fourSquareDataManager!!.isFavouriteVenue(venue_key) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({ fabState!!.value = it>0 }, {}, {})
    }

    fun toggleFavourite(venue_key : String) {
        Observable.fromCallable {  fourSquareDataManager!!.toggleFavourite(venue_key,fabState!!.value!!)}
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io()).
                subscribe( { id -> if(id >0) {fabState?.value = fabState?.value!!.not()
                    SharedDataStore.favouriteStateLiveData!!.value =fabState?.value} },
               {}, {})
    }

    class Factory : ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {

            return DetailViewModel(FourSquareDataManagerImpl.getInstance()) as T
        }
    }
}