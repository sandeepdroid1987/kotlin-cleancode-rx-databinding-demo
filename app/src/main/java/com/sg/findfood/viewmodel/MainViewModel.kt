package com.sg.findfood.viewmodel

import android.view.View
import androidx.databinding.ObservableInt
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sg.findfood.model.repository.FourSquareDataManager
import com.sg.findfood.model.repository.FourSquareDataManagerImpl
import com.sg.findfood.model.remote.SearchResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

import androidx.lifecycle.MutableLiveData
import com.sg.findfood.R
import com.sg.findfood.model.SharedDataStore
import com.sg.findfood.model.remote.Location
import com.sg.findfood.model.remote.Venue
import com.sg.findfood.view.main.adapter.SearchResultsAdapter
import com.sg.findfood.view.utils.Constants

/**
 * created by sandeep gupta on 17/3/19
 */

class MainViewModel(fourSquareDataManager: FourSquareDataManager) : ViewModel() {

    private var fourSquareDataManager: FourSquareDataManager = FourSquareDataManagerImpl.getInstance()
    private var disposable: CompositeDisposable = CompositeDisposable()


    var loading: ObservableInt? = null
    var showFab: ObservableInt? = null
    var resultCount: ObservableInt? = null
    var showList: ObservableInt? =null
    var selected: MutableLiveData<Venue>? = null
    var adapter: SearchResultsAdapter = SearchResultsAdapter(R.layout.list_item, this)


    // dependencies are passed in constructor, so that it can mocked in tests
    init {
        this.fourSquareDataManager = fourSquareDataManager
        selected = MutableLiveData<Venue>()
        loading = ObservableInt(View.GONE)
        resultCount = ObservableInt(Constants.STATE_BLANK)
        showFab = ObservableInt(View.GONE)
        showList = ObservableInt(View.GONE)
    }

    fun getVenueAdapter(): SearchResultsAdapter {
        return adapter;
    }

    fun onItemClick(index: Int) {
        adapter?.getVenueAt(index).let { selected?.value = it }
    }

    fun getVenueAt(index: Int): Venue? {
        return adapter?.getVenueAt(index)
    }

    fun searchPlaces(query: String) {
        loading?.set(View.VISIBLE)
        var subscription: Disposable = fourSquareDataManager.getSearchResult(query)
            .map { mapResponse(it) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ updateView(it) }, { handleError() }, {})

        disposable.add(subscription)
    }

    private fun updateView(response: SearchResponse) {

        response?.let {
            it.data?.let {
                it.venues?.let {
                    loading?.set(View.GONE)
                    resultCount?.set(it.size)
                    if (it.size > 0) {
                        showList?.set(View.VISIBLE)
                        showFab?.set(View.VISIBLE)
                    } else {
                        showList?.set(View.GONE)
                        showFab?.set(View.GONE)
                    }
                    adapter.setData(it)
                }
            }
        }
    }

    fun handleError() {
        loading?.set(View.GONE)
        resultCount?.set(Constants.STATE_ERROR)
        showFab?.set(View.GONE)
        showList?.set(View.GONE)

    }

    fun mapResponse(response: SearchResponse): SearchResponse {
        response?.let {
            it.data?.let {
                it.venues?.let {
                    it.forEach { it.favourite = fourSquareDataManager.isFavouriteVenue(it.id) > 0 }

                }
            }
        }
        return response
    }

    fun getDistance(loc: Location?): String {
        var results = FloatArray(1)
        loc?.let {
            android.location.Location.distanceBetween(
                it.lat!!,
                it.lng!!,
                Constants.seattle.latitude,
                Constants.seattle.longitude,
                results
            )
        }
        return String.format("%.2f Km", results[0] / 1000)
    }

    fun updateFavState(isFav: Boolean) {
        val pos = adapter.venues.indexOf(SharedDataStore.selectedVenue)
        adapter.venues.get(pos).favourite = isFav
        adapter.notifyItemChanged(pos)
    }

    fun clear() {
        if (!disposable.isDisposed())
            disposable.dispose()
    }

    class Factory : ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {

            return MainViewModel(FourSquareDataManagerImpl.getInstance()) as T
        }
    }

}
