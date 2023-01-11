package com.example.myapplication.services;

import com.example.myapplication.dto.RideDTO;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface IRideService {

    @GET("ride/driver/{driverId}/active")
    Call<RideDTO> getDriverActiveRide(@Path("driverId") Integer driverId);

    @GET("ride/passenger/{passengerId}/active")
    Call<RideDTO> getPassengerActiveRide(@Path("passengerId") Integer passengerId);

}
