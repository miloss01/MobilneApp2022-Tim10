package com.example.myapplication.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.myapplication.R;
import com.example.myapplication.dto.ChangeRequestDTO;
import com.example.myapplication.dto.DriverDTO;
import com.example.myapplication.dto.UserExpandedDTO;
import com.example.myapplication.dto.VehicleDTO;
import com.example.myapplication.services.IDriverService;
import com.example.myapplication.tools.Retrofit;
import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DriverAccountFragment extends Fragment {
    private View view;
    private ImageView driverImageView;
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
    private VehicleDTO vehicleForSaving;

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
        view.findViewById(R.id.driver_change_profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"),3);
            }

        });
        view.findViewById(R.id.driver_send_change_request).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageView iv1 = driverImageView;
                iv1.buildDrawingCache();
                Bitmap bitmap = iv1.getDrawingCache();

                ByteArrayOutputStream stream=new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
                byte[] image=stream.toByteArray();
                System.out.println("byte array:"+image);

                String img_str = "data:image/bmp;base64," + Base64.encodeToString(image, 0);
                Log.d("DEBUG","string:"+img_str);

                UserExpandedDTO driver = new UserExpandedDTO(id, nameEditText.getText().toString(),
                        lastNameEditText.getText().toString(), img_str,
                        phoneEditText.getText().toString(), emailEditText.getText().toString(),
                        addressEditText.getText().toString());
                vehicleForSaving.setModel(modelEditText.getText().toString());
                vehicleForSaving.setLicenseNumber(registrationEditText.getText().toString());
                vehicleForSaving.setPassengerSeats(Integer.parseInt(seatNumberEditText.getText().toString()));
                vehicleForSaving.setBabyTransport(babyCheckBox.isChecked());
                vehicleForSaving.setPetTransport(petsCheckBox.isChecked());
                sendChangeRequest(driver, vehicleForSaving);
            }

        });
        return view;
    }

    private void sendChangeRequest(UserExpandedDTO driver, VehicleDTO vehicleForSaving) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        ChangeRequestDTO changeRequestDTO = new ChangeRequestDTO(driver, vehicleForSaving, LocalDateTime.now().format(dateTimeFormatter));
        IDriverService driverService = Retrofit.retrofit.create(IDriverService.class);
        Call<ChangeRequestDTO> request = driverService.updateChangeRequest(id, changeRequestDTO);
        request.enqueue(new Callback<ChangeRequestDTO>() {
            @Override
            public void onResponse(Call<ChangeRequestDTO> call, Response<ChangeRequestDTO> response) {
                if (response.code() != 200) {
                    Snackbar.make(view, "Bad parameters for request", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                }else Snackbar.make(view, "Change request sent", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }

            @Override
            public void onFailure(Call<ChangeRequestDTO> call, Throwable t) {
                Snackbar.make(view, "Error", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == 3) {
                if (resultCode == Activity.RESULT_OK) {
                    Uri imageUri = data.getData();
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                    driverImageView.setImageBitmap(bitmap);
                    //data gives you the image uri. Try to convert that to bitmap
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    Log.e("TAG", "Selecting picture cancelled");
                }
            }
        } catch (Exception e) {
            Log.e("TAG", "Exception in onActivityResult : " + e.getMessage());
        }
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
                vehicleForSaving = vehicleDTO;
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
        driverImageView = view.findViewById(R.id.driver_image);
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
                if (driver.getProfilePicture().contains("base64")) {
                    String cleanImage = driver.getProfilePicture().substring(driver.getProfilePicture().indexOf(','));
                    Log.d("DEBUG", cleanImage);
                    byte[] decodedString = Base64.decode(cleanImage, Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    driverImageView.setImageBitmap(decodedByte);
                }
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