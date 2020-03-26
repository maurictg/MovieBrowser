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
                        int length, String genre, String company, ArrayList<String> actors, String director) {
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

    public void setLength(int length) {
        this.length = length;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public ArrayList<String> getActors() {
        return actors;
    }

    public void setActors(ArrayList<String> actors) {
        this.actors = actors;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }
}
