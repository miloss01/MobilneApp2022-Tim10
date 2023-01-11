package com.example.myapplication.services;

import com.example.myapplication.dto.RideDTO;
import com.example.myapplication.dto.VehicleResponceDTO;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface IVehicleService {

    @GET("vehicle/all")
    Call<VehicleResponceDTO> getVehicles();
}
