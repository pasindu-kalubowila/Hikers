package com.example.hikers.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.hikers.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class MapsActivity extends AppCompatActivity {
    public static GoogleMap map;

    Marker marker, companyMan;
    private Button branches, track;
    private FirebaseDatabase database;
    private Double lat;
    private Double lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        database = FirebaseDatabase.getInstance();


        SupportMapFragment fragment = new SupportMapFragment();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.map, fragment, "MyMap");
        transaction.commit();

        fragment.getMapAsync(new OnMapReadyCallback() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;
                map.setPadding(0, 0, 0, 100);
                LatLng location = new LatLng(6.896963, 79.860336);
                final CameraPosition.Builder builder = new CameraPosition.Builder();
                builder.target(location);
                builder.zoom(12);
                CameraPosition cameraPosition = builder.build();
                CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
                map.animateCamera(cameraUpdate);

                final MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(location);
                markerOptions.title("Our Main Branch Colombo");
                markerOptions.draggable(false);
                markerOptions.flat(false);
                // BitmapDescriptor makericon = BitmapDescriptorFactory.fromResource(.);
                // markerOptions.icon(makericon);
                marker = map.addMarker(markerOptions);
                marker.showInfoWindow();

                MapStyleOptions style = MapStyleOptions.loadRawResourceStyle(getApplicationContext(), R.raw.map);
                map.setMapStyle(style);
                FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(getApplicationContext());


                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    String permissionArray[] = new String[1];
                    permissionArray[0] = Manifest.permission.ACCESS_FINE_LOCATION;//array ekata one permission tika add kargannawa

                    requestPermissions(permissionArray, 200);
                } else {
              //      client.requestLocationUpdates(locationRequest, callback, Looper.myLooper());
                    map.setMyLocationEnabled(true);
                }
                UiSettings uiSettings = map.getUiSettings();
                uiSettings.setMyLocationButtonEnabled(true);
                uiSettings.setZoomControlsEnabled(true);
                uiSettings.setCompassEnabled(true);
                uiSettings.setMapToolbarEnabled(true);


                final LocationRequest locationRequest = new LocationRequest();
                locationRequest.setInterval(1000);
                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

                final LocationCallback callback = new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);

                    }
                };



    }
});
    }}