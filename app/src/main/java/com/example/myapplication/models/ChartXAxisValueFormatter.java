package com.example.myapplication.models;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ChartXAxisValueFormatter implements IAxisValueFormatter {

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
//        axis.setLabelCount(3, true);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-dd-MM", Locale.GERMAN);
        Date pomoc = null;
        try {
            pomoc = formatter.parse("2025-30-12");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date date = new Date((long)value + pomoc.getTime());
        //Specify the format you'd like
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.GERMAN);
        return sdf.format(date);
    }
}
