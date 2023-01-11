package com.example.myapplication.services;

import com.example.myapplication.dto.DriverDTO;
import com.example.myapplication.dto.RideDTO;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface IDriverService {

    @GET("driver/{id}")
    Call<DriverDTO> getDriver(@Path("id") Integer id);

}
