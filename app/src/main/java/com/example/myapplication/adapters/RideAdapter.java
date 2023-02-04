package com.example.myapplication.adapters;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.dto.RideDTO;
import com.example.myapplication.dto.RideResponseDTO;
import com.example.myapplication.models.DriverRide;
import com.example.myapplication.tools.RideMockup;

public class RideAdapter extends BaseAdapter {
    private Activity activity;
    private RideResponseDTO rideResponseDTO;

    public RideAdapter(Activity activity, RideResponseDTO rideResponseDTO) {
        this.activity = activity;
        this.rideResponseDTO = rideResponseDTO;
    }

    @Override
    public int getCount() {
        return rideResponseDTO.getTotalCount();
    }

    @Override
    public Object getItem(int i) {
        return rideResponseDTO.getResults().get(i);
    }

    @Override
    public long getItemId(int i) {
        return rideResponseDTO.getResults().get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        RideDTO rideDTO = rideResponseDTO.getResults().get(i);

        if(view==null)
            view = activity.getLayoutInflater().inflate(R.layout.driver_ride_history_list_item, null);

        TextView departure = view.findViewById(R.id.driver_ride_history_departure);
        TextView startTime = view.findViewById(R.id.driver_ride_history_startTime);
        TextView destination = view.findViewById(R.id.driver_ride_history_destination);
        TextView endTime = view.findViewById(R.id.driver_ride_history_endTime);
        TextView price = view.findViewById(R.id.driver_ride_history_price);

        departure.setText(rideDTO.getLocations().get(0).getDeparture().getAddress());
        startTime.setText(rideDTO.getStartTime());
        destination.setText(rideDTO.getLocations().get(0).getDestination().getAddress());
        endTime.setText(rideDTO.getEndTime());
        price.setText("+" + rideDTO.getTotalCost() + " din");

        return view;
    }
}
