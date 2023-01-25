package com.example.myapplication.activities;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.adapters.DriverMessagesAdapter;
import com.example.myapplication.dto.MessageReceivedDTO;
import com.example.myapplication.dto.MessageResponseDTO;
import com.example.myapplication.dto.MessageSentDTO;
import com.example.myapplication.dto.PassengerDTO;
import com.example.myapplication.dto.UserExpandedDTO;
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
    private UserExpandedDTO user;
    private Long driverId;
    private RecyclerView recyclerView;
    private String messageType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_chat);

        Toolbar toolbar = findViewById(R.id.driver_inbox_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        recyclerView = findViewById(R.id.recycler_driverchat);

        Intent i = getIntent();
        if (i != null && i.hasExtra("USER") && i.hasExtra("DRIVER_ID") && i.hasExtra("MESSAGE_TYPE")) {
            this.user = (UserExpandedDTO) i.getSerializableExtra("USER");
            this.driverId = i.getLongExtra("DRIVER_ID", 0);
            this.messageType = i.getStringExtra("MESSAGE_TYPE");
            getSupportActionBar().setTitle("Chat - " + user.getName() + " " + user.getSurname());
        }
//        else if (i != null && i.hasExtra("DRIVER_ID") && i.hasExtra("MESSAGE_TYPE") ) {
//            this.driverId = i.getLongExtra("DRIVER_ID", 0);
//            this.messageType = i.getStringExtra("MESSAGE_TYPE");
//            getSupportActionBar().setTitle("Chat - SUPPORT");
//        }

        IAppUserService appUserService = Retrofit.retrofit.create(IAppUserService.class);
        Call<MessageResponseDTO> messagesCall = appUserService.getMessages(this.driverId.intValue());

        messagesCall.enqueue(new Callback<MessageResponseDTO>() {
            @Override
            public void onResponse(Call<MessageResponseDTO> call, Response<MessageResponseDTO> response) {
                if (response.code() == 200 && response.body() != null) {
                    filterMessagesWithPassenger(DriverChatActivity.this.user.getId().longValue(), response.body().getResults());

                    mMessageRecycler = (RecyclerView) findViewById(R.id.recycler_driverchat);
                    mMessageAdapter = new DriverMessagesAdapter(DriverChatActivity.this, messageList, user, driverId);
                    mMessageRecycler.setLayoutManager(new LinearLayoutManager(DriverChatActivity.this));
                    mMessageRecycler.setAdapter(mMessageAdapter);
                    recyclerView.scrollToPosition(messageList.size() - 1);
                }
            }

            @Override
            public void onFailure(Call<MessageResponseDTO> call, Throwable t) {
                Log.d("DEBUG", "Error getting messages", t);
            }
        });

        TextView tvMessage = findViewById(R.id.edit_driverchat_message);
        Button sendButton = findViewById(R.id.btn_driverchat_send);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Call<MessageReceivedDTO> messagesCall = appUserService.sendMessageByUserId(
                        user.getId().intValue(),
                        new MessageSentDTO(user.getId().longValue(),
                            tvMessage.getText().toString(),
                            DriverChatActivity.this.messageType, 0L)
                );
                messagesCall.enqueue(new Callback<MessageReceivedDTO>() {
                    @Override
                    public void onResponse(Call<MessageReceivedDTO> call, Response<MessageReceivedDTO> response) {
                        if (response.code() == 200 && response.body() != null) {
                            Log.i("TAG", response.body().getSenderId().toString() + " " + response.body().getReceiverId());
                            DriverChatActivity.this.messageList.add(response.body());
//                            mMessageRecycler = (RecyclerView) findViewById(R.id.recycler_driverchat);
//                            mMessageAdapter = new DriverMessagesAdapter(DriverChatActivity.this, messageList, passenger, driverId);
//                            mMessageRecycler.setLayoutManager(new LinearLayoutManager(DriverChatActivity.this));
                            sortMessages();
                            mMessageRecycler.setAdapter(mMessageAdapter);
                            recyclerView.scrollToPosition(messageList.size() - 1);
                            tvMessage.setText("");
                        } else {
                            Log.d("DEBUG", "Error sending message" + response.code() + response.toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<MessageReceivedDTO> call, Throwable t) {
                        Log.d("DEBUG", "Error sending message", t);
                    }
                });
            }
        });

    }

    private void filterMessagesWithPassenger(Long passengerId, ArrayList<MessageReceivedDTO> messages) {
        for (MessageReceivedDTO message : messages) {
            if (message.getSenderId().equals(passengerId) || message.getReceiverId().equals(passengerId)) {
                this.messageList.add(message);
            }
        }
        sortMessages();
    }

    private void sortMessages() {
        this.messageList.sort((message1, message2) -> {
            LocalDateTime t1 = LocalDateTime.parse(message1.getTimeOfSending(), DriverInboxActivity.formatter);
            LocalDateTime t2 = LocalDateTime.parse(message2.getTimeOfSending(), DriverInboxActivity.formatter);
            if (t1.isAfter(t2)) return 1;
            else if (t1.isBefore(t2)) return -1;
            else return 0;
        });
    }

}