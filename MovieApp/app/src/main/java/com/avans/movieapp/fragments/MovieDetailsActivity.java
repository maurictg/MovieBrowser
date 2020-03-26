package com.avans.movieapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.avans.movieapp.R;
import com.avans.movieapp.base_logic.API;
import com.avans.movieapp.models.Movie;
import com.avans.movieapp.models.MovieDetails;
import com.squareup.picasso.Picasso;

public class MovieDetailsActivity extends AppCompatActivity {

    private final String TAG = MovieDetailsActivity.class.getSimpleName();

    private Movie movie;
    private ImageView image;
    private TextView title;
    private TextView genre;
    private TextView company;
    private TextView summary;
    private TextView age;
    private TextView rating;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail);

        image = findViewById(R.id.movie_detail_image);
        title = findViewById(R.id.movie_detail_title);
        genre = findViewById(R.id.movie_detail_genre);
        company = findViewById(R.id.movie_detail_company);
        summary = findViewById(R.id.movie_detail_summary);
        age = findViewById(R.id.movie_detail_age);
        rating = findViewById(R.id.movie_detail_rating);

        Intent intent = getIntent();
        this.movie = (Movie) intent.getSerializableExtra("MOVIE");

        API.getMovieDetails(movie, (data, success) -> {
            if (success) {
                MovieDetails movies = (MovieDetails) data;


                title.setText(movies.getTitle());
                genre.setText(movies.getGenre());
                company.setText(movies.getGenre());
                summary.setText(movies.getOverview());
                age.setText(movies.isAdultString());
                rating.setText(movies.getVoteAverageString());
            }
        });

        Picasso.get().load(movie.getImageUrlPoster()).into(image);
    }
}
