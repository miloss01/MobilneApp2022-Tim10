package com.example.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;

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

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        Button button = (Button) findViewById(R.id.register_summbit_btn);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Snackbar.make(view, "Please confirm registration on your email", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
//                TextView name = (TextView)view.findViewById(R.id.notification_for_registry);
//                name.setText("Please confirm registration on your email");
            }
        });


    }
}