package com.example.myapplication.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.models.Ride;

import java.time.format.DateTimeFormatter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RideDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RideDetailsFragment extends Fragment {

    private static Ride ride;

    public RideDetailsFragment() {
        // Required empty public constructor
    }

    public static Object newInstance(Ride ride) {
        RideDetailsFragment.ride = ride;
        return new RideDetailsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ride_details, container, false);

        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        TextView tvStartTime = (TextView) view.findViewById(R.id.textview_ridedetails_starttime);
        tvStartTime.setText(ride.getTime().getStart().format(format));

        TextView tvPrice = (TextView) view.findViewById(R.id.textview_ridedetails_price);
        String price = "$" + String.format("%,.2f", ride.getPrice());
        tvPrice.setText(price);

        return view;
    }
}