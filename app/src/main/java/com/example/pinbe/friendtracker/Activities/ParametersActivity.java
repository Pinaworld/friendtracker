package com.example.pinbe.friendtracker.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.pinbe.friendtracker.R;

public class ParametersActivity extends AppCompatActivity {

    private TextView themeTitle;
    private TextView warningMessage;
    private Switch defaultThemeSwitch;
    private Switch theme1Switch;
    private Switch theme2Switch;
    private Button validateParametersButton;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private String currentTheme;
    private String newTheme;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parameters);
        Toolbar toolbar = findViewById(R.id.toolbar);

        themeTitle = findViewById(R.id.themeTitle);
        defaultThemeSwitch = findViewById(R.id.defaultThemeSwitch);
        theme1Switch = findViewById(R.id.theme1Switch);
        theme2Switch = findViewById(R.id.theme2Switch);
        validateParametersButton = findViewById(R.id.validateParametersButton);
        warningMessage = findViewById(R.id.messageWarning);

        pref = getApplicationContext().getSharedPreferences("THEME", android.content.Context.MODE_PRIVATE);
        editor = pref.edit();

        currentTheme = pref.getString("my_theme", "");

        switch(currentTheme){
            case "theme1":
                theme1Switch.setChecked(true);
                break;
            case "theme2":
                theme2Switch.setChecked(true);
                break;
            default:
                defaultThemeSwitch.setChecked(true);
                break;
        }

       setSwitchListeners();

        validateParametersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!newTheme.equals(currentTheme)){
                    restartApplication();
                }
                else{
                    finish();
                }
            }
        });
    }

    private void setSwitchListeners() {
        defaultThemeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                theme1Switch.setOnCheckedChangeListener(null);
                theme2Switch.setOnCheckedChangeListener(null);
                theme1Switch.setChecked(false);
                theme2Switch.setChecked(false);
                setSwitchListeners();
                saveSelectedTheme("");
            }
        });

        theme1Switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                defaultThemeSwitch.setOnCheckedChangeListener(null);
                theme2Switch.setOnCheckedChangeListener(null);
                defaultThemeSwitch.setChecked(false);
                theme2Switch.setChecked(false);
                setSwitchListeners();
                saveSelectedTheme("theme1");
            }
        });

        theme2Switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                theme1Switch.setOnCheckedChangeListener(null);
                defaultThemeSwitch.setOnCheckedChangeListener(null);
                theme1Switch.setChecked(false);
                defaultThemeSwitch.setChecked(false);
                setSwitchListeners();
                saveSelectedTheme("theme2");
            }
        });
    }

    private void saveSelectedTheme(String theme) {
        editor.putString("my_theme", theme);
        editor.commit();
        newTheme = theme;
    }

    private void restartApplication(){
        Intent intent = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(intent);
    }

}
