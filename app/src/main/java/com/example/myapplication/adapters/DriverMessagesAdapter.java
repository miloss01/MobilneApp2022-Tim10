package com.example.myapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.dto.MessageReceivedDTO;
import com.example.myapplication.dto.UserExpandedDTO;

import java.util.ArrayList;
import java.util.Objects;

public class DriverMessagesAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private ArrayList<MessageReceivedDTO> mMessageList;
    private UserExpandedDTO user;
    private Long personalId;

    private static final int VIEW_MESSAGE_ME = 1;
    private static final int VIEW_MESSAGE_OTHER = 2;

    public DriverMessagesAdapter(Context context, ArrayList<MessageReceivedDTO> messageList,
                                 UserExpandedDTO passenger, Long personalId) {
        mContext = context;
        mMessageList = messageList;
        this.user = passenger;
        this.personalId = personalId;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        if (viewType == VIEW_MESSAGE_ME) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.driver_message_me_cell, parent, false);
            return new DriverMeMessageHolder(view);
        } else if (viewType == VIEW_MESSAGE_OTHER) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.driver_message_other_cell, parent, false);
            return new DriverOtherMessageHolder(view, user);
        }

        return null;
    }

    @Override
    public int getItemViewType(int position) {
        MessageReceivedDTO message = (MessageReceivedDTO) mMessageList.get(position);
        Long userId = Objects.equals(personalId, message.getSenderId()) ? message.getReceiverId() : message.getSenderId();

        if (Objects.equals(message.getSenderId(), userId)) {
            return VIEW_MESSAGE_OTHER;  // Passenger sent the message
        } else {
            return VIEW_MESSAGE_ME;
        }
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MessageReceivedDTO message = (MessageReceivedDTO) mMessageList.get(position);

        switch (holder.getItemViewType()) {
            case VIEW_MESSAGE_ME:
                ((DriverMeMessageHolder) holder).bind(message);
                break;
            case VIEW_MESSAGE_OTHER:
                ((DriverOtherMessageHolder) holder).bind(message);
        }
    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }
}