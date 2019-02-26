package com.example.pinbe.friendtracker.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.pinbe.friendtracker.R;


public class SplashScreen extends Activity {

    private static int TIMEOUT = 3000;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(i);
                finish();
            }
        }, TIMEOUT);
    }
}
