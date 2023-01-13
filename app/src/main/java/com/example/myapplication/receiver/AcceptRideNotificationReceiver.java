package com.example.myapplication.receiver;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.RemoteInput;

import com.example.myapplication.dto.ReasonDTO;
import com.example.myapplication.services.IRideService;
import com.example.myapplication.tools.Retrofit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AcceptRideNotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Long rideId = intent.getLongExtra("rideId", 500L);
        Log.d("TAG", rideId.toString());
        IRideService rideService = Retrofit.retrofit.create(IRideService.class);
        Call<Void> acceptOrDenyRideResponseCall = null;

        if (intent.getAction().equals("ACCEPT_RIDE")) {
            Log.d("TAG", "accept");
            acceptOrDenyRideResponseCall = rideService.acceptRide(rideId.intValue());
        }

        Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
        if (remoteInput != null) {
            String input = remoteInput.getString("DENY_RIDE_INPUT", "prazno");
            Log.d("TAG", input);
            acceptOrDenyRideResponseCall = rideService.cancelRide(rideId.intValue(), new ReasonDTO(input));
        }

        if (acceptOrDenyRideResponseCall == null)
            return;

        acceptOrDenyRideResponseCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() != 200)
                    return;

                Log.d("TAG", "dodata ili odbijena");
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("TAG", "greska u prihvatanju ili odbijanju voznje");
            }
        });

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(654234);
    }

}
