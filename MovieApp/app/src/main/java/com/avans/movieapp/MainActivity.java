package com.avans.movieapp;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.avans.movieapp.base_logic.API;
import com.avans.movieapp.fragments.HomeFragment;
import com.avans.movieapp.fragments.ProfileFragment;
import com.avans.movieapp.fragments.SavedFragment;
import com.avans.movieapp.fragments.SearchFragment;
import com.avans.movieapp.models.Movie;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private final String TAG = MainActivity.class.getSimpleName();
    private BottomNavigationView bottomNavigationView;

    private MenuItem nav_profile;
    private MenuItem nav_saved;

    BadgeDrawable badgeDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavMethod);

        Menu navigation = bottomNavigationView.getMenu();
        badgeDrawable = BadgeDrawable.create(this);
        nav_profile = navigation.findItem(R.id.nav_profile);
        nav_saved = navigation.findItem(R.id.nav_saved);
        
        setNotification(nav_profile);
        setNotification(nav_saved);

        getSupportFragmentManager().beginTransaction().replace(R.id.container, new HomeFragment()).commit();

        API.searchMovies("How to train your dragon", ((data, success) -> {
            if (success){
                ArrayList<Movie> movies = (ArrayList<Movie>)data;
                Log.d(TAG, movies.toString());
            }

        }));

    }
    private void setNotification(MenuItem menuItem) {
        bottomNavigationView.getOrCreateBadge(menuItem.getItemId());
    }
    private void clearNotification(MenuItem menuItem) {
        bottomNavigationView.removeBadge(menuItem.getItemId());
    }

    // Bottom navigation buttons
    private BottomNavigationView.OnNavigationItemSelectedListener bottomNavMethod = item -> {
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
            case R.id.nav_profile:
                fragment = new ProfileFragment();
                clearNotification(nav_profile);
                break;
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
        return true;
    };

    // Resize GridManager of the RecycleViews
    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (dpWidth / 180);
        return noOfColumns;
    }

}
