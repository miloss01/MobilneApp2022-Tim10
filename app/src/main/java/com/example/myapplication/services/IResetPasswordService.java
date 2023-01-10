package com.example.myapplication.services;

import com.example.myapplication.dto.PasswordResetCodeDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface IResetPasswordService {

    @POST("user/resetPassword")
    Call<Void> requestCode(@Body PasswordResetCodeDTO passwordResetCodeDTO);

    @PUT("user/resetPassword")
    Call<Void> applyCode(@Body PasswordResetCodeDTO passwordResetCodeDTO);

}
