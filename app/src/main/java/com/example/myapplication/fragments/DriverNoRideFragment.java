package com.example.myapplication.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.myapplication.R;

public class DriverNoRideFragment extends Fragment {

    private static final String ARG_PARAM_USERID = "userId";

    private String userId;

    public DriverNoRideFragment() {
    }

    public static DriverNoRideFragment newInstance(String userId) {
        DriverNoRideFragment fragment = new DriverNoRideFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM_USERID, userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userId = getArguments().getString(ARG_PARAM_USERID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_driver_no_ride, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Switch toggle = getView().findViewById(R.id.driver_main_toggle);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                TextView t = getView().findViewById(R.id.driver_main_label_active);
                // Send request to backend to change active status TODO
                if (isChecked) {
                    t.setText("ACTIVE");
                } else {
                    t.setText("NOT ACTIVE");
                }
            }
        });

    }
}