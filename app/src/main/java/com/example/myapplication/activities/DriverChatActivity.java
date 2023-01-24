package com.example.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.myapplication.R;
import com.example.myapplication.adapters.DriverMessagesAdapter;
import com.example.myapplication.dto.MessageReceivedDTO;
import com.example.myapplication.dto.MessageResponseDTO;
import com.example.myapplication.dto.PassengerDTO;
import com.example.myapplication.services.IAppUserService;
import com.example.myapplication.tools.Retrofit;

import java.time.LocalDateTime;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DriverChatActivity extends AppCompatActivity {

    private RecyclerView mMessageRecycler;
    private DriverMessagesAdapter mMessageAdapter;
    private ArrayList<MessageReceivedDTO> messageList = new ArrayList<>();
    private PassengerDTO passenger;
    private Long driverId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_chat);
        Intent i = getIntent();
        if (i != null && i.hasExtra("PASSENGER") && i.hasExtra("DRIVER_ID")) {
            this.passenger = (PassengerDTO) i.getSerializableExtra("PASSENGER");
            this.driverId = i.getLongExtra("DRIVER_ID", 0);
        }

        IAppUserService appUserService = Retrofit.retrofit.create(IAppUserService.class);
        Call<MessageResponseDTO> messagesCall = appUserService.getMessages(this.driverId.intValue());

        messagesCall.enqueue(new Callback<MessageResponseDTO>() {
            @Override
            public void onResponse(Call<MessageResponseDTO> call, Response<MessageResponseDTO> response) {
                if (response.code() == 200 && response.body() != null) {
                    filterMessagesWithPassenger(DriverChatActivity.this.passenger.getId(), response.body().getResults());

                    mMessageRecycler = (RecyclerView) findViewById(R.id.recycler_driverchat);
                    mMessageAdapter = new DriverMessagesAdapter(DriverChatActivity.this, messageList, passenger, driverId);
                    mMessageRecycler.setLayoutManager(new LinearLayoutManager(DriverChatActivity.this));
                    mMessageRecycler.setAdapter(mMessageAdapter);
                }
            }

            @Override
            public void onFailure(Call<MessageResponseDTO> call, Throwable t) {
                Log.d("DEBUG", "Error getting messages", t);
            }
        });
    }

    private void filterMessagesWithPassenger(Long passengerId, ArrayList<MessageReceivedDTO> messages) {
        for (MessageReceivedDTO message : messages) {
            if (message.getSenderId().equals(passengerId) || message.getReceiverId().equals(passengerId)) {
                this.messageList.add(message);
            }
        }
        this.messageList.sort((message1, message2) -> {
            LocalDateTime t1 = LocalDateTime.parse(message1.getTimeOfSending(), DriverInboxActivity.formatter);
            LocalDateTime t2 = LocalDateTime.parse(message2.getTimeOfSending(), DriverInboxActivity.formatter);
            return t1.isAfter(t2) ? 1 : -1;
        });
    }

}