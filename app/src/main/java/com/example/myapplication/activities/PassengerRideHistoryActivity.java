package com.example.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.example.myapplication.R;
import com.example.myapplication.adapters.PassengerRideAdapter;
import com.example.myapplication.models.Ride;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class PassengerRideHistoryActivity extends AppCompatActivity {

    public static ArrayList<Ride> ridesList = new ArrayList<Ride>();
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_ride_history);

        //toolbar.setTitle("Your previous rides")

        setupData();
        setupList();

    }

    private void setupData(){
        for (int i = 0; i < 3; i++) {
            ridesList.add(new Ride((i+2.3)*17, LocalDateTime.now().minusHours(3).plusMinutes(i*13), LocalDateTime.now().minusHours(3).plusMinutes(14*i)));
        }
    }

    private void setupList(){
        listView = (ListView) findViewById(R.id.passenger_rides_view);
        //PassengerRidesBaseAdapter adapter = new PassengerRidesBaseAdapter(PassengerRideHistoryActivity.this);
        PassengerRideAdapter adapter = new PassengerRideAdapter(getApplicationContext(),
                R.layout.passenger_ride_cell, ridesList);
        listView.setAdapter(adapter);

     }

    private void setupOnclickListener() {

    }

}