package com.example.myapplication.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DriverReportFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DriverReportFragment extends Fragment {


    public DriverReportFragment() {
        // Required empty public constructor
    }


    public static DriverReportFragment newInstance() {
        DriverReportFragment fragment = new DriverReportFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_driver_report, container, false);
    }
}