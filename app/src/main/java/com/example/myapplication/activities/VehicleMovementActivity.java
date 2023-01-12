package com.example.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.myapplication.Constants;
import com.example.myapplication.R;
import com.example.myapplication.dto.DepartureDestinationLocationsDTO;
import com.example.myapplication.dto.RideDTO;
import com.example.myapplication.services.AuthService;
import com.example.myapplication.services.IRideService;
import com.example.myapplication.tools.Retrofit;

import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.disposables.Disposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;

public class VehicleMovementActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_movement);
//        Bundle extras = getIntent().getExtras();
//        Log.d("DEBUG", "USAOOOO");
//
//        if (extras != null) {
//            loadRide(extras);
//        }
    }

    private void loadRide(Bundle extras) {
        Integer rideId = extras.getInt("rideId");
        //The key argument here must match that used in the other activity
        IRideService rideService = Retrofit.retrofit.create(IRideService.class);
        Call<RideDTO> rideResponce = rideService.getRideById(rideId);

        rideResponce.enqueue(new Callback<RideDTO>() {
            @Override
            public void onResponse(Call<RideDTO> call, Response<RideDTO> response) {
                if (response.code() != 200)
                    return;

                RideDTO rideDTO = response.body();
                fillView(rideDTO);
            }

            @Override
            public void onFailure(Call<RideDTO> call, Throwable t) {
                Log.d("TAG", "upomoc");
            }
        });
    }

    private void fillView(RideDTO rideDTO) {
        TextView start = findViewById(R.id.star_textview);
        TextView end = findViewById(R.id.end_textview);
        TextView time = findViewById(R.id.time_textview);
        DepartureDestinationLocationsDTO departureDestination = rideDTO.getLocations().get(0);
        start.setText(departureDestination.getDeparture().getAddress());
        end.setText(departureDestination.getDestination().getAddress());
        for (int i = rideDTO.getEstimatedTimeInMinutes(); i >= 0; i--) {
            int SPLASH_TIME_OUT = 2000;
            int finalI = i;
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {

                    time.setText(finalI);
                    finish(); // da nebi mogao da ode back na splash
                }
            }, SPLASH_TIME_OUT);

        }
//        StompClient stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, Constants.websocketBaseUrl);
//        stompClient.connect();
//
//
//        Disposable subscribe = stompClient.topic("/vehicle-location").subscribe(topicMessage -> {
//            Log.d("TAG", topicMessage.getPayload());
//        });

    }
}