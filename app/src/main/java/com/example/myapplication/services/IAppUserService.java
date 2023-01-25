package com.example.myapplication.services;

import com.example.myapplication.dto.IsActiveDTO;
import com.example.myapplication.dto.MessageReceivedDTO;
import com.example.myapplication.dto.MessageResponseDTO;
import com.example.myapplication.dto.MessageSentDTO;
import com.example.myapplication.dto.UserExpandedDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IAppUserService {

    @PUT("user/changeActiveFlag/{id}")
    Call<IsActiveDTO> changeActiveFlag(@Path("id") Integer id, @Body IsActiveDTO isActiveDTO);

    @POST("user/{id}/message")
    Call<MessageReceivedDTO> sendMessageByUserId(@Path("id") Integer id, @Body MessageSentDTO messageSentDTO);

    @GET("user/{id}/message")
    Call<MessageResponseDTO> getMessages(@Path("id") Integer id);

    @GET("user/1")
    Call<UserExpandedDTO> getById(@Query("id") Integer id);

}

