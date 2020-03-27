package com.avans.movieapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
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
    private TextView summaryName;
    private TextView summary;
    private TextView age;
    private RatingBar rating;
    private TextView ratingNumber;
    private Button addToList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail);

        image = findViewById(R.id.movie_detail_image);
        title = findViewById(R.id.movie_detail_title);
        genre = findViewById(R.id.movie_detail_genre);
        company = findViewById(R.id.movie_detail_company);
        summaryName = findViewById(R.id.movie_detail_summary_name);
        summary = findViewById(R.id.movie_detail_summary);
        age = findViewById(R.id.movie_detail_age);
        rating = findViewById(R.id.movie_detail_rating);
        ratingNumber = findViewById(R.id.movie_detail_rating_number);
        addToList = findViewById(R.id.movie_detail_list);

        Intent intent = getIntent();
        this.movie = (Movie) intent.getSerializableExtra("MOVIE");

        API.getMovieDetails(movie, (data, success) -> {
            if (success) {
                MovieDetails movies = (MovieDetails) data;

                title.setText(movies.getTitle());
                genre.setText(movies.getGenre());
                company.setText(movies.getCompany());
                summaryName.setText("Summary: ");
                summary.setText(movies.getOverview());
                age.setText(String.format("Adult movie: %s", movies.isAdultString()));
                rating.setRating((int) movies.getVoteAverage());
                ratingNumber.setText(movies.getVoteAverageString());
            }
        });

        Picasso.get().load(movie.getImageUrlPoster()).into(image);
    }
}
