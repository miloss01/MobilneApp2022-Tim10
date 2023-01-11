package com.example.myapplication.services;

import com.example.myapplication.dto.RideDTO;
import com.example.myapplication.dto.UserExtendedDTO;
import com.example.myapplication.dto.VehicleDTO;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface IDriverService {
    @GET("driver/{id}")
    Call<UserExtendedDTO> getDriver(@Path("id") Integer id);

    @GET("driver/{id}/vehicle")
    Call<VehicleDTO> getVehicleByDriverId(@Path("id") Integer id);
}
