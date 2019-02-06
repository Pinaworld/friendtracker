package com.example.pinbe.friendtracker.Activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.example.pinbe.friendtracker.Fragments.MapFragment;
import com.example.pinbe.friendtracker.Fragments.MenuParametersFragment;
import com.example.pinbe.friendtracker.Fragments.MenuFriendsFragment;
import com.example.pinbe.friendtracker.Fragments.MenuGroupFragment;
import com.example.pinbe.friendtracker.R;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Fragment currentMenuFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.configureToolBar();

        FrameLayout frameLayout = findViewById(R.id.activity_main_frame_layout);
        frameLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.remove(currentMenuFragment);
                    ft.commit();

                    return false;
            }
        });

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.activity_main_frame_layout, new MapFragment(), "FRAGMENTS").commitAllowingStateLoss();
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

}
