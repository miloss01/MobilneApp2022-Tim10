package com.example.myapplication.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.dto.DepartureDestinationLocationsDTO;
import com.example.myapplication.dto.LocationDTO;
import com.example.myapplication.dto.RideCreationDTO;
import com.example.myapplication.dto.RideDTO;
import com.example.myapplication.dto.UserDTO;
import com.example.myapplication.dto.UserExtendedDTO;
import com.example.myapplication.dto.VehicleDTO;
import com.example.myapplication.fragments.MapFragment;
import com.example.myapplication.fragments.TimePickerFragment;
import com.example.myapplication.services.IDriverService;
import com.example.myapplication.services.IRideService;
import com.example.myapplication.tools.FragmentTransition;
import com.example.myapplication.tools.Retrofit;
import com.google.android.material.snackbar.Snackbar;
import com.shuhart.stepview.StepView;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RideCreationActivity extends AppCompatActivity {

    private int currentStep = 0;
    private RideCreationDTO ride;
    private StepView stepView;
    private EditText departure;
    private EditText destination;
    private LocationDTO departureDTO = new LocationDTO();
    private LocationDTO destinationDTO = new LocationDTO();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_creation);
        departure = findViewById(R.id.reservation_departure);
        destination = findViewById(R.id.reservation_destination);
        MapFragment mapFragment = MapFragment.newInstance();
        FragmentTransition.to(mapFragment, this, false, R.id.steper_map);
        findViewById(R.id.stepper_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (departure.getText().toString().equals("") || destination.getText().toString().equals("")) {
                    Snackbar.make(stepView, "Booking aborted. Fill in the locations", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    return;
                }
                departureDTO.setAddress(departure.getText().toString());
                destinationDTO.setAddress(destination.getText().toString());
                mapFragment.drawRoute(departureDTO, destinationDTO);
            }
        });
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

        TextView startTextView = (TextView) findViewById(R.id.time_text);
        if (startTextView.getText().toString().equals("")){
            Snackbar.make(stepView, "Booking aborted. Select time.", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return;
        }
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
        LocalDateTime setDate = LocalDate.now().atTime(Integer.parseInt(dates[0]), Integer.parseInt(dates[1]));
        if (now.getHour()>setDate.getHour()) {
            setDate = setDate.plusDays(1);
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
                departureDTO,
                destinationDTO
        ));
        passengers.add(new UserDTO(
                Long.parseLong(Retrofit.sharedPreferences.getString("user_id", null)),
                Retrofit.sharedPreferences.getString("user_email", null)));
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        ride = new RideCreationDTO(departureDestinationLocations, passengers, setDate.format(dateTimeFormatter), vehicleType, babyTransport, petTransport, estimatedTime);
        System.out.println(ride);

        saveInBase();
    }

    private void saveInBase() {
        IRideService rideService = Retrofit.retrofit.create(IRideService.class);
        Call<RideDTO> reservation = rideService.addRide(ride);
        reservation.enqueue(new Callback<RideDTO>() {
            @Override
            public void onResponse(Call<RideDTO> call, Response<RideDTO> response) {
                if (response.code() != 200)
                    return;
                RideDTO rideDTO = response.body();
                assert rideDTO != null;
                Log.d("TAG", rideDTO.toString());
                showInformation(rideDTO);

            }

            @Override
            public void onFailure(Call<RideDTO> call, Throwable t) {
                Log.d("TAG", "greska");
                Snackbar.make(stepView, "Ride could not be booked", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }


        public void showInformation(RideDTO rideDTO){
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            final View newLocationPopup = getLayoutInflater().inflate(R.layout.new_location, null);

            Button cancel_location_btn = (Button) newLocationPopup.findViewById(R.id.cancel_location_btn);
            TextView driverName = (TextView) newLocationPopup.findViewById(R.id.driver_name);
            TextView driverMail = (TextView) newLocationPopup.findViewById(R.id.driver_mail);
            TextView driverPhone = (TextView) newLocationPopup.findViewById(R.id.driver_phone);
            TextView vehicleReg = (TextView) newLocationPopup.findViewById(R.id.vehicle_registration);
            TextView vehicleModel = (TextView) newLocationPopup.findViewById(R.id.vehicle_model);

            fillDriver(driverName, driverMail, driverPhone, rideDTO.getDriver().getId());
            fillVehicle(vehicleReg, vehicleModel, rideDTO.getDriver().getId());

            dialogBuilder.setView(newLocationPopup);
            AlertDialog dialog = dialogBuilder.create();
            dialog.show();


        cancel_location_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    private void fillVehicle(TextView vehicleReg, TextView vehicleModel, Long id) {
        IDriverService driverService = Retrofit.retrofit.create(IDriverService.class);
        Call<VehicleDTO> reservation = driverService.getVehicleByDriverId(Math.toIntExact(id));
        reservation.enqueue(new Callback<VehicleDTO>() {
            @Override
            public void onResponse(Call<VehicleDTO> call, Response<VehicleDTO> response) {
                if (response.code() != 200)
                    return;
                VehicleDTO vehicleDTO = response.body();
                vehicleReg.setText(vehicleDTO.getLicenseNumber());
                vehicleModel.setText(vehicleDTO.getModel());
            }

            @Override
            public void onFailure(Call<VehicleDTO> call, Throwable t) {
                Log.d("TAG", "greska vo");
            }
        });
    }

    private void fillDriver(TextView driverName, TextView driverMail, TextView driverPhone, Long id) {
        IDriverService driverService = Retrofit.retrofit.create(IDriverService.class);
        Call<UserExtendedDTO> callback = driverService.getDriver(Math.toIntExact(id));
        callback.enqueue(new Callback<UserExtendedDTO>() {
            @Override
            public void onResponse(Call<UserExtendedDTO> call, Response<UserExtendedDTO> response) {
                if (response.code() != 200)
                    return;
                UserExtendedDTO driver = response.body();
                assert driver != null;
                driverName.setText(driver.getName() + " " + driver.getSurname());
                driverMail.setText(driver.getEmail());
                driverPhone.setText(driver.getTelephoneNumber());
            }

            @Override
            public void onFailure(Call<UserExtendedDTO> call, Throwable t) {

            }
        });
    }

}