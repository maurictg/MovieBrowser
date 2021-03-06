package com.avans.movieapp.activities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.avans.movieapp.R;
import com.avans.movieapp.fragments.HomeFragment;
import com.avans.movieapp.fragments.SavedFragment;
import com.avans.movieapp.fragments.SearchFragment;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private final String TAG = MainActivity.class.getSimpleName();
    private BottomNavigationView bottomNavigationView;

    private MenuItem nav_saved;
    private MenuItem nav_search;

    private BadgeDrawable badgeDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_YES);
        //Show popup when no wifi found
        if (((ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE)).getAllNetworks().length<=0) {
            System.out.println("WIFI OFF");
            new AlertDialog.Builder(this,R.style.AlertDialogStyle)
                    .setTitle("No internet")
                    .setMessage("Unable to connect to the internet. Are you sure your wifi is on?")
                    .setIcon(R.drawable.ic_signal_wifi_off_24dp)
                    .show();
        }

        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavMethod);

        // Menu notifications
        Menu navigation = bottomNavigationView.getMenu();
        badgeDrawable = BadgeDrawable.create(this);
        nav_saved = navigation.findItem(R.id.nav_saved);
        
        setNotification(nav_saved);

        // Set the default fragment to HomeFragment
        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new HomeFragment()).commit();
        }

        setTheme(R.style.AppTheme_Base);
    }

    private void setNotification(MenuItem menuItem) {
        bottomNavigationView.getOrCreateBadge(menuItem.getItemId());
    }

    private void clearNotification(MenuItem menuItem) {
        bottomNavigationView.removeBadge(menuItem.getItemId());
    }

    // Bottom navigation buttons
    private final BottomNavigationView.OnNavigationItemSelectedListener bottomNavMethod = item -> {
        Fragment fragment = null;
        assert false;
        switch (item.getItemId()) {
            case R.id.nav_home:
                fragment = new HomeFragment();
                break;
            case R.id.nav_search:
                fragment = new SearchFragment();
                break;
            case R.id.nav_saved:
                fragment = new SavedFragment();
                clearNotification(nav_saved);
                break;
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
        return true;
    };
}
