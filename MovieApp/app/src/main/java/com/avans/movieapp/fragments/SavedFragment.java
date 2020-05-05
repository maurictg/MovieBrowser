package com.avans.movieapp.fragments;

import android.content.Intent;
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
import com.avans.movieapp.adapters.SavedAdapter;
import com.avans.movieapp.base_logic.API;
import com.avans.movieapp.models.Movie;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class SavedFragment extends Fragment implements Serializable {

    private static final String TAG = SavedFragment.class.getSimpleName();
    private SavedAdapter mSavedList;

    private ArrayList<Movie> movies = new ArrayList<>();

    public SavedFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_saved, container, false);

        TextView tvTitle = v.findViewById(R.id.tvTitle);
        Shader shader = new LinearGradient(tvTitle.getWidth(), tvTitle.getLineHeight(), 0, 0, Color.parseColor("#00B3E4"), Color.parseColor("#90CEA1"),
                Shader.TileMode.REPEAT);
        tvTitle.getPaint().setShader(shader);

        mSavedList = new SavedAdapter(movies, (data, success) -> {
            Movie m = (Movie) data;
            Log.d(TAG, "onCreateView, M:" + "Title: " + m.getTitle());
            Intent intent = new Intent(getActivity(), MovieDetailsActivity.class);
            intent.putExtra("MOVIE", m);

            startActivity(intent);
        });

        RecyclerView mSaveRecycler = v.findViewById(R.id.rvSave);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mSaveRecycler.setLayoutManager(layoutManager);
        mSaveRecycler.setAdapter(mSavedList);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        API.getMoviesFromMovieList((data, success) -> {
            if (success) {
                movies.clear();
                movies.addAll((ArrayList<Movie>) data);
                mSavedList.notifyDataSetChanged();
            }
        });
    }
}
