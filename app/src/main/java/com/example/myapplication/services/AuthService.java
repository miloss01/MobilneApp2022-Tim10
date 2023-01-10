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
import com.example.myapplication.dto.LoginDTO;
import com.example.myapplication.dto.TokenResponseDTO;
import com.example.myapplication.tools.Retrofit;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthService {

    private Activity activity;
    private SharedPreferences sharedPreferences;

    public AuthService(Activity activity) {
        this.activity = activity;
        this.sharedPreferences = activity.getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
    }

    public boolean isLoggedIn() {
        String jwt = sharedPreferences.getString("user_jwt", null);
        return jwt != null;
    }

    public void login(String email, String password) {

        ILoginService loginService = Retrofit.retrofit.create(ILoginService.class);
        Call<TokenResponseDTO> jwtResponseCall = loginService.login(new LoginDTO(email, password));

        jwtResponseCall.enqueue(new Callback<TokenResponseDTO>() {
            @Override
            public void onResponse(Call<TokenResponseDTO> call, Response<TokenResponseDTO> response) {
                System.out.println(response.body());

                if (response.code() != 200) {
                    redirect();
                    return;
                }

                String token = response.body().getAccessToken();

                JWT jwt = new JWT(token);
                String email = jwt.getSubject();
                String id = jwt.getClaim("id").asString();
                String role = jwt.getClaim("role").asString();

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("user_jwt", token);
                editor.putString("user_email", email);
                editor.putString("user_id", id);
                editor.putString("user_role", role);
                editor.apply();

                redirect();
            }

            @Override
            public void onFailure(Call<TokenResponseDTO> call, Throwable t) {
                System.out.println(t.getMessage());

                redirect();
            }
        });
    }

    public void logout() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user_jwt", null);
        editor.putString("user_email", null);
        editor.putString("user_id", null);
        editor.putString("user_role", null);
        editor.apply();

        redirect();
    }

    public Map<String, String> getUserData() {
        Map<String, String> map = new HashMap<>();
        map.put("user_jwt", sharedPreferences.getString("user_jwt", null));
        map.put("user_email", sharedPreferences.getString("user_email", null));
        map.put("user_id", sharedPreferences.getString("user_id", null));
        map.put("user_role", sharedPreferences.getString("user_role", null));
        return map;
    }

    public void redirect() {

        if (!isLoggedIn()) {
            ContextCompat.startActivity(activity, new Intent(activity, LoginActivity.class), null);
            return;
        }

        String userRole = getUserData().get("user_role");

        if (userRole.equals("PASSENGER"))
            ContextCompat.startActivity(activity, new Intent(activity, PassengerMainActivity.class), null);
        else if (userRole.equals("DRIVER"))
            ContextCompat.startActivity(activity, new Intent(activity, DriverMainActivity.class), null);
        else
            System.out.println("Ili je greska ili je ulogovan admin");

        return;
    }

}
