package com.example.myapplication.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.dto.IsActiveDTO;
import com.example.myapplication.dto.MessageReceivedDTO;
import com.example.myapplication.dto.UserDTO;
import com.example.myapplication.services.IAppUserService;
import com.example.myapplication.tools.Retrofit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuickMessageDialog extends DialogFragment {

    public Integer rideId;

    public QuickMessageDialog() {
    }


    public static QuickMessageDialog newInstance(int rideId) {
        QuickMessageDialog dialog = new QuickMessageDialog();
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
        View dialogView = inflater.inflate(R.layout.dialog_quick_message, null);
        builder.setView(dialogView)
                .setTitle("Send message to all passengers")
                .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        TextView messageField = dialogView.findViewById(R.id.quick_message_field);
                        sendMessage(rideId, messageField.getText().toString());
                    }
                })
                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        QuickMessageDialog.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

    private void sendMessage(int rideId, String messageContent) {
    }
}