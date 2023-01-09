package com.example.myapplication.services;

import com.example.myapplication.dto.RideCreationDTO;
import com.example.myapplication.dto.RideDTO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface IRideService {
    @POST("ride")
    Call<RideDTO> addRide(@Body RideCreationDTO rideCreationDTO);
}
