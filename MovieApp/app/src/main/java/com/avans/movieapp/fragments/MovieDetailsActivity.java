package com.avans.movieapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.avans.movieapp.R;
import com.avans.movieapp.models.Movie;
import com.squareup.picasso.Picasso;

public class MovieDetailsActivity extends Fragment {

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.movie_detail, container, false);

        image = v.findViewById(R.id.movie_detail_image);
        title = v.findViewById(R.id.movie_detail_title);
        genre = v.findViewById(R.id.movie_detail_genre);
        company = v.findViewById(R.id.movie_detail_company);
        summary = v.findViewById(R.id.movie_detail_summary);
        age = v.findViewById(R.id.movie_detail_age);
        rating = v.findViewById(R.id.movie_detail_rating);

        Picasso.get().load(movie.getImageUrlPoster()).into(image);
        title.setText(movie.getTitle());
        genre.setText("");
        company.setText("");
        summary.setText("");
        age.setText("");
        rating.setText("");

        return v;
    }
}
