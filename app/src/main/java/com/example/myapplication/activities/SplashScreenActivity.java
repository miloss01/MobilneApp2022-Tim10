package com.example.myapplication.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.auth0.android.jwt.JWT;
import com.example.myapplication.Constants;
import com.example.myapplication.R;
import com.example.myapplication.services.AuthService;
import com.example.myapplication.tools.Retrofit;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreenActivity extends Activity {

    private AuthService authService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        authService = new AuthService(this);

        int SPLASH_TIME_OUT = 3000;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {

                System.out.println(authService.getUserData());

                Retrofit.sharedPreferences = getSharedPreferences(Constants.SHARED_PREF_NAME, MODE_PRIVATE);

                if (!authService.isLoggedIn())
                    startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
                else
                    authService.redirect();

                finish(); // da nebi mogao da ode back na splash
            }
        }, SPLASH_TIME_OUT);
    }

}