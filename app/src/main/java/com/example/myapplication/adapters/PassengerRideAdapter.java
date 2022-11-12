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
import com.example.myapplication.models.Ride;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PassengerRideAdapter extends ArrayAdapter<Ride> {

    ArrayList<Ride> ridesList;

    public PassengerRideAdapter(Context context, int resource, List<Ride> ridesList) {
        super(context, resource, ridesList);
        this.ridesList = (ArrayList<Ride>) ridesList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Ride ride = ridesList.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.passenger_ride_cell,
                    parent, false);
        }

        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        TextView tvTime = (TextView) convertView.findViewById(R.id.textview_ridecell_time);
        tvTime.setText(ride.getTime().getStart().format(format).toString());

        TextView tvPrice = (TextView) convertView.findViewById(R.id.textview_ridecell_price);
        String price = "$" + String.format("%,.2f", ride.getPrice());
        tvPrice.setText(price);

        return convertView;
    }
}
