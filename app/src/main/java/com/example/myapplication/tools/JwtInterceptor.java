package com.example.myapplication.tools;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.myapplication.services.AuthService;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class JwtInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        String token = Retrofit.sharedPreferences.getString("user_jwt", "asd");

        Request newRequest  = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer " + token)
                .build();
        return chain.proceed(newRequest);
    }

}
