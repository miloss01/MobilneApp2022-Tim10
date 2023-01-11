package com.example.myapplication.tools;

import android.content.SharedPreferences;

import com.example.myapplication.Constants;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.converter.jackson.JacksonConverterFactory;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;

public class Retrofit {
    public static SharedPreferences sharedPreferences;

    public static StompClient stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, Constants.websocketBaseUrl);

    public static JwtInterceptor jwtInterceptor = new JwtInterceptor();

    public static OkHttpClient client = new OkHttpClient().newBuilder()
            .addInterceptor(jwtInterceptor)
            .addInterceptor(new ErrorInterceptor())
            .build();

    public static retrofit2.Retrofit retrofit = new retrofit2.Retrofit.Builder()
            .client(client)
            .baseUrl(Constants.baseUrl)
            .addConverterFactory(JacksonConverterFactory.create())
            .build();

}
