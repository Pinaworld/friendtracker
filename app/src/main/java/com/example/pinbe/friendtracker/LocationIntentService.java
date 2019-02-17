package com.example.pinbe.friendtracker;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import static com.example.pinbe.friendtracker.Constants.LOCATION_UPDATE;
import static com.example.pinbe.friendtracker.Constants.UPDATE_INTERVAL_IN_MILLISECONDS;

public class LocationIntentService extends Service implements LocationListener{

    private LocationManager locationManager;
    private Intent mIntentService;
    private PendingIntent mPendingIntent;
    boolean isGPSEnable = false;
    boolean isNetworkEnable = false;
    Location location;
    Criteria criteria;

    IBinder mBinder = new LocationIntentService.LocalBinder();


public class LocalBinder extends Binder {
        public LocationIntentService getServerInstance() {
            return LocationIntentService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(LOCATION_UPDATE, "Creation of listener");

        mIntentService = new Intent(Constants.LOCATION_UPDATE);

        criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_HIGH);
        criteria.setAltitudeRequired(false);
        criteria.setSpeedRequired(false);
        criteria.setCostAllowed(true);
        criteria.setBearingRequired(false);

        criteria.setHorizontalAccuracy(Criteria.ACCURACY_HIGH);
        criteria.setVerticalAccuracy(Criteria.ACCURACY_HIGH);
        fn_getlocation();
    }

    @Override
    public void onLocationChanged(Location location) {
        fn_getlocation();
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


    @SuppressLint("MissingPermission")
    private void fn_getlocation(){
        locationManager = (LocationManager)getApplicationContext().getSystemService(LOCATION_SERVICE);
        isGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isGPSEnable && !isNetworkEnable){

        }else {

            if (isNetworkEnable){
                location = null;
                locationManager.requestLocationUpdates(UPDATE_INTERVAL_IN_MILLISECONDS,0,criteria, this, null);
                if (locationManager!=null){

                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (location!=null){
                        fn_update(location);
                    }
                }

            }
            if (isGPSEnable){
                location = null;
                locationManager.requestLocationUpdates(UPDATE_INTERVAL_IN_MILLISECONDS,0,criteria, this, null);
                if (locationManager!=null){
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (location!=null){
                        fn_update(location);
                    }
                }
            }


        }

    }

    private void fn_update(Location location){

        mIntentService.putExtra("Location", location);
        sendBroadcast(mIntentService);
    }

    @SuppressLint("MissingPermission")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        locationManager.removeUpdates(this);
    }
}
