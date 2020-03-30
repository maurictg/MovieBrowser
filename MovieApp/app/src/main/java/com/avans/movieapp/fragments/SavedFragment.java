package com.avans.movieapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterViewAnimator;

import com.avans.movieapp.R;
import com.avans.movieapp.base_logic.DisplayCalc;
import com.avans.movieapp.models.Movie;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class SavedFragment extends Fragment implements Serializable {

    private static final String TAG = SavedFragment.class.getSimpleName();

    private Parcelable state;
    RecyclerView rvSaved;

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
