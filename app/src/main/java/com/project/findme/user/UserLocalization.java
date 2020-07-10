package com.project.findme.user;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Camera;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.project.findme.HelperClasses.HomeAdapter.FriendHelperClass;
import com.project.findme.HelperClasses.HomeAdapter.FriendsHelperClass;
import com.project.findme.HelperClasses.HomeAdapter.Location;
import com.project.findme.HelperClasses.UserInfo;
import com.project.findme.R;
import com.project.findme.common.Authentication.StartScreen;

import java.util.ArrayList;
import java.util.List;

public class UserLocalization extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Marker currentUser;
    private FirebaseDatabase database;
    private DatabaseReference locationRef;
    private DatabaseReference users;
    private GeoFire geoFire;
    private FirebaseUser user;
    private List<LatLng> dangerousArea;
    private List<String> usersUID = new ArrayList<String>();
    private List<Location> locations = new ArrayList<Location>();
    private Switch switcher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_localization);
        switcher = findViewById(R.id.switch_button);

        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        buildLocationRequest();
                        buildLocationCallback();
                        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(UserLocalization.this);

                        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                                .findFragmentById(R.id.map);
                        mapFragment.getMapAsync(UserLocalization.this);
                        settingGeoFire();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(UserLocalization.this, "You must enable the permission", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                }).check();


        switcher.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (locations != null) {
                    if (!isChecked) {
                        for (Location location : locations) {
                            if(location.getMarker()!=null)
                            location.getMarker().setVisible(false);
                        }
                    } else {

                        for (Location location : locations) {
                            if(location.getMarker()!=null)
                            location.getMarker().setVisible(true);
                        }


                    }
                }
            }
        });
    }


    private void settingGeoFire() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            startActivity(new Intent(getApplicationContext(), StartScreen.class));
        }

        database = FirebaseDatabase.getInstance();
        locationRef = database.getReference("locations");
        users = database.getReference("users").child(user.getUid()).child("friends");
        geoFire = new GeoFire(locationRef);
    }

    private void buildLocationCallback() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(final LocationResult locationResult) {
                if (mMap != null) {


                    geoFire.setLocation(user.getUid(), new GeoLocation(locationResult.getLastLocation().getLatitude(),
                            locationResult.getLastLocation().getLongitude()), new GeoFire.CompletionListener() {
                        @Override
                        public void onComplete(String key, DatabaseError error) {
                            if (currentUser != null) {
                                currentUser.remove();
                            }
                            currentUser = mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(locationResult.getLastLocation().getLatitude(),
                                            locationResult.getLastLocation().getLongitude()))
                                    .title("You"));
                            currentUser.showInfoWindow();

                            //Move camera
                            mMap.animateCamera(CameraUpdateFactory
                                    .newLatLngZoom(currentUser.getPosition(), 17f));
                            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                        }
                    });
                }
            }
        };
    }


    private void getFullName(final Location location) {
        users.child(location.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.i("MyLog", "Entered");
                String username = dataSnapshot.child("fullname").getValue().toString();
                location.setUsername(username);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    private void buildLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setSmallestDisplacement(10f);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.mapstyle));

            if (!success) {
                Log.i("MyLog", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.i("MyLog", "Can't find style. Error: ", e);
        }

        mMap.getUiSettings().setZoomControlsEnabled(true);


        if (fusedLocationProviderClient != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
            }
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());

            GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(33.7832, -7.4056), 100f);
            geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
                @Override
                public void onKeyEntered(String key, final GeoLocation location) {
                    Log.i("MyLog", "Key = " + key);
                    if (!user.getUid().equals(key)) {
                        if (!keyAlreadyExistsinList(key)) {
                            final Location userLocation = new Location();
                            userLocation.setKey(key);
                            users.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.getValue()!=null){
                                    if(dataSnapshot.getValue(FriendHelperClass.class).getStatus().equals("True")) {
                                        String username = dataSnapshot.getValue(FriendHelperClass.class).getUsername();
                                        //Log.i("MyLog","Username= "+username);
                                        userLocation.setUsername(username);
                                        addMarkerForUser(username, userLocation, location);
                                    }
                                }}

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Log.i("MyLog", databaseError.getMessage());
                                }
                            });


                        }


                    }
                }

                @Override
                public void onKeyExited(String key) {

                }

                @Override
                public void onKeyMoved(String key, GeoLocation location) {
                    if (!user.getUid().equals(key)) {
                        if (getLocationByKey(key) == null) {
                            Log.i("MyLog", "The user is not your friend");
                        } else {
                            Location userLocation = getLocationByKey(key);
                            userLocation.getMarker().remove();
                            Marker marker = mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(location.latitude, location.longitude))
                                    .title(userLocation.getUsername())
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                            marker.showInfoWindow();
                            userLocation.setMarker(marker);
                            Log.i("MyLog", "Locations = " + locations.toString());


                        }
                    }
                }

                @Override
                public void onGeoQueryReady() {

                }

                @Override
                public void onGeoQueryError(DatabaseError error) {

                }
            });
        }
    }

    private void addMarkerForUser(String username, Location userLocation, GeoLocation location) {
        Marker marker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(location.latitude, location.longitude))
                .title(username)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)
                ));
        marker.showInfoWindow();
        userLocation.setMarker(marker);
        locations.add(userLocation);
        //Log.i("MyLog","Locations = "+locations.toString());
    }


    private boolean keyAlreadyExistsinList(String key) {
        if (locations == null) {
            return false;
        } else {
            for (Location location : locations) {
                if (location.getKey().equals(key)) {
                    return true;
                }
            }
        }
        return false;
    }


    private Location getLocationByKey(String key) {
        if (locations == null) {
            return null;
        } else {
            for (Location location : locations) {
                if (location.getKey().equals(key))
                    return location;

            }

            return null;
        }


    }

    @Override
    protected void onStop() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        super.onStop();
    }
}
