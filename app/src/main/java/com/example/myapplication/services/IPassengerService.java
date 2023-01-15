package com.example.myapplication.services;

import com.example.myapplication.dto.PassengerDTO;
import com.example.myapplication.dto.RideDTO;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface IPassengerService {

    @GET("passenger/{id}")
    Call<PassengerDTO> getPassenger(@Path("id") Integer driverId);

}
