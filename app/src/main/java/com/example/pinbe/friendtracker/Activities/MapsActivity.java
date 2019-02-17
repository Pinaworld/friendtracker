package com.example.pinbe.friendtracker.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.pinbe.friendtracker.BackgroundDetectedActivitiesService;
import com.example.pinbe.friendtracker.Constants;
import com.example.pinbe.friendtracker.Fragments.MenuFriendsFragment;
import com.example.pinbe.friendtracker.Fragments.MenuGroupFragment;
import com.example.pinbe.friendtracker.Fragments.MenuParametersFragment;
import com.example.pinbe.friendtracker.LocationIntentService;
import com.example.pinbe.friendtracker.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback{

    private Toolbar toolbar;

    private Fragment currentMenuFragment;
    private SupportMapFragment mapFragment;

    private GoogleMap googleMap;
    private MarkerOptions markerOption;
    private Marker marker;

    private boolean mLocationPermissionGranted;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Location mCurrentLocation;
    private LatLng mDefaultLocation;

    private FirebaseAuth auth;

    private String currentActivity;
    BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        currentActivity = "standing";

        this.configureToolBar();
    }


    @Override
    protected void onResume() {
        super.onResume();

        registerReceiver(broadcastReceiver, new IntentFilter(Constants.DETECTED_ACTIVITY));
        registerReceiver(broadcastReceiver, new IntentFilter(Constants.LOCATION_UPDATE));
    }

    @Override
    protected void onPause() {
        super.onPause();

        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
        stopServices();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(broadcastReceiver != null){
            unregisterReceiver(broadcastReceiver);
            stopServices();
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        setMapActions();
        updateLocationUI();
        initLocation();
        setBroadCastReceiver();
        startServices();
    }

    private void initLocation(){
        Log.d(Constants.LOCATION_UPDATE, "Current location is null. Using defaults.");
        try {
            if (mLocationPermissionGranted) {
                Task task = mFusedLocationProviderClient.getLastLocation();

                task.addOnCompleteListener(this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            initPosition((Location) task.getResult());
                        }
                        else {
                            Log.d(Constants.LOCATION_UPDATE, "Current location is null. Using defaults.");
                            Log.e(Constants.LOCATION_UPDATE, "Exception: %s", task.getException());
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, Constants.DEFAULT_ZOOM));
                            googleMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        }
        catch(SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void setBroadCastReceiver(){
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.i("RECEIVED INTENT", intent.getAction());

                if (intent.getAction().equals(Constants.DETECTED_ACTIVITY)) {
                    String detectedActivity = intent.getStringExtra("activity");
                    Log.i(ACTIVITY_SERVICE, detectedActivity);
                    currentActivity = detectedActivity;
                    setMarker();
                }

                if (intent.getAction().equals(Constants.LOCATION_UPDATE)) {
                    Location location = (Location) intent.getExtras().get("Location");
                    setLocation(location);
                }
            }
        };

        registerReceiver(broadcastReceiver, new IntentFilter(Constants.DETECTED_ACTIVITY));
        registerReceiver(broadcastReceiver, new IntentFilter(Constants.LOCATION_UPDATE));
    }

    private void startServices() {
        Intent intentLocation = new Intent(this, LocationIntentService.class);
        startService(intentLocation);
        Intent intent = new Intent(this, BackgroundDetectedActivitiesService.class);
        startService(intent);

    }

    private void stopServices() {
        Intent intent = new Intent(this, BackgroundDetectedActivitiesService.class);
        stopService(intent);

        Intent intentLocation = new Intent(this, LocationIntentService.class);
        stopService(intentLocation);
    }



    private void setMarker() {
        if (marker != null && googleMap != null) {
            if (this.currentActivity.equals("standing")) {
                marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.standing));
            }
            if (this.currentActivity.equals("running")) {
                marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.on_foot));
            }
            if (this.currentActivity.equals("in_vehicle")) {
                marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.in_vehicle));
            }
            if (this.currentActivity.equals("tilting")) {
                marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.standing));
            }
            if (this.currentActivity.equals("walking")) {
                marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.on_foot));
            }
            if (this.currentActivity.equals("on_bicycle")) {
                marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.on_bicycle));
            }
            if (this.currentActivity.equals("STILL")) {
                marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.standing));
            } else {
                marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.standing));
            }

        }
    }

    private void initPosition(Location initialLocation){
        mCurrentLocation = initialLocation;
        LatLng location = new LatLng(initialLocation.getLatitude(), initialLocation.getLongitude());
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, Constants.DEFAULT_ZOOM));
        markerOption = new MarkerOptions().position(location).title("Moi");
        marker = googleMap.addMarker(markerOption);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(location));
    }

    private void setLocation(Location newLocation) {
        if (newLocation != null) {
            mCurrentLocation = newLocation;
            LatLng location = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());

            marker.setPosition(location);

            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, Constants.DEFAULT_ZOOM));
        } else {
            Log.w(Constants.TAG, "Current location is null. Using defaults.");
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, Constants.DEFAULT_ZOOM));
            googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        }
    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            Toast.makeText(this, "Vous devez activer la géolocalisation pour obtenir votre position.", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    Constants.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case Constants.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }

    private void updateLocationUI() {
        if (googleMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                googleMap.setMyLocationEnabled(true);
                googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                googleMap.setMyLocationEnabled(false);
                googleMap.getUiSettings().setMyLocationButtonEnabled(false);
                mCurrentLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        currentMenuFragment = null;

        switch (id) {
            case R.id.menu_groups:
                currentMenuFragment = new MenuGroupFragment();
                break;
            case R.id.menu_friends:
                currentMenuFragment = new MenuFriendsFragment();
                break;
            case R.id.menu_parameters:
                currentMenuFragment = new MenuParametersFragment();
                break;
            default:

                break;
        }

        setMenuFragment();
        return (super.onOptionsItemSelected(item));
    }

    private void setMenuFragment() {
        if (currentMenuFragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.menu_frame_layout, currentMenuFragment)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main_menu_drawer, menu);
        return true;
    }


    private void configureToolBar() {
        this.toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private boolean removeCurrentFragment() {
        if (currentMenuFragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.remove(currentMenuFragment);
            ft.commit();

        }
        Log.d("TOUCH", "REMOVE FRAGMENT");
        return false;
    }

    private void setMapActions() {
        this.googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng arg0) {
                removeCurrentFragment();
            }
        });

        this.googleMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                removeCurrentFragment();
            }
        });
    }
}
