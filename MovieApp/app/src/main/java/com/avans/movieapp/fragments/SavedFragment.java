package com.avans.movieapp.fragments;

import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.avans.movieapp.R;
import com.avans.movieapp.base_logic.DisplayCalc;

import java.io.Serializable;

/**
 * A simple {@link Fragment} subclass.
 */
public class SavedFragment extends Fragment implements Serializable {

    private static final String TAG = SavedFragment.class.getSimpleName();
    RecyclerView rvSaved;
    private Parcelable state;

    public SavedFragment() {
        // Required empty public constructor

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_saved, container, false);

        RecyclerView mSaveRecycler = v.findViewById(R.id.rvSave);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), DisplayCalc.calculateNoOfColumns(getActivity()));
        mSaveRecycler.setLayoutManager(layoutManager);
        TextView tvTitle = v.findViewById(R.id.tvTitle);
        Shader shader = new LinearGradient(tvTitle.getWidth(), tvTitle.getLineHeight(),0 , 0, Color.parseColor("#00B3E4"), Color.parseColor("#90CEA1"),
                Shader.TileMode.REPEAT);
        tvTitle.getPaint().setShader(shader);
        return v;
    }


    @Override
    public void onPause() {
        // Save ListView state @ onPause
        Log.d(TAG, "saving listview state @ onPause");
        //AdapterViewAnimator listView = null;
        //state = listView.onSaveInstanceState();
        super.onPause();
    }


}
