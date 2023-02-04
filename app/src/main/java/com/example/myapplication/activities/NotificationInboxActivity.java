package com.example.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.example.myapplication.R;
import com.example.myapplication.adapters.NotificationAdapter;
import com.example.myapplication.dto.MessageReceivedDTO;
import com.example.myapplication.dto.NotificationDTO;
import com.example.myapplication.dto.UserExpandedDTO;
import com.example.myapplication.models.Notification;
import com.example.myapplication.providers.NotificationProvider;
import com.example.myapplication.services.AuthService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

public class NotificationInboxActivity extends AppCompatActivity {

    private ArrayList<Notification> notifications = new ArrayList<>();
    private ListView listView;
    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    private static String userId;

    SimpleCursorAdapter dataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_inbox);

        Toolbar toolbar = findViewById(R.id.notification_inbox_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Notifications");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        AuthService authService = new AuthService(this);
        userId = authService.getUserData().get("user_id");

        setupData();
    }

    @SuppressLint("Range")
    private void setupData() {

        // wipe database data
//        getApplicationContext().getContentResolver().delete(NotificationProvider.CONTENT_URI, null, null);

        Uri notifs = Uri.parse(NotificationProvider.URL);
        Cursor c = managedQuery(notifs, null, null, null, null);

        if (c.moveToFirst()) {
            if (c.getString(c.getColumnIndex(NotificationProvider.RECEIVER_ID)).equals(userId)) {
                notifications.add(new Notification(c.getString(c.getColumnIndex(NotificationProvider.MESSAGE)),
                        LocalDateTime.parse(c.getString(c.getColumnIndex(NotificationProvider.TIME_OF_RECEIVING)))));
            }
            } while (c.moveToNext()) {
            if (c.getString(c.getColumnIndex(NotificationProvider.RECEIVER_ID)).equals(userId)) {
                notifications.add(new Notification(c.getString(c.getColumnIndex(NotificationProvider.MESSAGE)),
                        LocalDateTime.parse(c.getString(c.getColumnIndex(NotificationProvider.TIME_OF_RECEIVING)))));
            }
        };



        setupList();
    }

    private void setupList() {
        listView = findViewById(R.id.notification_inbox_listView);
        notifications.sort((message1, message2) -> {
            LocalDateTime t1 = message1.getTimeOfReceiving();
            LocalDateTime t2 = message2.getTimeOfReceiving();
            return t1.isBefore(t2) ? 1 : -1;
        });
        NotificationAdapter adapter = new NotificationAdapter(getApplicationContext(), R.layout.notification_cell, notifications);
        listView.setAdapter(adapter);
    }

}