package com.avans.movieapp.models;

import java.util.Date;

public class Movie {

    private int id;
    private String title;
    private String description;
    private String imageUrl;
    private boolean isAdult;
    private Date releaseDate;
    private double voteAverage;

    public Movie(String title) {
        this.title = title; //For debug
    }


    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public boolean isAdult() {
        return isAdult;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    //Relations
    //ArrayList with Genres
    //ArrayList with Languages
}
