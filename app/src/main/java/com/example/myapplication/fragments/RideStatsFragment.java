package com.example.myapplication.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.activities.PassengerAccountActivity;
import com.example.myapplication.dialogs.DatePickerFragment;
import com.example.myapplication.dto.ReportDTO;
import com.example.myapplication.services.IPassengerService;
import com.example.myapplication.tools.Retrofit;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.android.material.snackbar.Snackbar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RideStatsFragment extends Fragment {

    private static PassengerAccountActivity parentActivity;

    private View view;
    private TextView dateFromTextView;
    private TextView dateToTextView;
    private TextView totalKmTextView;
    private TextView averageKmTextView;
    private TextView totalRideTextView;
    private TextView averageRideTextView;
    private TextView totalMoneyTextView;
    private TextView averageMoneyTextView;
    private BarChart moneyBarChart;
    private BarChart kmBarChart;
    private BarChart rideNumBarChart;

    public RideStatsFragment() {
        // Required empty public constructor
    }

    public static RideStatsFragment newInstance(PassengerAccountActivity view) {
        RideStatsFragment.parentActivity = view;
        return new RideStatsFragment();
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_ride_stats, container, false);
        dateFromTextView = view.findViewById(R.id.tv_passengerstats_dateFrom);
        dateToTextView= view.findViewById(R.id.tv_passengerstats_dateTo);
        totalKmTextView = view.findViewById(R.id.tv_passengerstats_totalKm);
        averageKmTextView = view.findViewById(R.id.tv_passengerstats_avgKm);
        totalRideTextView = view.findViewById(R.id.tv_passengerstats_totalRide);
        averageRideTextView = view.findViewById(R.id.tv_passengerstats_avgRide);
        totalMoneyTextView = view.findViewById(R.id.tv_passengerstats_totalMoney);
        averageMoneyTextView = view.findViewById(R.id.tv_passengerstats_avgMoney);
        moneyBarChart = view.findViewById(R.id.barChart_passengerstats_money);
        kmBarChart = view.findViewById(R.id.barChart_passengerstats_km);
        rideNumBarChart = view.findViewById(R.id.barChart_passengerstats_ride);

        view.findViewById(R.id.btn_passengerstats_generate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isCorrectDate()) return;
                initBarChart(kmBarChart);
                initBarChart(rideNumBarChart);
                initBarChart(moneyBarChart);
                generateReport();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDestroyView() {
        Toolbar tb = parentActivity.findViewById(R.id.passenger_account_toolbar);
        tb.setTitle("Account");
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        Toolbar tb = parentActivity.findViewById(R.id.passenger_account_toolbar);
        tb.setTitle("Account");
        super.onDetach();
    }



    private boolean isCorrectDate() {
        if (dateFromTextView.getText().toString().equals("") || dateToTextView.getText().toString().equals("")) {
            Snackbar.make(view, "Select date please", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return false;
        }
        return true;
    }

    private void generateReport() {
        String fromDateStr = dateFromTextView.getText().toString().substring("Selected: ".length()).trim();
        String toDateStr = dateToTextView.getText().toString().substring("Selected: ".length()).trim();
        Log.d("TAG", Retrofit.sharedPreferences.getString("user_id", null));
        getDataForNumGraph(fromDateStr, toDateStr);
        getdataForDistanceGraph(fromDateStr, toDateStr);
        getDataForMoneyGraph(fromDateStr, toDateStr);
//        fillChartWithData(kmBarChart, "Kilometers");
//        fillChartWithData(rideNumBarChart, "Number of rides");
    }

    private void getDataForMoneyGraph(String fromDateStr, String toDateStr) {
        IPassengerService PassengerService = Retrofit.retrofit.create(IPassengerService.class);
        Call<ReportDTO> reportKm = PassengerService.getMoneyReport(Integer.valueOf(Retrofit.sharedPreferences.getString("user_id", null)),
                fromDateStr, toDateStr);
        reportKm.enqueue(new Callback<ReportDTO>() {
            @Override
            public void onResponse(Call<ReportDTO> call, Response<ReportDTO> response) {
                if (response.code() != 200){
                    Snackbar.make(view, "No good" + response.code(), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    return;
                }
                ReportDTO reportDTO = response.body();
                Log.d("TAG", String.valueOf(reportDTO));
                assert reportDTO != null;
                fillChartWithData(moneyBarChart, "Money", reportDTO.getValues());
                totalMoneyTextView.setText("Total: " + reportDTO.getTotal());
                averageMoneyTextView.setText("Average: " + reportDTO.getAverage());
            }

            @Override
            public void onFailure(Call<ReportDTO> call, Throwable t) {
                Snackbar.make(view, "No good nana", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void getdataForDistanceGraph(String fromDateStr, String toDateStr) {
        IPassengerService PassengerService = Retrofit.retrofit.create(IPassengerService.class);
        Call<ReportDTO> reportKm = PassengerService.getDistanceReport(Integer.valueOf(Retrofit.sharedPreferences.getString("user_id", null)),
                fromDateStr, toDateStr);
        reportKm.enqueue(new Callback<ReportDTO>() {
            @Override
            public void onResponse(Call<ReportDTO> call, Response<ReportDTO> response) {
                if (response.code() != 200){
                    Snackbar.make(view, "No good" + response.code(), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    return;
                }
                ReportDTO reportDTO = response.body();
                Log.d("TAG", String.valueOf(reportDTO));
                assert reportDTO != null;
                fillChartWithData(kmBarChart, "Kilometers", reportDTO.getValues());
                totalKmTextView.setText("Total: " + reportDTO.getTotal());
                averageKmTextView.setText("Average: " + reportDTO.getAverage());
            }

            @Override
            public void onFailure(Call<ReportDTO> call, Throwable t) {
                Snackbar.make(view, "No good nana", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void getDataForNumGraph(String fromDateStr, String toDateStr) {
        IPassengerService PassengerService = Retrofit.retrofit.create(IPassengerService.class);
        Call<ReportDTO> report = PassengerService.getRideNumReport(Integer.valueOf(Retrofit.sharedPreferences.getString("user_id", null)),
                fromDateStr, toDateStr);
        report.enqueue(new Callback<ReportDTO>() {
            @Override
            public void onResponse(Call<ReportDTO> call, Response<ReportDTO> response) {
                if (response.code() != 200){
                    Snackbar.make(view, "No good" + response.code(), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    return;
                }
                ReportDTO reportDTO = response.body();
                Log.d("TAG", String.valueOf(reportDTO));
                assert reportDTO != null;
                fillChartWithData(rideNumBarChart, "Number of rides", reportDTO.getValues());
                totalRideTextView.setText(String.format("Total: %s", reportDTO.getTotal()));
                averageRideTextView.setText(String.format("Average: %s", reportDTO.getAverage()));
            }

            @Override
            public void onFailure(Call<ReportDTO> call, Throwable t) {
                Snackbar.make(view, "No good nana", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void fillChartWithData(BarChart barChart, String title, HashMap<String, Double> values) {
        ArrayList<Double> valueList = new ArrayList<Double>();
        ArrayList<BarEntry> entries = new ArrayList<>();
//        String title = "Title";

        //input data
//        for(int i = 0; i < 6; i++){
//            valueList.add(i * 100.1);
//        }
//
//        float date = LocalDateTime.now().getYear();
//        //fit the data into a bar
//        for (int i = 0; i < valueList.size(); i++) {
//            BarEntry barEntry = new BarEntry(i, valueList.get(i).floatValue());
//            entries.add(barEntry);
//        }
        float i = 1.1f;
        for (Map.Entry<String, Double> set: values.entrySet()){
            Log.d("TAG", set.getKey());
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-dd-MM", Locale.GERMAN);
            Date ah = null;
            Date pomoc = null;
            try {
                ah = formatter.parse(set.getKey());
                pomoc = formatter.parse("2025-30-12");
            } catch (ParseException e) {
                e.printStackTrace();
            }
            float dateForXAxis = ah.getTime();
            dateForXAxis = dateForXAxis - pomoc.getTime();
            Log.d("TAG", String.valueOf(dateForXAxis));
            BarEntry barEntry = new BarEntry(i, set.getValue().floatValue());
            i += 1;
            entries.add(barEntry);
        }

        BarDataSet barDataSet = new BarDataSet(entries, title);
        initBarDataSet(barDataSet);

        BarData data = new BarData(barDataSet);
        barChart.setData(data);
        barChart.invalidate();
    }

    private void initBarDataSet(BarDataSet barDataSet){
        //Changing the color of the bar
        barDataSet.setColor(Color.parseColor("#304567"));
        //Setting the size of the form in the legend
        barDataSet.setFormSize(15f);
        //showing the value of the bar, default true if not set
        barDataSet.setDrawValues(false);
        //setting the text size of the value of the bar
        barDataSet.setValueTextSize(12f);
    }

    private void initBarChart(BarChart barChart){
        //hiding the grey background of the chart, default false if not set
        barChart.setDrawGridBackground(false);
        //remove the bar shadow, default false if not set
        barChart.setDrawBarShadow(false);
        //remove border of the chart, default false if not set
        barChart.setDrawBorders(false);
        //remove the description label text located at the lower right corner
        Description description = new Description();
        description.setEnabled(false);
        barChart.setDescription(description);
//        barChart.setVisibleXRangeMinimum(3000000000000000f);
//        barChart.setVisibleXRangeMaximum(3f);
//        barChart.getXAxis().setLabelCount(3, /*force: */true);
//        barChart.getXAxis().mAxisMinimum = 100000000000f;
//        barChart.getXAxis().mAxisMaximum = 200000000000f;

        //setting animation for y-axis, the bar will pop up from 0 to its value within the time we set
        barChart.animateY(1000);
        //setting animation for x-axis, the bar will pop up separately within the time we set
        barChart.animateX(1000);
//        barChart.getXAxis().setLabelCount(5);

        XAxis xAxis = barChart.getXAxis();
        //change the position of x-axis to the bottom
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //set the horizontal distance of the grid line
//        xAxis.setGranularity(1f);
        //hiding the x-axis line, default true if not set
        xAxis.setDrawAxisLine(false);
        //hiding the vertical grid lines, default true if not set
        xAxis.setDrawGridLines(false);
//        xAxis.setTypeface(tfLight);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        //xAxis.setLabelCount(4, true);
//        xAxis.setAxisMinimum(29000000000f);
//        xAxis.setAxisMaximum(30000000000f);
//        xAxis.setGranularityEnabled(true);
////        xAxis.setAxisMinimum(24 * 60 * 60 * 1000f);
//        xAxis.setGranularity( 24 * 60 * 60f);


//        xAxis.setGranularityEnabled(false);
//        xAxis.setValueFormatter(new ChartXAxisValueFormatter());

        YAxis leftAxis = barChart.getAxisLeft();
        //hiding the left y-axis line, default true if not set
        leftAxis.setDrawAxisLine(false);

        YAxis rightAxis = barChart.getAxisRight();
        //hiding the right y-axis line, default true if not set
        rightAxis.setDrawAxisLine(false);

        Legend legend = barChart.getLegend();
        //setting the shape of the legend form to line, default square shape
        legend.setForm(Legend.LegendForm.LINE);
        //setting the text size of the legend
        legend.setTextSize(11f);
        //setting the alignment of legend toward the chart
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        //setting the stacking direction of legend
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        //setting the location of legend outside the chart, default false if not set
        legend.setDrawInside(false);

    }

}