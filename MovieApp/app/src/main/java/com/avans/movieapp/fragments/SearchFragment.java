package com.avans.movieapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.avans.movieapp.R;
import com.avans.movieapp.adapters.VideosAdapter;
import com.avans.movieapp.base_logic.API;
import com.avans.movieapp.base_logic.DisplayCalc;
import com.avans.movieapp.models.Movie;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {
    private ArrayList<Movie> movies;


    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search, container, false);

        movies = new ArrayList<>();

        CheckBox lang_en = v.findViewById(R.id.lang_en);
        CheckBox lang_nl = v.findViewById(R.id.lang_nl);
        CheckBox lang_de = v.findViewById(R.id.lang_de);
        RatingBar ratingBar = v.findViewById(R.id.rating);

        DrawerLayout mDrawerLayout = v.findViewById(R.id.drawer_layout);
        NavigationView mNavigation = v.findViewById(R.id.nav_view);

        Button reset = v.findViewById(R.id.reset);
        reset.setOnClickListener(arg0 -> Toast.makeText(getActivity(), "Reset clicked", Toast.LENGTH_SHORT).show());

        ImageButton filter_chip = v.findViewById(R.id.filter_chip);
        filter_chip.setOnClickListener(v12 -> mDrawerLayout.openDrawer(mNavigation));

        RecyclerView mSearchRecycler = v.findViewById(R.id.rvSearch);
        EditText editText = v.findViewById(R.id.etSearch);

        RecyclerView.Adapter adapter = new VideosAdapter(movies, (data, success) -> {

            Movie m = (Movie) data;
            Log.d("M:", "Title: " + m.getTitle());

            Intent intent = new Intent();

            intent.putExtra("MOVIE", m);

            startActivity(intent);
        });

        mSearchRecycler.setAdapter(adapter);

        editText.setOnKeyListener((v1, keyCode, event) -> {
            if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                String searchTerm = editText.getText().toString();
                if (!searchTerm.isEmpty()) {
                    API.searchMovies(searchTerm, (data, success) -> {
                        if (success) {
                            ArrayList<Movie> results = (ArrayList<Movie>) data;
                            movies.clear();
                            movies.addAll(results);
                            adapter.notifyDataSetChanged();
                        }
                    });
                }
                return true;
            }
            return false;
        });

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), DisplayCalc.calculateNoOfColumns(getActivity()));
        mSearchRecycler.setLayoutManager(layoutManager);

        return v;
    }
}
