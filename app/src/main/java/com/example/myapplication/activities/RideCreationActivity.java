package com.example.myapplication.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.DialogFragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.databinding.ActivityMainBinding;
import com.example.myapplication.dto.DepartureDestinationLocationsDTO;
import com.example.myapplication.dto.LocationDTO;
import com.example.myapplication.dto.LoginDTO;
import com.example.myapplication.dto.RideCreationDTO;
import com.example.myapplication.dto.RideDTO;
import com.example.myapplication.dto.TokenResponseDTO;
import com.example.myapplication.dto.UserDTO;
import com.example.myapplication.fragments.TimePickerFragment;
import com.example.myapplication.services.AuthService;
import com.example.myapplication.services.ILoginService;
import com.example.myapplication.services.IRideService;
import com.example.myapplication.tools.Retrofit;
import com.google.android.material.snackbar.Snackbar;
import com.shuhart.stepview.StepView;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;

public class RideCreationActivity extends AppCompatActivity {

    private int currentStep = 0;
    private RideCreationDTO ride;
    private AuthService authService;
    private StepView stepView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_creation);
        authService = new AuthService(this);
        stepView = findViewById(R.id.step_view);
        stepView.getState()
                .selectedTextColor(ContextCompat.getColor(this, com.google.android.material.R.color.material_on_surface_emphasis_high_type))
                .animationType(StepView.ANIMATION_CIRCLE)
                .selectedCircleColor(ContextCompat.getColor(this, R.color.black))
                .selectedCircleRadius(getResources().getDimensionPixelSize(R.dimen.fab_margin))
                .selectedStepNumberColor(ContextCompat.getColor(this, R.color.white))

                .animationDuration(getResources().getInteger(android.R.integer.config_shortAnimTime))
                .stepLineWidth(getResources().getDimensionPixelSize(R.dimen.fab_margin))
                .textSize(getResources().getDimensionPixelSize(R.dimen.fab_margin))
                .stepNumberTextSize(getResources().getDimensionPixelSize(R.dimen.fab_margin))
                //.typeface(ResourcesCompat.getFont(context, R.font.class))
                // other state methods are equal to the corresponding xml attributes
                .commit();
        //stepView.go(step, true);
        stepView.done(false);

        findViewById(R.id.next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentStep < stepView.getStepCount() - 1) {
                    currentStep++;
                    stepView.go(currentStep, true);
                    displaySelectedView();
                } else {
                    stepView.done(true);
                    AlertDialog.Builder builder = new AlertDialog.Builder(stepView.getContext());
                    builder.setMessage("Book a ride with this info?").setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();
                }
            }
        });

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentStep > 0) {
                    currentStep--;
                }
                stepView.done(false);
                stepView.go(currentStep, true);
                displaySelectedView();

            }
        });

        Spinner spiner = findViewById(R.id.sppiner_vehicle_type);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.spiner_vehicle_type));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spiner.setAdapter(adapter);
    }

    void displaySelectedView(){
        View locationTime = this.findViewById(R.id.location_and_time);
        View prefrences = this.findViewById(R.id.prefrences);
        switch (currentStep) {
            case 0:
                locationTime.setVisibility(View.VISIBLE);
                prefrences.setVisibility(View.GONE);
                break;
            case 1:
                locationTime.setVisibility(View.GONE);
                prefrences.setVisibility(View.VISIBLE);
        }
    }

    public void showTimePickerDialog(View v) {
        TextView receiver_msg = (TextView) this.findViewById(R.id.time_text);
        DialogFragment newFragment = new TimePickerFragment(receiver_msg);
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            String selectTime = "Selected Time: ";
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    sendRideData(selectTime);
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    break;
            }
        }
    };

    private void sendRideData(String selectTime) {
        EditText departure = findViewById(R.id.reservation_departure);
        EditText destination = findViewById(R.id.reservation_destination);
        TextView startTextView = (TextView) findViewById(R.id.time_text);
        String startTime = startTextView.getText().toString().substring(selectTime.length());
        Spinner spinner = findViewById(R.id.sppiner_vehicle_type);
        String vehicleType = spinner.getSelectedItem().toString().toLowerCase(Locale.ROOT);
        CheckBox petCheckBox = findViewById(R.id.pet_checkbox);
        boolean petTransport = petCheckBox.isChecked();
        CheckBox babyCheckBox = findViewById(R.id.children_checkbox);
        boolean babyTransport = babyCheckBox.isChecked();
        int estimatedTime = 5;
        if (departure.getText().toString().equals("") || destination.getText().toString().equals("")) {
            Snackbar.make(stepView, "Booking aborted. Fill in the locations", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return;
        }
        String[] dates = startTime.split(":");

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime setDate = LocalDate.now().atTime(Integer.parseInt(dates[0]), Integer.parseInt(dates[0]));
        if (now.getHour()>setDate.getHour()) {
            setDate.plusDays(1);
            System.out.println("aaaaaaaaaaaaaaaaa");
        }
        long minutesBetween = ChronoUnit.MINUTES.between(now, setDate);
        if (minutesBetween > 300 || minutesBetween < 0) {
            Snackbar.make(stepView, "Booking aborted. Cannot book more than 5 hours in advance", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            System.out.println(setDate);
            System.out.println(minutesBetween);
            return;
        }
        ArrayList<DepartureDestinationLocationsDTO> departureDestinationLocations = new ArrayList<>();
        ArrayList<UserDTO> passengers = new ArrayList<>();
        departureDestinationLocations.add(new DepartureDestinationLocationsDTO(
                new LocationDTO(departure.getText().toString(), 45.2366791, 19.8160032),
                new LocationDTO(destination.getText().toString(), 45.2366791, 19.8160032)
        ));
        passengers.add(new UserDTO(
                Long.parseLong(Objects.requireNonNull(authService.getUserData().get("user_id"))),
                authService.getUserData().get("user_email")));
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
        ride = new RideCreationDTO(departureDestinationLocations, passengers, setDate.format(dateTimeFormatter), vehicleType, babyTransport, petTransport, estimatedTime);
        System.out.println(ride);
        IRideService rideService = Retrofit.retrofit.create(IRideService.class);

        Call<RideDTO> jwtResponseCall = rideService.addRide(ride);
        System.out.println(jwtResponseCall);
    }


}