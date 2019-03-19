package com.sg.findfood.view.map

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.model.*
import com.sg.findfood.R
import com.sg.findfood.model.remote.Venue
import com.sg.findfood.view.detail.PlaceDetailActivity
import kotlinx.android.synthetic.main.activity_main.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.*
import com.sg.findfood.model.SharedDataStore
import com.sg.findfood.view.utils.Constants

/**
 * created by sandeep gupta on 17/3/19
 */

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    var mapFragment: SupportMapFragment? = null

    var venues: List<Venue>? = null

    companion object {
        fun launch(context: Context, bundle: Bundle) {
            var intent = Intent(context, MapActivity::class.java).also { it.putExtras(bundle) }
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        setSupportActionBar(toolbar)

        mapFragment = supportFragmentManager.findFragmentById(R.id.g_map) as SupportMapFragment

        mapFragment?.getMapAsync(this);

        venues = intent.extras.getParcelableArrayList<Venue>("venues")
    }

    override fun onMapReady(googleMap: GoogleMap) {
        var mMap = googleMap

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        val builder = LatLngBounds.builder()

        addMarkersOnMap(mMap,builder)
        setInfoClick(mMap)
        var cameraUpdate  = getCameraUpdate(builder)
        mMap.moveCamera(cameraUpdate)
    }

    private fun addMarkersOnMap(mMap : GoogleMap, builder : LatLngBounds.Builder) {
        // include source location
        builder.include(Constants.seattle)
        mMap.addMarker(MarkerOptions()
            .position(Constants.seattle)
            .title("Seattle")
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)))
            .showInfoWindow();

        // include nearby places
        for (venue in venues!!) {
            venue?.let {
                val markerOptions = MarkerOptions()
                val latlng = LatLng(it.location?.lat!!, it.location?.lng!!);
                builder.include(latlng)
                markerOptions.position(latlng)
                    .title(venue.name)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                val m: Marker? = mMap?.addMarker(markerOptions)
                m?.tag = it
            }
        }
    }
    private fun setInfoClick(mMap : GoogleMap) {
        mMap!!.setOnInfoWindowClickListener( { marker ->
            marker.tag?.let{SharedDataStore.selectedVenue = it as Venue
            PlaceDetailActivity.launch(this@MapActivity, it) }})
    }

    private fun getCameraUpdate(builder : LatLngBounds.Builder) : CameraUpdate {
        val width = resources.displayMetrics.widthPixels
        val height = resources.displayMetrics.heightPixels
        val padding = (width * 0.12).toInt() // offset from edges of the map 12% of screen
        return CameraUpdateFactory.newLatLngBounds(builder.build(), width, height,padding)
    }

}