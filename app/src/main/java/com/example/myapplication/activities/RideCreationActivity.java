package com.example.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.databinding.ActivityMainBinding;
import com.example.myapplication.fragments.TimePickerFragment;
import com.shuhart.stepview.StepView;

import java.util.ArrayList;

public class RideCreationActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    private int currentStep = 0;
    private int currentStep2 = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_ride_creation);
        StepView stepView = findViewById(R.id.step_view);
        stepView.getState()
                .selectedTextColor(ContextCompat.getColor(this, com.google.android.material.R.color.material_on_surface_emphasis_high_type))
                .animationType(StepView.ANIMATION_CIRCLE)
                .selectedCircleColor(ContextCompat.getColor(this, R.color.black))
                .selectedCircleRadius(getResources().getDimensionPixelSize(R.dimen.fab_margin))
                .selectedStepNumberColor(ContextCompat.getColor(this, com.google.android.material.R.color.m3_ref_palette_white))

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

}