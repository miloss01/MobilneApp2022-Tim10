package com.example.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.adapters.PassengerAccountAdapter;
import com.example.myapplication.tools.UsersMokap;

public class PassengerAccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_account);
        View view = findViewById(R.id.passacc_view);
        PassengerAccountAdapter adapter = new PassengerAccountAdapter(UsersMokap.getUserByEmail("examplemail@sally.com"), view);
    }
}