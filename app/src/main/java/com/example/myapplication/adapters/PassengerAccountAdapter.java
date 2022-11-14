package com.example.myapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.models.Ride;
import com.example.myapplication.models.User;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PassengerAccountAdapter {

    private User user;

    public PassengerAccountAdapter(User u, View v) {
        this.user = u;
        getView(v);
    }

    public View getView(View view) {

        TextView tvName = view.findViewById(R.id.textview_passacc_name);
        tvName.setText(user.getName() + " " + user.getLastName());

        TextView tvEmail = view.findViewById(R.id.textview_passacc_email);
        tvEmail.setText(user.getEmail());

        return view;
    }
}
