package com.example.myapplication.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.myapplication.R;
import com.example.myapplication.tools.FragmentTransition;


public class DriverMainAccountFragment extends Fragment {


    public DriverMainAccountFragment() {
        // Required empty public constructor
    }

    public static DriverMainAccountFragment newInstance() {
        return new DriverMainAccountFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
            // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_driver_main_account, container, false);
        Button button = view.findViewById(R.id.driver_account_btn);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                FragmentTransition.to(DriverAccountFragment.newInstance(), getActivity(), false, R.id.downView);
            }
        });

        Button buttonStat = view.findViewById(R.id.driver_statistic_btn);
        buttonStat.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                FragmentTransition.to(DriverStatisticFragment.newInstance(), getActivity(), false, R.id.downView);
            }
        });

        Button buttonReport = view.findViewById(R.id.driver_report_btn);
        buttonReport.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                FragmentTransition.to(DriverReportFragment.newInstance(), getActivity(), false, R.id.downView);
            }
        });
        return view;
    }
}