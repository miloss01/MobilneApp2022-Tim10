package com.example.myapplication.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.activities.DriverInboxActivity;
import com.example.myapplication.dto.MessageReceivedDTO;
import com.example.myapplication.dto.UserExpandedDTO;
import com.example.myapplication.models.Notification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class NotificationAdapter extends ArrayAdapter<Notification> {

    private List<Notification> notifications;

    public NotificationAdapter(Context context, int resource, ArrayList<Notification> notifs) {
        super(context, resource, notifs);
        this.notifications = notifs;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Notification notification = notifications.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.notification_cell,
                    parent, false);
        }

        TextView tvTime = convertView.findViewById(R.id.textview_notifcell_time);
        tvTime.setText(notification.getTimeOfReceiving().format(DriverInboxActivity.formatter));
        TextView tvMessage = convertView.findViewById(R.id.textview_notifcell_message);
        tvMessage.setText(notification.getMessage());

        return convertView;
    }

}
