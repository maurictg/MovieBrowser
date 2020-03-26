package com.avans.movieapp.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.avans.movieapp.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private TextView tvFeedCount;
    private TextView tvListCount;
    private TextView tvReviewCount;

    private int feedCount = 0;
    private int listCount = 0;
    private int reviewCount = 0;

    private ImageButton settings;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        ImageView profPic = v.findViewById(R.id.profilePic);
        tvFeedCount = v.findViewById(R.id.feed_notif_count);
        tvListCount = v.findViewById(R.id.list_count_notif);
        tvReviewCount = v.findViewById(R.id.review_notif_count);

        tvFeedCount.setGravity(Gravity.CENTER);
        tvListCount.setGravity(Gravity.CENTER);
        tvReviewCount.setGravity(Gravity.CENTER);

        tvFeedCount.setPadding(20, 20, 20, 20);
        tvListCount.setPadding(20, 20, 20, 20);
        tvReviewCount.setPadding(20, 20, 20, 20);

        tvFeedCount.setText(feedCounter());
        tvListCount.setText(listCounter());
        tvReviewCount.setText(reviewCounter());

        settings = v.findViewById(R.id.btn_settings);
        settings.setOnClickListener(this::onClick);

        /* TODO METHOD TO LOAD URL, WITH DEFAULT STATE. THIS IS A DUMMY PLACEMENT */
        Picasso
                .get()
                .load("https://moonvillageassociation.org/wp-content/uploads/2018/06/default-profile-picture1.jpg")
                .transform(new CircleTransform())
                .resize(200, 200)
                .into(profPic)  ;
        return v;
    }
    public void onClick(View view) {
        Intent intent = new Intent(getActivity(), SettingsActivity.class);
        startActivity(intent);
    }
    private String feedCounter() {
        if (feedCount != 0) {
            tvFeedCount.setEnabled(true);
            return String.valueOf(feedCount);
        } else {
            return null;
        }
    }

    private String reviewCounter() {
        if (reviewCount != 0) {
            tvReviewCount.setEnabled(true);
            return String.valueOf(reviewCount);
        } else {
            return null;
        }
    }

    private String listCounter() {
        if (listCount != 0) {
            tvListCount.setEnabled(true);
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
