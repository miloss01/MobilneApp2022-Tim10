package com.example.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.myapplication.R;
import com.example.myapplication.services.AuthService;

public class DriverMainActivity extends AppCompatActivity {

    private AuthService authService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_main);

        authService = new AuthService(this);

        Toolbar toolbar = findViewById(R.id.driver_main_toolbar);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Uber");
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });

        Switch toggle = findViewById(R.id.driver_main_toggle);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                TextView t = findViewById(R.id.driver_main_label_active);
                if (isChecked) {
                    t.setText("ACTIVE");
                } else {
                    t.setText("NOT ACTIVE");
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.driver_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.driver_menu_history) {
            Intent intent1 = new Intent(this, DriverRideHistoryActivity.class);
            this.startActivity(intent1);
            return true;
        }
        if (id == R.id.driver_menu_account) {
            Intent intent1 = new Intent(this, DriverAccountActivity.class);
            this.startActivity(intent1);
            return true;
        }
        if (id == R.id.driver_menu_inbox) {
            Intent intent1 = new Intent(this, DriverInboxActivity.class);
            this.startActivity(intent1);
            return true;
        }
        if (id == R.id.driver_logout_account) {
            authService.logout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
