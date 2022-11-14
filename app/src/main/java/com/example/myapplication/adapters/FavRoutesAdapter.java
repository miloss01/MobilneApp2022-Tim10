package com.example.myapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.myapplication.R;
import com.example.myapplication.models.Ride;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class FavRoutesAdapter extends ArrayAdapter<String> {

    private ArrayList<String> locations = new ArrayList<String>();

    public FavRoutesAdapter(Context context, int resource) {
        super(context, resource);
        this.locations.add("Fifth Avenue 123, NY, NY");
        this.locations.add("Temerinski put 123, Novi Sad");
        this.locations.add("St James Street 25, NY, NY");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        String location = locations.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.fav_route_cell,
                    parent, false);
        }

        TextView tvName = (TextView) convertView.findViewById(R.id.textview_favroutecell_name);
        tvName.setText(location);

        return convertView;
    }
}
