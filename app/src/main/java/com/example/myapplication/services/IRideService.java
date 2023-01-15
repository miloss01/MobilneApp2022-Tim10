package com.example.myapplication.services;
import com.example.myapplication.dto.ReasonDTO;
import com.example.myapplication.dto.RideCreationDTO;
import com.example.myapplication.dto.RideDTO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface IRideService {

    @GET("ride/driver/{driverId}/active")
    Call<RideDTO> getDriverActiveRide(@Path("driverId") Integer driverId);

    @GET("ride/passenger/{passengerId}/active")
    Call<RideDTO> getPassengerActiveRide(@Path("passengerId") Integer passengerId);

    @POST("ride")
    Call<RideDTO> addRide(@Body RideCreationDTO rideCreationDTO);

    @PUT("ride/{id}/panic")
    Call<Void> sendPanic(@Path("id") Integer id, @Body ReasonDTO reasonDTO);

    @GET("ride/{id}")
    Call<RideDTO> getRideById(@Path("id") Integer id);

    @GET("ride/driver/{driverId}/accepted")
    Call<RideDTO> getDriverAcceptedRide(@Path("driverId") Integer driverId);

    @PUT("ride/{id}/end")
    Call<Void> endRide(@Path("id") Integer id);

    @PUT("ride/{id}/accept")
    Call<Void> acceptRide(@Path("id") Integer id);

    @PUT("ride/{id}/cancel")
    Call<Void> cancelRide(@Path("id") Integer id, @Body ReasonDTO reasonDTO);

    @PUT("ride/{id}/start")
    Call<Void> startRide(@Path("id") Integer id);

}
