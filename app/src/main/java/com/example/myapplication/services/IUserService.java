package com.example.myapplication.services;


import com.example.myapplication.dto.PassengerRequestDTO;
import com.example.myapplication.dto.PassengerResponseDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface IUserService {
    @POST("passenger")
    Call<PassengerResponseDTO> savePassenger(@Body PassengerRequestDTO passengerRequestDTO);
}
