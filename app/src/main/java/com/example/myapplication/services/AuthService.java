package com.example.myapplication.services;



import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.core.content.ContextCompat;

import com.auth0.android.jwt.JWT;
import com.example.myapplication.Constants;
import com.example.myapplication.activities.DriverMainActivity;
import com.example.myapplication.activities.LoginActivity;
import com.example.myapplication.activities.PassengerMainActivity;
import com.example.myapplication.activities.SplashScreenActivity;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Retrofit;

public class AuthService {

    private Activity activity;
    private SharedPreferences sharedPreferences;

    public AuthService(Activity activity) {
        this.activity = activity;
        this.sharedPreferences = activity.getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
    }

    public boolean isLoggedIn() {
        String jwt = sharedPreferences.getString("user_jwt", "");
        return jwt != "";
    }

    public void login(String jwtString) {
        JWT jwt = new JWT(jwtString);
        String email = jwt.getSubject();
        String id = jwt.getClaim("id").asString();
        String role = jwt.getClaim("role").asString();

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user_jwt", jwtString);
        editor.putString("user_email", email);
        editor.putString("user_id", id);
        editor.putString("user_role", role);
        editor.apply();

        redirect();
    }

    public void logout() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user_jwt", "");
        editor.putString("user_email", "");
        editor.putString("user_id", "");
        editor.putString("user_role", "");
        editor.apply();

        redirect();
    }

    public Map<String, String> getUserData() {
        Map<String, String> map = new HashMap<>();
        map.put("user_jwt", sharedPreferences.getString("user_jwt", ""));
        map.put("user_email", sharedPreferences.getString("user_email", ""));
        map.put("user_id", sharedPreferences.getString("user_id", ""));
        map.put("user_role", sharedPreferences.getString("user_role", ""));
        return map;
    }

    public void redirect() {

        if (!isLoggedIn())
            ContextCompat.startActivity(activity, new Intent(activity, LoginActivity.class), null);

        String userRole = getUserData().get("user_role");

        if (userRole.equals("PASSENGER"))
            ContextCompat.startActivity(activity, new Intent(activity, PassengerMainActivity.class), null);
        else if (userRole.equals("DRIVER"))
            ContextCompat.startActivity(activity, new Intent(activity, DriverMainActivity.class), null);
        else
            System.out.println("Ili je greska ili je ulogovan admin");
    }

}
