package com.example.myapplication.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.myapplication.R;
import com.example.myapplication.activities.PassengerAccountActivity;
import com.example.myapplication.adapters.FavRoutesAdapter;
import com.example.myapplication.adapters.PassengerRideAdapter;

import java.util.List;

public class FavoriteRoutesFragment extends Fragment {

    private static PassengerAccountActivity view;

    public FavoriteRoutesFragment() {
    }

    public static FavoriteRoutesFragment newInstance(PassengerAccountActivity view) {
        FavoriteRoutesFragment fragment = new FavoriteRoutesFragment();
        FavoriteRoutesFragment.view = view;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setupList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorite_routes, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDestroyView() {
        Toolbar tb = view.findViewById(R.id.passenger_account_toolbar);
        tb.setTitle("Account");
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        Toolbar tb = view.findViewById(R.id.passenger_account_toolbar);
        tb.setTitle("Account");
        super.onDetach();
    }

//    private void setupList() {
//        ListView listView = (ListView) view.findViewById(R.id.favroutes_listview);
//        FavRoutesAdapter adapter = new FavRoutesAdapter(view, R.layout.fav_route_cell);
//        listView.setAdapter(adapter);
//    }

}