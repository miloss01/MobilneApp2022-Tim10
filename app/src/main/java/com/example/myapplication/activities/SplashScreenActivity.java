package com.example.myapplication.activities;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.example.myapplication.R;

public class SplashScreenActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
//        int SPLASH_TIME_OUT = 15000;
//        new Timer().schedule(new TimerTask() {
//            @Override
//            public void run() {
//                startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
//                finish(); // da nebi mogao da ode back na splash
//            }
//        }, SPLASH_TIME_OUT);
    }

}