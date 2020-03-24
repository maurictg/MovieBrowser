package com.avans.movieapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.avans.movieapp.MainActivity;
import com.avans.movieapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SavedFragment extends Fragment {

    public SavedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment\
        View v = inflater.inflate(R.layout.fragment_saved, container, false);
        RecyclerView mHomeRecycler = v.findViewById(R.id.rvSave);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), MainActivity.calculateNoOfColumns(getActivity()));
        mHomeRecycler.setLayoutManager(layoutManager);
        return v;
    }
}
