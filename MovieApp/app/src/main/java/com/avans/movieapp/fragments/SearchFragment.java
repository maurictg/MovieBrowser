package com.avans.movieapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.avans.movieapp.MainActivity;
import com.avans.movieapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_search, container, false);
        RecyclerView mHomeRecycler = v.findViewById(R.id.rvSearch);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), MainActivity.calculateNoOfColumns(getActivity()));
        mHomeRecycler.setLayoutManager(layoutManager);
        return v;
    }

}
