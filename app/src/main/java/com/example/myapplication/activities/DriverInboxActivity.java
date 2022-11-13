package com.example.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.example.myapplication.R;
import com.example.myapplication.adapters.DriverInboxAdapter;
import com.example.myapplication.adapters.PassengerRideAdapter;
import com.example.myapplication.models.Message;
import com.example.myapplication.models.Ride;
import com.example.myapplication.tools.InboxMokap;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class DriverInboxActivity extends AppCompatActivity {

    public static ArrayList<Message> messagesList = new ArrayList<Message>();
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_inbox);
        setupData();
        setupList();
        //setupOnclickListener();
    }

    private void setupData(){
        messagesList = InboxMokap.getMessages();
//        for (int i = 0; i < 3; i++) {
//        }
    }

    private void setupList(){
        listView = (ListView) findViewById(R.id.driver_inbox_view);
        //PassengerRidesBaseAdapter adapter = new PassengerRidesBaseAdapter(PassengerRideHistoryActivity.this);
        DriverInboxAdapter adapter = new DriverInboxAdapter(getApplicationContext(),
                R.layout.driver_inbox_cell, messagesList);
        listView.setAdapter(adapter);

    }

}