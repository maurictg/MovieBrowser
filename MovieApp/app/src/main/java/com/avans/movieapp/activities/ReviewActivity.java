package com.avans.movieapp.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.avans.movieapp.R;

public class ReviewActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_popup);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        getWindow().setLayout((int) (displayMetrics.widthPixels * .8), (int) (displayMetrics.heightPixels * .30));

        ImageButton closeBtn = findViewById(R.id.cancel_button);
        closeBtn.setOnClickListener(v -> finish());
        RatingBar review = findViewById(R.id.rt_review);
        ImageButton sendBtn = findViewById(R.id.ibSend);
        sendBtn.setOnClickListener(v -> {
            if (review.getRating() > .5) {
                Toast.makeText(getApplicationContext(), "Thanks for your review!", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }
}
