package com.avans.movieapp.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.avans.movieapp.R;
import com.avans.movieapp.activities.SettingsActivity;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    TextView tvUsername;


    private ImageButton settings;
    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        tvUsername = v.findViewById(R.id.tvUsername);
        tvUsername.setText(R.string.username);

        ImageView profPic = v.findViewById(R.id.profilePic);

        settings = v.findViewById(R.id.btn_settings);
        settings.setOnClickListener(this::onClick);

        /* TODO METHOD TO LOAD URL, WITH DEFAULT STATE. THIS IS A DUMMY PLACEMENT */
        Picasso
                .get()
                .load("https://moonvillageassociation.org/wp-content/uploads/2018/06/default-profile-picture1.jpg")
                .transform(new CircleTransform())
                .resize(200, 200)
                .into(profPic);

        TextView tvTitle = v.findViewById(R.id.tvTitle);
        Shader shader = new LinearGradient(tvTitle.getWidth(), tvTitle.getLineHeight(),0 , 0, Color.parseColor("#00B3E4"), Color.parseColor("#90CEA1"),
                Shader.TileMode.REPEAT);
        tvTitle.getPaint().setShader(shader);
        return v;
    }

    public void onClick(View view) {
        Intent intent = new Intent(getActivity(), SettingsActivity.class);
        startActivity(intent);
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
