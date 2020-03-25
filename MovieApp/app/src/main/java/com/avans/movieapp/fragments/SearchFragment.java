package com.avans.movieapp.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.avans.movieapp.R;
import com.avans.movieapp.base_logic.DisplayCalc;

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
        RecyclerView mSearchRecycler = v.findViewById(R.id.rvSearch);
        EditText editText = (EditText) v.findViewById(R.id.etSearch);
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
//              Might work better
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                Empty required method
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //TODO Listener to search update
            }

        });
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), DisplayCalc.calculateNoOfColumns(getActivity()));
        mSearchRecycler.setLayoutManager(layoutManager);
        return v;
    }

}
