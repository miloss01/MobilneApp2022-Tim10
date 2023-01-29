package com.example.myapplication.tools;


import android.util.Log;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.example.myapplication.dto.ErrorMessage;
import com.fasterxml.jackson.core.JsonParseException;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class ErrorInterceptor implements Interceptor {

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {

        Request request = chain.request();
        Response response = chain.proceed(request);

        if (response.code() == 500) {
            Log.d("TAG", "Server error");
        } else if (response.code() == 400 || response.code() == 404) {
            Log.d("TAG", response.body().string());
        }

        return response;

    }

}
