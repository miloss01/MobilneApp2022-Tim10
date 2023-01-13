package com.example.myapplication.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.myapplication.R;
import com.example.myapplication.dto.DriverDTO;
import com.example.myapplication.dto.VehicleDTO;
import com.example.myapplication.services.IDriverService;
import com.example.myapplication.tools.Retrofit;
import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DriverAccountFragment extends Fragment {
    private View view;
    private EditText nameEditText;
    private EditText lastNameEditText;
    private EditText phoneEditText;
    private EditText emailEditText;
    private EditText addressEditText;
    private EditText modelEditText;
    private EditText registrationEditText;
    private EditText seatNumberEditText;
    private CheckBox babyCheckBox;
    private  CheckBox petsCheckBox;
    private Integer id;

    public DriverAccountFragment() {
        // Required empty public constructor
    }


    public static DriverAccountFragment newInstance() {
        DriverAccountFragment fragment = new DriverAccountFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        id = Integer.valueOf(Retrofit.sharedPreferences.getString("user_id", null));
        view =  inflater.inflate(R.layout.fragment_driver_account, container, false);
        loadData();
        return view;
    }

    private void loadData() {
        loadDriver();
        loadVehicle();
    }

    private void loadVehicle() {
        modelEditText = view.findViewById(R.id.driver_vehicle_model);
        registrationEditText = view.findViewById(R.id.driver_vehicle_registration);
        seatNumberEditText = view.findViewById(R.id.driver_seatNum);
        babyCheckBox = view.findViewById(R.id.driver_baby_check_box);
        petsCheckBox = view.findViewById(R.id.driver_pet_check_box);
        IDriverService driverService = Retrofit.retrofit.create(IDriverService.class);
        Call<VehicleDTO> reservation = driverService.getVehicleByDriverId(id);
        reservation.enqueue(new Callback<VehicleDTO>() {
            @Override
            public void onResponse(Call<VehicleDTO> call, Response<VehicleDTO> response) {
                if (response.code() != 200)
                    return;
                VehicleDTO vehicleDTO = response.body();
                Log.d("DEBUG", vehicleDTO.toString());
                registrationEditText.setText(vehicleDTO.getLicenseNumber());
                modelEditText.setText(vehicleDTO.getModel());
                seatNumberEditText.setText(vehicleDTO.getPassengerSeats() + "");
                babyCheckBox.setChecked(vehicleDTO.getBabyTransport());
                petsCheckBox.setChecked(vehicleDTO.getPetTransport());
            }

            @Override
            public void onFailure(Call<VehicleDTO> call, Throwable t) {
                Log.d("TAG", "greska vo");
            }
        });
    }

    private void loadDriver() {
        nameEditText = view.findViewById(R.id.driver_name);
        lastNameEditText = view.findViewById(R.id.driver_lastName);
        phoneEditText = view.findViewById(R.id.driver_phone);
        emailEditText = view.findViewById(R.id.driver_email);
        addressEditText = view.findViewById(R.id.driver_adress);
        IDriverService driverService = Retrofit.retrofit.create(IDriverService.class);
        Call<DriverDTO> callback = driverService.getDriver(id);
        callback.enqueue(new Callback<DriverDTO>() {
            @Override
            public void onResponse(Call<DriverDTO> call, Response<DriverDTO> response) {
                if (response.code() != 200)
                    return;
                DriverDTO driver = response.body();
                assert driver != null;
                nameEditText.setText(driver.getName());
                lastNameEditText.setText(driver.getSurname());
                emailEditText.setText(driver.getEmail());
                phoneEditText.setText(driver.getTelephoneNumber());
                addressEditText.setText(driver.getAddress());
            }

            @Override
            public void onFailure(Call<DriverDTO> call, Throwable t) {

            }
        });
    }
}