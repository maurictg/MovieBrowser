package com.avans.movieapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.ScrollView;
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
import java.util.Arrays;

public class MovieDetailsActivity extends AppCompatActivity {

    private static final String LIFECYCLE_CALLBACKS_TEXT_KEY = "callbacks";
    private final String TAG = MovieDetailsActivity.class.getSimpleName();
    public ArrayList<Movie> savedList;
    private Movie movie;
    private ImageView image;
    private TextView title;
    private TextView genre;
    private TextView company;
    private TextView summary;
    private TextView age;
    private RatingBar rating;
    private ImageButton addToList;
    private ImageButton share;
    private ImageButton ibReview;
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
        addToList = findViewById(R.id.movie_detail_list);
        share = findViewById(R.id.movie_detail_share);
        review = findViewById(R.id.movie_detail_review);

        LinearLayout main = findViewById(R.id.main);

        ibReview = findViewById(R.id.ibReview);
        ibReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ReviewActivity.class));
            }
        });

        savedList = new ArrayList<>();
        addToList.setOnClickListener(v -> {
            // TODO add to list
//            if () {
//            }
            Toast.makeText(this, movie.getTitle() + " added", Toast.LENGTH_SHORT).show();
        });

        share.setOnClickListener(v -> {
            Intent share = new Intent(Intent.ACTION_SEND);

            share.putExtra(Intent.EXTRA_TEXT, "I've found a movie for you! \n \n" + movie.getTitle() + "\n" + movie.getImageUrlBackdrop() + "\n\n" + movie.getOverview());
            share.setType("text/*");

            startActivity(Intent.createChooser(share, "Share via"));
        });

        Intent intent = getIntent();
        this.movie = (Movie) intent.getSerializableExtra("MOVIE");

        //Add to recent
        SharedPreferences sp = getSharedPreferences("MOVIES", MODE_PRIVATE);
        ArrayList<String> ids = new ArrayList<>(Arrays.asList(sp.getString("recent", "").split(",")));
        if (!ids.contains(Integer.toString(movie.getId()))) {
            ids.add(Integer.toString(movie.getId()));
            SharedPreferences.Editor e = sp.edit();
            e.putString("recent", TextUtils.join(",", ids));
            e.apply();
        }

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
