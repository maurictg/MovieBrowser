package com.avans.movieapp;

import com.avans.movieapp.models.MovieDetails;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    private Date date = new Date();
    private ArrayList<Integer> listInteger = new ArrayList<>();
    private ArrayList<String> listString = new ArrayList<>();

    private MovieDetails movie = new MovieDetails(1, "Henk, The Movie", "Overview", "", "", true, date, 4.2, listInteger, 12, "Action", "FireFox", listString, "Henk van Voort");

    @Test
    public void createMovieWithCapitalsResultTrue() {

        Assert.assertEquals("Henk, The Movie", movie.getTitle());
    }

    @Test
    public void createMovieNoCapitalsResultFalse() {

        Assert.assertNotEquals("henk, the movie", movie.getTitle());
    }

    @Test
    public void createMovieAdultToStringResultTrue() {

        Assert.assertEquals("yes", movie.isAdultString());
    }

    @Test
    public void createMovieAdultToStringResultFalse() {

        Assert.assertNotEquals("true", movie.isAdultString());
    }
}