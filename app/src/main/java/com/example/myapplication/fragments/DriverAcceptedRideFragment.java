package com.example.myapplication.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.activities.DriverMainActivity;
import com.example.myapplication.dialogs.CancelRideDialog;
import com.example.myapplication.dialogs.PanicDialog;
import com.example.myapplication.dto.NotificationDTO;
import com.example.myapplication.dto.PassengerDTO;
import com.example.myapplication.dto.RideDTO;
import com.example.myapplication.dto.UserDTO;
import com.example.myapplication.services.IRideService;
import com.example.myapplication.tools.Retrofit;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.Serializable;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DriverAcceptedRideFragment extends Fragment {

    private static final String ARG_PARAM_RIDEDTO = "rideDTO";

    private RideDTO rideDTO;

    public DriverAcceptedRideFragment() {
    }

    public static DriverAcceptedRideFragment newInstance(RideDTO rideDTO) {
        DriverAcceptedRideFragment fragment = new DriverAcceptedRideFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM_RIDEDTO, (Serializable) rideDTO);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            rideDTO = (RideDTO) getArguments().getSerializable(ARG_PARAM_RIDEDTO);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_driver_accepted_ride, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        displayRideDetails();
        setUpButtons();
    }

    private void displayRideDetails() {
        TextView departure = getView().findViewById(R.id.driver_accepted_ride_departure);
        departure.setText("Departure: " + rideDTO.getLocations().get(0).getDeparture().getAddress());
        TextView destination = getView().findViewById(R.id.driver_accepted_ride_destination);
        destination.setText("Destination: " + rideDTO.getLocations().get(0).getDestination().getAddress());
        TextView startTime = getView().findViewById(R.id.driver_accepted_ride_startTime);
        startTime.setText("Start time: " + rideDTO.getStartTime());
        TextView price = getView().findViewById(R.id.driver_accepted_ride_price);
        price.setText(rideDTO.getTotalCost() + "RSD");
        TextView estimatedMinutes = getView().findViewById(R.id.driver_accepted_ride_estimatedTime);
        estimatedMinutes.setText(rideDTO.getEstimatedTimeInMinutes() + " minutes");
    }

    private void setUpButtons() {
        Button cancel = (Button) getView().findViewById(R.id.btn_driver_accepted_ride_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                CancelRideDialog cancelRideDialog = CancelRideDialog.newInstance(rideDTO.getId().intValue());
                cancelRideDialog.show(getActivity().getSupportFragmentManager(), "cancel_dialog");
            }
        });
        Button start = (Button) getView().findViewById(R.id.btn_driver_accepted_ride_start);
        start.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                IRideService rideService = Retrofit.retrofit.create(IRideService.class);
                Call<Void> startRideCall = rideService.startRide(rideDTO.getId().intValue());

                startRideCall.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Log.d("TAG", "Ride started");
                        getActivity().finish();
                        getActivity().overridePendingTransition(0, 0);
                        startActivity(new Intent(getActivity(), DriverMainActivity.class));
                        getActivity().overridePendingTransition(0, 0);

                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.d("TAG", "Error when starting ride", t);
                    }
                });
            }
        });
    }

}