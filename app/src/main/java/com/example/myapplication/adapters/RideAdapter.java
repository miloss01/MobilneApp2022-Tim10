package com.example.myapplication.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.models.DriverRide;
import com.example.myapplication.tools.RideMockup;

public class RideAdapter extends BaseAdapter {
    private Activity activity;

    public RideAdapter(Activity activity) {
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return RideMockup.getMockupRides().size();
    }

    @Override
    public Object getItem(int i) {
        return RideMockup.getMockupRides().get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        DriverRide ride = RideMockup.getMockupRides().get(i);

        if(view==null)
            view = activity.getLayoutInflater().inflate(R.layout.driver_ride_history_list_item, null);

        TextView departure = view.findViewById(R.id.driver_ride_history_departure);
        TextView startTime = view.findViewById(R.id.driver_ride_history_startTime);
        TextView destination = view.findViewById(R.id.driver_ride_history_destination);
        TextView endTime = view.findViewById(R.id.driver_ride_history_endTime);
        TextView kilometers = view.findViewById(R.id.driver_ride_history_kilometers);
        TextView rating = view.findViewById(R.id.driver_ride_history_rating);
        TextView price = view.findViewById(R.id.driver_ride_history_price);

        departure.setText(ride.departure);
        startTime.setText(ride.startTime);
        destination.setText(ride.destination);
        endTime.setText(ride.endTime);
        kilometers.setText(ride.kilometers + " km");
        rating.setText("⭐ " + ride.rating);
        price.setText("+" + ride.price + " din");

        return view;
    }
}
