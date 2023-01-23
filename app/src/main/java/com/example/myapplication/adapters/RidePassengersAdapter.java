package com.example.myapplication.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.dto.PassengerDTO;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class RidePassengersAdapter extends ArrayAdapter<PassengerDTO> {

    ArrayList<PassengerDTO> passengersList;
    public boolean forHistoryView = false;

    public RidePassengersAdapter(Context context, int resource, List<PassengerDTO> passengerDTOS) {
        super(context, resource, passengerDTOS);
        this.passengersList = (ArrayList<PassengerDTO>) passengerDTOS;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        PassengerDTO passengerDTO = passengersList.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.ridedetails_passenger_cell,
                    parent, false);
        }

        ImageView imageView = (ImageView) convertView.findViewById(R.id.driver_active_ride_passengerImage);
        if (passengerDTO.getProfilePicture() != null) {
            if (!passengerDTO.getProfilePicture().startsWith("data")) new RidePassengersAdapter.DownloadImageTask(imageView).execute(passengerDTO.getProfilePicture());
            else {
                final String encodedString = passengerDTO.getProfilePicture();
                final String pureBase64Encoded = encodedString.substring(encodedString.indexOf(",")  + 1);
                final byte[] decodedBytes = Base64.decode(pureBase64Encoded, Base64.DEFAULT);
                Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
                imageView.setImageBitmap(decodedBitmap);
            }
        }
        TextView name = (TextView) convertView.findViewById(R.id.driver_active_ride_passengerName);
        name.setText(passengerDTO.getName() + " " + passengerDTO.getSurname());

        Button callBtn = convertView.findViewById(R.id.btn_driver_active_ride_passengerCall);
        callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + passengerDTO.getTelephoneNumber()));
                getContext().startActivity(intent);
            }
        });
        if (forHistoryView) callBtn.setVisibility(View.INVISIBLE);
        return convertView;
    }

    @Override
    public int getCount() {
        return passengersList.size();
    }


    public static class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
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
