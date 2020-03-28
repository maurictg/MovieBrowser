package com.avans.movieapp.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
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
    private ArrayList<Integer> genreIds;
    private String originalLanguage = "N/A";

    private boolean visible; //For filtering

    public Movie(int id, String title, String overview,
                 String imageUrlPoster, String imageUrlBackdrop, boolean isAdult,
                 Date releaseDate, double voteAverage, ArrayList<Integer> genreIds) {
        this.id = id;
        this.title = title;
        this.overview = overview;
        this.imageUrlPoster = imageUrlPoster;
        this.imageUrlBackdrop = imageUrlBackdrop;
        this.isAdult = isAdult;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
        this.genreIds = genreIds;
        this.visible = true;
    }

    public Movie(int id, String title, String overview,
                 String imageUrlPoster, String imageUrlBackdrop, boolean isAdult,
                 Date releaseDate, double voteAverage, ArrayList<Integer> genreIds, String originalLanguage) {
        this.id = id;
        this.title = title;
        this.overview = overview;
        this.imageUrlPoster = imageUrlPoster;
        this.imageUrlBackdrop = imageUrlBackdrop;
        this.isAdult = isAdult;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
        this.genreIds = genreIds;
        this.originalLanguage = originalLanguage;
        this.visible = true;
    }

    public ArrayList<Integer> getGenreIds() {
        return genreIds;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getImageUrlPoster() {
        return imageUrlPoster;
    }

    public void setImageUrlPoster(String imageUrlPoster) {
        this.imageUrlPoster = imageUrlPoster;
    }

    public String getImageUrlBackdrop() {
        return imageUrlBackdrop;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void setImageUrlBackdrop(String imageUrlBackdrop) {
        this.imageUrlBackdrop = imageUrlBackdrop;
    }

    public boolean isAdult() {
        return isAdult;
    }

    public String isAdultString() {
        if (isAdult) {
            return "yes";

        } else {
            return "no";
        }
    }

    public void setAdult(boolean adult) {
        isAdult = adult;
    }

    public Date getReleaseDate() {
        return (releaseDate != null) ? releaseDate : new Date(0);
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public String getVoteAverageString() {
        return voteAverage + "";
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    //Sorting using Collections.sort(ArrayList<Movie> movies, Movie.*Sorter);
    public static Comparator<Movie> ReleaseDateSorter = new Comparator<Movie>() {
        @Override
        public int compare(Movie o1, Movie o2) {
            return o1.getReleaseDate().compareTo(o2.getReleaseDate());
        }
    };

    public static Comparator<Movie> RatingSorter = new Comparator<Movie>() {
        @Override
        public int compare(Movie o1, Movie o2) {
            return (int)((o1.getVoteAverage()*100) - (o2.getVoteAverage()*100));
        }
    };

    public static Comparator<Movie> VisibleSorter = new Comparator<Movie>() {
        @Override
        public int compare(Movie o1, Movie o2) {
            return (o1.isVisible() == o2.isVisible() ? 0 : (o2.isVisible() ? 1 : -1));
        }
    };

}
