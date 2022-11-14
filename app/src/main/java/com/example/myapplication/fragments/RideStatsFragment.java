package com.example.myapplication.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.activities.PassengerAccountActivity;
import com.example.myapplication.models.Ride;

public class RideStatsFragment extends Fragment {

    private static PassengerAccountActivity view;

    public RideStatsFragment() {
        // Required empty public constructor
    }

    public static RideStatsFragment newInstance(PassengerAccountActivity view) {
        RideStatsFragment.view = view;
        return new RideStatsFragment();
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ride_stats, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDestroyView() {
        Toolbar tb = view.findViewById(R.id.passenger_account_toolbar);
        tb.setTitle("Account");
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        Toolbar tb = view.findViewById(R.id.passenger_account_toolbar);
        tb.setTitle("Account");
        super.onDetach();
    }
}