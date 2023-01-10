package com.example.myapplication.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.myapplication.Constants;
import com.example.myapplication.R;
import com.example.myapplication.dto.TokenResponseDTO;
import com.example.myapplication.dto.LoginDTO;
import com.example.myapplication.services.AuthService;
import com.example.myapplication.services.ILoginService;
import com.example.myapplication.tools.Retrofit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class LoginActivity extends Activity {

    private AuthService authService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        authService = new AuthService(this);

        Button btn = findViewById(R.id.login_button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TextView emailTextView = findViewById(R.id.login_edit_email);
                TextView passwordTextView = findViewById(R.id.login_edit_password);

                authService.login(emailTextView.getText().toString(), passwordTextView.getText().toString());

                finish();

            }
        });

        Button btn2 = findViewById(R.id.login_reg_button);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, PassengerRegisterActivity.class);
                startActivity(i);
                finish();
            }
        });

        Button btn3 = findViewById(R.id.login_forgot_password_button);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, ResetPasswordActivity.class);
                startActivity(i);
            }
        });

    }
}
