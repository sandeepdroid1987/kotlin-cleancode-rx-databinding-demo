package com.sg.findfood.viewmodel

import androidx.databinding.ObservableInt
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.sg.findfood.model.MockDataManagerImpl
import io.reactivex.subscribers.TestSubscriber
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsInstanceOf.any
import org.junit.Test
import org.junit.runner.RunWith
import io.reactivex.schedulers.Schedulers
import io.reactivex.android.plugins.RxAndroidPlugins
import org.junit.Before
import org.junit.BeforeClass
import org.mockito.Matchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Spy
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue


class MainViewModelTest {

    private val mockDataManagerImpl = MockDataManagerImpl()
    private var mainViewModel: MainViewModel = MainViewModel(mockDataManagerImpl)
    private val VISIBLE = 0
    private val GONE = 8

    companion object {
        @BeforeClass
        @JvmStatic
        fun setupClass() {
            RxAndroidPlugins.setInitMainThreadSchedulerHandler { o -> Schedulers.trampoline() }
        }
    }

    @Test
    fun whenSearchResult_thenShowListAndFab() {
        var search = "Coffee"
        mockDataManagerImpl.setResponseForSuccess()
        mainViewModel.searchPlaces(search)
        assertEquals(mainViewModel.showFab!!.get(), VISIBLE)
        assertEquals(mainViewModel.showList!!.get(), VISIBLE)
    }

    @Test
    fun whenSearchResultFailed_thenHideListAndFab() {
        var search = "Coffee"
        mockDataManagerImpl.setResponseForFailure()
        mainViewModel.searchPlaces(search)
        assertEquals(mainViewModel.showFab!!.get(), GONE)
        assertEquals(mainViewModel.showList!!.get(), GONE)
    }


}