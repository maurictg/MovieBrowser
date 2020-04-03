package com.avans.movieapp.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.avans.movieapp.R;
import com.avans.movieapp.activities.MovieDetailsActivity;
import com.avans.movieapp.adapters.VideosAdapter;
import com.avans.movieapp.base_logic.API;
import com.avans.movieapp.interfaces.ICallback;
import com.avans.movieapp.models.Movie;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private final String TAG = HomeFragment.class.getSimpleName();

    private ArrayList<Movie> moviesDiscover;
    private ArrayList<Movie> moviesRecent;

    private VideosAdapter adapterDiscover;
    private VideosAdapter adapterRecent;

    public HomeFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        Log.d(TAG, "Called onCreateView");
        moviesDiscover = new ArrayList<>();
        moviesRecent = new ArrayList<>();

        RecyclerView rvHomeDiscover = view.findViewById(R.id.rvHomeDiscover);
        RecyclerView rvHomeRecent = view.findViewById(R.id.rvHomeRecent);

        RecyclerView.LayoutManager lmDiscover = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        RecyclerView.LayoutManager lmRecent = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

        rvHomeDiscover.setLayoutManager(lmDiscover);
        rvHomeRecent.setLayoutManager(lmRecent);

        adapterDiscover = new VideosAdapter(moviesDiscover, new ClickCallback());
        adapterRecent = new VideosAdapter(moviesRecent, new ClickCallback());

        rvHomeDiscover.setAdapter(adapterDiscover);
        rvHomeRecent.setAdapter(adapterRecent);


        //Discover
        API.discover((data, success) -> {
            if (success) {
                moviesDiscover.addAll((ArrayList<Movie>) data);
                adapterDiscover.notifyDataSetChanged();
            }
        });

        //Fill recent
        SharedPreferences sp = getActivity().getSharedPreferences("MOVIES", MODE_PRIVATE);
        String[] ids = sp.getString("recent", "").split(",");
        for (String id : ids) {
            API.getMovieById(id, (data, success) -> {
                if (success) {
                    moviesRecent.add((Movie) data);
                    adapterRecent.notifyItemInserted(moviesRecent.size());
                }
            });
        }

        TextView tvHome = view.findViewById(R.id.tvTitle);
        Shader shader = new LinearGradient(tvHome.getWidth(), tvHome.getLineHeight(),0 , 0, Color.parseColor("#00B3E4"), Color.parseColor("#90CEA1"),
                Shader.TileMode.REPEAT);

        tvHome.getPaint().setShader(shader);
        return view;
    }

    class ClickCallback implements ICallback {
        @Override
        public void callback(Object data, boolean success) {
            Movie m = (Movie) data;
            Log.d("M:", "Title: " + m.getTitle());

            Intent intent = new Intent(getActivity().getApplicationContext(), MovieDetailsActivity.class);
            intent.putExtra("MOVIE", m);

            startActivity(intent);
        }
    }

}
