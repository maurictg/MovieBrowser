package com.avans.movieapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
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
    private Spinner mSortDropdown;

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
//                lang_de.isChecked();

        RatingBar ratingBar = v.findViewById(R.id.rating);
//        ratingBar.getNumStars();

        mSortDropdown = v.findViewById(R.id.spinner);
        mSortDropdown.setOnItemSelectedListener(new onSortTypeClick());

        DrawerLayout mDrawerLayout = v.findViewById(R.id.drawer_layout);
        NavigationView mNavigation = v.findViewById(R.id.nav_view);

        Button reset = v.findViewById(R.id.reset);
        reset.setOnClickListener(arg0 -> Toast.makeText(getActivity(), "Reset clicked", Toast.LENGTH_SHORT).show());

        ImageButton filter_chip = v.findViewById(R.id.filter_chip);
        filter_chip.setOnClickListener(v12 -> mDrawerLayout.openDrawer(mNavigation));

        RecyclerView mSearchRecycler = v.findViewById(R.id.rvSearch);
        EditText editText = v.findViewById(R.id.etSearch);

        LinearLayout genreLayout = v.findViewById(R.id.genreLayout);

        RecyclerView.Adapter movieAdapter = new VideosAdapter(movies, (data, success) -> {

            Movie m = (Movie) data;
            Log.d("M:", "Title: " + m.getTitle());

            Intent intent = new Intent(getActivity().getApplicationContext(), MovieDetailsActivity.class);

            intent.putExtra("MOVIE", m);

            startActivity(intent);
        });
        mSearchRecycler.setAdapter(movieAdapter);

        editText.setOnKeyListener((v1, keyCode, event) -> {
            if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                String searchTerm = editText.getText().toString();
                if (!searchTerm.isEmpty()) {
                    API.searchMovies(searchTerm, (data, success) -> {
                        if (success) {
                            ArrayList<Movie> results;
                            results = (ArrayList<Movie>) data;
                            editText.setText("");
                            movies.clear();
                            movies.addAll(results);
                            Log.d("List: ",results.toString());
                            movieAdapter.notifyDataSetChanged();
                        }
                    });
                }
                return true;
            }
            return false;
        });

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), DisplayCalc.calculateNoOfColumns(getActivity()));
        mSearchRecycler.setLayoutManager(layoutManager);

        TextView[] textViewArray = new TextView[10];
        for(int i = 0; i < 10; i++) {
            textViewArray[i] = new TextView(getContext());
            textViewArray[i].setText("GENREAA");
            textViewArray[i].setBackground(ContextCompat.getDrawable(getContext(), R.drawable.bg_filter));
            genreLayout.addView(textViewArray[i]);
        }
        return v;
    }

    private class onSortTypeClick implements android.widget.AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            Toast.makeText(getActivity(), mSortDropdown.getSelectedItem() +" selected", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }
}
