package com.avans.movieapp.base_logic;

import android.util.Log;

import com.avans.movieapp.models.Movie;

import java.util.ArrayList;
import java.util.Collections;

public class Filters {
    public static void FilterLanguages(ArrayList<Movie> movies, ArrayList<String> languages) {
        for (Movie m: movies) {
            boolean satisfies = languages.contains(m.getOriginalLanguage());
            m.setVisible(satisfies);
        }
        sort(movies);
    }

    public static void FilterRating(ArrayList<Movie> movies, double minRating) {
        for (Movie m : movies) {
            boolean satisfies = (m.getVoteAverage() >= minRating);
            m.setVisible(satisfies);
        }
        sort(movies);
    }

    private static void sort(ArrayList<Movie> movies) {
        Collections.sort(movies, Movie.VisibleSorter);
    }
}
