package com.example.pinbe.friendtracker.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.example.pinbe.friendtracker.R;


public class SplashScreen extends Activity {

    private static int TIMEOUT = 3000;

    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("THEME", android.content.Context.MODE_PRIVATE);
        String theme = pref.getString("my_theme", "");
        switch (theme) {
            case "theme1":
                setTheme(R.style.theme1);
                break;
            case "theme2":
                setTheme(R.style.theme2);
                break;
            case "default":
                setTheme(R.style.AppTheme);
                break;
            default:
                break;
        }
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
