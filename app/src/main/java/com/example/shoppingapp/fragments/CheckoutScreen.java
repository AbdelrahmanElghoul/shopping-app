package com.example.shoppingapp.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.shoppingapp.Map;
import com.example.shoppingapp.R;
import com.example.shoppingapp.util.UpdateUI;
import com.here.sdk.core.GeoCoordinates;
import com.here.sdk.mapviewlite.MapViewLite;

import butterknife.BindView;
import butterknife.ButterKnife;


import static android.content.Context.LOCATION_SERVICE;
import static androidx.core.content.ContextCompat.checkSelfPermission;
import static com.example.shoppingapp.fragments.Permissions.REQUEST_LOCATION;


public class CheckoutScreen extends Fragment implements UpdateUI {

    @BindView(R.id.location_change_txt)
    TextView txt_location_change;
    @BindView(R.id.get_location_img)
    ImageView imgLocate;
    @BindView(R.id.txt_address)
    TextView txt_address;
    @BindView(R.id.map)
    MapViewLite mapView;
    Map map;
    LocationManager locationManager;
    LocationListener locationListener;
   GeoCoordinates geoCoordinates;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Map.setMapCredentials(getContext());
        View v = inflater.inflate(R.layout.fragment_checkout_screen, container, false);
        ButterKnife.bind(this, v);
        return v;
    }


    @SuppressLint("MissingPermission")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView.onCreate(savedInstanceState);
        map = new Map(getContext(), mapView,this);
        map.loadMapScene();

        imgLocate.setOnClickListener(v -> {
            requestPermission1();
            locationManager = (LocationManager) getContext().getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 10000, 500, locationListener);
            locationManager.requestLocationUpdates(locationManager.NETWORK_PROVIDER, 10000, 500, locationListener);
        });

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                geoCoordinates =new GeoCoordinates(location.getLatitude(),location.getLongitude());
                map.getAddressForCoordinates(geoCoordinates);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch(requestCode){
            case REQUEST_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getContext(), "All good", Toast.LENGTH_SHORT).show();
                return;
            }
        }

    }

    private void requestPermission1(){
        if (checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(getContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    Permissions.REQUEST_LOCATION);

            return;
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void update(String address) {
        address =address.replace(", ",",\n");

        txt_address.setText(address);

    }
}

