package com.avans.movieapp.models;

public class MovieDetails extends Movie {

    private int length;

    public MovieDetails(String title){
        super(title);

    }

    public int getLength() {
        return length;

    }


}
