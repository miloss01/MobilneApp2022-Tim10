package com.example.myapplication.services;

import com.example.myapplication.dto.TokenResponseDTO;
import com.example.myapplication.dto.LoginDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ILoginService {

    @POST("user/login")
    Call<TokenResponseDTO> login(@Body LoginDTO loginDTO);

}
