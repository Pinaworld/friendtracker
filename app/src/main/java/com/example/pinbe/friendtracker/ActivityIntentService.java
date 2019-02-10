package com.example.pinbe.friendtracker;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.preference.PreferenceManager;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

public class ActivityIntentService extends IntentService {

    public ActivityIntentService() {
        super(Constants.TAG);
    }
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (ActivityRecognitionResult.hasResult(intent)) {
            ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);

            String detectedActivities = getActivityString(getApplicationContext(), result.getMostProbableActivity().getType());

            PreferenceManager.getDefaultSharedPreferences(this)
                    .edit()
                    .putString(Constants.DETECTED_ACTIVITY, detectedActivities)
                    .apply();
        }
    }

    public static String getActivityString(Context context, int detectedActivityType) {
        Resources resources = context.getResources();
        switch(detectedActivityType) {
            case DetectedActivity.ON_BICYCLE:
                return resources.getString(R.string.on_bicycle);
            case DetectedActivity.ON_FOOT:
                return resources.getString(R.string.on_foot);
            case DetectedActivity.RUNNING:
                return resources.getString(R.string.running);
            case DetectedActivity.STILL:
                return resources.getString(R.string.standing);
            case DetectedActivity.TILTING:
                return resources.getString(R.string.tilting);
            case DetectedActivity.WALKING:
                return resources.getString(R.string.on_foot);
            case DetectedActivity.IN_VEHICLE:
                return resources.getString(R.string.in_vehicle);
            default:
                return resources.getString(R.string.tilting);
        }
    }
}
