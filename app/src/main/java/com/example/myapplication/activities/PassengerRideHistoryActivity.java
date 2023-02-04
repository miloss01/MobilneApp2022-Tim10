package com.example.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.myapplication.R;
import com.example.myapplication.adapters.PassengerRideAdapter;
import com.example.myapplication.adapters.RideAdapter;
import com.example.myapplication.dto.RideDTO;
import com.example.myapplication.dto.RideResponseDTO;
import com.example.myapplication.fragments.RideDetailsFragment;
import com.example.myapplication.models.Ride;
import com.example.myapplication.services.IDriverService;
import com.example.myapplication.services.IPassengerService;
import com.example.myapplication.tools.FragmentTransition;
import com.example.myapplication.tools.Retrofit;

import java.time.LocalDateTime;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PassengerRideHistoryActivity extends AppCompatActivity {

    public static ArrayList<RideDTO> ridesList = new ArrayList<RideDTO>();
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_ride_history);

        Toolbar toolbar = findViewById(R.id.passenger_ridehistory_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Ride history");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        setupData();
    }

    private void setupData(){
        String passengerId = Retrofit.sharedPreferences.getString("user_id", null);
        IPassengerService passengerService = Retrofit.retrofit.create(IPassengerService.class);
        Call<RideResponseDTO> rideResponseDTOCall = passengerService.getPassengersRides(Integer.parseInt(passengerId));

        rideResponseDTOCall.enqueue(new Callback<RideResponseDTO>() {
            @Override
            public void onResponse(Call<RideResponseDTO> call, Response<RideResponseDTO> response) {
                RideResponseDTO rideResponseDTO = response.body();
                ridesList = rideResponseDTO.getResults();
                setupList();
                setupOnclickListener();
            }

            @Override
            public void onFailure(Call<RideResponseDTO> call, Throwable t) {
                Log.d("TAG", "Error for getting passenger rides", t);
            }
        });
    }

    private void setupList(){
        listView = (ListView) findViewById(R.id.passenger_rides_view);
        PassengerRideAdapter adapter = new PassengerRideAdapter(getApplicationContext(),
                R.layout.passenger_ride_cell, ridesList);
        listView.setAdapter(adapter);
     }

    private void setupOnclickListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                FragmentTransition.to((Fragment) RideDetailsFragment.newInstance(ridesList.get(position)),
                        PassengerRideHistoryActivity.this, true,
                        R.id.passenger_rides_rellay);
            }
        });
    }

}