package com.sg.findfood.view.main

import android.os.Bundle
import android.os.Parcelable
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.GooglePlayServicesUtil
import com.sg.findfood.R
import com.sg.findfood.databinding.ActivityMainBinding
import com.sg.findfood.model.SharedDataStore
import com.sg.findfood.model.repository.FourSquareDataManagerImpl
import com.sg.findfood.model.remote.Venue
import com.sg.findfood.view.map.MapActivity
import com.sg.findfood.view.detail.PlaceDetailActivity
import com.sg.findfood.view.utils.Constants
import com.sg.findfood.viewmodel.MainViewModel
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.util.ArrayList
import java.util.concurrent.TimeUnit

/**
 * created by sandeep gupta on 17/3/19
 */

class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setSupportActionBar(toolbar)
        FourSquareDataManagerImpl.Companion.initializeDb(this)
        configureViewModel()
        setObserverForTextChange()

        //check for google services for map
        fab.setOnClickListener { view ->
            if (hasGoogleServices()) {
                val bundle = Bundle().also {
                    it.putParcelableArrayList(
                        Constants.EXTRA_VENUES,
                        viewModel.adapter.venues as ArrayList<out Parcelable>
                    )
                }
                MapActivity.launch(this, bundle)
            } else {
                showError()
            }
        }
        setObserverForSelectedVenue()
        setObserverForFavourite()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setObserverForTextChange() {
        Observable.create(ObservableOnSubscribe<String> { subscriber ->

            auto_search.addTextChangedListener(object : TextWatcher {

                override fun afterTextChanged(editable: Editable) = subscriber.onNext(editable.toString())
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })
        })
            .debounce(250, TimeUnit.MILLISECONDS)
            .distinct()
            .filter { text -> text.isNotBlank() }
            .subscribe { text ->
                viewModel.searchPlaces(text)
            }
    }

    private fun setObserverForSelectedVenue() {
        //check for google services for map
        viewModel.selected?.observe(
            this,
            Observer<Venue> { venue ->
                if (hasGoogleServices()) {
                    PlaceDetailActivity.launch(this@MainActivity, venue)
                } else {
                    showError()
                }
                SharedDataStore.selectedVenue = venue
            })
    }

    private fun setObserverForFavourite() {
        SharedDataStore.favouriteStateLiveData!!.observe(this, Observer { isFav -> viewModel.updateFavState(isFav) })
    }

    private fun configureViewModel() {
        val factory = MainViewModel.Factory()
        viewModel = ViewModelProviders.of(this, factory)
            .get(MainViewModel::class.java)
        binding?.viewmodel = viewModel
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel?.clear()
    }

    fun hasGoogleServices(): Boolean {
        return GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this) == ConnectionResult.SUCCESS;

    }

    fun showError() {
        Toast.makeText(getApplicationContext(), "This device is not supported", Toast.LENGTH_LONG).show();
    }
}
