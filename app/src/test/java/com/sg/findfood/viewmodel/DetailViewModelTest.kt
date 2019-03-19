package com.sg.findfood.viewmodel

import com.nhaarman.mockito_kotlin.any
import com.sg.findfood.model.MockDataManagerImpl
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import kotlin.test.assertEquals

class DetailViewModelTest {

    private val mockDataManagerImpl = MockDataManagerImpl()
    private var detailViewModel: DetailViewModel? =null
    private val VISIBLE = 8
    private val GONE = 0

    companion object {
        @BeforeClass
        @JvmStatic
        fun setupClass() {
            RxAndroidPlugins.setInitMainThreadSchedulerHandler { o -> Schedulers.trampoline() }
        }
    }

    @Before
    fun before() {
        detailViewModel = DetailViewModel(mockDataManagerImpl)
    }

    @Test
    fun whenFav_thenShowFabHighlighted(){
      mockDataManagerImpl.setFavAsSaved()
        detailViewModel!!.isFavourite(any())
        assertEquals(detailViewModel!!.fabState!!.value, true)
    }

    @Test
    fun whenNotFav_thenShowFabNormal(){
        mockDataManagerImpl.setFavAsNotSaved()
        detailViewModel!!.isFavourite(any())
        assertEquals(detailViewModel!!.fabState!!.value, false)
    }

}