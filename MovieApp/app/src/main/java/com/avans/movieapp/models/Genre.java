package com.avans.movieapp.models;

public class Genre {

    private final String genreName;
    private final int genreId;

    public Genre(String genreName, int genreId) {
        this.genreName = genreName;
        this.genreId = genreId;
    }

    public String getName() {
        return genreName;
    }

    public int getId() {
        return genreId;
    }
}
