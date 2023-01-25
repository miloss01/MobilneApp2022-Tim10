package com.example.myapplication.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.activities.DriverInboxActivity;
import com.example.myapplication.dto.MessageReceivedDTO;
import com.example.myapplication.dto.PassengerDTO;
import com.example.myapplication.dto.UserExpandedDTO;

import java.time.LocalDateTime;

public class DriverOtherMessageHolder extends RecyclerView.ViewHolder {

    ImageView imgProfilePicture;
    TextView tvMessage, tvTime, tvName;
    UserExpandedDTO user;

    DriverOtherMessageHolder(View itemView, UserExpandedDTO passenger) {
        super(itemView);

        this.user = passenger;
        imgProfilePicture = (ImageView) itemView.findViewById(R.id.image_driverchat_profile_other);
        tvName = (TextView) itemView.findViewById(R.id.tv_driverchat_user_other);
        tvMessage = (TextView) itemView.findViewById(R.id.tv_driverchat_message_other);
        tvTime = (TextView) itemView.findViewById(R.id.tv_driverchat_timestamp_other);
    }

    void bind(MessageReceivedDTO message) {
        tvMessage.setText(message.getMessage());
        LocalDateTime t = LocalDateTime.parse(message.getTimeOfSending());
        tvTime.setText(t.format(DriverInboxActivity.formatter));
        tvName.setText(user.getName() + " " + user.getSurname());

        if (user.getProfilePicture() != null ) {
            if (!user.getProfilePicture().startsWith("data")) new RidePassengersAdapter.DownloadImageTask(imgProfilePicture).execute(user.getProfilePicture());
            else {
                final String encodedString = user.getProfilePicture();
                final String pureBase64Encoded = encodedString.substring(encodedString.indexOf(",")  + 1);
                final byte[] decodedBytes = Base64.decode(pureBase64Encoded, Base64.DEFAULT);
                Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
                imgProfilePicture.setImageBitmap(decodedBitmap);
            }
        }

    }
}