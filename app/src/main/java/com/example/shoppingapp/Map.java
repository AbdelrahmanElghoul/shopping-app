package com.example.shoppingapp;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.shoppingapp.util.UpdateUI;
import com.here.sdk.core.GeoCircle;
import com.here.sdk.core.GeoCoordinates;
import com.here.sdk.core.LanguageCode;
import com.here.sdk.core.engine.SDKNativeEngine;
import com.here.sdk.core.engine.SDKOptions;
import com.here.sdk.core.errors.InstantiationErrorException;
import com.here.sdk.engine.InitProvider;
import com.here.sdk.mapviewlite.MapCircle;
import com.here.sdk.mapviewlite.MapCircleStyle;
import com.here.sdk.mapviewlite.MapOverlay;
import com.here.sdk.mapviewlite.MapStyle;
import com.here.sdk.mapviewlite.MapViewLite;
import com.here.sdk.mapviewlite.PixelFormat;
import com.here.sdk.mapviewlite.WatermarkPlacement;
import com.here.sdk.search.AddressQuery;
import com.here.sdk.search.Place;
import com.here.sdk.search.SearchEngine;
import com.here.sdk.search.SearchOptions;

import timber.log.Timber;

public class Map {
    private Context context;
    private SearchEngine searchEngine;
    private MapViewLite mapView;
    SearchOptions reverseGeocodingOptions;
    MapOverlay<View> mapOverlay;
    UpdateUI updateUI;




    public Map(Context context, MapViewLite mapView, Fragment fragment) {
        this.context = context;
        this.mapView = mapView;
        updateUI=(UpdateUI) fragment;
    }


    public void loadMapScene() {
        try {
            searchEngine=new SearchEngine();
        } catch (InstantiationErrorException e) {
            Timber.e(e.getMessage());
        }
        mapView.setWatermarkPosition(WatermarkPlacement.BOTTOM_CENTER,0);
        // Load a scene from the SDK to render the map with a map style.
        mapView.getMapScene().loadScene(MapStyle.NORMAL_DAY, errorCode -> {
            if (errorCode == null) {
                mapView.getCamera().setTarget(new GeoCoordinates(52.530932, 13.384915));
                mapView.getCamera().setZoomLevel(14);
                Timber.d("loaded");
            } else {
                Timber.e("onLoadScene failed: " + errorCode.toString());
            }
        });
    }

    public static void setMapCredentials(Context context){
        InitProvider.initialize(context);
        SDKOptions sdkOptions = new SDKOptions(
                context.getString(R.string.ACCESS_KEY_ID),
                context.getString(R.string.ACCESS_KEY_SECRET),
                context.getCacheDir().getPath());

        SDKNativeEngine sdkNativeEngine = new SDKNativeEngine(sdkOptions);
        SDKNativeEngine.setSharedInstance(sdkNativeEngine);
    }

    private void SearchLocation(String queryString,GeoCoordinates geoCoordinates){
        AddressQuery query = new AddressQuery(queryString, geoCoordinates);

        int maxItems = 30;
        SearchOptions options = new SearchOptions(LanguageCode.DE_DE, maxItems);

        searchEngine.search(query, options, (searchError, list) -> {

            if (searchError != null) {
                Timber.d( "Error: " + searchError.toString());
                return;
            }

            for (Place geocodingResult : list) {
                //...
            }

            Timber.d("Size: " + list.size());
        });
    }

    private MapCircle createMapCircle(GeoCoordinates geoCoordinates) {
        float radiusInMeters = 10;
        GeoCircle geoCircle = new GeoCircle(geoCoordinates, radiusInMeters);
        MapCircleStyle mapCircleStyle = new MapCircleStyle();
        mapCircleStyle.setFillColor(0x00908AA0, PixelFormat.RGBA_8888);
        MapCircle mapCircle = new MapCircle(geoCircle, mapCircleStyle);

        return mapCircle;
    }

    public void getAddressForCoordinates(GeoCoordinates geoCoordinates) {
        if(mapOverlay!=null) mapView.removeMapOverlay(mapOverlay);

        Timber.d(geoCoordinates.toString());
        int maxItems = 1;
        reverseGeocodingOptions = new SearchOptions(LanguageCode.EN_GB, maxItems);

        searchEngine.search(geoCoordinates, reverseGeocodingOptions, (searchError, list) -> {
            if (searchError != null) {
                Toast.makeText(context, searchError.toString(), Toast.LENGTH_SHORT).show();

                return;
            }


//            MapCircle mapCircle = createMapCircle(geoCoordinates);
//            MapScene mapScene = mapView.getMapScene();
//            mapScene.addMapCircle(mapCircle);

            ImageView imageView=new ImageView(context);
            imageView.setImageResource(R.drawable.locate_img);
            mapOverlay= new MapOverlay<>(imageView, geoCoordinates);
            mapView.addMapOverlay(mapOverlay);

//           CameraAnimator cameraAnimator = new CameraAnimator(mapView.getCamera());
//            cameraAnimator.setTimeInterpolator(new AccelerateDecelerateInterpolator());
//
//            cameraAnimator.moveTo(geoCoordinates, 14);

            mapView.getCamera().setTarget(geoCoordinates);

            // If error is null, list is guaranteed to be not empty.

            updateUI.update(list.get(0).getAddress().addressText);
            Timber.tag("Reverse address").d(list.get(0).getAddress().addressText);
        });

    }


}

/*public class CameraAnimator{

    Camera camera;

    public CameraAnimator(Camera camera) {
        this.camera = camera;
    }

    public void moveTo(GeoCoordinates destination, double targetZoom) {
        CameraUpdate targetCameraUpdate = createTargetCameraUpdate(destination, targetZoom);
        createAnimation(targetCameraUpdate);
        startAnimation(targetCameraUpdate);
    }
    private CameraUpdate createTargetCameraUpdate(GeoCoordinates destination, double targetZoom) {
        double targetTilt = 0;

        // Take the shorter bearing difference.
        double targetBearing = camera.getBearing() > 180 ? 360 : 0;

        return new CameraUpdate(targetTilt, targetBearing, targetZoom, destination);
    }

    private void createAnimation(CameraUpdate cameraUpdate) {
        valueAnimatorList.clear();

        // Interpolate current values for zoom, tilt, bearing, lat/lon to the desired new values.
        ValueAnimator zoomValueAnimator = createAnimator(camera.getZoomLevel(), cameraUpdate.zoomLevel);
        ValueAnimator tiltValueAnimator = createAnimator(camera.getTilt(), cameraUpdate.tilt);
        ValueAnimator bearingValueAnimator = createAnimator(camera.getBearing(), cameraUpdate.bearing);
        ValueAnimator latitudeValueAnimator = createAnimator(
                camera.getTarget().latitude, cameraUpdate.target.latitude);
        ValueAnimator longitudeValueAnimator = createAnimator(
                camera.getTarget().longitude, cameraUpdate.target.longitude);

        valueAnimatorList.add(zoomValueAnimator);
        valueAnimatorList.add(tiltValueAnimator);
        valueAnimatorList.add(bearingValueAnimator);
        valueAnimatorList.add(latitudeValueAnimator);
        valueAnimatorList.add(longitudeValueAnimator);

        // Update all values together.
        zoomValueAnimator.addUpdateListener(animation -> {
            float zoom = (float) zoomValueAnimator.getAnimatedValue();
            float tilt = (float) tiltValueAnimator.getAnimatedValue();
            float bearing = (float) bearingValueAnimator.getAnimatedValue();
            float latitude = (float) latitudeValueAnimator.getAnimatedValue();
            float longitude = (float) longitudeValueAnimator.getAnimatedValue();

            GeoCoordinates intermediateGeoCoordinates = new GeoCoordinates(latitude, longitude);
            camera.updateCamera(new CameraUpdate(tilt, bearing, zoom, intermediateGeoCoordinates));
        });
    }

    private ValueAnimator createAnimator(double from, double to) {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat((float) from, (float )to);
        if (timeInterpolator != null) {
            valueAnimator.setInterpolator(timeInterpolator);
        }
        return valueAnimator;
    }

    private void startAnimation(CameraUpdate cameraUpdate) {
        if (animatorSet != null) {
            animatorSet.cancel();
        }

        animatorSet = new AnimatorSet();
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                camera.updateCamera(cameraUpdate);
            }
        });

        animatorSet.playTogether(valueAnimatorList);
        animatorSet.setDuration(animationDurationInMillis);
        animatorSet.start();
    }
}*/
