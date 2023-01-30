package com.example.myapplication.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.activities.DriverMainActivity;
import com.example.myapplication.dialogs.PanicDialog;
import com.example.myapplication.dto.IsActiveDTO;
import com.example.myapplication.services.AuthService;
import com.example.myapplication.services.IAppUserService;
import com.example.myapplication.services.IAuthService;
import com.example.myapplication.services.IRideService;
import com.example.myapplication.tools.Retrofit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DriverNoRideFragment extends Fragment {

    private static final String ARG_PARAM_USERID = "userId";

    private String userId;

    public DriverNoRideFragment() {
    }

    public static DriverNoRideFragment newInstance(String userId) {
        DriverNoRideFragment fragment = new DriverNoRideFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM_USERID, userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userId = getArguments().getString(ARG_PARAM_USERID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_driver_no_ride, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Switch toggle = getView().findViewById(R.id.driver_main_toggle);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                AuthService authService = new AuthService(getActivity());
                if (isChecked) authService.startWorkingTime();
                else authService.endWorkingTime();
                changeActiveFlag(isChecked, toggle);

            }
        });

        IAppUserService appUserService = Retrofit.retrofit.create(IAppUserService.class);
        String driverId = Retrofit.sharedPreferences.getString("user_id", null);
        Call<IsActiveDTO> getCall = appUserService.getActiveFlag(Integer.valueOf(driverId));

        getCall.enqueue(new Callback<IsActiveDTO>() {
            @Override
            public void onResponse(Call<IsActiveDTO> call, Response<IsActiveDTO> response) {
                TextView t = getView().findViewById(R.id.driver_main_label_active);
                if (response.body().isActive()) toggle.setChecked(true);
                else toggle.setChecked(false);
            }

            @Override
            public void onFailure(Call<IsActiveDTO> call, Throwable t) {
                Toast.makeText(getContext(),
                        "Couldn't get active status", Toast.LENGTH_SHORT).show();
                Log.d("DEBUG", "Error getting active status", t);
            }
        });

        Button refresh = getView().findViewById(R.id.driver_main_noRide_refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
                getActivity().overridePendingTransition(0, 0);
                startActivity(new Intent(getActivity(), DriverMainActivity.class));
                getActivity().overridePendingTransition(0, 0);
            }
        });

    }

    private void changeActiveFlag(boolean status, Switch toggle) {
        IAppUserService appUserService = Retrofit.retrofit.create(IAppUserService.class);
        String driverId = Retrofit.sharedPreferences.getString("user_id", null);
        Call<IsActiveDTO> changeCall = appUserService.changeActiveFlag(
                Integer.valueOf(driverId),
                new IsActiveDTO(status));

        changeCall.enqueue(new Callback<IsActiveDTO>() {
            @Override
            public void onResponse(Call<IsActiveDTO> call, Response<IsActiveDTO> response) {
                TextView t = getView().findViewById(R.id.driver_main_label_active);
                if (response.body().isActive()) t.setText("ACTIVE");
                else t.setText("INACTIVE");
                Log.d("DEBUG", "Changed active status");
            }

            @Override
            public void onFailure(Call<IsActiveDTO> call, Throwable t) {
                Toast.makeText(getContext(),
                        "Couldn't change active status", Toast.LENGTH_SHORT).show();
                Log.d("DEBUG", "Error changing active status", t);
            }
        });
    }

}