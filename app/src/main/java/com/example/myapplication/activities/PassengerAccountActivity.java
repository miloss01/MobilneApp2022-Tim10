package com.example.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.adapters.PassengerAccountAdapter;
import com.example.myapplication.fragments.FavoriteRoutesFragment;
import com.example.myapplication.fragments.PassengerEditAccountFragment;
import com.example.myapplication.fragments.RideDetailsFragment;
import com.example.myapplication.fragments.RideStatsFragment;
import com.example.myapplication.models.User;
import com.example.myapplication.tools.FragmentTransition;
import com.example.myapplication.tools.UsersMokap;
import com.google.android.material.snackbar.Snackbar;

public class PassengerAccountActivity extends AppCompatActivity {

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_account);
        View view = findViewById(R.id.passacc_view);
        this.user = UsersMokap.getUserByEmail("examplemail@sally.com");
        PassengerAccountAdapter adapter = new PassengerAccountAdapter( user, view);

        Toolbar toolbar = findViewById(R.id.passenger_account_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        setupButtons();

    }

    private void setupButtons() {
        PassengerAccountActivity thisView = this;
        Toolbar toolbar = findViewById(R.id.passenger_account_toolbar);

        Button button = (Button) findViewById(R.id.btn_passacc_stats);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                toolbar.setTitle("Ride stats");
                FragmentTransition.to((Fragment) RideStatsFragment.newInstance(thisView),
                        thisView, true,
                        R.id.passacc_rellay);
            }
        });

        button = (Button) findViewById(R.id.btn_passacc_favroutes);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                toolbar.setTitle("Favorite routes");
                FragmentTransition.to((Fragment) FavoriteRoutesFragment.newInstance(thisView),
                        thisView, true,
                        R.id.passacc_rellay);
            }
        });

        ImageButton imgbutton = (ImageButton) findViewById(R.id.btn_passacc_edit);
        imgbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                FragmentTransition.to((Fragment) new PassengerEditAccountFragment(user, thisView),
                        thisView, true,
                        R.id.passacc_rellay);
            }
        });

    }
}