package com.example.hikers.activities;

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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.hikers.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.PolyUtil;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class MapHicker extends AppCompatActivity {
    public static GoogleMap map;
    Marker marker, companyMan;
    HashMap<String, Marker> maps = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_hicker);
     final   SupportMapFragment fragment = new SupportMapFragment();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.map_hiker_frame, fragment, "MyMap");
        transaction.commit();


        final DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("groups");
        reference1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                final String groupName=dataSnapshot.getKey();

                reference1.child(dataSnapshot.getKey()).child("clients").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull final DataSnapshot dataSnapshot, @Nullable String s) {

                        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(dataSnapshot.getKey())){

                            System.out.println("Group Name   "+groupName);

                            Toast.makeText(MapHicker.this, ""+groupName, Toast.LENGTH_SHORT).show();


///asynch method eka



                            fragment.getMapAsync(new OnMapReadyCallback() {
                                @TargetApi(Build.VERSION_CODES.M)
                                @Override
                                public void onMapReady(GoogleMap googleMap) {


                                    map = googleMap;
                /*LatLng location = new LatLng(6.896963, 79.860336);
                final CameraPosition.Builder builder = new CameraPosition.Builder();
                builder.target(location);
                builder.zoom(12);
                CameraPosition cameraPosition = builder.build();
                CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
                MyMap.animateCamera(cameraUpdate);*/
/*
                final MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(location);
                markerOptions.title("You");
                markerOptions.draggable(false);
                markerOptions.flat(false);
                BitmapDescriptor makericon1 = BitmapDescriptorFactory.fromResource(R.drawable.amu_bubble_mask);
                markerOptions.icon(makericon1);
                marker = MyMap.addMarker(markerOptions);
                marker.showInfoWindow();*/

                                    MapStyleOptions style = MapStyleOptions.loadRawResourceStyle(getApplicationContext(), R.raw.map);
                                    map.setMapStyle(style);

                                    UiSettings uiSettings = map.getUiSettings();
                                    uiSettings.setZoomControlsEnabled(true);
                                    uiSettings.setCompassEnabled(true);
                                    uiSettings.setMapToolbarEnabled(true);


                                    LocationRequest locationRequest = new LocationRequest();
                                    locationRequest.setInterval(5000);
                                    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

                                    LocationCallback callback = new LocationCallback() {
                                        @Override
                                        public void onLocationResult(LocationResult locationResult) {
                       /* double l1 = locationResult.getLastLocation().getLatitude();
                        double l2 = locationResult.getLastLocation().getLongitude();

                        LatLng location2 = new LatLng(l1, l2);
                        MarkerOptions markerOptions2 = new MarkerOptions();
                        markerOptions2.position(location2);

                        markerOptions2.title("This is me");
                        markerOptions2.draggable(false);
                        markerOptions2.flat(false);
                       // BitmapDescriptor makericon = BitmapDescriptorFactory.fromResource(R.drawable.placeholder);
                       // markerOptions2.icon(makericon);
                        if (marker != null) {
                            marker.remove();

                        }
                        marker = MyMap.addMarker(markerOptions2);
                        marker.showInfoWindow();

                        Toast.makeText(MapHicker.this, l1 + " " + l2, Toast.LENGTH_SHORT).show();*/

                                            final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

                                            DatabaseReference refClient = FirebaseDatabase.getInstance().getReference("UserLocations");

                                            refClient.addChildEventListener(new ChildEventListener() {
                                                @Override
                                                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {


                                                    //  Toast.makeText(MapHicker.this, ""+dataSnapshot.getKey(), Toast.LENGTH_SHORT).show();
                                                    System.out.println(dataSnapshot.getKey());

                                                    Marker marker1 = maps.get(dataSnapshot.getKey().toString());
                                                    if (marker1 != null) {
                                                        marker1.remove();

                                                    }
                                                    map.clear();
                                                    double lon = Double.parseDouble(dataSnapshot.child("lon").getValue().toString());
                                                    double lan = Double.parseDouble(dataSnapshot.child("lat").getValue().toString());
                                                    LatLng location2Client = new LatLng(lan, lon);
                                                    final MarkerOptions markerOptions2Client = new MarkerOptions();
                                                    markerOptions2Client.position(location2Client);

                                                    DatabaseReference name = FirebaseDatabase.getInstance().getReference("Users").child(dataSnapshot.getKey()).child("username");
                                                    System.out.println("keyyyy  "+dataSnapshot.getKey());
final String key=dataSnapshot.getKey();



                                                    name.addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                         final   DatabaseReference reference = FirebaseDatabase.getInstance().getReference("groups").child(groupName).child("clients");
                                                            reference.addChildEventListener(new ChildEventListener() {
                                                                @Override
                                                                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                                                    System.out.println(key+"child group "+dataSnapshot.getKey());





                                                                    if (key.equals(dataSnapshot.getKey())){


                                                                        //Fullname eka
                                                                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(key);
                                                                        reference.child("fullname").addValueEventListener(new ValueEventListener() {
                                                                            @Override
                                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                                //   Toast.makeText(context, ""+dataSnapshot.getValue(), Toast.LENGTH_SHORT).show();
                                                                              String  fullName=dataSnapshot.getValue().toString();
                                                                                System.out.println(groupName+" Innawa "+fullName+"    "+key);

                                                                                markerOptions2Client.title(fullName);
                                                                                markerOptions2Client.draggable(true);
                                                                                markerOptions2Client.flat(false);
                                                                                BitmapDescriptor makericonClient = BitmapDescriptorFactory.fromResource(R.drawable.ic_action_name);
                                                                                markerOptions2Client.icon(makericonClient);
                                                                                Marker marker = map.addMarker(markerOptions2Client);
                                                                                marker.showInfoWindow();
                                                                                maps.put(dataSnapshot.getKey(), marker);

                                                                            }

                                                                            @Override
                                                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                            }
                                                                        });







                                                                    }else{
                                                                        Toast.makeText(MapHicker.this, "naaaaa", Toast.LENGTH_SHORT).show();

                                                                    }




                                                                }

                                                                @Override
                                                                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                                                                }

                                                                @Override
                                                                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                                                                }

                                                                @Override
                                                                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                                                                }

                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                }
                                                            });




                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                                        }
                                                    });
//


                                                }

                                                @Override
                                                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                                                }

                                                @Override
                                                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                                                }

                                                @Override
                                                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });


                                            String nameU = Objects.requireNonNull(dataSnapshot.getValue()).toString();

                                            System.out.println("name "+nameU);


                                            /*

                        refClient.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (companyMan != null) {
                                    companyMan.remove();

                                }

                                double lon = Double.parseDouble(dataSnapshot.child("lon").getValue().toString());
                                double lan =Double.parseDouble(dataSnapshot.child("lat").getValue().toString());
                                LatLng location2Client = new LatLng(lan,lon);
                                MarkerOptions markerOptions2Client = new MarkerOptions();
                                markerOptions2Client.position(location2Client);

                                markerOptions2Client.title(currentUser.toString()+"order");
                                markerOptions2Client.draggable(true);
                                markerOptions2Client.flat(false);
                                BitmapDescriptor makericonClient = BitmapDescriptorFactory.fromResource(R.drawable.ic_menu_send);
                                markerOptions2Client.icon(makericonClient);
                                companyMan = MyMap.addMarker(markerOptions2Client);
                                companyMan.showInfoWindow();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

*/


                                            double Latitude = locationResult.getLastLocation().getLatitude();
                                            double Longitude = locationResult.getLastLocation().getLongitude();


                                            DatabaseReference refClientx = FirebaseDatabase.getInstance().getReference("UserLocations").child(currentUser.getUid());
                                            refClientx.child("lat").setValue(Latitude);
                                            refClientx.child("lon").setValue(Longitude);


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
                                            //Toast.makeText(MapHicker.this, latLng.latitude + " " + latLng.longitude, Toast.LENGTH_SHORT).show();
                                            map.addMarker(markerOptions2);

                                            final PolylineOptions polylineOptions = new PolylineOptions();
                                            polylineOptions.width(10);
                                            polylineOptions.color(getColor(R.color.colorBlack));

                                            polylineOptions.add(marker.getPosition());
                                            polylineOptions.add(latLng);
                                            map.addPolyline(polylineOptions);


                                            List<Address> addresses = null;
                                            try {
                                                Geocoder geocoder = new Geocoder(MapHicker.this);
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
                                            AlertDialog.Builder builder1 = new AlertDialog.Builder(MapHicker.this);
                                            builder1.setTitle("Nearby Location");
                                            //builder1.setIcon(R.drawable.placeholder);


                                            DialogInterface.OnClickListener listener1 = new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    dialogInterface.dismiss();
                                                    //  Toast.makeText(MapHicker.this, item[i], Toast.LENGTH_SHORT).show();
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
//                                    Toast.makeText(MainActivity.this, "internet", Toast.LENGTH_SHORT).show();
                                                        HttpURLConnection con = null;

                                                        try {

                                                            System.out.println(start + " " + end);
                                                            URL url = new URL("https://maps.googleapis.com/maps/api/directions/json?origin=" + start
                                                                    + "" + end + "&mode=driving&key=AIzaSyAFgxrJB_V1_8JJT17gmwcB7unvdA_paQI");
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


                                                                final List<LatLng> decode = PolyUtil.decode(split3);

                                                                runOnUiThread(new Runnable() {
                                                                    @Override
                                                                    public void run() {
                                                                        PolylineOptions polylineOptions1 = new PolylineOptions();
                                                                        polylineOptions1.color(getColor(R.color.colorBlack));
                                                                        polylineOptions1.width(10);
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





                        };
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });












    }

}
