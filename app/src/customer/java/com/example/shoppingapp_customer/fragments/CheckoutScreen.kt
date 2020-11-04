package com.example.shoppingapp_customer.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.shoppingapp.R
import com.example.shoppingapp.util.Permissions
import com.example.shoppingapp.util.UpdateUI
import com.example.shoppingapp_customer.Map
import com.here.sdk.core.GeoCoordinates
import com.here.sdk.mapviewlite.MapViewLite

class CheckoutScreen : Fragment(), UpdateUI {
    private var txt_location_change: TextView? = null
    var imgLocate: ImageView? = null
    private var txt_address: TextView? = null
    var mapView: MapViewLite? = null
    var map: Map? = null
    var locationManager: LocationManager? = null
    var locationListener: LocationListener? = null
    var geoCoordinates: GeoCoordinates? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Map.setMapCredentials(context)
        return inflater.inflate(R.layout.fragment_checkout_screen, container, false)
    }

    fun BindView() {
        txt_location_change = view!!.findViewById(R.id.location_change_txt)
        imgLocate = view!!.findViewById(R.id.get_location_img)
        txt_address = view!!.findViewById(R.id.txt_address)
        mapView = view!!.findViewById(R.id.map)
    }

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        BindView()
        mapView!!.onCreate(savedInstanceState)
        map = Map(context, mapView, this)
        map!!.loadMapScene()
        imgLocate!!.setOnClickListener {
            requestPermission1()
            locationManager = context!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            locationManager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 500f, locationListener!!)
            locationManager!!.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 500f, locationListener!!)
        }
        locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                geoCoordinates = GeoCoordinates(location.latitude, location.longitude)
                map!!.getAddressForCoordinates(geoCoordinates)
            }

            override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
            override fun onProviderEnabled(provider: String) {}
            override fun onProviderDisabled(provider: String) {}
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Permissions.REQUEST_LOCATION.get) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(context, "All good", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun requestPermission1() {
        if (ContextCompat.checkSelfPermission(context!!,
                        Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context!!,
                        Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    Permissions.REQUEST_LOCATION.get)
            return
        }
    }

    override fun onResume() {
        super.onResume()
        mapView!!.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView!!.onDestroy()
    }

    override fun update(address: String) {
        var address = address
        address = address.replace(", ", ",\n")
        txt_address!!.text = address
    }
}