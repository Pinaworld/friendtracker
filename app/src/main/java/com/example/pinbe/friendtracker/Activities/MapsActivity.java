package com.example.pinbe.friendtracker.Activities;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.pinbe.friendtracker.ActivityIntentService;
import com.example.pinbe.friendtracker.Constants;
import com.example.pinbe.friendtracker.Fragments.MenuFriendsFragment;
import com.example.pinbe.friendtracker.Fragments.MenuGroupFragment;
import com.example.pinbe.friendtracker.Fragments.MenuParametersFragment;
import com.example.pinbe.friendtracker.R;
import com.google.android.gms.location.ActivityRecognitionClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import static com.example.pinbe.friendtracker.Constants.DETECTED_ACTIVITY;

public class MapsActivity extends AppCompatActivity  implements OnMapReadyCallback, SharedPreferences.OnSharedPreferenceChangeListener {

    private Toolbar toolbar;
    private Fragment currentMenuFragment;
    private SupportMapFragment mapFragment;
    private ActivityRecognitionClient mActivityRecognitionClient;
    private GoogleMap googleMap;
    private MarkerOptions markerOption;
    private Marker marker;
    private boolean mLocationPermissionGranted;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Location mLastKnownLocation;
    private LatLng mDefaultLocation;
    private Circle circle = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
        mActivityRecognitionClient = new ActivityRecognitionClient(getApplicationContext());

        this.configureToolBar();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        // Add a marker in Sydney and move the camera
        Log.d("MAP", "mapReady");
        setMapActions();
        updateLocationUI();

        getDeviceLocation();
    }

    public void requestUpdatesHandler() {
        Task<Void> task = mActivityRecognitionClient.requestActivityUpdates(
                3000,
                getActivityDetectionPendingIntent());

        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void result) {
                Log.d("ACTIVITY", "UPDATED");
                updateMapMarker();
            }
        });

        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Failed to remove activity updates!",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private PendingIntent getActivityDetectionPendingIntent() {
        Intent intent = new Intent(getApplicationContext(), ActivityIntentService.class);
        return PendingIntent.getService(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if (s.equals(DETECTED_ACTIVITY)) {
            updateMapMarker();
        }
    }

    protected void updateMapMarker() {
        String detectedActivity = PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                .getString(DETECTED_ACTIVITY, "");

        setMarker(detectedActivity);

    }

    public void setMarker(String activityType){

        if(activityType.equals("standing")) {
            markerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.standing));
        }
        if(activityType.equals("running")) {
            markerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.on_foot));
        }
        if(activityType.equals("in_vehicle")) {
            markerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.in_vehicle));
        }
        if(activityType.equals("tilting")) {
            markerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.standing));
        }
        if(activityType.equals("walking")) {
            markerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.on_foot));
        }
        if(activityType.equals("on_bicycle")) {
            markerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.on_bicycle));
        }
        if(activityType.equals("STILL")) {
            markerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.standing));
        }
        else{
            markerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.standing));
        }
        if (marker != null && googleMap != null) {
            marker.remove();
            this.googleMap.addMarker(markerOption);
        }
    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
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

    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                Task locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener() {

                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = (Location) task.getResult();
                            LatLng location = new LatLng(mLastKnownLocation.getLatitude(),mLastKnownLocation.getLongitude());
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, Constants.DEFAULT_ZOOM));

                            markerOption = new MarkerOptions().position(location).title("Vous êtes ici");
                            marker = googleMap.addMarker(markerOption);
                            setMarker("standing");

                            googleMap.moveCamera(CameraUpdateFactory.newLatLng(location));
                        } else {
                            Log.d(Constants.TAG, "Current location is null. Using defaults.");
                            Log.e(Constants.TAG, "Exception: %s", task.getException());
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, Constants.DEFAULT_ZOOM));
                            googleMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });

            }


            requestUpdatesHandler();
        } catch(SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
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
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        currentMenuFragment = null;

        switch (id){
            case R.id.menu_groups :
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
        return(super.onOptionsItemSelected(item));
    }

    public void setMenuFragment(){
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


    private void configureToolBar(){
        this.toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public boolean removeCurrentFragment() {
        if(currentMenuFragment != null){
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.remove(currentMenuFragment);
            ft.commit();

        }
        Log.d("TOUCH", "REMOVE FRAGMENT");
        return false;
    }

    public void setMapActions(){
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
