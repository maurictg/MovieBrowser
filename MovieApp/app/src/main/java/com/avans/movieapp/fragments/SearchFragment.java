package com.avans.movieapp.fragments;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.avans.movieapp.R;
import com.avans.movieapp.activities.MovieDetailsActivity;
import com.avans.movieapp.adapters.VideosAdapter;
import com.avans.movieapp.base_logic.API;
import com.avans.movieapp.base_logic.DisplayCalc;
import com.avans.movieapp.base_logic.Filters;
import com.avans.movieapp.models.Genre;
import com.avans.movieapp.models.Movie;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Collections;

public class SearchFragment extends Fragment {

    private static final String TAG = SearchFragment.class.getSimpleName();

    private ArrayList<Movie> movies;
    private Spinner mSortDropdown;
    private ArrayList<Integer> selectedGenresList = new ArrayList<>();
    private TextView[] textViewArray;

    private Drawable bgEnabled;
    private Drawable bgDisabled;
    private ArrayList<Genre> genres;
    private RecyclerView.Adapter adapter;
    private RecyclerView rvMovies;
    private GridLayout glLanguages;

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigation;

    private ProgressBar progressBar;
    private int progressStatus = 0;
    private Handler handler = new Handler();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView called");
        View v = inflater.inflate(R.layout.fragment_search, container, false);

        movies = new ArrayList<>();

        RatingBar ratingBar = v.findViewById(R.id.rating);
        ratingBar.setOnRatingBarChangeListener((ratingBar1, rating, fromUser) -> {
            if (fromUser) {
                Log.d(TAG, "onRatingChanged" + "Rating: " + rating * 2);
                Filters.FilterRating(movies, rating * 2);
                adapter.notifyDataSetChanged();
            }
        });

        glLanguages = v.findViewById(R.id.glLanguages);
        for (int i = 0; i < glLanguages.getChildCount(); i++) {
            View ch = glLanguages.getChildAt(i);
            if (ch instanceof CheckBox) {
                CheckBox cb = (CheckBox) ch;
                cb.setOnClickListener(v1 -> {
                    filterLanguages();
                });
            }
        }
        this.printGenres(v);

        mSortDropdown = v.findViewById(R.id.spinner);
        mSortDropdown.setOnItemSelectedListener(new onSortTypeClick());

        mDrawerLayout = v.findViewById(R.id.drawer_layout);

        mNavigation = v.findViewById(R.id.nav_view);

        Button reset = v.findViewById(R.id.reset);
        reset.setOnClickListener(arg0 -> Toast.makeText(getActivity(), "Reset clicked", Toast.LENGTH_SHORT).show());

        ImageButton filter_chip = v.findViewById(R.id.filter_chip);
        filter_chip.setOnClickListener(v12 -> mDrawerLayout.openDrawer(mNavigation));

        adapter = new VideosAdapter(movies, (data, success) -> {

            Movie m = (Movie) data;
            Log.d(TAG, "onCreateView, M:" + "Title: " + m.getTitle());
            Intent intent = new Intent(getActivity(), MovieDetailsActivity.class);
            intent.putExtra("MOVIE", m);

            startActivity(intent);
        });

        progressBar = v.findViewById(R.id.progressBar);
        progressBar.setProgressDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.gradient));

        EditText editText = v.findViewById(R.id.etSearch);
        editText.setOnKeyListener((v1, keyCode, event) -> {
            if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                String searchTerm = editText.getText().toString();
                if (!searchTerm.isEmpty()) {
                    showProgressBar();
                    API.searchMovies(searchTerm, (data, success) -> {
                        if (success) {
                            movies.clear();
                            movies.addAll((ArrayList<Movie>) data);
                            Log.d(TAG, "onCreateView: movie list before = " + movies.toString() + "\n" + movies.size());
                            this.filterMovies();
                            Log.d(TAG, "onCreateView: movie list after = " + movies.toString() + "\n" + movies.size());
                            adapter.notifyDataSetChanged();
                            ratingBar.setRating(ratingBar.getNumStars());
                            showProgressBar();
                            while (progressStatus < 100) {
                                progressStatus = movies.size() / adapter.getItemCount() * 100;
                                handler.post(() -> progressBar.setProgress(progressStatus));
                                try {
                                    // Sleep for 200 milliseconds.
                                    Thread.sleep(200);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            hideProgressBar();
                        }
                    });
                }
                return true;
            }
            return false;
        });

        rvMovies = v.findViewById(R.id.rvSearch);
        rvMovies.setAdapter(adapter);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), DisplayCalc.calculateNoOfColumns(getActivity()));
        rvMovies.setLayoutManager(layoutManager);

        return v;
    }

    private void filterLanguages() {
        ArrayList<String> languages = new ArrayList<>();
        for (int i = 0; i < glLanguages.getChildCount(); i++) {
            View ch = glLanguages.getChildAt(i);
            if (ch instanceof CheckBox) {
                CheckBox cb = (CheckBox) ch;
                if (cb.isChecked())
                    languages.add((String) cb.getTag());
            }
        }
        Filters.FilterLanguages(movies, languages);
        Collections.sort(movies, Movie.VisibleSorter);
        adapter.notifyDataSetChanged();
    }

    private void printGenres(View v) {
        Log.d(TAG, "printGenres called");
        LinearLayout genreLayout = v.findViewById(R.id.genreLayout);

        LinearLayout ll = new LinearLayout(getContext());
        ll.setOrientation(LinearLayout.VERTICAL);
        bgDisabled = ContextCompat.getDrawable(getContext(), R.drawable.bg_genre);
        bgEnabled = ContextCompat.getDrawable(getContext(), R.drawable.bg_genre_on);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(300, LinearLayout.LayoutParams.WRAP_CONTENT);

        API.getGenres(getActivity().getApplicationContext(), (data, success) -> {
            if (success) {
                genres = (ArrayList<Genre>) data;

                textViewArray = new TextView[genres.size()];

                for (int i = 1; i < genres.size(); i++) {
                    textViewArray[i] = new TextView(getContext());
                    textViewArray[i].setText(genres.get(i).getName().toUpperCase());
                    textViewArray[i].setBackground(bgDisabled);
                    textViewArray[i].setGravity(Gravity.CENTER);
                    textViewArray[i].setTextSize(12);
                    textViewArray[i].setTextColor(Color.WHITE);
                    textViewArray[i].setTypeface(null, Typeface.BOLD);
                    layoutParams.setMargins(10, 10, 10, 10);
                    layoutParams.gravity = Gravity.CENTER;
                    int j = i;

                    textViewArray[i].setOnClickListener(v1 -> {
                        if (textViewArray[j].getBackground() == bgDisabled) {
                            Toast.makeText(getContext(), "Genre added: " + genres.get(j).getName(), Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "printGenres, Genre added: " + (genres.get(j).getId()));
                            textViewArray[j].setBackground(bgEnabled);
                            selectedGenresList.add(genres.get(j).getId());

                        } else if (textViewArray[j].getBackground() == bgEnabled) {
                            Toast.makeText(getContext(), "Genre removed: " + genres.get(j).getName(), Toast.LENGTH_SHORT).show();
                            textViewArray[j].setBackground(bgDisabled);

                            for (int k = 0; k < selectedGenresList.size(); k++)
                                if (selectedGenresList.get(k) == genres.get(k).getId()) {
                                    selectedGenresList.remove(genres.get(k).getId());
                                }
                        }
                    });
                    genreLayout.addView(textViewArray[i], layoutParams);
                }
            }
        });
    }

    private void sort() {
        Log.d(TAG, "sort called");
        int position = mSortDropdown.getSelectedItemPosition();
        switch (position) {
            case 1: //Rating
            {
                Collections.sort(movies, Movie.RatingSorter);
                Collections.reverse(movies);
                Collections.sort(movies, Movie.VisibleSorter);
                adapter.notifyDataSetChanged();
            }
            break; //Date - New - old
            case 2: {
                Collections.sort(movies, Movie.ReleaseDateSorter);
                Collections.reverse(movies);
                Collections.sort(movies, Movie.VisibleSorter);
                adapter.notifyDataSetChanged();
            }
            break;
            case 3: //Date - Old- new
            {
                Collections.sort(movies, Movie.ReleaseDateSorter);
                Collections.sort(movies, Movie.VisibleSorter);
                adapter.notifyDataSetChanged();
            }
            break;
            default:
                break;
        }
        mDrawerLayout.closeDrawer(mNavigation);
    }

    private void filterMovies() {
        ArrayList<Movie> allMoviesList = new ArrayList<>(movies);
        ArrayList<Movie> deletables = new ArrayList<>();
        for (int i = 0; i < allMoviesList.size(); i++) {

            ArrayList<Integer> movieGenresList = allMoviesList.get(i).getGenreIds();
            for (int j = 0; j < movieGenresList.size(); j++) {
                int genreId = movieGenresList.get(j);
                if (selectedGenresList.size() > 0 && !(this.selectedGenresList.contains(genreId))) {
                    deletables.add(allMoviesList.get(i));
                }
            }
        }

        for (Movie m : deletables) {
            allMoviesList.remove(m);
        }
        movies.clear();
        movies.addAll(allMoviesList);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), DisplayCalc.calculateNoOfColumns(getActivity()));
        rvMovies.setLayoutManager(layoutManager);
    }

    private class onSortTypeClick implements android.widget.AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            sort();
        }

        public void onNothingSelected(AdapterView<?> parent) {
        }
    }

    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

}

