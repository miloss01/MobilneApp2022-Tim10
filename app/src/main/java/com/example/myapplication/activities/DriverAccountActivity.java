package com.example.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.dialogs.DatePickerFragment;
import com.example.myapplication.fragments.DriverMainAccountFragment;
import com.example.myapplication.tools.FragmentTransition;

public class DriverAccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_account);
        FragmentTransition.to(DriverMainAccountFragment.newInstance(), this, false, R.id.upView);
        //FragmentTransition.to(DriverAccountFragment.newInstance(), this, false, R.id.downView);
        Toolbar toolbar = findViewById(R.id.driver_account_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void showDatePickerDialogFrom(View v) {
        TextView proba = findViewById(R.id.fromDateLabel);
        DialogFragment newFragment = new DatePickerFragment(proba);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void showDatePickerDialogTo(View v) {
        TextView proba = findViewById(R.id.toDateLabel);
        DialogFragment newFragment = new DatePickerFragment(proba);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }
}