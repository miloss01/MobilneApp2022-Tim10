package com.example.myapplication.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;


public class DriverStatisticFragment extends Fragment {
    private View view;
    private TextView acceptanceRateTextView;
    private TextView workingHoursTextView;
    private TextView incomeTextView;


    public DriverStatisticFragment() {
        // Required empty public constructor
    }


    public static DriverStatisticFragment newInstance() {
        DriverStatisticFragment fragment = new DriverStatisticFragment();

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
        view = inflater.inflate(R.layout.fragment_driver_statistic, container, false);
        incomeTextView = view.findViewById(R.id.incoome);
        workingHoursTextView = view.findViewById(R.id.working_h);
        acceptanceRateTextView = view.findViewById(R.id.acceptance);
        view.findViewById(R.id.radio_day_stat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatedayStatistics();
            }
        });
        view.findViewById(R.id.radio_month_stat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateMonthStatistics();
            }
        });
        view.findViewById(R.id.radio_year_stat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateYearStatistics();
            }
        });
        return view;
    }

    private void updateYearStatistics() {
        acceptanceRateTextView.setText("god");
    }

    private void updateMonthStatistics() {
        acceptanceRateTextView.setText("god");
    }

    private void updatedayStatistics() {
        acceptanceRateTextView.setText("god");
    }
}