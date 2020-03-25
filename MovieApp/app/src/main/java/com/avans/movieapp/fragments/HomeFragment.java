package com.avans.movieapp.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

    private ArrayList<Movie> moviesDiscover;
    private ArrayList<Movie> moviesRecent;

    private VideosAdapter adapterDiscover;
    private VideosAdapter adapterRecent;

    private RecyclerView rvHomeDiscover;
    private RecyclerView rvHomeRecent;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.d(TAG, "Called onCreateView");
        moviesDiscover = new ArrayList<>();
        moviesRecent = new ArrayList<>();

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        rvHomeDiscover = view.findViewById(R.id.rvHomeDiscover);
        rvHomeRecent = view.findViewById(R.id.rvHomeRecent);

//        RecyclerView.LayoutManager LayoutManager = new GridLayoutManager(getActivity(), MainActivity.calculateNoOfColumns(getActivity()));
        RecyclerView.LayoutManager lmDiscover = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,false);
        RecyclerView.LayoutManager lmRecent = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,false);

        rvHomeDiscover.setLayoutManager(lmDiscover);
        rvHomeRecent.setLayoutManager(lmRecent);

        adapterDiscover = new VideosAdapter(moviesDiscover);
        adapterRecent = new VideosAdapter(moviesRecent);

        rvHomeDiscover.setAdapter(adapterDiscover);
        rvHomeRecent.setAdapter(adapterRecent);

        final boolean LOAD_TESTDATA = true; //Even om te voorkomen dat we de API overdosen bij het debuggen

        if (LOAD_TESTDATA) {
            for (int i = 1; i < 12; i++) {
                moviesDiscover.add(new Movie(i, "Titel " + i, "Overview van film " + i, "imageUrlPoster", "", false, new Date(), 4));
                moviesRecent.add(new Movie(i, "Titel " + i, "Overview van film " + i, "imageUrlPoster", "", false, new Date(), 4));

            }
        } else {
            //Test
            API.searchMovies("James bond", (data, success) -> {
                if (success) {
                    ArrayList<Movie> mvs = (ArrayList<Movie>) data;
                    moviesDiscover.addAll(mvs);
                }
            });
        }
        adapterDiscover.notifyDataSetChanged();
        adapterRecent.notifyDataSetChanged();
        return view;
    }

}
