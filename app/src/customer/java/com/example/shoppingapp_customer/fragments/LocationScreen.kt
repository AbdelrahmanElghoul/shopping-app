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
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.shoppingapp.Cart
import com.example.shoppingapp.R
import com.example.shoppingapp.util.Firebase
import com.example.shoppingapp.util.MapNotifier
import com.example.shoppingapp.util.Permissions
import com.example.shoppingapp.util.UpdateUI
import com.example.shoppingapp_customer.util.Map
import com.here.sdk.core.GeoCoordinates
import com.here.sdk.gestures.TapListener
import com.here.sdk.search.Place
import kotlinx.android.synthetic.customer.checkout_dialog_layout.view.*
import kotlinx.android.synthetic.customer.fragment_location_screen.*
import timber.log.Timber.e


open class LocationScreen : Fragment(), UpdateUI, MapNotifier {
    lateinit var map: Map
    lateinit var adapter:ArrayAdapter<String>
    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LocationListener
    lateinit var geoCoordinates: GeoCoordinates
    var searchResult: List<Place?>? =null
    lateinit var listener:AdapterView.OnItemClickListener
    private val list= mutableListOf<String?>()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Map.setMapCredentials(requireContext())
        return inflater.inflate(R.layout.fragment_location_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        map_lsf.onCreate(savedInstanceState)
        map = Map(requireContext(), map_lsf, this)
        map.loadMapScene()

        adapter=ArrayAdapter<String>(requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                list)
        et_search_lsf.setAdapter(adapter)

        listener= AdapterView.OnItemClickListener { _, _, position, _ ->
            map.cameraToAddress(searchResult!![position]?.geoCoordinates!!)
            map.markLocation(searchResult!![position]?.geoCoordinates!!)
        }
        txt_proceed_lsf.setOnClickListener{
            proceed()
        }
        map_lsf.gestures.tapListener = TapListener {
                val geoCoordinates = map_lsf.camera.viewToGeoCoordinates(it)
                map.markLocation(geoCoordinates)
        }
        et_search_lsf.onItemClickListener=listener

        img_locate_lsf.setOnClickListener {
//            requestPermission1()
            locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager

            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        Permissions.REQUEST_LOCATION.get)
            }else
                getLocation()
        }
        locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                geoCoordinates = GeoCoordinates(location.latitude, location.longitude)
                map.getCurrentLocation(geoCoordinates)
            }

            override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
            override fun onProviderEnabled(provider: String) {}
            override fun onProviderDisabled(provider: String) {}
        }
        img_back_lsf.setOnClickListener {
            requireActivity().onBackPressed()
        }
        img_search_lsf.setOnClickListener {
            val geo=GeoCoordinates(25.5870306, 30.3655002)
            val text=et_search_lsf.text.toString()
            if(text.isBlank()) return@setOnClickListener
            map.searchLocation(text, geo)
        }

    }

    private fun proceed(){
        if(et_search_lsf.text.isNullOrBlank()) return
        purchaseDialog()
    }
    private fun purchaseDialog() {

        val factory = LayoutInflater.from(requireContext())
        val alertDialog = AlertDialog.Builder(requireContext(),R.style.CustomDialogTheme)
        val layoutView: View = factory.inflate(R.layout.checkout_dialog_layout, null)
        val btnConfirm = layoutView.txt_confirm_ckdl
        val btnCancel = layoutView.txt_cancel_ckdl

        val txtAddress = layoutView.txt_address_ckdl
        val txtPrice = layoutView.txt_total_price_ckdl
        val txtCount = layoutView.txt_item_count_ckdl

        alertDialog.setView(layoutView)
                .setCancelable(false)
        val dialog = alertDialog.create()

        txtAddress.text=et_search_lsf.text.toString()
        txtCount.text= Cart.count.toString()
        txtPrice.text= Cart.total.toString()

        btnConfirm.setOnClickListener {
            map_lsf.visibility=View.INVISIBLE
           Firebase.makePurchase(this, et_search_lsf.text.toString(),R.id.action_locationScreen_to_homeScreen)
            dialog.dismiss()

        }
        btnCancel.setOnClickListener { dialog.dismiss() }
        dialog.show()

}


@SuppressLint("MissingPermission")
    fun getLocation(){
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                10000,
                500f,
                locationListener)

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                10000,
                500f,
                locationListener)

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        e("request permission")
        if ((requestCode == Permissions.REQUEST_LOCATION.get)) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                e("permission granted")
                getLocation()
            }
            else
                e("permission denied")
        }
    }

    override fun update(text: String?) {
        et_search_lsf.setText(text)
    }

    override fun notifyChange(places: List<Place?>?) {
        searchResult=places
        list.clear()
        searchResult?.forEach{ it ->
            list.add(it?.title)
        }
        adapter=ArrayAdapter<String>(requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                list)

        et_search_lsf.setAdapter(adapter)
        adapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        map_lsf.onDestroy()
    }


}