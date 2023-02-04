package com.example.myapplication.activities;

import static java.security.AccessController.getContext;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
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
import androidx.fragment.app.DialogFragment;

import com.example.myapplication.R;
import com.example.myapplication.dialogs.MakeReviewDialog;
import com.example.myapplication.dialogs.PanicDialog;
import com.example.myapplication.dialogs.QuickMessageDialog;
import com.example.myapplication.dto.DriverDTO;
import com.example.myapplication.dto.IsActiveDTO;
import com.example.myapplication.dto.NotificationDTO;
import com.example.myapplication.dto.ReasonDTO;
import com.example.myapplication.dto.RideDTO;
import com.example.myapplication.providers.NotificationProvider;
import com.example.myapplication.services.IAuthService;
import com.example.myapplication.services.IDriverService;
import com.example.myapplication.services.IRideService;
import com.example.myapplication.services.MapService;
import com.example.myapplication.tools.Retrofit;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.time.LocalDateTime;
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
    private DriverDTO driverDTO = new DriverDTO();
    private RideDTO rideDTO = new RideDTO();

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
        Retrofit.stompClient.topic("/ride-notification-passenger/" + passengerId).subscribe(topicMessage -> {
            Log.d("TAG", "asddd" + topicMessage.getPayload());
            Log.d("TAG", "opaljen");

            ObjectMapper objectMapper = new ObjectMapper();
            NotificationDTO notificationDTO = objectMapper.readValue(topicMessage.getPayload(), NotificationDTO.class);

            if (notificationDTO.getReason().equals("END_RIDE")) {

                NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "NOTIFICATION_CHANNEL")
                        .setContentTitle("Driver ended your ride")
                        .setContentText("Get out!")
                        .setSmallIcon(R.drawable.ic_message_icon)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setAutoCancel(true);

                ContentValues values = new ContentValues();
                values.put(NotificationProvider.MESSAGE, "Driver ended your ride");
                values.put(NotificationProvider.TIME_OF_RECEIVING, String.valueOf(LocalDateTime.now()));
                values.put(NotificationProvider.RECEIVER_ID, passengerId);
                Uri uri = getContentResolver().insert(
                        NotificationProvider.CONTENT_URI, values);

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

                notificationManager.notify(25234, builder.build());

                makeReview();
            }
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

                rideDTO = response.body();
                Log.d("TAG", rideDTO.toString());

                startTime.setText(rideDTO.getStartTime());
                departureText.setText(rideDTO.getLocations().get(0).getDeparture().getAddress());
                destinationText.setText(rideDTO.getLocations().get(0).getDestination().getAddress());
                price.setText(rideDTO.getTotalCost() + "din");
                estimatedTime = String.valueOf(rideDTO.getEstimatedTimeInMinutes());
                time.setText(elapsedTime + "/" + estimatedTime + "min");
                driverData.setText(rideDTO.getDriver().getEmail());

                IDriverService driverService = Retrofit.retrofit.create(IDriverService.class);
                Call<DriverDTO> getDriverResponse = driverService.getDriver(rideDTO.getDriver().getId().intValue());

                getDriverResponse.enqueue(new Callback<DriverDTO>() {
                    @Override
                    public void onResponse(Call<DriverDTO> call, Response<DriverDTO> response) {

                        if (response.code() != 200)
                            return;

                        driverDTO = response.body();

                        String ret = driverDTO.getName() + " " + driverDTO.getSurname() + " (" + driverDTO.getEmail() + ")";
                        driverData.setText(ret);

                        Button callBtn = findViewById(R.id.passenger_start_call_btn);
                        callBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(Intent.ACTION_DIAL);
                                intent.setData(Uri.parse("tel:" + driverDTO.getTelephoneNumber()));
                                startActivity(intent);
                            }
                        });

                        Button messageBtn = findViewById(R.id.passenger_message_btn);
                        messageBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
//                                Intent intent = new Intent(Intent.ACTION_SEND);
//                                intent.setData(Uri.parse("smsto:" + driverDTO.getTelephoneNumber()));
//                                if (intent.resolveActivity(getPackageManager()) != null) {
//                                    startActivity(intent);
//                                }
                                DialogFragment messageDialog = QuickMessageDialog.newInstance(rideDTO, false);
                                messageDialog.show(getSupportFragmentManager(), "quick_message_dialog");
                            }
                        });

                        Button panicBtn = findViewById(R.id.passenger_start_panic_btn);
                        panicBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                DialogFragment panicDialog = PanicDialog.newInstance(rideDTO.getId().intValue());
                                panicDialog.show(getSupportFragmentManager(), "panic_dialog");
                            }
                        });

                    }

                    @Override
                    public void onFailure(Call<DriverDTO> call, Throwable t) {
                        Log.d("TAG", "greska u povlacenju drivera", t);
                    }
                });

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
            // ovaj deo je samo za testiranje, notifikacija na ovaj kanal se salje kad vozac
            // pritisne na end ride dugme (verovatno ce se dobiti sa beka)
            String passengerId = Retrofit.sharedPreferences.getString("user_id", null);
            NotificationDTO data = new NotificationDTO("message", 1, "END_RIDE");
            ObjectMapper objectMapper = new ObjectMapper();
            String json = "asd";
            try {
                json = objectMapper.writeValueAsString(data);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            Retrofit.stompClient.send("/ride-notification-passenger/" + passengerId, json).subscribe();
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

    private void makeReview() {
        DialogFragment makeReviewDialog = MakeReviewDialog.newInstance(rideDTO.getId().intValue());
        makeReviewDialog.show(getSupportFragmentManager(), "make_review_dialog");
    }

}
