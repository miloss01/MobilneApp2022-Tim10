package com.example.myapplication.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.activities.DriverMainActivity;
import com.example.myapplication.dto.ReasonDTO;
import com.example.myapplication.services.IRideService;
import com.example.myapplication.tools.Retrofit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CancelRideDialog extends DialogFragment {

    public Integer rideId;
    private Activity activity;

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public CancelRideDialog() {
    }

    public static CancelRideDialog newInstance(int rideId) {
        CancelRideDialog dialog = new CancelRideDialog();
        Bundle args = new Bundle();
        args.putInt("rideId", rideId);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        rideId = getArguments().getInt("rideId");

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_cancel_ride, null);
        builder.setView(dialogView)
                .setTitle("Provide explanation for cancelling")
                .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        TextView cancellationTextView = dialogView.findViewById(R.id.cancel_ride_edit);
                        sendCancellation(rideId, cancellationTextView.getText().toString());
                    }
                })
                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        CancelRideDialog.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

    private void sendCancellation(Integer rideId, String reason) {
        IRideService rideService = Retrofit.retrofit.create(IRideService.class);
        Call<Void> cancelResponseCall = rideService.cancelRide(rideId, new ReasonDTO(reason));

        cancelResponseCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.d("TAG", "Ride cancelled");
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("TAG", "Error when cancelling ride", t);
            }
        });
        activity.finish();
        startActivity(new Intent(activity, DriverMainActivity.class));
    }

}