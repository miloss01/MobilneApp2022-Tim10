package com.example.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.myapplication.R;
import com.example.myapplication.adapters.DriverInboxAdapter;
import com.example.myapplication.dto.MessageReceivedDTO;
import com.example.myapplication.dto.MessageResponseDTO;
import com.example.myapplication.dto.PassengerDTO;
import com.example.myapplication.services.IAppUserService;
import com.example.myapplication.services.IPassengerService;
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

    private HashMap<Long, MessageReceivedDTO> filteredRecentPerUser = new HashMap<>();
    private HashMap<Long, PassengerDTO> users = new HashMap<>();  // users interacted with
    private ListView listView;
    private Long driverId;
    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");


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

        Spinner spinner = findViewById(R.id.spinner_driverinbox);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.spiner_pass_inbox_options));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
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
        messages.sort((message1, message2) -> {
            LocalDateTime t1 = LocalDateTime.parse(message1.getTimeOfSending(), formatter);
            LocalDateTime t2 = LocalDateTime.parse(message2.getTimeOfSending(), formatter);
            return t1.isBefore(t2) ? 1 : -1;
        });
        DriverInboxAdapter adapter = new DriverInboxAdapter(getApplicationContext(),
                R.layout.driver_inbox_cell, messages, this.users, this.driverId);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(DriverInboxActivity.this, DriverChatActivity.class);

                Integer userId;
                if (!Objects.equals(messages.get(i).getReceiverId(), DriverInboxActivity.this.driverId)) userId = messages.get(i).getReceiverId().intValue();
                else userId = messages.get(i).getSenderId().intValue();

                intent.putExtra("PASSENGER", DriverInboxActivity.this.users.get(userId.longValue()));
                intent.putExtra("DRIVER_ID", DriverInboxActivity.this.driverId);

                startActivity(intent);
            }
        });
    }

    // Get most recent message for every user that messages were exchanged with
    private void filterMessagesPerUser(ArrayList<MessageReceivedDTO> messages) {
        IPassengerService passengerService = Retrofit.retrofit.create(IPassengerService.class);

        // All messages ever exchanged by driver
        for (MessageReceivedDTO message : messages) {

            // Extracting the other user in message for info to display
            Integer userId = null;
            if (!Objects.equals(message.getReceiverId(), this.driverId)) userId = message.getReceiverId().intValue();
            else userId = message.getSenderId().intValue();
            Call<PassengerDTO> passengerCall = passengerService.getPassenger(userId);
            passengerCall.enqueue(new Callback<PassengerDTO>() {
                @Override
                public void onResponse(Call<PassengerDTO> call, Response<PassengerDTO> response) {
                    if (response.code() == 200 && response.body() != null) {
                        PassengerDTO passenger = response.body();

                        // Getting the most recent message for display in chat preview
                        if (DriverInboxActivity.this.filteredRecentPerUser.containsKey(passenger.getId())) {
                            try {
                                LocalDateTime currentMessageTime = LocalDateTime.parse(message.getTimeOfSending(), formatter);
                                LocalDateTime mostRecentMessageTime = LocalDateTime.parse(DriverInboxActivity.this.filteredRecentPerUser.get(passenger.getId()).getTimeOfSending(), formatter);
                                if (currentMessageTime.isAfter(mostRecentMessageTime)) {
                                    DriverInboxActivity.this.filteredRecentPerUser.put(passenger.getId(), message);
                                }
                            } catch (DateTimeParseException ex) {
                                DriverInboxActivity.this.filteredRecentPerUser.put(passenger.getId(), message);
                            }
                        } else {
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