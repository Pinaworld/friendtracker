package com.example.pinbe.friendtracker.Services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;

import com.example.pinbe.friendtracker.Constants;
import com.example.pinbe.friendtracker.R;
import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

public class ActivityIntentService extends IntentService {

    protected static final String TAG = ActivityIntentService.class.getSimpleName();

    public ActivityIntentService() {
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (ActivityRecognitionResult.hasResult(intent)) {
            ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);

            String detectedActivity = getActivityString(getApplicationContext(), result.getMostProbableActivity().getType());

            broadcastActivity(detectedActivity);
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

    private void broadcastActivity(String activity) {
        Intent intent = new Intent(Constants.DETECTED_ACTIVITY);
        intent.putExtra("activity", activity);
        sendBroadcast(intent);
    }
}
