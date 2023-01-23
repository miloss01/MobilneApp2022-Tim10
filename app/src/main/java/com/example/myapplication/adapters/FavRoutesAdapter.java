package com.example.myapplication.adapters;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.myapplication.R;
import com.example.myapplication.activities.PassengerAccountActivity;
import com.example.myapplication.activities.PassengerInboxActivity;
import com.example.myapplication.activities.PassengerMainActivity;
import com.example.myapplication.dto.FavoriteLocationDTO;
import com.example.myapplication.dto.FavoriteLocationResponseDTO;
import com.example.myapplication.fragments.FavoriteRoutesFragment;
import com.example.myapplication.models.Ride;
import com.example.myapplication.services.IRideService;
import com.example.myapplication.tools.Retrofit;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavRoutesAdapter extends ArrayAdapter<FavoriteLocationResponseDTO> {

    private ArrayList<FavoriteLocationResponseDTO> locations;
    private Context context;

    public FavRoutesAdapter(Context context, int resource, ArrayList<FavoriteLocationResponseDTO> locationDTOS) {
        super(context, resource, locationDTOS);
        this.context = context;
        this.locations = locationDTOS;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        FavoriteLocationResponseDTO location = locations.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.fav_route_cell,
                    parent, false);
        }

        TextView tvName = (TextView) convertView.findViewById(R.id.textview_favroutecell_name);
        tvName.setText(location.getFavoriteName());

        TextView tvDeparture = (TextView) convertView.findViewById(R.id.textview_favroutecell_departure);
        tvDeparture.setText(location.getLocations().get(0).getDeparture().getAddress());

        TextView tvDestination = (TextView) convertView.findViewById(R.id.textview_favroutecell_destination);
        tvDestination.setText(location.getLocations().get(0).getDestination().getAddress());

        IRideService rideService = Retrofit.retrofit.create(IRideService.class);

        Button rideAgainBtn = convertView.findViewById(R.id.btn_favroutecell_rideAgain);
        rideAgainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, PassengerMainActivity.class);
                i.putExtra("DESTINATION", location.getLocations().get(0).getDestination().getAddress());
                i.putExtra("DEPARTURE", location.getLocations().get(0).getDeparture().getAddress());
                startActivity(context, i, null);
            }
        });

        Button deleteBtn = convertView.findViewById(R.id.btn_favroutecell_delete);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<String> fav = rideService.deleteFavoriteLocation(location.getId().intValue());
                fav.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        try {
                            if (response.code() == 204) Toast.makeText(context, "Favorite location deleted!", Toast.LENGTH_LONG).show();
                        } catch (Exception ex) {
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });
            }
        });

        return convertView;
    }
}
