package com.avans.movieapp.models;

import java.util.ArrayList;
import java.util.Date;

public class MovieDetails extends Movie {

    private int length;
    private String genre;
    private String company;
    private ArrayList<String> actors;
    private String director;

    public MovieDetails(int id, String title, String overview, String imageUrlPoster, String imageUrlBackdrop, boolean isAdult, Date releaseDate, double voteAverage,
                        int length, String genre, String company, ArrayList<String> actors, String director){
        super(id, title, overview, imageUrlPoster, imageUrlBackdrop, isAdult, releaseDate, voteAverage);
        this.length = length;
        this.genre = genre;
        this.company = company;
        this.actors = actors;
        this.director = director;

    }

    public int getLength() {
        return length;

    }


}
