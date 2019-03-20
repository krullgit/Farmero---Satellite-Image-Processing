package com.example.matthes.farmero;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;

import java.util.List;

/**
 * The class:
 * - is the frame for the rest of the app by showing and initializing the bottom navigation
 */

public class MainActivity extends AppCompatActivity {

    // parameter
    private BottomNavigationView mMainNav;
    private FrameLayout mMainFrame;
    private MainFragment mainFragment;
    private FriendsFragment friendsFragment;
    private PhotoFragment photoFragment;
    private MapboxFragment mapboxFragment;
    private SettingsActivity settingsFragment;
    private List<Point> routeCoordinates;

    // Create Buttom Navigation
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menue_upper_right, menu);
        return true;
    }



    // like a constructor
    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        // run super constructor
        super.onCreate(savedInstanceState);

        // get mapbox instance with key
        Mapbox.getInstance(this, "pk.eyJ1IjoiZmFybWVybyIsImEiOiJjanJ6Zmd5cGcxODFzNDNsdnB0dW16bXh6In0.F6w_diZUKuqmcHGc1QNOGw");

        // render main layout
        setContentView(R.layout.activity_main);

        // initialize parameters
        mMainNav = (BottomNavigationView) findViewById(R.id.main_nav);
        mMainFrame = (FrameLayout) findViewById(R.id.main_frame);
        mainFragment = new MainFragment();
        photoFragment = new PhotoFragment();
        settingsFragment = new SettingsActivity();
        friendsFragment = new FriendsFragment();
        mapboxFragment = new MapboxFragment();

        // set default view to main page
        setFragment(mainFragment);

        // Event handler for bottom navigation bar
        mMainNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_home:
                        setFragment(mainFragment);
                        return true;
                    case R.id.nav_photo:
                        Intent intent = new Intent(MainActivity.this, CameraActivity.class);
                        startActivity(intent);
                        //setFragment(photoFragment);
                        return true;
                    case R.id.nav_map:

                        setFragment(mapboxFragment);
                        return true;
//                        Intent intent2 = new Intent(MainActivity.this, FieldBigActivity.class);
//                        startActivity(intent2);
//                        return true;
                    case R.id.nav_friends:
                        setFragment(friendsFragment);
                        return true;
                    default:
                        return false;
                }
            }
        });

        // require permission to save pictures on phone
        if (Build.VERSION.SDK_INT >= 23) {
            Log.d("fragment_camera2_basic", "fragment_camera2_basic");
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
        }
    }

    // FUNCTION to replace the fragment according to the nav bar
    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, fragment);
        fragmentTransaction.commit();
    }
}
