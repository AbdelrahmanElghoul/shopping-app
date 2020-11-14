package com.example.shoppingapp_customer.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.text.Editable
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
import com.example.shoppingapp_customer.util.Map
import com.here.sdk.core.GeoCoordinates
import com.here.sdk.mapviewlite.MapViewLite
import kotlinx.android.synthetic.customer.fragment_location_screen.*

class LocationScreen : Fragment(), UpdateUI {

    var imgLocate: ImageView? = null
    var mapView: MapViewLite? = null
    var map: Map? = null
    var locationManager: LocationManager? = null
    var locationListener: LocationListener? = null
    var geoCoordinates: GeoCoordinates? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Map.setMapCredentials(context)
        return inflater.inflate(R.layout.fragment_location_screen, container, false)
    }

    fun BindView() {
//        txt_location_change = view!!.findViewById(R.id.location_change_txt)
        imgLocate = requireView().findViewById(R.id.get_location_img)
        mapView = requireView().findViewById(R.id.map_lsf)
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
            locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
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
        img_back_lsf.setOnClickListener {
            requireActivity().onBackPressed()
        }

//        txt_skip_cksf.setOnClickListener {  it.findNavController().navigate(R.id.action_locationScreen_to_shippingFragment) }
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
        if (ContextCompat.checkSelfPermission(requireContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(requireContext(),
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
        et_search_lsf.setText(address)
    }
}