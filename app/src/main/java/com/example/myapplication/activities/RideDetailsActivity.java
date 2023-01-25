package com.example.myapplication.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.myapplication.R;
import com.example.myapplication.dto.DriverReviewResponseDTO;
import com.example.myapplication.dto.RideDTO;
import com.example.myapplication.dto.RideResponseDTO;
import com.example.myapplication.dto.RideReviewDTO;
import com.example.myapplication.dto.UserDTO;
import com.example.myapplication.dto.UserResponseDTO;
import com.example.myapplication.dto.VehicleReviewResponseDTO;
import com.example.myapplication.models.Ride;
import com.example.myapplication.services.IDriverService;
import com.example.myapplication.services.IReviewService;
import com.example.myapplication.tools.Retrofit;

import org.w3c.dom.Text;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RideDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.driver_ride_details, null);

        RideDTO rideDTO = (RideDTO) getIntent().getSerializableExtra("rideDTO");

        TextView startTime = (TextView) v.findViewById(R.id.textview_ridedetails_starttime);
        startTime.setText(rideDTO.getStartTime());
        TextView endTime = (TextView) v.findViewById(R.id.textview_ridedetails_endtime);
        endTime.setText(rideDTO.getEndTime());
        TextView departure = (TextView) v.findViewById(R.id.textview_ridedetails_startaddress);
        departure.setText("START: " + rideDTO.getLocations().get(0).getDeparture().getAddress());
        TextView destination = (TextView) v.findViewById(R.id.textview_ridedetails_endaddress);
        destination.setText("END: " + rideDTO.getLocations().get(0).getDestination().getAddress());
        TextView price = (TextView) v.findViewById(R.id.textview_ridedetails_price);
        price.setText("+" + rideDTO.getTotalCost() + " din");
        TextView passengersText = (TextView) v.findViewById(R.id.textview_ridedetails_passengers);
        passengersText.setText("Passengers (" + rideDTO.getPassengers().size() + ")");
        for (UserDTO passenger : rideDTO.getPassengers())
            passengersText.setText(passengersText.getText().toString() + "\n" + passenger.getEmail());

        IReviewService reviewService = Retrofit.retrofit.create(IReviewService.class);
        Call<List<RideReviewDTO>> rideReviewsCall = reviewService.getRideReview(rideDTO.getId().intValue());

        rideReviewsCall.enqueue(new Callback<List<RideReviewDTO>>() {
            @Override
            public void onResponse(Call<List<RideReviewDTO>> call, Response<List<RideReviewDTO>> response) {

                List<RideReviewDTO> rideReviewDTOList = response.body();
                Log.d("TAG", rideReviewDTOList.toString());

                LinearLayout mainLinear = (LinearLayout) v.findViewById(R.id.driver_ride_details_linear);

                for (RideReviewDTO rideReviewDTO : rideReviewDTOList) {

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.setMargins(0, 40, 0, 0);
                    LinearLayout reviewLinear = new LinearLayout(RideDetailsActivity.this);
                    reviewLinear.setLayoutParams(params);
                    reviewLinear.setOrientation(LinearLayout.VERTICAL);

                    TextView passengerEmail = new TextView(RideDetailsActivity.this);
                    passengerEmail.setTypeface(Typeface.DEFAULT_BOLD);
                    passengerEmail.setClickable(true);
                    passengerEmail.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(RideDetailsActivity.this, DriverInboxActivity.class);
                            intent.putExtra("email", passengerEmail.getText().toString());
                            startActivity(intent);
                        }
                    });
                    reviewLinear.addView(passengerEmail);

                    VehicleReviewResponseDTO vehicleReview = rideReviewDTO.getVehicleReview();
                    if (vehicleReview != null) {
                        TextView vehicleReviewText = new TextView(RideDetailsActivity.this);
                        vehicleReviewText.setText("Vehicle review: " + vehicleReview.getComment() + " - rating: " + vehicleReview.getRating() + " ⭐");
                        passengerEmail.setText(vehicleReview.getPassenger().getEmail());
                        reviewLinear.addView(vehicleReviewText);
                    }

                    DriverReviewResponseDTO driverReview = rideReviewDTO.getDriverReview();
                    if (driverReview != null) {
                        TextView driverReviewText = new TextView(RideDetailsActivity.this);
                        driverReviewText.setText("Driver review: " + driverReview.getComment() + " - rating: " + driverReview.getRating() + " ⭐");
                        passengerEmail.setText(driverReview.getPassenger().getEmail());
                        reviewLinear.addView(driverReviewText);
                    }

                    mainLinear.addView(reviewLinear);

                }

                setContentView(v);

                Toolbar toolbar = findViewById(R.id.driver_ride_details_toolbar);
                setSupportActionBar(toolbar);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
                getSupportActionBar().setTitle("Ride details");
                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });
            }

            @Override
            public void onFailure(Call<List<RideReviewDTO>> call, Throwable t) {
                Log.d("TAG", "greska u povlacenju reviewova o voznji", t);
            }
        });
    }

}
