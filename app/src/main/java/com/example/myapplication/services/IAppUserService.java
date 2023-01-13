package com.example.myapplication.services;

import com.example.myapplication.dto.IsActiveDTO;
import com.example.myapplication.dto.ReasonDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface IAppUserService {

    @PUT("user/changeActiveFlag/{id}")
    Call<IsActiveDTO> changeActiveFlag(@Path("id") Integer id, @Body IsActiveDTO isActiveDTO);


}

