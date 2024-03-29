package com.example.myapplication.activities;

import static java.lang.Math.round;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.text.AlteredCharSequence;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.myapplication.Constants;
import com.example.myapplication.R;
import com.example.myapplication.dto.DepartureDestinationLocationsDTO;
import com.example.myapplication.dto.EstimatedDataRequestDTO;
import com.example.myapplication.dto.EstimatedDataResponseDTO;
import com.example.myapplication.dto.Estimation;
import com.example.myapplication.dto.LocationDTO;
import com.example.myapplication.dto.MessageSentDTO;
import com.example.myapplication.dto.NotificationDTO;
import com.example.myapplication.providers.NotificationProvider;
import com.example.myapplication.services.AuthService;
import com.example.myapplication.fragments.MapFragment;
import com.example.myapplication.services.IPassengerService;
import com.example.myapplication.tools.FragmentTransition;
import com.example.myapplication.tools.Retrofit;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.material.snackbar.Snackbar;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import io.reactivex.disposables.Disposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;

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
    private String passengerId;
    private boolean gotNotification = false;

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
        passengerId = Retrofit.sharedPreferences.getString("user_id", null);

        //if (gotNotification)  fillDataTime();

        Intent i = getIntent();
        if (i != null && i.hasExtra("DEPARTURE") && i.hasExtra("DESTINATION")) {
            departure.setText(i.getStringExtra("DEPARTURE"));
            destination.setText(i.getStringExtra("DESTINATION"));
        }

        findViewById(R.id.pass_main_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (departure.getText().toString().equals("") || destination.getText().toString().equals("")) {
                    Snackbar.make(view, "Booking aborted. Fill in the locations", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    return;
                }
                updateMapAndEstimatedData();
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

        //String passengerId = Retrofit.sharedPreferences.getString("user_id", null);
//        Retrofit.stompClient.topic("/vehicle-time/" + passengerId).subscribe(topicMessage -> {
//            Log.d("TAG", topicMessage.getPayload());
//            TextView header = this.findViewById(R.id.headerForTime);
//            TextView time = this.findViewById(R.id.fillerForTime);
//            header.setText(R.string.arive_time_header);
//        });



        Retrofit.stompClient.topic("/ride-notification-passenger/" + passengerId).subscribe(topicMessage -> {
            Log.d("TAG", topicMessage.getPayload());

            ObjectMapper objectMapper = new ObjectMapper();
            NotificationDTO notificationDTO = objectMapper.readValue(topicMessage.getPayload(), NotificationDTO.class);

            if (notificationDTO.getReason().equals("START_RIDE")) {

                Intent intent = new Intent(this, PassengerCurrentRide.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "NOTIFICATION_CHANNEL")
                        .setContentTitle("Driver started your ride")
                        .setContentText("Click to go to current ride page!")
                        .setSmallIcon(R.drawable.ic_message_icon)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true);

                ContentValues values = new ContentValues();
                values.put(NotificationProvider.MESSAGE, "Driver started your ride");
                values.put(NotificationProvider.TIME_OF_RECEIVING, String.valueOf(LocalDateTime.now()));
                values.put(NotificationProvider.RECEIVER_ID, authService.getUserData().get("user_id"));
                Uri uri = getContentResolver().insert(
                        NotificationProvider.CONTENT_URI, values);

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

                notificationManager.notify(1000, builder.build());
            }
            if (notificationDTO.getReason().equals("ACCEPT_RIDE")) {
                Intent intent = new Intent(this, VehicleMovementActivity.class);
                Log.d("DEBUG", "pre extra");
                intent.putExtra("rideId", notificationDTO.getRideId());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                        PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
                Log.d("DEBUG", "posle extra");

                NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "NOTIFICATION_CHANNEL")
                        .setContentTitle("Driver Accepted your ride")
                        .setContentText(notificationDTO.getMessage())
                        .setSmallIcon(R.drawable.ic_message_icon)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        //.setContentIntent(pendingIntent)
                        .setAutoCancel(true);

                ContentValues values = new ContentValues();
                values.put(NotificationProvider.MESSAGE, "Driver accepted your ride");
                values.put(NotificationProvider.TIME_OF_RECEIVING, String.valueOf(LocalDateTime.now()));
                values.put(NotificationProvider.RECEIVER_ID, authService.getUserData().get("user_id"));
                Uri uri = getContentResolver().insert(
                        NotificationProvider.CONTENT_URI, values);

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
                notificationManager.notify(1001, builder.build());
                gotNotification = true;
            }
            if (notificationDTO.getReason().equals("DRIVER_ARRIVED")) {

                NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "NOTIFICATION_CHANNEL")
                        .setContentTitle("Driver arrived at departure place.")
                        .setContentText("Come here!")
                        .setSmallIcon(R.drawable.ic_message_icon)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setAutoCancel(true);

                ContentValues values = new ContentValues();
                values.put(NotificationProvider.MESSAGE, "Driver arrived at departure place");
                values.put(NotificationProvider.TIME_OF_RECEIVING, String.valueOf(LocalDateTime.now()));
                values.put(NotificationProvider.RECEIVER_ID, authService.getUserData().get("user_id"));
                Uri uri = getContentResolver().insert(
                        NotificationProvider.CONTENT_URI, values);

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

                notificationManager.notify(5683, builder.build());
            }
            if (notificationDTO.getReason().equals("DRIVER_CANCEL")) {

                NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "NOTIFICATION_CHANNEL")
                        .setContentTitle("Ride canceled.")
                        .setContentText(notificationDTO.getMessage())
                        .setSmallIcon(R.drawable.ic_message_icon)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setAutoCancel(true);

                ContentValues values = new ContentValues();
                values.put(NotificationProvider.MESSAGE, "Ride cancelled");
                values.put(NotificationProvider.TIME_OF_RECEIVING, String.valueOf(LocalDateTime.now()));
                values.put(NotificationProvider.RECEIVER_ID, authService.getUserData().get("user_id"));
                Uri uri = getContentResolver().insert(
                        NotificationProvider.CONTENT_URI, values);

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

                notificationManager.notify(5684, builder.build());
            }
//            if (notificationDTO.getReason().equals("END_RIDE")) {
//
//                NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "NOTIFICATION_CHANNEL")
//                        .setContentTitle("Ride ended.")
//                        .setContentText(notificationDTO.getMessage())
//                        .setSmallIcon(R.drawable.ic_message_icon)
//                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                        .setAutoCancel(true);
//
//                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
//
//                notificationManager.notify(5683, builder.build());
//            }
        });

        Retrofit.stompClient.topic("/ride-notification-message/" + passengerId).subscribe(topicMessage -> {

            Log.d("TAG", topicMessage.getPayload());

            ObjectMapper objectMapper = new ObjectMapper();
            MessageSentDTO messageSentDTO = objectMapper.readValue(topicMessage.getPayload(), MessageSentDTO.class);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "NOTIFICATION_CHANNEL")
                    .setContentTitle("New message.")
                    .setContentText(messageSentDTO.getMessage())
                    .setSmallIcon(R.drawable.ic_message_icon)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

            notificationManager.notify(9832, builder.build());

        });

        // ovaj deo je samo za testiranje, notifikacija na ovaj kanal se salje kad vozac
        // pritisne na start ride dugme (verovatno ce se dobiti sa beka)
        // druga notifikacija je kad vozac javi putnicima da je stigao na polaziste
//        NotificationDTO data = new NotificationDTO("message", 1, "START_RIDE");
//        NotificationDTO data2 = new NotificationDTO("message", 1, "DRIVER_ARRIVED");
//        ObjectMapper objectMapper = new ObjectMapper();
//        String json = "asd";
//        String json2 = "asd";
//        try {
//            json = objectMapper.writeValueAsString(data);
//            json2 = objectMapper.writeValueAsString(data2);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
//
//        Retrofit.stompClient.send("/ride-notification-passenger/" + passengerId, json).subscribe();
//        Retrofit.stompClient.send("/ride-notification-passenger/" + passengerId, json2).subscribe();



    }

    private void updateMapAndEstimatedData() {
        departureDTO.setAddress(departure.getText().toString());
        destinationDTO.setAddress(destination.getText().toString());
        Estimation estimation = mapFragment.drawRoute(departureDTO, destinationDTO);
        List<DepartureDestinationLocationsDTO> locationss = new ArrayList<>();
        locationss.add(new DepartureDestinationLocationsDTO(departureDTO, destinationDTO));
        EstimatedDataRequestDTO estimatedDataRequestDTO = new EstimatedDataRequestDTO(locationss, "standard", true, true, estimation.getKm());
        IPassengerService passengerService = Retrofit.retrofit.create(IPassengerService.class);
        Call<EstimatedDataResponseDTO> call = passengerService.getEstimatedData(estimatedDataRequestDTO);
        call.enqueue(new Callback<EstimatedDataResponseDTO>() {
            @Override
            public void onResponse(Call<EstimatedDataResponseDTO> call, Response<EstimatedDataResponseDTO> response) {
                if (response.code() != 200) {
                    Snackbar.make(view, "greska" + response.code(), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    return;
                }
                Log.e("DEBUG", "String.valueOf(estimatedDataResponseDTO)");
                EstimatedDataResponseDTO estimatedDataResponseDTO = response.body();
                Log.e("DEBUG", String.valueOf(estimatedDataResponseDTO));
                TextView priceTextView = findViewById(R.id.estimated_price);
                assert estimatedDataResponseDTO != null;
                priceTextView.setText(String.format("Estimated price: %s din", String.valueOf(estimatedDataResponseDTO.getEstimatedCost())));
                TextView timeTextView = findViewById(R.id.estimated_time);
                timeTextView.setText(String.format("Estimated time: %s min", round(estimation.getTimeInMin())));
            }

            @Override
            public void onFailure(Call<EstimatedDataResponseDTO> call, Throwable t) {
                Snackbar.make(view, "lose si povezala!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void fillDataTime() {
        TextView header = this.findViewById(R.id.headerForTime);
        TextView time = this.findViewById(R.id.fillerForTime);
        header.setText(R.string.arive_time_header);
        time.setText(4 + " min");
//
//        StompClient stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, Constants.websocketBaseUrl);
//        stompClient.connect();
//
//        Disposable subscribe = stompClient.topic("/vehicle-time").subscribe(topicMessage -> {
//            Log.d("TAG", topicMessage.getPayload());

        new android.os.Handler(Looper.getMainLooper()).postDelayed(
                new Runnable() {
                    public void run() {
                        time.setText(3 + " min");
                    }
                },
                3000);
        new android.os.Handler(Looper.getMainLooper()).postDelayed(
                new Runnable() {
                    public void run() {
                        time.setText(2 + " min");
                    }
                },
                3000*2);
        new android.os.Handler(Looper.getMainLooper()).postDelayed(
                new Runnable() {
                    public void run() {
                        time.setText(1 + " min");
                    }
                },
                3000*3);
        new android.os.Handler(Looper.getMainLooper()).postDelayed(
                new Runnable() {
                    public void run() {
                        time.setText(0 + " min");
                    }
                },
                3000*4);

            new android.os.Handler(Looper.getMainLooper()).postDelayed(
                    new Runnable() {
                        public void run() {
                            time.setText("Vehicle arrived!");
                            gotNotification = false;
                            NotificationDTO data = new NotificationDTO("your ride is here", 1, "DRIVER_ARRIVED");
                            ObjectMapper objectMapper = new ObjectMapper();
                            String json = "asd";
                            try {
                                json = objectMapper.writeValueAsString(data);
                            } catch (JsonProcessingException e) {
                                e.printStackTrace();
                            }

                            Retrofit.stompClient.send("/ride-notification-passenger/" + passengerId, json).subscribe();
                        }
                    },
                    3000 * 5);
//
//        });

    }

    @Override
    public void onResume(){
        super.onResume();
        passengerId = Retrofit.sharedPreferences.getString("user_id", null);
        //mapFragment.loadVehicles();
        if (gotNotification)  fillDataTime();
        else clearData();

    }

    @Override
    public void onPause(){
        super.onPause();
        passengerId = Retrofit.sharedPreferences.getString("user_id", null);
    }

    private void clearData() {
        TextView header = this.findViewById(R.id.headerForTime);
        TextView time = this.findViewById(R.id.fillerForTime);
        header.setText("");
        time.setText("");
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
//            this.startActivity(new Intent(this, PassengerInboxActivity.class));
            this.startActivity(new Intent(PassengerMainActivity.this, DriverInboxActivity.class));
            return true;
        }
        if (id == R.id.action_passenger_notifications) {
            this.startActivity(new Intent(PassengerMainActivity.this, NotificationInboxActivity.class));
            return true;
        }
        if (id == R.id.action_passenger_logout) {
            authService.logout();
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }


}