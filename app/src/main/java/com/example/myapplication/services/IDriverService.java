package com.example.myapplication.services;

import com.example.myapplication.dto.ChangeRequestDTO;
import com.example.myapplication.dto.ReportDTO;
import com.example.myapplication.dto.VehicleDTO;
import com.example.myapplication.dto.DriverDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IDriverService {
    @GET("driver/{id}/vehicle")
    Call<VehicleDTO> getVehicleByDriverId(@Path("id") Integer id);

    @GET("driver/{id}")
    Call<DriverDTO> getDriver(@Path("id") Integer id);

    @PUT("driver/change-request/{driverId}")
    Call<ChangeRequestDTO> updateChangeRequest(@Path("driverId") Integer driverId, @Body ChangeRequestDTO requestDTO);

    @GET("driver/{id}/vehicle")
    Call<VehicleDTO> getVehicle(@Path("id") Integer id);

    @GET("ride/report-ride-number/{driverId}/{from}/{to}")
    Call<ReportDTO> getRideNumReport(@Path("driverId") Integer driverId, @Path("from") String from, @Path("to") String to);

    @GET("ride/report-distance/{driverId}/{from}/{to}")
    Call<ReportDTO> getDistanceReport(@Path("driverId") Integer driverId, @Path("from") String from, @Path("to") String to);

}
