package com.info.mymapple

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.common.api.Status
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener

class MainActivity : AppCompatActivity(), OnMapReadyCallback{

    private var mGoogleMap : GoogleMap? = null
    private lateinit var autoCompleteFragment : AutocompleteSupportFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Places.initialize(applicationContext, getString(R.string.google_map_api_key))
        autoCompleteFragment = supportFragmentManager.findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment

        autoCompleteFragment.setPlaceFields(listOf(Place.Field.ID, Place.Field.ADDRESS, Place.Field.LAT_LNG))
        autoCompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener{
            override fun onError(p0: Status) {
               Log.d("what is main issue", p0.toString());
               Toast.makeText(this@MainActivity, "Some error occured in search $p0", Toast.LENGTH_LONG).show()
            }

            override fun onPlaceSelected(place: Place) {
                val latlng = place.latLng!!
                zoomOnMap(latlng)
            }

        })

        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment

        mapFragment.getMapAsync(this)

        val mapOptionsButton : ImageButton = findViewById(R.id.mapmenu)
        val popupMenu = PopupMenu(this,mapOptionsButton)

        popupMenu.menuInflater.inflate(R.menu.map_options, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem ->
            changeMap(menuItem.itemId)
            true
        }

        mapOptionsButton.setOnClickListener {
            popupMenu.show()
        }
    }

    private fun zoomOnMap(latlng : LatLng){
       val newLatLngZoom = CameraUpdateFactory.newLatLngZoom(latlng,12f)
       mGoogleMap?.animateCamera(newLatLngZoom)
    }

    private fun changeMap(itemId: Int) {
       when(itemId)
       {
           R.id.normal_map -> mGoogleMap?.mapType = GoogleMap.MAP_TYPE_NORMAL
           R.id.hybrid_map -> mGoogleMap?.mapType = GoogleMap.MAP_TYPE_HYBRID
           R.id.satellite_map -> mGoogleMap?.mapType = GoogleMap.MAP_TYPE_SATELLITE
           R.id.terrain_map -> mGoogleMap?.mapType = GoogleMap.MAP_TYPE_TERRAIN
       }
    }

    override fun onMapReady(googleMap: GoogleMap) {
       mGoogleMap = googleMap
    }

//    private fun checkSelfLocationPermission() {
//        if (ContextCompat.checkSelfPermission(this@MainActivity, Manifest.permission.ACCESS_FINE_LOCATION) !== PackageManager.PERMISSION_GRANTED) {
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this@MainActivity, Manifest.permission.ACCESS_FINE_LOCATION)) {
//                ActivityCompat.requestPermissions(this@MainActivity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
//            } else {
//                ActivityCompat.requestPermissions(this@MainActivity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
//            }
//        }
//    }
}