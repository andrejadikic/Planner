package com.example.planner.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.example.planner.R;

import timber.log.Timber;

public class SecondFragment extends Fragment {

    public SecondFragment() {
        super(R.layout.fragment_first);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.e("ON CREATE");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Timber.e("ON CREATE VIEW");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Timber.e("ON VIEW CREATED");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Timber.e("ON DESTROY VIEW");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Timber.e("ON DESTROY");
    }
}
