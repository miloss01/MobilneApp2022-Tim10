package com.example.myapplication.services;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.core.content.ContextCompat;

import com.auth0.android.jwt.JWT;
import com.example.myapplication.activities.LoginActivity;
import com.example.myapplication.dto.LoginDTO;
import com.example.myapplication.dto.PasswordResetCodeDTO;
import com.example.myapplication.dto.TokenResponseDTO;
import com.example.myapplication.tools.Retrofit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPasswordService {

    private Activity activity;

    public ResetPasswordService(Activity activity) {
        this.activity = activity;
    }

    public void requestCode(String email) {

        IResetPasswordService resetPasswordService = Retrofit.retrofit.create(IResetPasswordService.class);
        Call<Void> requestCodeResponseCall = resetPasswordService.requestCode(new PasswordResetCodeDTO(email, null, null));

        requestCodeResponseCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 204)
                    System.out.println("poslat kod");
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });

    }

    public void applyCode(String email, String code, String newPassword) {

        IResetPasswordService resetPasswordService = Retrofit.retrofit.create(IResetPasswordService.class);
        Call<Void> applyCodeResponseCall = resetPasswordService.applyCode(new PasswordResetCodeDTO(email, newPassword, code));

        applyCodeResponseCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 204) {
                    System.out.println("promenjena sifra");
                    ContextCompat.startActivity(activity, new Intent(activity, LoginActivity.class), null);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });

    }

}
