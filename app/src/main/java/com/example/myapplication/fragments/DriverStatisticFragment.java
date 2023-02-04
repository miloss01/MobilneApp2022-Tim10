package com.example.myapplication.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.dto.StatisticsDTO;
import com.example.myapplication.services.IDriverService;
import com.example.myapplication.services.IRideService;
import com.example.myapplication.tools.Retrofit;
import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DriverStatisticFragment extends Fragment {
    private View view;
    private TextView acceptanceRateTextView;
    private TextView workingHoursTextView;
    private TextView incomeTextView;
    private TextView kmTextView;


    public DriverStatisticFragment() {
        // Required empty public constructor
    }


    public static DriverStatisticFragment newInstance() {
        DriverStatisticFragment fragment = new DriverStatisticFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_driver_statistic, container, false);
        incomeTextView = view.findViewById(R.id.incoome);
        workingHoursTextView = view.findViewById(R.id.working_h);
        acceptanceRateTextView = view.findViewById(R.id.acceptance);
        kmTextView = view.findViewById(R.id.kilometers_stat);
        view.findViewById(R.id.radio_day_stat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatedayStatistics();
            }
        });
        view.findViewById(R.id.radio_month_stat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateMonthStatistics();
            }
        });
        view.findViewById(R.id.radio_year_stat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateYearStatistics();
            }
        });
        return view;
    }

    private void updateYearStatistics() {
        IDriverService driverService = Retrofit.retrofit.create(IDriverService.class);
        Call<StatisticsDTO> call = driverService.getYearStatistics(Integer.valueOf(Retrofit.sharedPreferences.getString("user_id", null)));
        call.enqueue(new Callback<StatisticsDTO>() {
            @Override
            public void onResponse(Call<StatisticsDTO> call, Response<StatisticsDTO> response) {
                if (response.code() != 200){
                    Snackbar.make(view, "No good" + response.code(), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    return;
                }
                StatisticsDTO statisticsDTO = response.body();
                acceptanceRateTextView.setText("Average aceptance: " + statisticsDTO.getAcceptanceRate() + "%");
                workingHoursTextView.setText("Average working hours: " + statisticsDTO.getWorkingHours()/60);
                kmTextView.setText("Average kilometers driven: " + statisticsDTO.getKilometers());
                incomeTextView.setText("Average income: " + statisticsDTO.getIncome() + "din");

            }

            @Override
            public void onFailure(Call<StatisticsDTO> call, Throwable t) {
                Snackbar.make(view, "No good nana", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        });
    }

    private void updateMonthStatistics() {
        IDriverService driverService = Retrofit.retrofit.create(IDriverService.class);
        Call<StatisticsDTO> call = driverService.getMonthStatistics(Integer.valueOf(Retrofit.sharedPreferences.getString("user_id", null)));
        call.enqueue(new Callback<StatisticsDTO>() {
            @Override
            public void onResponse(Call<StatisticsDTO> call, Response<StatisticsDTO> response) {
                if (response.code() != 200){
                    Snackbar.make(view, "No good" + response.code(), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    return;
                }
                StatisticsDTO statisticsDTO = response.body();
                acceptanceRateTextView.setText("Average aceptance: " + statisticsDTO.getAcceptanceRate() + "%");
                workingHoursTextView.setText("Average working hours: " + statisticsDTO.getWorkingHours()/60);
                kmTextView.setText("Average kilometers driven: " + statisticsDTO.getKilometers());
                incomeTextView.setText("Average income: " + statisticsDTO.getIncome() + "din");

            }

            @Override
            public void onFailure(Call<StatisticsDTO> call, Throwable t) {
                Snackbar.make(view, "No good nana", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        });
    }

    private void updatedayStatistics() {
        IDriverService driverService = Retrofit.retrofit.create(IDriverService.class);
        Call<StatisticsDTO> call = driverService.getDayStatistics(Integer.valueOf(Retrofit.sharedPreferences.getString("user_id", null)));
        call.enqueue(new Callback<StatisticsDTO>() {
            @Override
            public void onResponse(Call<StatisticsDTO> call, Response<StatisticsDTO> response) {
                if (response.code() != 200){
                    Snackbar.make(view, "No good" + response.code(), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    return;
                }
                StatisticsDTO statisticsDTO = response.body();
                acceptanceRateTextView.setText("Average aceptance: " + statisticsDTO.getAcceptanceRate() + "%");
                workingHoursTextView.setText("Average working hours: " + statisticsDTO.getWorkingHours()/60);
                kmTextView.setText("Average kilometers driven: " + statisticsDTO.getKilometers());
                incomeTextView.setText("Average income: " + statisticsDTO.getIncome() + "din");

            }

            @Override
            public void onFailure(Call<StatisticsDTO> call, Throwable t) {
                Snackbar.make(view, "No good nana", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        });
    }
}