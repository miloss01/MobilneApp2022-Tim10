package com.example.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.myapplication.R;
import com.example.myapplication.dto.ErrorMessage;
import com.example.myapplication.dto.LoginDTO;
import com.example.myapplication.dto.RideDTO;
import com.example.myapplication.dto.TokenResponseDTO;
import com.example.myapplication.services.AuthService;
import com.example.myapplication.services.IRideService;
import com.example.myapplication.tools.Retrofit;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DriverMainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private AuthService authService;
    private GoogleMap mMap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_main);

        authService = new AuthService(this);

        Toolbar toolbar = findViewById(R.id.driver_main_toolbar);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Uber");
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.driver_map);
        mapFragment.getMapAsync(this);

        Switch toggle = findViewById(R.id.driver_main_toggle);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                TextView t = findViewById(R.id.driver_main_label_active);
                if (isChecked) {
                    t.setText("ACTIVE");
                } else {
                    t.setText("NOT ACTIVE");
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.driver_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.driver_menu_history) {
            Intent intent1 = new Intent(this, DriverRideHistoryActivity.class);
            this.startActivity(intent1);
            return true;
        }
        if (id == R.id.driver_menu_account) {
            Intent intent1 = new Intent(this, DriverAccountActivity.class);
            this.startActivity(intent1);
            return true;
        }
        if (id == R.id.driver_menu_inbox) {
            Intent intent1 = new Intent(this, DriverInboxActivity.class);
            this.startActivity(intent1);
            return true;
        }
        if (id == R.id.driver_logout_account) {
            authService.logout();
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        String driverId = Retrofit.sharedPreferences.getString("user_id", null);

        IRideService rideService = Retrofit.retrofit.create(IRideService.class);
        Call<RideDTO> activeRideResponseCall = rideService.getDriverActiveRide(Integer.parseInt(driverId));

        activeRideResponseCall.enqueue(new Callback<RideDTO>() {
            @Override
            public void onResponse(Call<RideDTO> call, Response<RideDTO> response) {
                if (response.code() != 200)
                    return;

                RideDTO rideDTO = response.body();
                Log.d("TAG", rideDTO.toString());

                String departureAddress = rideDTO.getLocations().get(0).getDeparture().getAddress();
                double departureLat = rideDTO.getLocations().get(0).getDeparture().getLatitude();
                double departureLon = rideDTO.getLocations().get(0).getDeparture().getLongitude();
                String destinationAddress = rideDTO.getLocations().get(0).getDestination().getAddress();
                double destinationLat = rideDTO.getLocations().get(0).getDestination().getLatitude();
                double destinationLon = rideDTO.getLocations().get(0).getDestination().getLongitude();

                LatLng departure = new LatLng(departureLat, departureLon);
                mMap.addMarker(new MarkerOptions()
                        .position(departure)
                        .title(departureAddress));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(departure, 13));

                LatLng destination = new LatLng(destinationLat, destinationLon);
                mMap.addMarker(new MarkerOptions()
                        .position(destination)
                        .title(destinationAddress));

                
            }

            @Override
            public void onFailure(Call<RideDTO> call, Throwable t) {
                Log.d("TAG", "greska", t);
            }
        });
    }
}
