package com.example.myapplication.fragments;

import static java.lang.String.*;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.text.format.DateFormat;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.myapplication.R;
import com.example.myapplication.activities.RideCreationActivity;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TimePickerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener{
    TextView textView;

    public TimePickerFragment() {
        // Required empty public constructor
    }

    public TimePickerFragment(TextView textView) {
        this.textView = textView;
    }


    public static TimePickerFragment newInstance() {

        return new TimePickerFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        super.onCreate(savedInstanceState);

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        String time = String.format("Selected time: %d:%d", i, i1);
        this.textView.setText(time);
    }
}