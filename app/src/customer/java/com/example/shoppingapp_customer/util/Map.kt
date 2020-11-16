package com.example.shoppingapp_customer.util

import android.content.Context
import android.graphics.PorterDuff
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.shoppingapp.R
import com.example.shoppingapp.util.MapNotifier
import com.example.shoppingapp.util.UpdateUI
import com.here.sdk.core.GeoCoordinates
import com.here.sdk.core.LanguageCode
import com.here.sdk.core.engine.SDKNativeEngine
import com.here.sdk.core.engine.SDKOptions
import com.here.sdk.core.errors.InstantiationErrorException
import com.here.sdk.engine.InitProvider
import com.here.sdk.mapviewlite.*
import com.here.sdk.search.*
import timber.log.Timber.*

class Map(private val context: Context, private val mapView: MapViewLite, fragment: Fragment) {
    private lateinit var searchEngine: SearchEngine
    var reverseGeocodingOptions: SearchOptions? = null
    var mapOverlay: MapOverlay<View>? = null
    var updateUI: UpdateUI = fragment as UpdateUI
    private var mapNotifier = fragment as MapNotifier
    var zoomlvl = 14.0

    fun loadMapScene() {
        try {
            searchEngine = SearchEngine()
        } catch (e: InstantiationErrorException) {
            e(e)
        }
        mapView.setWatermarkPosition(WatermarkPlacement.BOTTOM_CENTER, 0)
        mapView.mapScene.loadScene(MapStyle.NORMAL_DAY) {
            if (it == null) {
               val geo=GeoCoordinates(25.5870306, 30.3655002)
                mapView.camera.target = GeoCoordinates(52.530932, 13.384915)
                mapView.camera.zoomLevel = zoomlvl
            } else {
                e("Loading map failed: mapError:  $it")
            }
        }
    }

    fun searchLocation(queryString: String, geoCoordinates: GeoCoordinates){
        val tag="Search location"
        val query = TextQuery(queryString, geoCoordinates)
        val maxItems = 100
        val options = SearchOptions(LanguageCode.AR_SA, maxItems)
        var error=false
        searchEngine.search(query, options) { searchError: SearchError?, list: List<Place>? ->
            if (searchError != null) {
                tag(tag).e("$searchError")
                error=true
                return@search
            }
            if(list==null) error=true
            tag("$tag list size").d("Size: ${list?.size}")
            if(!error)
                mapNotifier.notifyChange(list)
        }
    }
    fun getCurrentLocation(geoCoordinates: GeoCoordinates) {
        e("geoX ${geoCoordinates.latitude} geoY ${geoCoordinates.longitude}")
        if (mapOverlay != null) mapView.removeMapOverlay(mapOverlay!!)

        d(geoCoordinates.toString())
        val maxItems = 1
        reverseGeocodingOptions = SearchOptions(LanguageCode.AR_SA, maxItems)
        searchEngine.search(geoCoordinates, reverseGeocodingOptions!!) { searchError: SearchError?, list: List<Place>? ->
            if (searchError != null) {
                Toast.makeText(context, searchError.toString(), Toast.LENGTH_SHORT).show()
                return@search
            }

            val imageView = ImageView(context)
            imageView.setColorFilter(context.getColor(R.color.btn_color), PorterDuff.Mode.SRC_ATOP)
            imageView.setImageResource(R.drawable.locate_img)
            mapOverlay = MapOverlay(imageView, geoCoordinates)
            mapView.addMapOverlay(mapOverlay!!)

//           CameraAnimator cameraAnimator = new CameraAnimator(mapView.getCamera());
//            cameraAnimator.setTimeInterpolator(new AccelerateDecelerateInterpolator());
//            cameraAnimator.moveTo(geoCoordinates, 14);

            mapView.camera.target = geoCoordinates

            // If error is null, list is guaranteed to be not empty.
            updateUI.update(list!![0].address.addressText)
            tag("Reverse address").d(list[0].address.addressText)
        }
    }
    fun cameraToAddress(geoCoordinates: GeoCoordinates) {
        if (mapOverlay != null) mapView.removeAllViews()
        d(geoCoordinates.toString())
        val maxItems = 1
        reverseGeocodingOptions = SearchOptions(LanguageCode.AR_SA, maxItems)
        searchEngine.search(geoCoordinates, reverseGeocodingOptions!!) { searchError: SearchError?, list: List<Place>? ->
            if (searchError != null) {
                Toast.makeText(context, searchError.toString(), Toast.LENGTH_SHORT).show()
                return@search
            }

            mapView.camera.target = geoCoordinates
            mapView.camera.zoomLevel = 15.0

            updateUI.update(list!![0].address.addressText)
            tag("Reverse address").d(list[0].address.addressText)
        }
    }

    fun markLocation(geoCoordinates: GeoCoordinates){
        if (mapOverlay != null) mapView.removeMapOverlay(mapOverlay!!)
        val imageView = ImageView(context)
        imageView.setColorFilter(context.getColor(R.color.btn_color), PorterDuff.Mode.SRC_ATOP)
        imageView.setImageResource(R.drawable.locate_img)
        mapOverlay = MapOverlay(imageView, geoCoordinates)
        mapView.addMapOverlay(mapOverlay!!)

        getCurrentLocation(geoCoordinates)
    }

    companion object {
        fun setMapCredentials(context: Context) {
            InitProvider.initialize(context)
            val sdkOptions = SDKOptions(
                    context.getString(R.string.ACCESS_KEY_ID),
                    context.getString(R.string.ACCESS_KEY_SECRET),
                    context.cacheDir.path)
            val sdkNativeEngine = SDKNativeEngine(sdkOptions)
            SDKNativeEngine.setSharedInstance(sdkNativeEngine)
        }
    }

}

