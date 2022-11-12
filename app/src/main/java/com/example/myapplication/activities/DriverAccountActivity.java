package com.example.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.myapplication.R;
import com.example.myapplication.fragments.DriverMainAccountFragment;
import com.example.myapplication.tools.FragmentTransition;

public class DriverAccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_account);
        FragmentTransition.to(DriverMainAccountFragment.newInstance(), this, false, R.id.upView);
        //FragmentTransition.to(DriverAccountFragment.newInstance(), this, false, R.id.downView);

    }
}