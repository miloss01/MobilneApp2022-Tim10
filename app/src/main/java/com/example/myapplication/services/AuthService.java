package com.example.myapplication.services;



import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.auth0.android.jwt.JWT;
import com.example.myapplication.Constants;
import com.example.myapplication.activities.DriverMainActivity;
import com.example.myapplication.activities.LoginActivity;
import com.example.myapplication.activities.PassengerMainActivity;
import com.example.myapplication.dto.IsActiveDTO;
import com.example.myapplication.dto.LoginDTO;
import com.example.myapplication.dto.TokenResponseDTO;
import com.example.myapplication.dto.WorkingHourDTO;
import com.example.myapplication.tools.Retrofit;
import com.google.android.material.snackbar.Snackbar;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthService {

    private Activity activity;

    public AuthService(Activity activity) {
        this.activity = activity;
    }

    public boolean isLoggedIn() {
        String jwt = Retrofit.sharedPreferences.getString("user_jwt", null);
        return jwt != null;
    }

    public void login(String email, String password) {

        IAuthService loginService = Retrofit.retrofit.create(IAuthService.class);
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

                setLoggedInUser(token);

                changeActiveFlag(true);

                startWorkingTime();

                redirect();
            }

            @Override
            public void onFailure(Call<TokenResponseDTO> call, Throwable t) {
                System.out.println(t.getMessage());

                redirect();
            }
        });
    }

    public void setLoggedInUser(String token) {
        JWT jwt = new JWT(token);
        String email = jwt.getSubject();
        String id = jwt.getClaim("id").asString();
        String role = jwt.getClaim("role").asString();

        SharedPreferences.Editor editor = Retrofit.sharedPreferences.edit();
        editor.putString("user_jwt", token);
        editor.putString("user_email", email);
        editor.putString("user_id", id);
        editor.putString("user_role", role);
        editor.apply();
    }

    public void setLoggedOutUser() {
        SharedPreferences.Editor editor = Retrofit.sharedPreferences.edit();
        editor.putString("user_jwt", null);
        editor.putString("user_email", null);
        editor.putString("user_id", null);
        editor.putString("user_role", null);
        editor.apply();
    }

    public void logout() {
        endWorkingTime();
        changeActiveFlag(false);
    }

    public Map<String, String> getUserData() {
        Map<String, String> map = new HashMap<>();
        map.put("user_jwt", Retrofit.sharedPreferences.getString("user_jwt", null));
        map.put("user_email", Retrofit.sharedPreferences.getString("user_email", null));
        map.put("user_id", Retrofit.sharedPreferences.getString("user_id", null));
        map.put("user_role", Retrofit.sharedPreferences.getString("user_role", null));
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

    public void changeActiveFlag(boolean flag) {

        Map<String, String> map = getUserData();
        String id = map.get("user_id");

        if (id == null)
            return;

        IAuthService loginService = Retrofit.retrofit.create(IAuthService.class);
        Call<IsActiveDTO> changeActiveFlagResponse = loginService.changeActiveFlag(Integer.parseInt(id), new IsActiveDTO(flag));
        Log.d("TAG", "zove se" + flag);
        changeActiveFlagResponse.enqueue(new Callback<IsActiveDTO>() {
            @Override
            public void onResponse(Call<IsActiveDTO> call, Response<IsActiveDTO> response) {
                if (response.code() == 200) {

                    if (!flag) {
                        setLoggedOutUser();
                        redirect();
                    }

                }
                else {
                    Log.d("TAG", "greska, verovatno je istekao jwt token");
                    setLoggedOutUser();
                    redirect();
                }
            }

            @Override
            public void onFailure(Call<IsActiveDTO> call, Throwable t) {
                System.out.println(t.getMessage());
                Log.d("TAG", "greska", t);
            }
        });

    }

    public void startWorkingTime() {
        String role = Retrofit.sharedPreferences.getString("user_role", null);
        Log.i("TAG", "role is: " + role);
        if (role.equals("DRIVER")) {

            IDriverService driverService = Retrofit.retrofit.create(IDriverService.class);

            WorkingHourDTO workingHourDTO = new WorkingHourDTO();
            workingHourDTO.setStart(LocalDateTime.now().toString());
            Call<WorkingHourDTO> addCall = driverService.saveWorkingHour(
                    Integer.parseInt( Retrofit.sharedPreferences.getString("user_id", null)), workingHourDTO);
            addCall.enqueue(new Callback<WorkingHourDTO>() {
                @Override
                public void onResponse(Call<WorkingHourDTO> call, Response<WorkingHourDTO> response) {
                    if (response.code() != 200) {
                        Log.d("TAG", "Response error saving working hour");
                        Log.d("TAG", response.toString());
                    }
                }

                @Override
                public void onFailure(Call<WorkingHourDTO> call, Throwable t) {
                    System.out.println(t.getMessage());
                    Log.d("TAG", "Error saving working hour", t);
                }
            });

        }
    }

    public void endWorkingTime() {
        String role = Retrofit.sharedPreferences.getString("user_role", null);
        Log.i("TAG", "role is: " + role);
        if (role.equals("DRIVER")) {

            IDriverService driverService = Retrofit.retrofit.create(IDriverService.class);

            WorkingHourDTO workingHourDTO = new WorkingHourDTO();
            workingHourDTO.setEnd(LocalDateTime.now().toString());
            Call<WorkingHourDTO> addCall = driverService.saveWorkingHour(
                    Integer.parseInt( Retrofit.sharedPreferences.getString("user_id", null)), workingHourDTO);
            addCall.enqueue(new Callback<WorkingHourDTO>() {
                @Override
                public void onResponse(Call<WorkingHourDTO> call, Response<WorkingHourDTO> response) {
                    if (response.code() != 200) {
                        Log.d("TAG", "Response error saving working hour");
                        Log.d("TAG", response.toString());
                    }
                }

                @Override
                public void onFailure(Call<WorkingHourDTO> call, Throwable t) {
                    System.out.println(t.getMessage());
                    Log.d("TAG", "Error saving working hour", t);
                }
            });

        }
    }

}
