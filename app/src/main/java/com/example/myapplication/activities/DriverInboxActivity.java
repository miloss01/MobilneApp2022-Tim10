package com.example.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.myapplication.R;
import com.example.myapplication.adapters.DriverInboxAdapter;
import com.example.myapplication.dto.MessageReceivedDTO;
import com.example.myapplication.dto.MessageResponseDTO;
import com.example.myapplication.dto.UserExpandedDTO;
import com.example.myapplication.services.IAppUserService;
import com.example.myapplication.tools.Retrofit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DriverInboxActivity extends AppCompatActivity {

    private ArrayList<MessageReceivedDTO> messages;
    private HashMap<Long, MessageReceivedDTO> filteredRecentPerUser = new HashMap<>();
    private HashMap<Long, UserExpandedDTO> users = new HashMap<>();  // users interacted with
    private ListView listView;
    private Long driverId;
    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    public Spinner filterSpinner;
    public String filter = "all";


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

        this.filterSpinner = findViewById(R.id.spinner_driverinbox_filter);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.spiner_pass_inbox_options));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filterSpinner.setAdapter(adapter);

        setUpButtons();
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
                    DriverInboxActivity.this.messages = response.body().getResults();
                    createChats();
                }
            }

            @Override
            public void onFailure(Call<MessageResponseDTO> call, Throwable t) {
                Log.d("DEBUG", "Error getting messages", t);
            }
        });
    }

    private void setupList(String filter){
        listView = findViewById(R.id.driver_inbox_view);
        ArrayList<MessageReceivedDTO> messages = new ArrayList<>(this.filteredRecentPerUser.values());
        messages = filterMessages(filter, messages);

        // Order date descending
        messages.sort((message1, message2) -> {
            LocalDateTime t1 = LocalDateTime.parse(message1.getTimeOfSending());
            LocalDateTime t2 = LocalDateTime.parse(message2.getTimeOfSending());
            return t1.isBefore(t2) ? 1 : -1;
        });
        // Order support chats first
        messages.sort((message1, message2) -> {
            if (message1.getType().equals("support") && !message2.getType().equals("support")) return -1;
            if (!message1.getType().equals("support") && message2.getType().equals("support")) return 1;
            return 0;
        });
        DriverInboxAdapter adapter = new DriverInboxAdapter(getApplicationContext(),
                R.layout.driver_inbox_cell, messages, this.users, this.driverId);
        listView.setAdapter(adapter);
        ArrayList<MessageReceivedDTO> finalMessages = messages;
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(DriverInboxActivity.this, DriverChatActivity.class);

                Integer userId;
                if (!Objects.equals(finalMessages.get(i).getReceiverId(), DriverInboxActivity.this.driverId)) userId = finalMessages.get(i).getReceiverId().intValue();
                else userId = finalMessages.get(i).getSenderId().intValue();

                intent.putExtra("USER", DriverInboxActivity.this.users.get(userId.longValue()));
                intent.putExtra("DRIVER_ID", DriverInboxActivity.this.driverId);
                intent.putExtra("MESSAGE_TYPE", finalMessages.get(i).getType());

                startActivity(intent);
            }
        });
    }

    private void setUpButtons() {
        Button supportChat = findViewById(R.id.btn_driverinbox_supportChat);
        supportChat.setEnabled(false);

        Button applyFilterBtn = findViewById(R.id.btn_driverinbox_applyfilter);
        applyFilterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DriverInboxActivity.this.filter = filterSpinner.getSelectedItem().toString().toLowerCase(Locale.ROOT);
                if (DriverInboxActivity.this.filter.equals("ride") || DriverInboxActivity.this.filter.equals("panic")) {
                    supportChat.setVisibility(View.INVISIBLE);
                } else supportChat.setVisibility(View.VISIBLE);
                createChats();
            }
        });

    }

    private ArrayList<MessageReceivedDTO> filterMessages(String filter, ArrayList<MessageReceivedDTO> messages) {
        ArrayList<MessageReceivedDTO> filtered = new ArrayList<>();
        if (filter.equals("all")) return messages;
        for (MessageReceivedDTO message : messages) {
            if (message.getType().equals(filter)) filtered.add(message);
        }
        return filtered;
    }


    private void createChats() {
        IAppUserService appUserService = Retrofit.retrofit.create(IAppUserService.class);

        // All messages ever exchanged by driver
        for (MessageReceivedDTO message : this.messages) {

            // Extracting the other user in message for info to display
            Integer userId = null;
            if (!Objects.equals(message.getReceiverId(), this.driverId)) userId = message.getReceiverId().intValue();
            else userId = message.getSenderId().intValue();
            Call<UserExpandedDTO> passengerCall = appUserService.getById(userId);
            passengerCall.enqueue(new Callback<UserExpandedDTO>() {
                @Override
                public void onResponse(Call<UserExpandedDTO> call, Response<UserExpandedDTO> response) {
                    if (response.code() == 200 && response.body() != null) {
                        UserExpandedDTO user = response.body();

                        // Getting the most recent message for display in chat preview
                        if (DriverInboxActivity.this.filteredRecentPerUser.containsKey(user.getId().longValue())) {
                            try {
                                LocalDateTime currentMessageTime = LocalDateTime.parse(message.getTimeOfSending());
                                LocalDateTime mostRecentMessageTime = LocalDateTime.parse(DriverInboxActivity.this.filteredRecentPerUser.get(user.getId().longValue()).getTimeOfSending());
                                if (currentMessageTime.isAfter(mostRecentMessageTime)) {
                                    DriverInboxActivity.this.filteredRecentPerUser.put(user.getId().longValue(), message);
                                }
                            } catch (DateTimeParseException ex) {
                                Log.i("TAG", message.getTimeOfSending());
                                DriverInboxActivity.this.filteredRecentPerUser.put(user.getId().longValue(), message);
                            }
                        } else {
                            DriverInboxActivity.this.users.put(user.getId().longValue(), user);
                            DriverInboxActivity.this.filteredRecentPerUser.put(user.getId().longValue(), message);
                        }
                        setupList(DriverInboxActivity.this.filter);
                    }
                }

                @Override
                public void onFailure(Call<UserExpandedDTO> call, Throwable t) {
                    Log.d("TAG", "Error for getting passenger", t);
                }
            });
        }
    }

}