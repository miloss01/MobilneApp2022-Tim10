package com.example.myapplication.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.myapplication.R;

import java.util.zip.Inflater;

public class PassengerMainActivity extends AppCompatActivity {

    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_main);

        Toolbar toolbar = findViewById(R.id.passenger_main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Uber");

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
        return super.onOptionsItemSelected(item);

    }


}