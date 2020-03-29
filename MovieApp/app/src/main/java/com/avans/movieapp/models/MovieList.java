package com.avans.movieapp.models;

public class MovieList {

    private String description;
    private int id;
    private int itemCount;
    private String name;

    public MovieList(String description, int id, int itemCount, String name){
        this.description = description;
        this.id = id;
        this.itemCount = itemCount;
        this.name = name;
    }

    public int getId() {
        return id;
    }
}
