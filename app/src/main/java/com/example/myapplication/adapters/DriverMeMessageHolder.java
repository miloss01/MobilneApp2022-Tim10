package com.example.myapplication.adapters;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.activities.DriverInboxActivity;
import com.example.myapplication.dto.MessageReceivedDTO;

import java.time.LocalDateTime;

public class DriverMeMessageHolder extends RecyclerView.ViewHolder {

    TextView tvMessage, tvTime;

    DriverMeMessageHolder(View itemView) {
        super(itemView);

        tvMessage = (TextView) itemView.findViewById(R.id.text_driverchat_message_me);
        tvTime = (TextView) itemView.findViewById(R.id.text_driverchat_timestamp_me);
    }

    void bind(MessageReceivedDTO message) {
        tvMessage.setText(message.getMessage());
        LocalDateTime t = LocalDateTime.parse(message.getTimeOfSending());
        tvTime.setText(t.format(DriverInboxActivity.formatter));
    }
}