package com.example.myapplication.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.myapplication.R;
import com.example.myapplication.activities.PassengerAccountActivity;
import com.example.myapplication.adapters.FavRoutesAdapter;
import com.example.myapplication.adapters.PassengerRideAdapter;
import com.example.myapplication.dto.FavoriteLocationDTO;
import com.example.myapplication.dto.FavoriteLocationResponseDTO;
import com.example.myapplication.services.IRideService;
import com.example.myapplication.tools.Retrofit;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoriteRoutesFragment extends Fragment {

    private static PassengerAccountActivity view;
    private ListView listView;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorite_routes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupList();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

//    @Override
//    public void onDestroyView() {
//        Toolbar tb = view.findViewById(R.id.passenger_account_toolbar);
//        tb.setTitle("Account");
//        super.onDestroyView();
//    }

    @Override
    public void onDetach() {
        Toolbar tb = view.findViewById(R.id.passenger_account_toolbar);
        tb.setTitle("Account");
        super.onDetach();
    }

    private void setupList() {

        IRideService rideService = Retrofit.retrofit.create(IRideService.class);
        Call<List<FavoriteLocationResponseDTO>> fav = rideService.getFavoriteLocations();
        fav.enqueue(new Callback<List<FavoriteLocationResponseDTO>>() {
            @Override
            public void onResponse(Call<List<FavoriteLocationResponseDTO>> call, Response<List<FavoriteLocationResponseDTO>> response) {
                ArrayList<FavoriteLocationResponseDTO> locationDTOS = (ArrayList<FavoriteLocationResponseDTO>) response.body();
                listView = (ListView) getView().findViewById(R.id.passenger_favorites_listView);
                FavRoutesAdapter adapter = new FavRoutesAdapter(view, R.layout.fav_route_cell, locationDTOS);
                listView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<FavoriteLocationResponseDTO>> call, Throwable t) {
                Log.i("TAG", t.getMessage());
            }
        });


    }

}