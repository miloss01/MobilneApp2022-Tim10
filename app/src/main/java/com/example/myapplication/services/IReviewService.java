package com.example.myapplication.services;

import com.example.myapplication.dto.DriverReviewRequestDTO;
import com.example.myapplication.dto.VehicleReviewRequestDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface IReviewService {

    @POST("review/{rideId}/vehicle")
    Call<Void> addVehicleReview(@Path("rideId") Integer rideId, @Body VehicleReviewRequestDTO vehicleReviewRequestDTO);

    @POST("review/{rideId}/driver")
    Call<Void> addDriverReview(@Path("rideId") Integer rideId, @Body DriverReviewRequestDTO driverReviewRequestDTO);

}
