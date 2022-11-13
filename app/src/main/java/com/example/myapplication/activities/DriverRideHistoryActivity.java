package com.example.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.myapplication.R;
import com.example.myapplication.adapters.RideAdapter;

public class DriverRideHistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ride_history);

        RideAdapter adapter = new RideAdapter(this);
        ListView list = findViewById(R.id.driver_ride_history_list);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Intent intent = new Intent(DriverRideHistoryActivity.this, RideDetailsActivity.class);
//                startActivity(intent);
            }
        });

        Toolbar toolbar = findViewById(R.id.driver_ride_history_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Driver ride history");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }



//    @Override
//    public void onListItemClick(ListView l, View v, int position, long id) {
//        super.onListItemClick(l, v, position, id);
//        Snackbar.make(l, "Tralal", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show();
////        Cinema cinema = Mokap.getCinemas().get(position);
////
////        /*
////         * Ako nasoj aktivnosti zelimo da posaljemo nekakve podatke
////         * za to ne treba da koristimo konstruktor. Treba da iskoristimo
////         * identicnu strategiju koju smo koristili kda smo radili sa
////         * fragmentima! Koristicemo Bundle za slanje podataka. Tacnije
////         * intent ce formirati Bundle za nas, ali mi treba da pozovemo
////         * odgovarajucu putExtra metodu.
////         * */
////        Intent intent = new Intent(getActivity(), DetailActivity.class);
////        intent.putExtra("name", cinema.getName());
////        intent.putExtra("descr", cinema.getDescription());
////        startActivity(intent);
//    }

}
