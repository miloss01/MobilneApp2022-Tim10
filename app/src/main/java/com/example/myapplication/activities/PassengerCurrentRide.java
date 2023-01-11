package com.example.myapplication.activities;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.myapplication.R;
import com.example.myapplication.dto.RideDTO;
import com.example.myapplication.services.IRideService;
import com.example.myapplication.services.MapService;
import com.example.myapplication.tools.Retrofit;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Action;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PassengerCurrentRide extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private List<LatLng> path = new ArrayList();
    private Marker simMarker;
    private MapService mapService;
    private TextView startTime;
    private TextView departureText;
    private TextView destinationText;
    private TextView price;
    private TextView time;
    private TextView driverData;
    private Integer elapsedTime = 0;
    private String estimatedTime = "NaN";

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_current_ride);

        mapService = new MapService();

        Toolbar toolbar = findViewById(R.id.passenger_current_ride_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Current ride");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        startTime = findViewById(R.id.pass_curr_ride_start_time);
        departureText = findViewById(R.id.pass_curr_ride_departure);
        destinationText = findViewById(R.id.pass_curr_ride_destination);
        price = findViewById(R.id.pass_curr_ride_price);
        time = findViewById(R.id.pass_curr_ride_time);
        driverData = findViewById(R.id.pass_curr_ride_driver_data);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.passenger_current_ride_map);
        mapFragment.getMapAsync(this);

        Button startSimBtn = findViewById(R.id.passenger_start_sim_btn);
        startSimBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                elapsedTime = 0;
                startSimulation();
            }
        });

        String passengerId = Retrofit.sharedPreferences.getString("user_id", null);
        Retrofit.stompClient.topic("/passenger/" + passengerId + "/end-ride").subscribe(topicMessage -> {
            Log.d("TAG", topicMessage.getPayload());

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "NOTIFICATION_CHANNEL")
                    .setContentTitle("Driver ended your ride")
                    .setContentText("Get out!")
                    .setSmallIcon(R.drawable.ic_message_icon)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

            notificationManager.notify(25234, builder.build());
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        String passengerId = Retrofit.sharedPreferences.getString("user_id", null);

        IRideService rideService = Retrofit.retrofit.create(IRideService.class);
        Call<RideDTO> activeRideResponseCall = rideService.getPassengerActiveRide(Integer.parseInt(passengerId));

        activeRideResponseCall.enqueue(new Callback<RideDTO>() {
            @Override
            public void onResponse(Call<RideDTO> call, Response<RideDTO> response) {
                if (response.code() != 200)
                    return;

                RideDTO rideDTO = response.body();
                Log.d("TAG", rideDTO.toString());

                startTime.setText(rideDTO.getStartTime());
                departureText.setText(rideDTO.getLocations().get(0).getDeparture().getAddress());
                destinationText.setText(rideDTO.getLocations().get(0).getDestination().getAddress());
                price.setText(rideDTO.getTotalCost() + "din");
                estimatedTime = String.valueOf(rideDTO.getEstimatedTimeInMinutes());
                time.setText(elapsedTime + "/" + estimatedTime + "min");
                driverData.setText(rideDTO.getDriver().getEmail());

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
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(departure, 14));

                LatLng destination = new LatLng(destinationLat, destinationLon);
                mMap.addMarker(new MarkerOptions()
                        .position(destination)
                        .title(destinationAddress));

                String origin = "" + departureLat + "," + departureLon;
                String end = "" + destinationLat + "," + destinationLon;

                path = mapService.getPath(origin, end);

                //Draw the polyline
                if (path.size() > 0) {
                    Log.d("TAG", "duzina" + path.size());
                    PolylineOptions opts = new PolylineOptions().addAll(path).color(Color.BLUE).width(5);
                    mMap.addPolyline(opts);
                }

                simMarker = mMap.addMarker(new MarkerOptions().position(path.get(0)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
            }

            @Override
            public void onFailure(Call<RideDTO> call, Throwable t) {
                Log.d("TAG", "greska", t);
            }
        });

    }

    private void showMarker(Integer i) {

        if (i > path.size() - 1) {
            String passengerId = Retrofit.sharedPreferences.getString("user_id", null);
            Retrofit.stompClient.send("/passenger/" + passengerId + "/end-ride", "gotovo").subscribe();
            return;
        }

        new android.os.Handler(Looper.getMainLooper()).postDelayed(
                new Runnable() {
                    public void run() {
                        moveMarker(path.get(i));

                        if (i > path.size() - 1 - 5 && i < path.size() - 1)
                            showMarker(path.size() - 1);
                        else
                            showMarker(i + 5);

                        Log.d("TAG", i + " od " + path.size());
                        time.setText(elapsedTime + "/" + estimatedTime + "min");
                        elapsedTime += 2;
                    }
                },
                1000);
    }

    private void startSimulation() {
        showMarker(0);
    }

    private void moveMarker(LatLng loc) {
        simMarker.remove();
        simMarker = mMap.addMarker(new MarkerOptions().position(loc).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
    }
}
