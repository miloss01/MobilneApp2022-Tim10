package com.example.myapplication.dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.example.myapplication.fragments.TimePickerFragment;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    TextView textView;

    public DatePickerFragment() {
        // Required empty public constructor
    }

    public DatePickerFragment(TextView textView) {
        this.textView = textView;
    }


    public static DatePickerFragment newInstance() {

        return new DatePickerFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        super.onCreate(savedInstanceState);

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(requireContext(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        int monthModified = month + 1;
        String dayStr = day + "";
        if (dayStr.length() == 1) dayStr = "0" + dayStr;
        String monthStr = monthModified + "";
        if (monthStr.length() == 1) monthStr = "0" + monthStr;
        String time = String.format("Selected: %s.%s.%d 00:00", dayStr, monthStr, year);
        this.textView.setText(time);
    }
}