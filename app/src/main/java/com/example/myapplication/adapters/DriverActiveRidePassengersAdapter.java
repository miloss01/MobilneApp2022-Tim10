package com.example.myapplication.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.dto.PassengerDTO;

import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class DriverActiveRidePassengersAdapter extends ArrayAdapter<PassengerDTO> {

    ArrayList<PassengerDTO> passengersList;

    public DriverActiveRidePassengersAdapter(Context context, int resource, List<PassengerDTO> passengerDTOS) {
        super(context, resource, passengerDTOS);
        this.passengersList = (ArrayList<PassengerDTO>) passengerDTOS;
        Log.i("TAG", "tu smo u konstruktoru Adaptera");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        PassengerDTO passengerDTO = passengersList.get(position);
        Log.i("TAG", "tu smo u getView");

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.driver_active_ride_passenger_cell,
                    parent, false);
        }

        ImageView imageView = (ImageView) convertView.findViewById(R.id.driver_active_ride_passengerImage);
        new DownloadImageTask(imageView).execute(passengerDTO.getProfilePicture());

        TextView name = (TextView) convertView.findViewById(R.id.driver_active_ride_passengerName);
        name.setText(passengerDTO.getName() + " " + passengerDTO.getSurname());

        return convertView;
    }

    @Override
    public int getCount() {
        return passengersList.size();
    }


    private static class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

}
