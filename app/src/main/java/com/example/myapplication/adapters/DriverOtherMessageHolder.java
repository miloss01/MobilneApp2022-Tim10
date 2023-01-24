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

public class DriverOtherMessageHolder extends RecyclerView.ViewHolder {

    ImageView imgProfilePicture;
    TextView tvMessage, tvTime, tvName;

    DriverOtherMessageHolder(View itemView) {
        super(itemView);

        imgProfilePicture = (ImageView) itemView.findViewById(R.id.image_driverchat_profile_other);
        tvName = (TextView) itemView.findViewById(R.id.tv_driverchat_user_other);
        tvMessage = (TextView) itemView.findViewById(R.id.tv_driverchat_message_other);
        tvTime = (TextView) itemView.findViewById(R.id.tv_driverchat_timestamp_other);
    }

    void bind(MessageReceivedDTO message) {
        tvMessage.setText(message.getMessage());
        tvTime.setText(message.getTimeOfSending());

//        if (passenger.getProfilePicture() != null ) {
//            if (!passenger.getProfilePicture().startsWith("data")) new RidePassengersAdapter.DownloadImageTask(imageView).execute(passenger.getProfilePicture());
//            else {
//                final String encodedString = passenger.getProfilePicture();
//                final String pureBase64Encoded = encodedString.substring(encodedString.indexOf(",")  + 1);
//                final byte[] decodedBytes = Base64.decode(pureBase64Encoded, Base64.DEFAULT);
//                Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
//                imageView.setImageBitmap(decodedBitmap);
//            }
//        }

    }
}