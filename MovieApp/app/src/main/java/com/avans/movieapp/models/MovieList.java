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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
