package com.example.myapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.R;
import com.example.myapplication.models.Message;
import com.example.myapplication.models.Ride;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DriverInboxAdapter extends ArrayAdapter<Message> {

    private List<Message> messagesList;

    public DriverInboxAdapter(Context context, int resource, List<Message> messages) {
        super(context, resource, messages);
        this.messagesList = messages;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Message message = messagesList.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.driver_inbox_cell,
                    parent, false);
        }

        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        TextView tvTime = (TextView) convertView.findViewById(R.id.textview_messagecell_time);
        tvTime.setText(String.format(message.getDate().toString(), format));

        TextView tvSender = (TextView) convertView.findViewById(R.id.textview_messagecell_sendername);
        String name = message.getHeader();
        tvSender.setText(name);

        if (message.getAvatar()==1){
            convertView.setBackgroundColor(500005);
        }
        if (message.getAvatar()==2){
            convertView.setBackgroundColor(500002);
        }
        if (message.getAvatar()==3){
            convertView.setBackgroundColor(500004);
        }

        return convertView;
    }
}
