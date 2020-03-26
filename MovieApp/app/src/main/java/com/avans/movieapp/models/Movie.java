package com.avans.movieapp.models;

import java.io.Serializable;
import java.util.Date;

public class Movie implements Serializable {

    private int id;
    private String title;
    private String overview;
    private String imageUrlPoster;
    private String imageUrlBackdrop;
    private boolean isAdult;
    private Date releaseDate;
    private double voteAverage;

    public Movie(int id, String title, String overview,
                 String imageUrlPoster, String imageUrlBackdrop, boolean isAdult,
                 Date releaseDate, double voteAverage) {
        this.id = id;
        this.title = title;
        this.overview = overview;
        this.imageUrlPoster = imageUrlPoster;
        this.imageUrlBackdrop = imageUrlBackdrop;
        this.isAdult = isAdult;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
    }


    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getImageUrlPoster() {
        return imageUrlPoster;
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

    public String getImageUrlBackdrop() {
        return imageUrlBackdrop;
    }
    //Relations
    //ArrayList with Genres
    //ArrayList with Languages
}
