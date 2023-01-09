package com.example.myapplication.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;

import com.example.myapplication.R;
import com.example.myapplication.fragments.DriverStatisticFragment;
import com.example.myapplication.services.AuthService;
import com.example.myapplication.tools.FragmentTransition;

import java.util.zip.Inflater;

public class PassengerMainActivity extends AppCompatActivity {

    private CharSequence mTitle;
    private AlertDialog dialog;
    private AlertDialog.Builder dialogBuilder;
    private EditText new_location;
    private Button add_location_btn, cancel_location_btn;
    private AuthService authService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_main);

        authService = new AuthService(this);

        Toolbar toolbar = findViewById(R.id.passenger_main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Uber");

//        Button buttonStat = this.findViewById(R.id.add_departure_to_favoutites_btn);
//        buttonStat.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
//                createAddLocationDialog();
//            }
//        });
        Button rideCreation = this.findViewById(R.id.open_ride_creation);
        rideCreation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PassengerMainActivity.this, RideCreationActivity.class);
                startActivity(intent);
            }
        });

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
        if (id == R.id.action_passenger_logout) {
            authService.logout();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }


//    public void createAddLocationDialog(){
//        dialogBuilder = new AlertDialog.Builder(this);
//        final View newLocationPopup = getLayoutInflater().inflate(R.layout.new_location, null);
//        new_location = (EditText) newLocationPopup.findViewById(R.id.new_location);
//
//        add_location_btn = (Button) newLocationPopup.findViewById(R.id.add_departure_to_favoutites_btn);
//        cancel_location_btn = (Button) newLocationPopup.findViewById(R.id.cancel_location_btn);
//
//        dialogBuilder.setView(newLocationPopup);
//        dialog = dialogBuilder.create();
//        dialog.show();
//
//        add_location_btn.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
////nanananananan
//            }
//        });
//
//        cancel_location_btn.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
//                dialog.dismiss();
//            }
//        });
//    }


}