package com.avans.movieapp.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.avans.movieapp.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
    private ImageView profPic;

    private TextView int_prof_feed;
    private TextView int_prof_list;
    private TextView int_prof_reviews;

    private int feedCount = 0;
    private int listCount = 0;
    private int reviewCount = 0;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        profPic = v.findViewById(R.id.profilePic);
        int_prof_feed = v.findViewById(R.id.feed_notif_count);
        int_prof_list = v.findViewById(R.id.list_count_notif);
        int_prof_reviews = v.findViewById(R.id.review_notif_count);

        int_prof_feed.setGravity(Gravity.CENTER);
        int_prof_list.setGravity(Gravity.CENTER);
        int_prof_reviews.setGravity(Gravity.CENTER);

        int_prof_feed.setPadding(20,20,20,20);
        int_prof_list.setPadding(20,20,20,20);
        int_prof_reviews.setPadding(20,20,20,20);

        int_prof_feed.setText(feedCounter());
        int_prof_list.setText(listCounter());
        int_prof_reviews.setText(reviewCounter());

        /* TODO METHOD TO LOAD URL, WITH DEFAULT STATE. THIS IS A DUMMY PLACEMENT */
        Picasso
                .get()
                .load("https://moonvillageassociation.org/wp-content/uploads/2018/06/default-profile-picture1.jpg")
                .transform(new CircleTransform())
                .resize(200, 200)
                .into(profPic);
        return v;
    }

    private String feedCounter() {
        if (feedCount != 0) {
            int_prof_feed.setEnabled(true);
            return String.valueOf(feedCount);
        } else {
            return null;
        }
    }

    private String reviewCounter() {
        if (reviewCount != 0) {
            int_prof_reviews.setEnabled(true);
            return String.valueOf(reviewCount);
        } else {
            return null;
        }
    }
    private String listCounter() {
        if (listCount != 0) {
            int_prof_list.setEnabled(true);
            return String.valueOf(listCount);
        } else {
            return null;
        }
    }

    public class CircleTransform implements Transformation {
        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());

            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
            if (squaredBitmap != source) {
                source.recycle();
            }

            Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            BitmapShader shader = new BitmapShader(squaredBitmap,
                    Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            paint.setShader(shader);
            paint.setAntiAlias(true);

            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);

            squaredBitmap.recycle();
            return bitmap;
        }

        @Override
        public String key() {
            return "circle";
        }
    }

}
