package com.example.myapplication.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.adapters.DriverActiveRidePassengersAdapter;
import com.example.myapplication.dto.PassengerDTO;
import com.example.myapplication.dto.RideDTO;
import com.example.myapplication.dto.UserDTO;
import com.example.myapplication.services.IPassengerService;
import com.example.myapplication.tools.FragmentTransition;
import com.example.myapplication.tools.Retrofit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DriverActiveRideFragment extends Fragment {

    private static final String ARG_PARAM_RIDEDTO = "rideDTO";

    private RideDTO rideDTO;

    private ListView listView;

    private List<PassengerDTO> passengers;

    private class TimerHandler {

        int seconds = 0, minutes = 0, hour = 0;

        public void run() {
            Timer timer = new Timer();
            TextView timerTextView = getView().findViewById(R.id.driver_active_ride_timer);
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            seconds += 1;
                            if (seconds == 60) {
                                minutes += 1;
                                if (minutes == 60) {
                                    hour += 1;
                                    minutes = 0;
                                } else hour = minutes / 60;
                                seconds = 0;
                                timerTextView.setText(String.format("%02d", hour) + ":" + String.format("%02d", minutes) + ":" + String.format("%02d", seconds));
                            }
                            timerTextView.setText(String.format("%02d", hour) + ":" + String.format("%02d", minutes) + ":" + String.format("%02d", seconds));
                        }
                    });
                }
            }, 0, 1000);
        }
    }

    public DriverActiveRideFragment() {
    }

       public static DriverActiveRideFragment newInstance(RideDTO rideDTO) {
        DriverActiveRideFragment fragment = new DriverActiveRideFragment();
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
        View v = inflater.inflate(R.layout.fragment_driver_active_ride, container, false);
        passengers = displayPassengers();
        listView = (ListView) v.findViewById(R.id.driver_active_ride_passengersList);
        //myLayout.addView(mListView)
//        ListView mListView = new ListView(getActivity());
        DriverActiveRidePassengersAdapter adapter = new DriverActiveRidePassengersAdapter(
                getActivity(),
                R.layout.driver_active_ride_passenger_cell,
                passengers);
        Log.i("TAG", "COUNT: " + adapter.getCount());
        listView.setAdapter(adapter);
        adapter.getCount();
        Log.i("TAG", "COUNT: " + adapter.getCount());
        Log.i("TAG", "PASSENGERS: " + passengers.size());

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.i("TAG", "Loaded rideDTO in DriverActiveFragment: " + rideDTO.toString());
        displayRideDetails();
        listView = (ListView) getView().findViewById(R.id.driver_active_ride_passengersList);
        //myLayout.addView(mListView)
//        ListView mListView = new ListView(getActivity());
        DriverActiveRidePassengersAdapter adapter = new DriverActiveRidePassengersAdapter(
                getActivity(),
                R.layout.driver_active_ride_passenger_cell,
                passengers);
        Log.i("TAG", "COUNT: " + adapter.getCount());
        listView.setAdapter(adapter);
        adapter.getCount();
        Log.i("TAG", "COUNT: " + adapter.getCount());
        Log.i("TAG", "PASSENGERS: " + passengers.size());
    }

    private void displayRideDetails() {
        TextView departure = getView().findViewById(R.id.driver_active_ride_departure);
        departure.setText("Departure: " + rideDTO.getLocations().get(0).getDeparture().getAddress());
        TextView destination = getView().findViewById(R.id.driver_active_ride_destination);
        destination.setText("Destination: " + rideDTO.getLocations().get(0).getDestination().getAddress());
        TextView startTime = getView().findViewById(R.id.driver_active_ride_startTime);
        startTime.setText("Start time: " + rideDTO.getStartTime());
        TextView price = getView().findViewById(R.id.driver_active_ride_price);
        price.setText(rideDTO.getTotalCost() + "RSD");

        displayPassengers();

        TimerHandler timerHandler = new TimerHandler();
        timerHandler.run();
    }

    private List<PassengerDTO> displayPassengers() {
        IPassengerService passengerService = Retrofit.retrofit.create(IPassengerService.class);

        List<PassengerDTO> passengers = new ArrayList<>();

        for (UserDTO userDTO : rideDTO.getPassengers()) {
            Call<PassengerDTO> passengerResponseCall = passengerService.getPassenger(userDTO.getId().intValue());
            passengerResponseCall.enqueue(new Callback<PassengerDTO>() {
                @Override
                public void onResponse(Call<PassengerDTO> call, Response<PassengerDTO> response) {

                    PassengerDTO passengerDTO = response.body();
                    passengers.add(passengerDTO);

                    Log.d("TAG", "Passenger for ride: " + rideDTO.getId() + " " + passengerDTO.getEmail());
                    DriverActiveRidePassengersAdapter adapter = new DriverActiveRidePassengersAdapter(
                            getActivity(),
                            R.layout.driver_active_ride_passenger_cell,
                            passengers);
                    Log.i("TAG", "COUNT: " + adapter.getCount());
                    listView.setAdapter(adapter);
                }

                @Override
                public void onFailure(Call<PassengerDTO> call, Throwable t) {
                    Log.d("TAG", "greska", t);
                }
            });
        }



        return passengers;
    }

    private void setUpButtons() {
        Button panic = (Button) getView().findViewById(R.id.btn_driver_active_ride_panic);
        panic.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            }
        });
        Button end = (Button) getView().findViewById(R.id.btn_driver_active_ride_end);
        panic.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            }
        });
    }

}

