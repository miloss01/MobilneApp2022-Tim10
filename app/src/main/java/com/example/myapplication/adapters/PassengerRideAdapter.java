package com.example.myapplication.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.R;
import com.example.myapplication.dto.RideDTO;
import com.example.myapplication.models.Ride;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PassengerRideAdapter extends ArrayAdapter<RideDTO> {

    ArrayList<RideDTO> ridesList;

    public PassengerRideAdapter(Context context, int resource, List<RideDTO> ridesList) {
        super(context, resource, ridesList);
        this.ridesList = (ArrayList<RideDTO>) ridesList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        RideDTO ride = ridesList.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.passenger_ride_cell,
                    parent, false);
        }

        TextView tvTime = (TextView) convertView.findViewById(R.id.textview_ridecell_time);
        tvTime.setText(ride.getStartTime());

        TextView tvPrice = (TextView) convertView.findViewById(R.id.textview_ridecell_price);
        String price = String.valueOf(ride.getTotalCost()) + " RSD";
        tvPrice.setText(price);

        TextView tvDeparture = (TextView) convertView.findViewById(R.id.textview_ridecell_departure);
        tvDeparture.setText(ride.getLocations().get(0).getDeparture().getAddress());

        TextView tvDestination = (TextView) convertView.findViewById(R.id.textview_ridecell_destination);
        tvDestination.setText(ride.getLocations().get(0).getDestination().getAddress());


        return convertView;
    }
}
