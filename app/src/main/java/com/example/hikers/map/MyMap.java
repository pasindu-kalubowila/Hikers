package com.example.hikers.map;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.widget.Button;
import android.widget.Toast;

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
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class MyMap extends AppCompatActivity {
    public static GoogleMap map;
    private Button branches, animi;
    Marker marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.google_map);
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

                LatLng location = new LatLng(6.896963, 79.860336);
                final CameraPosition.Builder builder = new CameraPosition.Builder();
                builder.target(location);
                builder.zoom(15);
                CameraPosition cameraPosition = builder.build();
                CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
                map.animateCamera(cameraUpdate);

                final MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(location);
                markerOptions.title("Our main Branch");
                markerOptions.draggable(false);
                markerOptions.flat(false);
                BitmapDescriptor makericon = BitmapDescriptorFactory.fromResource(R.drawable.placeholder);
                markerOptions.icon(makericon);
                marker = map.addMarker(markerOptions);
                marker.showInfoWindow();

                MapStyleOptions style = MapStyleOptions.loadRawResourceStyle(getApplicationContext(), R.raw.map);
                map.setMapStyle(style);

                UiSettings uiSettings = map.getUiSettings();
                uiSettings.setZoomControlsEnabled(true);
                uiSettings.setCompassEnabled(true);
                uiSettings.setMapToolbarEnabled(true);


                LocationRequest locationRequest = new LocationRequest();
                locationRequest.setInterval(1000);
                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

                LocationCallback callback = new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        double l1 = locationResult.getLastLocation().getLatitude();
                        double l2 = locationResult.getLastLocation().getLongitude();

                        LatLng location2 = new LatLng(l1, l2);
                        MarkerOptions markerOptions2 = new MarkerOptions();
                        markerOptions2.position(location2);

                        markerOptions2.title("Here is you");
                        markerOptions2.draggable(false);
                        markerOptions2.flat(false);
                        BitmapDescriptor makericon = BitmapDescriptorFactory.fromResource(R.drawable.placeholder);
                        markerOptions2.icon(makericon);
                        if (marker != null) {
                            marker.remove();

                        }
                        marker = map.addMarker(markerOptions2);
                        marker.showInfoWindow();
                        //  Toast.makeText(MyMap.this, "HI", Toast.LENGTH_SHORT).show();
                        //   Toast.makeText(MyMap.this, l1 + " " + l2, Toast.LENGTH_SHORT).show();


                    }
                };

                FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(getApplicationContext());


                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    String permissionArray[] = new String[1];
                    permissionArray[0] = Manifest.permission.ACCESS_FINE_LOCATION;//array ekata one permission tika add kargannawa

                    requestPermissions(permissionArray, 200);
                } else {
                    client.requestLocationUpdates(locationRequest, callback, Looper.myLooper());
                    map.setMyLocationEnabled(true);
                }

                GoogleMap.OnMapLongClickListener listener = new GoogleMap.OnMapLongClickListener() {
                    @Override
                    public void onMapLongClick(final LatLng latLng) {
//                        if (marker != null) {
//                            marker.remove();
//
//                        }
                        MarkerOptions markerOptions2 = new MarkerOptions();
                        markerOptions2.position(latLng);
//                        marker = MyMap.addMarker(markerOptions2);
                        marker.showInfoWindow();
                        //Toast.makeText(MyMap.this, latLng.latitude + " " + latLng.longitude, Toast.LENGTH_SHORT).show();
                        map.addMarker(markerOptions2);

                        final PolylineOptions polylineOptions = new PolylineOptions();
                        polylineOptions.width(10);
                        polylineOptions.color(getColor(R.color.colorPrimaryDark));

                        polylineOptions.add(marker.getPosition());
                        polylineOptions.add(latLng);
                        map.addPolyline(polylineOptions);


                        List<Address> addresses = null;
                        try {
                            Geocoder geocoder = new Geocoder(MyMap.this);
                            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 5);
//                            for (Address address : addresses) {
//                                System.out.println(address.getAddressLine(0));
//                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        final String item[] = new String[addresses.size()];
                        int i = 0;
                        for (Address address : addresses) {
                            item[i] = address.getAddressLine(0);
                            i++;
                        }
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(MyMap.this);
                        builder1.setTitle("Nearby Location");
                        builder1.setIcon(R.drawable.placeholder);


                        DialogInterface.OnClickListener listener1 = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                Toast.makeText(MyMap.this, item[i], Toast.LENGTH_SHORT).show();
                            }
                        };
                        builder1.setSingleChoiceItems(item, 0, listener1);
                        builder1.show();
                        final String start = marker.getPosition().latitude + "," + marker.getPosition().longitude + "&destination=";
                        final String end = latLng.latitude + "," + latLng.longitude;

                        Thread t = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                if (checkSelfPermission(Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {

                                    String permissionArray[] = new String[1];
                                    permissionArray[0] = Manifest.permission.INTERNET;//array ekata one permission tika add kargannawa

                                    requestPermissions(permissionArray, 201);
                                } else {

                                    System.out.println("/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////");
//                                    Toast.makeText(MyMap.this, "internet", Toast.LENGTH_SHORT).show();
                                    HttpURLConnection con = null;

                                    try {

                                        System.out.println(start + " " + end);
                                        URL url = new URL("https://maps.googleapis.com/maps/api/directions/json?origin=" + start
                                                + "" + end + "&mode=driving&key=AIzaSyDBf4U_kcVHNbZkgm5lb2qLhHXrnEJSEus");
                                        con = (HttpURLConnection) url.openConnection();
                                        con.setRequestMethod("GET");
                                        con.setChunkedStreamingMode(0);//meken karanne request body eke tiyena characters gana hariyatama specify karana ekai meken karanne, get widiyata nisa '0' danne,post widiyata yawanawa nam yawaan characters gana hariyatama dena eken memory eka apathe yana eka adu karaganna puluwan

                                        if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {//HTTP_OK kiyanne responce eka success nam kiyana eka(200 code eka enne ok unama)
                                            InputStream is = con.getInputStream();

                                            String responceText = "";
                                            int data = 0;

                                            while ((data = is.read()) != -1) {
                                                responceText += (char) data;
                                            }
                                            System.out.println(responceText);

                                            String split1 = responceText.split("overview_polyline")[1];
                                            String split2 = split1.split("points\" : \"")[1];
                                            String split3 = split2.split("\"")[0];

                                            System.out.println("//////////////////////////////////////////////////////////////////////////////////////////////////////////////");

                                            final List<LatLng> decode = PolyUtil.decode(split3);

                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    PolylineOptions polylineOptions1 = new PolylineOptions();
                                                    polylineOptions1.color(getColor(R.color.colorPrimary));
                                                    polylineOptions1.width(5);
                                                    polylineOptions1.addAll(decode);
                                                    map.addPolyline(polylineOptions1);
                                                }
                                            });
                                        }

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    } finally {//try giyath catch ekata giyath finally ekata ena nisa mekedi connection eka close karana wede karanawa

                                        if (con != null) {
                                            con.disconnect();
                                        }
                                    }

                                }

                            }
                        });

                        t.start();
                    }
                };
                map.setOnMapLongClickListener(listener);

            }
        });


    }

}
