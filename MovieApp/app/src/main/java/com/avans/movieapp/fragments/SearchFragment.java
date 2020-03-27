package com.avans.movieapp.fragments;

import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.avans.movieapp.R;
import com.avans.movieapp.adapters.VideosAdapter;
import com.avans.movieapp.base_logic.API;
import com.avans.movieapp.base_logic.DisplayCalc;
import com.avans.movieapp.models.Genre;
import com.avans.movieapp.models.Movie;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {

    private ArrayList<Movie> movies;
    private Spinner mSortDropdown;
    private ArrayList<Integer> genresList = new ArrayList<>();
    private TextView[] textViewArray;

    private Drawable bgEnabled;
    private Drawable bgDisabled;
    private ArrayList<Genre> genres;

    public SearchFragment() {
        // Required empty public constructor
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
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
                            Log.d("List: ", results.toString());
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

        printGenres(v);
        return v;
    }

    private void printGenres(View v) {
        Genre genre = null;

        LinearLayout genreLayout = v.findViewById(R.id.genreLayout);

        LinearLayout ll = new LinearLayout(getContext());
        ll.setOrientation(LinearLayout.VERTICAL);
        bgDisabled = ContextCompat.getDrawable(getContext(), R.drawable.bg_genre);
        bgEnabled = ContextCompat.getDrawable(getContext(), R.drawable.bg_genre_on);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(300, LinearLayout.LayoutParams.WRAP_CONTENT);

        API.getGenres((data, success) -> {
            if (success) {
                genres = (ArrayList<Genre>) data;

                textViewArray = new TextView[genres.size()];

                for (int i = 1; i < genres.size(); i++) {
                    textViewArray[i] = new TextView(getContext());
                    textViewArray[i].setText(genres.get(i).getName().toUpperCase());
                    textViewArray[i].setBackground(bgDisabled);
                    textViewArray[i].setGravity(Gravity.CENTER);
                    textViewArray[i].setTextSize(12);
                    textViewArray[i].setTypeface(null, Typeface.BOLD);
                    layoutParams.setMargins(10, 10, 10, 10);
                    layoutParams.gravity = Gravity.CENTER;
                    int j = i;
                    textViewArray[i].setOnClickListener(v1 -> {
                        if (textViewArray[j].getBackground() == bgDisabled) {
                            Toast.makeText(getContext(), "Genre added: " + genres.get(j).getName(), Toast.LENGTH_SHORT).show();
                            Log.d("Genre added: ", String.valueOf(genres.get(j).getId()));
                            textViewArray[j].setBackground(bgEnabled);
                            genresList.add(genres.get(j).getId());

                        } else if (textViewArray[j].getBackground() == bgEnabled) {
                            Toast.makeText(getContext(), "Genre removed: " + genres.get(j).getName(), Toast.LENGTH_SHORT).show();
                            textViewArray[j].setBackground(bgDisabled);
                            for (int k = 0; k < genresList.size(); k++)
                                if (genresList.get(k) == genres.get(k).getId())
                                    genresList.remove(genres.get(k).getId());
                        }
                    });
                    genreLayout.addView(textViewArray[i], layoutParams);
                }
            }
        });

    }


    private class onSortTypeClick implements android.widget.AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            Toast.makeText(getActivity(), mSortDropdown.getSelectedItem() + " selected", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }
}
