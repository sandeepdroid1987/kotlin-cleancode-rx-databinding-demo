package com.sg.findfood.view.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.maps.CameraUpdateFactory
import com.sg.findfood.R
import com.sg.findfood.model.remote.Venue
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.sg.findfood.databinding.ActivityDetailBinding
import com.sg.findfood.view.utils.Constants
import com.sg.findfood.viewmodel.DetailViewModel
import kotlinx.android.synthetic.main.activity_detail.*



/**
 * created by sandeep gupta on 17/3/19
 */

class PlaceDetailActivity : AppCompatActivity(), OnMapReadyCallback {

    private val MAP_PADDING = 100;
    private var mMap: GoogleMap? = null
    private var venue: Venue? = null
    private var binding: ActivityDetailBinding? = null


    companion object {
        val EXTRAS_KEY_VENUE: String = "venue"

        fun launch(context: Context, venue: Venue) {
            var intent = Intent(context, PlaceDetailActivity::class.java)
            val bundle = Bundle()
            bundle.putParcelable(EXTRAS_KEY_VENUE, venue)
            intent.putExtras(bundle)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail)
        venue = intent.getParcelableExtra(EXTRAS_KEY_VENUE)
        val viewModel = ViewModelProviders.of(this, DetailViewModel.Factory())
            .get(DetailViewModel::class.java)
        viewModel.setData(venue!!)

        binding?.viewmodel = viewModel


        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

        viewModel.fabState?.observe(this,
            Observer<Boolean> { isFav ->
                if (isFav)
                    fab.setImageDrawable(
                        ContextCompat.getDrawable(this@PlaceDetailActivity, R.drawable.ic_baseline_favorite_24px)
                    )
                else
                    fab.setImageDrawable(
                        ContextCompat.getDrawable(this@PlaceDetailActivity, R.drawable.ic_baseline_favorite_border_24px)
                    )
            })
        viewModel.isFavourite(venue!!.id)


        fab.setOnClickListener { view ->
            viewModel.toggleFavourite(venue!!.id)

        }
    }

    /**
     * Called when the map is ready to add all markers and objects to the map.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap!!.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        addMarkers()
    }

    /**
     * Add Markers with default info windows to the map.
     */
    fun addMarkers() {
        venue?.location?.let {

            //configure size
            val placeLoc = LatLng(it.lat!!, it.lng!!)
            val builder = LatLngBounds.builder()
            builder.include(Constants.seattle)
            builder.include(placeLoc)

            //sourcemarker i.e seattle
            val sourceMarker = MarkerOptions()
                .position(Constants.seattle)
                .title("Seattle")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
            mMap!!.addMarker(sourceMarker)

            //destinationMarker
            val destMarker = MarkerOptions()
                .position(placeLoc)
                .title(venue!!.name)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
            mMap!!.addMarker(destMarker)

            //move camera, not animate
            val width = resources.displayMetrics.widthPixels
            val height = resources.displayMetrics.heightPixels/2
            val padding = (width * 0.12).toInt() // offset from edges of the map 12% of screen
            val cu = CameraUpdateFactory.newLatLngBounds(builder.build(), width, height,padding)
            mMap!!.moveCamera(cu)
        }
    }

}