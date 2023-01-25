package com.example.myapplication.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.dto.MessageReceivedDTO;
import com.example.myapplication.dto.PassengerDTO;

public class DriverOtherMessageHolder extends RecyclerView.ViewHolder {

    ImageView imgProfilePicture;
    TextView tvMessage, tvTime, tvName;
    PassengerDTO passengerDTO;

    DriverOtherMessageHolder(View itemView, PassengerDTO passenger) {
        super(itemView);

        this.passengerDTO = passenger;
        imgProfilePicture = (ImageView) itemView.findViewById(R.id.image_driverchat_profile_other);
        tvName = (TextView) itemView.findViewById(R.id.tv_driverchat_user_other);
        tvMessage = (TextView) itemView.findViewById(R.id.tv_driverchat_message_other);
        tvTime = (TextView) itemView.findViewById(R.id.tv_driverchat_timestamp_other);
    }

    void bind(MessageReceivedDTO message) {
        tvMessage.setText(message.getMessage());
        tvTime.setText(message.getTimeOfSending());
        tvName.setText(passengerDTO.getName() + " " + passengerDTO.getSurname());

        if (passengerDTO.getProfilePicture() != null ) {
            if (!passengerDTO.getProfilePicture().startsWith("data")) new RidePassengersAdapter.DownloadImageTask(imgProfilePicture).execute(passengerDTO.getProfilePicture());
            else {
                final String encodedString = passengerDTO.getProfilePicture();
                final String pureBase64Encoded = encodedString.substring(encodedString.indexOf(",")  + 1);
                final byte[] decodedBytes = Base64.decode(pureBase64Encoded, Base64.DEFAULT);
                Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
                imgProfilePicture.setImageBitmap(decodedBitmap);
            }
        }

    }
}