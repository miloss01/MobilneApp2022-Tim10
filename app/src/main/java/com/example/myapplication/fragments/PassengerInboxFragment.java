package com.example.myapplication.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;
import com.example.myapplication.adapters.PassengerInboxAdapter;


public class PassengerInboxFragment extends Fragment {


    public PassengerInboxFragment() {
        // Required empty public constructor
    }


    public static PassengerInboxFragment newInstance() {
        PassengerInboxFragment fragment = new PassengerInboxFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*PassengerInboxAdapter adapter = new PassengerInboxAdapter(getActivity());
        setListAdapter(adapter);*/

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.map_layout, container, false);
    }
}