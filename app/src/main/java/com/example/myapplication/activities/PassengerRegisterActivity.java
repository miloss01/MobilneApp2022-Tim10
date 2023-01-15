package com.example.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.databinding.ActivityMainBinding;
import com.example.myapplication.dto.PassengerRequestDTO;
import com.example.myapplication.dto.PassengerResponseDTO;
import com.example.myapplication.dto.RideDTO;
import com.example.myapplication.services.IUserService;
import com.example.myapplication.tools.Retrofit;
import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PassengerRegisterActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_register);
        PassengerRegisterActivity view2 = this;

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        Button button = (Button) findViewById(R.id.register_summbit_btn);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
//                Intent intent = new Intent(view2, PassengerMainActivity.class);
//                Intent intent = new Intent(view2, DriverMainActivity.class);
//                startActivity(intent);
                EditText nameEdit = findViewById(R.id.reg_name);
                EditText latnameEdit = findViewById(R.id.reg_last_name);
                EditText phoneEdit = findViewById(R.id.reg_phone);
                EditText emailEdit = findViewById(R.id.reg_email);
                EditText adressEdit = findViewById(R.id.reg_adress);
                EditText passwordEdit = findViewById(R.id.reg_password);
                EditText passwordRepeatEdit = findViewById(R.id.reg_repeat_password);

                String name = nameEdit.getText().toString();
                String lastname = latnameEdit.getText().toString();
                String phoneNum = phoneEdit.getText().toString();
                String email = emailEdit.getText().toString();
                String adress = adressEdit.getText().toString();
                String password = passwordEdit.getText().toString();
                String passwordRepeat = passwordRepeatEdit.getText().toString();

                if (!password.equals(passwordRepeat)) {
                    Snackbar.make(view, "Password and repeat password don't match", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    return;
                }
                IUserService userService = Retrofit.retrofit.create(IUserService.class);
                Call<PassengerResponseDTO> passengerResponse = userService.savePassenger(new PassengerRequestDTO(name, lastname, "", phoneNum, email, adress, password));
                passengerResponse.enqueue(new Callback<PassengerResponseDTO>() {
                    @Override
                    public void onResponse(Call<PassengerResponseDTO> call, Response<PassengerResponseDTO> response) {
                        if (response.code() != 200) {
                            Snackbar.make(view, "Data not correct", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                            return;
                        }
                        PassengerResponseDTO responseDTO = response.body();
                        Log.d("TAG", responseDTO.toString());
                        Snackbar.make(view, "Please confirm registration on your email", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }

                    @Override
                    public void onFailure(Call<PassengerResponseDTO> call, Throwable t) {
                        Snackbar.make(view, "Registration failed", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                });

            }
        });


    }
}