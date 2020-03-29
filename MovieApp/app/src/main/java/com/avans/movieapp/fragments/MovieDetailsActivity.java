package com.avans.movieapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.avans.movieapp.R;
import com.avans.movieapp.base_logic.API;
import com.avans.movieapp.models.Movie;
import com.avans.movieapp.models.MovieDetails;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieDetailsActivity extends AppCompatActivity {

    private final String TAG = MovieDetailsActivity.class.getSimpleName();
    private static final String LIFECYCLE_CALLBACKS_TEXT_KEY = "callbacks";

    private Movie movie;
    private ImageView image;
    private TextView title;
    private TextView genre;
    private TextView company;
    private TextView summary;
    private TextView age;
    private RatingBar rating;
    private TextView ratingNumber;
    private ImageButton addToList;

    public ArrayList<Movie> savedList;

    private RecyclerView rvSaved;

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
        ratingNumber = findViewById(R.id.movie_detail_rating_number);

        savedList = new ArrayList<>();
        addToList = findViewById(R.id.movie_detail_list);
        addToList.setOnClickListener(v -> {
            // TODO add to list
//            if () {
//            }
            Toast.makeText(this, movie.getTitle() + " added", Toast.LENGTH_SHORT).show();
        });

        Intent intent = getIntent();
        this.movie = (Movie) intent.getSerializableExtra("MOVIE");

        API.getMovieDetails(movie, (data, success) -> {
            if (success) {
                MovieDetails movies = (MovieDetails) data;
                title.setText(movies.getTitle());
                genre.setText(movies.getGenre());
                company.setText(movies.getCompany());
                summary.setText(movies.getOverview());
                age.setText(String.format("Adult movie: %s", movies.isAdultString()));
                rating.setRating((int) (movies.getVoteAverage() / 2));
            }
        });

        Picasso.get().load(movie.getImageUrlPoster()).into(image);
    }

}
