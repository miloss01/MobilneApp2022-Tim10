package com.example.myapplication.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.adapters.RidePassengersAdapter;
import com.example.myapplication.dto.PassengerDTO;
import com.example.myapplication.fragments.FavoriteRoutesFragment;
import com.example.myapplication.fragments.PassengerEditAccountFragment;
import com.example.myapplication.fragments.RideStatsFragment;
import com.example.myapplication.models.User;
import com.example.myapplication.services.IPassengerService;
import com.example.myapplication.tools.FragmentTransition;
import com.example.myapplication.tools.Retrofit;
import com.example.myapplication.tools.UsersMokap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PassengerAccountActivity extends AppCompatActivity {

    private PassengerDTO passenger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_account);

        getPassengerAccount();

        Toolbar toolbar = findViewById(R.id.passenger_account_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        setupButtons();
    }

    private void getPassengerAccount() {
        IPassengerService passengerService = Retrofit.retrofit.create(IPassengerService.class);
        String passengerId = Retrofit.sharedPreferences.getString("user_id", null);
        Call<PassengerDTO> passengerResponseCall = passengerService.getPassenger(Integer.valueOf(passengerId));
        passengerResponseCall.enqueue(new Callback<PassengerDTO>() {
            @Override
            public void onResponse(Call<PassengerDTO> call, Response<PassengerDTO> response) {
                PassengerAccountActivity.this.passenger = response.body();
                setUpData(PassengerAccountActivity.this.passenger);
            }

            @Override
            public void onFailure(Call<PassengerDTO> call, Throwable t) {
                Log.d("TAG", "greska", t);
                Toast.makeText(PassengerAccountActivity.this,
                        "Couldn't load account details at the moment", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void setUpData(PassengerDTO passenger) {
        if (passenger != null) {
            PassengerAccountActivity.this.passenger = passenger;
            TextView name = findViewById(R.id.textview_passacc_name);
            name.setText(passenger.getName() + " "  + passenger.getSurname());
            TextView email = findViewById(R.id.textview_passacc_email);
            email.setText(passenger.getEmail());
            ImageView imageView = (ImageView) findViewById(R.id.img_passacc_profile);
            if (passenger.getProfilePicture() != null ) {
                if (!passenger.getProfilePicture().startsWith("data")) new RidePassengersAdapter.DownloadImageTask(imageView).execute(passenger.getProfilePicture());
                else {
                    final String encodedString = passenger.getProfilePicture();
                    final String pureBase64Encoded = encodedString.substring(encodedString.indexOf(",")  + 1);
                    final byte[] decodedBytes = Base64.decode(pureBase64Encoded, Base64.DEFAULT);
                    Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
                    imageView.setImageBitmap(decodedBitmap);
                }
            }

        }
    }

    private void setupButtons() {
        PassengerAccountActivity thisView = this;
        Toolbar toolbar = findViewById(R.id.passenger_account_toolbar);

        Button button = (Button) findViewById(R.id.btn_passacc_stats);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                toolbar.setTitle("Ride stats");
                FragmentTransition.to((Fragment) RideStatsFragment.newInstance(thisView),
                        thisView, true,
                        R.id.passacc_rellay);
            }
        });

        button = (Button) findViewById(R.id.btn_passacc_favroutes);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                toolbar.setTitle("Favorite routes");
                FragmentTransition.to((Fragment) FavoriteRoutesFragment.newInstance(thisView),
                        thisView, true,
                        R.id.passacc_rellay);
            }
        });

        ImageButton imgbutton = (ImageButton) findViewById(R.id.btn_passacc_edit);
        imgbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                FragmentTransition.to((Fragment) PassengerEditAccountFragment.newInstance(passenger),
                        thisView, true,
                        R.id.passacc_rellay);
            }
        });
    }
}