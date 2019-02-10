package com.example.pinbe.friendtracker.Fragments;


import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.pinbe.friendtracker.ActivityIntentService;
import com.example.pinbe.friendtracker.R;
import com.google.android.gms.location.ActivityRecognitionClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import static com.example.pinbe.friendtracker.Constants.DETECTED_ACTIVITY;

public class MapFragment extends Fragment implements OnMapReadyCallback, SharedPreferences.OnSharedPreferenceChangeListener    {

    private SupportMapFragment mapFragment;
    private ActivityRecognitionClient mActivityRecognitionClient;
    private GoogleMap googleMap;
    private MarkerOptions markerOption;
    private Marker marker;


    public MapFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mapFragment.getMapAsync(this);
        mActivityRecognitionClient = new ActivityRecognitionClient(getContext());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        // Add a marker in Sydney and move the camera
        Log.d("MAP", "mapReady");
        LatLng testParis = new LatLng(48.8534, 2.3488);
        markerOption = new MarkerOptions().position(testParis).title("ici c'est Paris");
        setMarker("standing");
        marker = this.googleMap.addMarker(markerOption);
        this.googleMap.moveCamera(CameraUpdateFactory.newLatLng(testParis));
        requestUpdatesHandler();
    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }*/
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

    public void requestUpdatesHandler() {

        Task<Void> task = mActivityRecognitionClient.requestActivityUpdates(
                3000,
                getActivityDetectionPendingIntent());

        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void result) {
                Toast.makeText(getContext(), "Removed activity updates successfully!",
                        Toast.LENGTH_SHORT).show();
                Log.d("ACTIVITY", "UPDATED");
                updateMapMarker();
            }
        });

        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Failed to remove activity updates!",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private PendingIntent getActivityDetectionPendingIntent() {
        Intent intent = new Intent(getContext(), ActivityIntentService.class);
        return PendingIntent.getService(getContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if (s.equals(DETECTED_ACTIVITY)) {
            updateMapMarker();
        }
    }

    protected void updateMapMarker() {
        String detectedActivity = PreferenceManager.getDefaultSharedPreferences(getContext())
                                    .getString(DETECTED_ACTIVITY, "");

        setMarker(detectedActivity);

    }
}
