package com.example.myapplication.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.myapplication.R;
import com.example.myapplication.services.AuthService;
import com.example.myapplication.services.ResetPasswordService;

public class ResetPasswordActivity extends Activity {

    private ResetPasswordService resetPasswordService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        resetPasswordService = new ResetPasswordService(this);

        TextView emailTextView = findViewById(R.id.reset_edit_email);
        TextView codeTextView = findViewById(R.id.reset_edit_code);
        TextView passwordTextView = findViewById(R.id.reset_edit_password);
        Button requestCodeBtn = findViewById(R.id.reset_send_button);
        Button applyCodeBtn = findViewById(R.id.reset_confirm_button);

        requestCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPasswordService.requestCode(emailTextView.getText().toString());
                System.out.println("request gotovo u activity");
            }
        });

        applyCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPasswordService.applyCode(emailTextView.getText().toString(), codeTextView.getText().toString(), passwordTextView.getText().toString());
                System.out.println("apply gotovo u activity");
            }
        });

    }

}
