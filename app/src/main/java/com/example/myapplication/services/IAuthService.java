package com.example.myapplication.services;

import com.example.myapplication.dto.IsActiveDTO;
import com.example.myapplication.dto.TokenResponseDTO;
import com.example.myapplication.dto.LoginDTO;

import okhttp3.Interceptor;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface IAuthService {

    @POST("user/login")
    Call<TokenResponseDTO> login(@Body LoginDTO loginDTO);

    @PUT("user/changeActiveFlag/{id}")
    Call<IsActiveDTO> changeActiveFlag(@Path("id") Integer id, @Body IsActiveDTO isActiveDTO);

}
