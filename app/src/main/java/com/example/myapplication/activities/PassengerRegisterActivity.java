package com.example.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.databinding.ActivityMainBinding;
import com.google.android.material.snackbar.Snackbar;

public class PassengerRegisterActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_register);
        PassengerRegisterActivity view2 = this;

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        Button button = (Button) findViewById(R.id.register_summbit_btn);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Snackbar.make(view, "Please confirm registration on your email", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
//                Intent intent = new Intent(view2, PassengerMainActivity.class);
                Intent intent = new Intent(view2, DriverMainActivity.class);
                startActivity(intent);
            }
        });


    }
}