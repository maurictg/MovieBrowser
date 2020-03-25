package com.avans.movieapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.avans.movieapp.MainActivity;
import com.avans.movieapp.R;
import com.avans.movieapp.adapters.VideosAdapter;
import com.avans.movieapp.base_logic.API;
import com.avans.movieapp.models.Movie;

import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private final String TAG = HomeFragment.class.getSimpleName();

    private ArrayList<Movie> movies;
    private View view;
    private RecyclerView rvHome;
    private VideosAdapter adapter;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.d(TAG, "Called onCreateView");
        movies = new ArrayList<>();

        view = inflater.inflate(R.layout.fragment_home, container, false);

        rvHome = view.findViewById(R.id.rvHome);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), /*MainActivity.calculateNoOfColumns(getActivity())*/1);
        rvHome.setLayoutManager(layoutManager);

        adapter = new VideosAdapter(movies);
        rvHome.setAdapter(adapter);

        final boolean LOAD_TESTDATA = true; //Even om te voorkomen dat we de API overdosen bij het debuggen

        if (LOAD_TESTDATA) {
            for (int i = 1; i < 12; i++) {
                movies.add(new Movie(i, "Titel " + i, "Overview van film " + i, "imageUrlPoster", "", false, new Date(), 4));
            }
            adapter.notifyDataSetChanged();
        } else {
            //Test
            API.searchMovies("James bond", (data, success) -> {
                if (success) {
                    ArrayList<Movie> mvs = (ArrayList<Movie>) data;
                    movies.addAll(mvs);
                    adapter.notifyDataSetChanged();
                }
            });
        }

        return view;
    }

}
