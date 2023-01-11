package com.example.myapplication.tools;


import android.util.Log;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.example.myapplication.dto.ErrorMessage;

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
            ErrorMessage errorMessage = (ErrorMessage) Retrofit.retrofit.responseBodyConverter(
                            ErrorMessage.class, ErrorMessage.class.getAnnotations())
                    .convert(response.body());
            Log.d("TAG", errorMessage.toString());
        }

        return response;

    }

}
