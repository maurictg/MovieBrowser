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
import com.avans.movieapp.models.Movie;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private final String TAG = HomeFragment.class.getSimpleName();

    private ArrayList<Movie> movies;
    private View view;
    private RecyclerView rvHome;
    private VideosAdapter adapter;

    public HomeFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.d(TAG, "Called onCreateView");
        movies = new ArrayList<>();
        
        view = inflater.inflate(R.layout.fragment_home, container, false);

        rvHome = view.findViewById(R.id.rvHome);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), MainActivity.calculateNoOfColumns(getActivity()));
        rvHome.setLayoutManager(layoutManager);

        adapter = new VideosAdapter(movies);
        rvHome.setAdapter(adapter);

//        movies.add(new Movie("Test"));
//        movies.add(new Movie("Test 2"));
//        movies.add(new Movie("Testje"));
        adapter.notifyDataSetChanged();

        return view;
    }

}
