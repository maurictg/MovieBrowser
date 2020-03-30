package com.avans.movieapp.base_logic;

import com.avans.movieapp.models.Movie;

import java.util.ArrayList;
import java.util.Collections;

public class Filters {
    public static void FilterLanguages(ArrayList<Movie> movies, String language) {
        for (Movie m : movies) {
            boolean satisfies = m.getOriginalLanguage().equalsIgnoreCase(language);
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
