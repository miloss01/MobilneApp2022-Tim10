package com.example.myapplication.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.text.AlteredCharSequence;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;

import com.example.myapplication.R;
import com.example.myapplication.dto.LocationDTO;
import com.example.myapplication.dto.NotificationDTO;
import com.example.myapplication.services.AuthService;
import com.example.myapplication.fragments.MapFragment;
import com.example.myapplication.tools.FragmentTransition;
import com.example.myapplication.tools.Retrofit;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.material.snackbar.Snackbar;

import java.util.zip.Inflater;

public class PassengerMainActivity extends AppCompatActivity {

    private CharSequence mTitle;
    private AlertDialog dialog;
    private AlertDialog.Builder dialogBuilder;
//    private EditText new_location;
//    private Button add_location_btn, cancel_location_btn;
    private View view;
    private AuthService authService;
    private EditText departure;
    private EditText destination;
    private LocationDTO departureDTO = new LocationDTO();
    private LocationDTO destinationDTO = new LocationDTO();
    private MapFragment mapFragment;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_main);
        mapFragment = MapFragment.newInstance();
        FragmentTransition.to(mapFragment, this, false, R.id.passenger_map_container);

        departure = findViewById(R.id.pass_departure);
        destination = findViewById(R.id.pass_destination);
        authService = new AuthService(this);

        Toolbar toolbar = findViewById(R.id.passenger_main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Uber");

//        Button buttonStat = this.findViewById(R.id.add_departure_to_favoutites_btn);
//        buttonStat.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
//                createAddLocationDialog();
//            }
//        });
        //mapFragment.loadVehicles();
        findViewById(R.id.pass_main_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (departure.getText().toString().equals("") || destination.getText().toString().equals("")) {
                    Snackbar.make(view, "Booking aborted. Fill in the locations", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    return;
                }
                departureDTO.setAddress(departure.getText().toString());
                destinationDTO.setAddress(destination.getText().toString());
                mapFragment.drawRoute(departureDTO, destinationDTO);
            }

        });
        Button rideCreation = this.findViewById(R.id.open_ride_creation);
        rideCreation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PassengerMainActivity.this, RideCreationActivity.class);
                startActivity(intent);
            }
        });


//        FragmentTransition.to(MapFragment.newInstance(), this, false, R.id.passenger_map_container);

        String passengerId = Retrofit.sharedPreferences.getString("user_id", null);
        Retrofit.stompClient.topic("/ride-notification-passenger/" + passengerId).subscribe(topicMessage -> {
            Log.d("TAG", topicMessage.getPayload());

            ObjectMapper objectMapper = new ObjectMapper();
            NotificationDTO notificationDTO = objectMapper.readValue(topicMessage.getPayload(), NotificationDTO.class);

            if (notificationDTO.getReason().equals("START_RIDE")) {

                Intent intent = new Intent(this, PassengerCurrentRide.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "NOTIFICATION_CHANNEL")
                        .setContentTitle("Driver started your ride")
                        .setContentText("Click to go to current ride page!")
                        .setSmallIcon(R.drawable.ic_message_icon)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true);

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

                notificationManager.notify(1000, builder.build());
            }
        });

        // ovaj deo je samo za testiranje, notifikacija na ovaj kanal se salje kad vozac
        // pritisne na start ride dugme (verovatno ce se dobiti sa beka)
        NotificationDTO data = new NotificationDTO("message", 1, "START_RIDE");
        ObjectMapper objectMapper = new ObjectMapper();
        String json = "asd";
        try {
            json = objectMapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        Retrofit.stompClient.send("/ride-notification-passenger/" + passengerId, json).subscribe();

    }

    @Override
    public void onResume(){
        super.onResume();
        //mapFragment.loadVehicles();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_passenger, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_passenger_account) {
            this.startActivity(new Intent(this, PassengerAccountActivity.class));
            return true;
        }
        if (id == R.id.action_passenger_ride_history) {
            this.startActivity(new Intent(this, PassengerRideHistoryActivity.class));
            return true;
        }
        if (id == R.id.action_passenger_inbox) {
            this.startActivity(new Intent(this, PassengerInboxActivity.class));
            return true;
        }
        if (id == R.id.action_passenger_logout) {
            authService.logout();
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }


//    public void createAddLocationDialog(){
//        dialogBuilder = new AlertDialog.Builder(this);
//        final View newLocationPopup = getLayoutInflater().inflate(R.layout.new_location, null);
//        new_location = (EditText) newLocationPopup.findViewById(R.id.new_location);
//
//        add_location_btn = (Button) newLocationPopup.findViewById(R.id.add_departure_to_favoutites_btn);
//        cancel_location_btn = (Button) newLocationPopup.findViewById(R.id.cancel_location_btn);
//
//        dialogBuilder.setView(newLocationPopup);
//        dialog = dialogBuilder.create();
//        dialog.show();
//
//        add_location_btn.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
////nanananananan
//            }
//        });
//
//        cancel_location_btn.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
//                dialog.dismiss();
//            }
//        });
//    }


}