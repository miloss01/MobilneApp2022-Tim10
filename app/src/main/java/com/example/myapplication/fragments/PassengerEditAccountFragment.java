package com.example.myapplication.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.myapplication.R;
import com.example.myapplication.activities.PassengerAccountActivity;
import com.example.myapplication.models.User;

public class PassengerEditAccountFragment extends Fragment {

    private User user;
    private PassengerAccountActivity view;
    public PassengerEditAccountFragment(User u, PassengerAccountActivity v) {
        this.user = u;
        this.view = v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_passenger_edit_account, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        EditText etName = getView().findViewById(R.id.et_passacc_name);
        etName.setText(user.getName());
        EditText etLastName = getView().findViewById(R.id.et_passacc_lastname);
        etLastName.setText(user.getLastName());
        EditText etPhone = getView().findViewById(R.id.et_passacc_phone);
        if (user.getPhone() != null) etPhone.setText(user.getPhone());
        EditText etEmail = getView().findViewById(R.id.et_passacc_email);
        etEmail.setText(user.getEmail());
    }
}