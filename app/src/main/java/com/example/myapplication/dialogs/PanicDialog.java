package com.example.myapplication.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.myapplication.R;
import com.example.myapplication.dto.ReasonDTO;
import com.example.myapplication.services.IRideService;
import com.example.myapplication.tools.Retrofit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PanicDialog extends DialogFragment {

    public Integer rideId;

    public static PanicDialog newInstance(int rideId) {
        PanicDialog d = new PanicDialog();


        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("rideId", rideId);
        d.setArguments(args);

        return d;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        rideId = getArguments().getInt("rideId");

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_panic, null);
        builder.setView(dialogView)
                .setTitle("Send panic to administrator")
                .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        TextView panicText = dialogView.findViewById(R.id.panic_reason_edit);
                        sendPanic(rideId, panicText.getText().toString());
                    }
                })
                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        PanicDialog.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

    private void sendPanic(Integer rideId, String reason) {
        IRideService rideService = Retrofit.retrofit.create(IRideService.class);
        Call<Void> panicResponseCall = rideService.sendPanic(rideId, new ReasonDTO(reason));

        panicResponseCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.d("TAG", "poslat panic");
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("TAG", "greska u slanju panic", t);
            }
        });
    }

}
