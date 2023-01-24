package com.example.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.adapters.DriverInboxAdapter;
import com.example.myapplication.adapters.PassengerRideAdapter;
import com.example.myapplication.dto.IsActiveDTO;
import com.example.myapplication.dto.MessageReceivedDTO;
import com.example.myapplication.dto.MessageResponseDTO;
import com.example.myapplication.dto.PassengerDTO;
import com.example.myapplication.dto.RideResponseDTO;
import com.example.myapplication.models.Message;
import com.example.myapplication.models.Ride;
import com.example.myapplication.services.IAppUserService;
import com.example.myapplication.services.IPassengerService;
import com.example.myapplication.tools.InboxMokap;
import com.example.myapplication.tools.Retrofit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DriverInboxActivity extends AppCompatActivity {

    public static ArrayList<Message> messagesList = new ArrayList<Message>();
    private HashMap<Long, MessageReceivedDTO> filteredRecentPerUser = new HashMap<>();
    private HashMap<Long, PassengerDTO> users = new HashMap<>();  // users interacted with
    private ListView listView;
    private Long driverId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_inbox);

        Toolbar toolbar = findViewById(R.id.driver_inbox_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Inbox");
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
        IAppUserService appUserService = Retrofit.retrofit.create(IAppUserService.class);
        String driverId = Retrofit.sharedPreferences.getString("user_id", null);
        this.driverId = Long.valueOf(driverId);
        Call<MessageResponseDTO> messagesCall = appUserService.getMessages(this.driverId.intValue());

        messagesCall.enqueue(new Callback<MessageResponseDTO>() {
            @Override
            public void onResponse(Call<MessageResponseDTO> call, Response<MessageResponseDTO> response) {
                if (response.code() == 200 && response.body() != null) {
                    filterMessagesPerUser(response.body().getResults());
                }
            }

            @Override
            public void onFailure(Call<MessageResponseDTO> call, Throwable t) {
                Log.d("DEBUG", "Error getting messages", t);
            }
        });
    }

    private void setupList(){
        listView = findViewById(R.id.driver_inbox_view);
        ArrayList<MessageReceivedDTO> messages = new ArrayList<>(this.filteredRecentPerUser.values());
        DriverInboxAdapter adapter = new DriverInboxAdapter(getApplicationContext(),
                R.layout.driver_inbox_cell, messages, this.users, this.driverId);
        listView.setAdapter(adapter);
    }

    // Get most recent message for every user that messages were exchanged with
    private void filterMessagesPerUser(ArrayList<MessageReceivedDTO> messages) {
        IPassengerService passengerService = Retrofit.retrofit.create(IPassengerService.class);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

        // All messages ever exchanged by driver
        for (MessageReceivedDTO message : messages) {

            Log.i("TAG", message.getMessage());
            // Extracting the other user in message for info to display
            Integer userId = null;
            if (!Objects.equals(message.getReceiverId(), this.driverId)) userId = message.getReceiverId().intValue();
            else userId = message.getSenderId().intValue();
            Call<PassengerDTO> passengerCall = passengerService.getPassenger(userId);
            Log.i("TAG", "userId " + userId.toString());
            passengerCall.enqueue(new Callback<PassengerDTO>() {
                @Override
                public void onResponse(Call<PassengerDTO> call, Response<PassengerDTO> response) {
                    if (response.code() == 200 && response.body() != null) {
                        PassengerDTO passenger = response.body();

                        // Getting the most recent message for display in chat preview
                        if (DriverInboxActivity.this.filteredRecentPerUser.containsKey(passenger.getId())) {
                            Log.i("TAG", "userId is already there");
                            try {
                                LocalDateTime currentMessageTime = LocalDateTime.parse(message.getTimeOfSending(), formatter);
                                LocalDateTime mostRecentMessageTime = LocalDateTime.parse(DriverInboxActivity.this.filteredRecentPerUser.get(passenger.getId()).getTimeOfSending(), formatter);
                                if (currentMessageTime.isAfter(mostRecentMessageTime)) {
                                    Log.i("TAG", "userId is updated to newer message");
                                    DriverInboxActivity.this.filteredRecentPerUser.put(passenger.getId(), message);
                                }
                            } catch (DateTimeParseException ex) {
                                Log.i("TAG", ex.toString());
                                DriverInboxActivity.this.filteredRecentPerUser.put(passenger.getId(), message);
                            }
                        } else {
                            Log.i("TAG", "userId is put 1st time");
                            DriverInboxActivity.this.users.put(passenger.getId(), passenger);
                            DriverInboxActivity.this.filteredRecentPerUser.put(passenger.getId(), message);
                        }
                        setupList();
                    }
                }

                @Override
                public void onFailure(Call<PassengerDTO> call, Throwable t) {
                    Log.d("TAG", "Error for getting passenger", t);
                }
            });
        }
    }

}