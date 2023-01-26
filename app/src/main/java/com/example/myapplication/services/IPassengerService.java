package com.example.myapplication.services;

import com.example.myapplication.dto.PassengerDTO;
import com.example.myapplication.dto.PassengerRequestDTO;
import com.example.myapplication.dto.ReportDTO;
import com.example.myapplication.dto.RideDTO;
import com.example.myapplication.dto.RideResponseDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface IPassengerService {

    @GET("passenger/{id}")
    Call<PassengerDTO> getPassenger(@Path("id") Integer passengerId);

    @GET("passenger/{id}/ride")
    Call<RideResponseDTO> getPassengersRides(@Path("id") Integer id);

    @PUT("passenger/{id}")
    Call<PassengerDTO> updatePassenger(@Path("id") Integer passengerId, @Body PassengerRequestDTO passengerDTO);

    @GET("statistics/passenger-report-ride-number/{passengerId}/{from}/{to}")
    Call<ReportDTO> getRideNumReport(@Path("passengerId") Integer passengerId, @Path("from") String from, @Path("to") String to);

    @GET("statistics/passenger-report-distance/{passengerId}/{from}/{to}")
    Call<ReportDTO> getDistanceReport(@Path("passengerId") Integer passengerId, @Path("from") String from, @Path("to") String to);

    @GET("statistics/passenger-report-money/{passengerId}/{from}/{to}")
    Call<ReportDTO> getMoneyReport(@Path("passengerId") Integer passengerId, @Path("from") String from, @Path("to") String to);


}
