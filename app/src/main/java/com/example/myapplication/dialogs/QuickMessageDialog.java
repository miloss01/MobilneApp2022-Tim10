package com.example.myapplication.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.activities.DriverMainActivity;
import com.example.myapplication.dto.MessageReceivedDTO;
import com.example.myapplication.dto.MessageSentDTO;
import com.example.myapplication.dto.RideDTO;
import com.example.myapplication.dto.UserDTO;
import com.example.myapplication.services.IAppUserService;
import com.example.myapplication.tools.Retrofit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuickMessageDialog extends DialogFragment {

    public RideDTO rideDTO;
    public static Context context;

    public QuickMessageDialog() {
    }


    public static QuickMessageDialog newInstance(RideDTO rideDTO, Boolean isDriverSender) {
        QuickMessageDialog dialog = new QuickMessageDialog();
        Bundle args = new Bundle();
        args.putSerializable("rideDTO", rideDTO);
        args.putBoolean("isDriverSender", isDriverSender);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        rideDTO = (RideDTO) getArguments().getSerializable("rideDTO");
        Boolean isDriverSender = getArguments().getBoolean("isDriverSender");

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_quick_message, null);
        builder.setView(dialogView)
                .setTitle("Send message to all passengers")
                .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        TextView messageField = dialogView.findViewById(R.id.quick_message_field);
                        if (isDriverSender)
                            sendMessage(messageField.getText().toString());
                        else
                            sendMessageAsPassenger(messageField.getText().toString());
                    }
                })
                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        QuickMessageDialog.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

    private void sendMessageAsPassenger(String messageContent) {

        String passengerId = Retrofit.sharedPreferences.getString("user_id", null);
        IAppUserService appUserService = Retrofit.retrofit.create(IAppUserService.class);

        Long driverId = rideDTO.getDriver().getId();

        MessageSentDTO messageSentDTO = new MessageSentDTO(driverId, messageContent, "ride", rideDTO.getId());

        Call<MessageReceivedDTO> sendCall = appUserService.sendMessageByUserId(driverId.intValue(), messageSentDTO);

        sendCall.enqueue(new Callback<MessageReceivedDTO>() {
            @Override
            public void onResponse(Call<MessageReceivedDTO> call, Response<MessageReceivedDTO> response) {
                Log.d("TAG", "Sent message to driverId: " + driverId);
//                Toast.makeText(context,"Message sent", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<MessageReceivedDTO> call, Throwable t) {
//                Toast.makeText(context,
//                        "Couldn't send message", Toast.LENGTH_SHORT).show();
                Log.d("TAG", "Error sending message", t);
            }
        });

    }

    private void sendMessage(String messageContent) {
        IAppUserService appUserService = Retrofit.retrofit.create(IAppUserService.class);

        for (UserDTO userDTO : rideDTO.getPassengers()) {
            Long receiverId = userDTO.getId();
            MessageSentDTO messageSentDTO = new MessageSentDTO(receiverId, messageContent, "ride", rideDTO.getId());

            Call<MessageReceivedDTO> sendCall = appUserService.sendMessageByUserId(receiverId.intValue(), messageSentDTO);

            sendCall.enqueue(new Callback<MessageReceivedDTO>() {
                @Override
                public void onResponse(Call<MessageReceivedDTO> call, Response<MessageReceivedDTO> response) {
                    Log.d("DEBUG", "Sent message to passengerId: " + receiverId);
                    Toast.makeText(context,"Message sent", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<MessageReceivedDTO> call, Throwable t) {
                    Toast.makeText(context,
                            "Couldn't send message", Toast.LENGTH_SHORT).show();
                    Log.d("DEBUG", "Error sending message", t);
                }
            });
        }
    }
}