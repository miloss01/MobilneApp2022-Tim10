package com.example.myapplication.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.activities.DriverInboxActivity;
import com.example.myapplication.activities.PassengerMainActivity;
import com.example.myapplication.adapters.RidePassengersAdapter;
import com.example.myapplication.dialogs.AddFavoriteDialog;
import com.example.myapplication.dialogs.MakeReviewDialog;
import com.example.myapplication.dto.DriverDTO;
import com.example.myapplication.dto.DriverReviewResponseDTO;
import com.example.myapplication.dto.PassengerDTO;
import com.example.myapplication.dto.RideDTO;
import com.example.myapplication.dto.RideReviewDTO;
import com.example.myapplication.dto.UserDTO;
import com.example.myapplication.dto.VehicleReviewResponseDTO;
import com.example.myapplication.services.IDriverService;
import com.example.myapplication.services.IPassengerService;
import com.example.myapplication.services.IReviewService;
import com.example.myapplication.services.MapService;
import com.example.myapplication.tools.Retrofit;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RideDetailsFragment extends Fragment implements OnMapReadyCallback  {

    private static RideDTO ride;
    private GoogleMap mMap;
    private MapService mapService;
    private List<LatLng> path = new ArrayList();
    private ListView listView;


    public RideDetailsFragment() {
    }

    public static Object newInstance(RideDTO ride) {
        RideDetailsFragment.ride = ride;
        return new RideDetailsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mapService = new MapService();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.passenger_ride_details_map);
        mapFragment.getMapAsync(this);
        displayAccounts();
        setUpReviewButton();
        setUpFavoriteButton();
        setUpRideAgainButton();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ride_details, container, false);

        TextView tvStartTime = (TextView) view.findViewById(R.id.textview_ridedetails_starttime);
        tvStartTime.setText(ride.getStartTime());
        TextView tvEndTime = (TextView) view.findViewById(R.id.textview_ridedetails_endtime);
        tvEndTime.setText(ride.getStartTime());

        TextView tvPrice = (TextView) view.findViewById(R.id.textview_ridedetails_price);
        String price = String.valueOf(ride.getTotalCost()) + " RSD";
        tvPrice.setText(price);

        TextView tvDeparture = (TextView) view.findViewById(R.id.textview_ridedetails_departure);
        tvDeparture.setText(ride.getLocations().get(0).getDeparture().getAddress());

        TextView tvDestination = (TextView) view.findViewById(R.id.textview_ridedetails_destination);
        tvDestination.setText(ride.getLocations().get(0).getDestination().getAddress());

        TextView tvDistance = (TextView) view.findViewById(R.id.textview_ridedetails_distance);
        tvDistance.setText(ride.getDistance().toString() + " km");

        displayReviews(view);

        return view;
    }

    private void displayAccounts() {
        IPassengerService passengerService = Retrofit.retrofit.create(IPassengerService.class);
        IDriverService driverService = Retrofit.retrofit.create(IDriverService.class);
        listView = (ListView) getView().findViewById(R.id.ridedetails_passengersList);
        List<PassengerDTO> passengers = new ArrayList<>();

        for (UserDTO userDTO : ride.getPassengers()) {
            Log.d("DEBUG", "DTO passenger: " + userDTO.getEmail());
            Call<PassengerDTO> passengerResponseCall = passengerService.getPassenger(userDTO.getId().intValue());
            passengerResponseCall.enqueue(new Callback<PassengerDTO>() {
                @Override
                public void onResponse(Call<PassengerDTO> call, Response<PassengerDTO> response) {

                    PassengerDTO passengerDTO = response.body();
                    passengers.add(passengerDTO);

                    RidePassengersAdapter adapter = new RidePassengersAdapter(
                            getActivity(),
                            R.layout.ridedetails_passenger_cell,
                            passengers);
                    adapter.forHistoryView = true;
                    listView.setAdapter(adapter);
                }

                @Override
                public void onFailure(Call<PassengerDTO> call, Throwable t) {
                    Log.d("TAG", "greska", t);
                }
            });
        }

        Call<DriverDTO> getDriverResponse = driverService.getDriver(ride.getDriver().getId().intValue());

        getDriverResponse.enqueue(new Callback<DriverDTO>() {
            @Override
            public void onResponse(Call<DriverDTO> call, Response<DriverDTO> response) {

                if (response.code() != 200)
                    return;

                DriverDTO driverDTO = response.body();

                String ret = driverDTO.getName() + " " + driverDTO.getSurname();
                TextView driverName = getView().findViewById(R.id.textview_ridedetails_drivername);
                driverName.setText(ret);

                ImageView imageView = (ImageView) getView().findViewById(R.id.img_ridedetails_driverphoto);
                if (driverDTO.getProfilePicture() != null) {
                    if (!driverDTO.getProfilePicture().startsWith("data")) new RidePassengersAdapter.DownloadImageTask(imageView).execute(driverDTO.getProfilePicture());
                    else {
                        final String encodedString = driverDTO.getProfilePicture();
                        final String pureBase64Encoded = encodedString.substring(encodedString.indexOf(",")  + 1);
                        final byte[] decodedBytes = Base64.decode(pureBase64Encoded, Base64.DEFAULT);
                        Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
                        imageView.setImageBitmap(decodedBitmap);
                    }

                }
                Button messageBtn = getView().findViewById(R.id.btn_ridedetails_contactdriver);
                messageBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(getActivity(), DriverInboxActivity.class));
                    }
                });

            }

            @Override
            public void onFailure(Call<DriverDTO> call, Throwable t) {
                Log.d("TAG", "greska u povlacenju drivera", t);
            }
        });

    }

    private void displayReviews(View v) {
        IReviewService reviewService = Retrofit.retrofit.create(IReviewService.class);
        Call<List<RideReviewDTO>> rideReviewsCall = reviewService.getRideReview(ride.getId().intValue());

        rideReviewsCall.enqueue(new Callback<List<RideReviewDTO>>() {
            @Override
            public void onResponse(Call<List<RideReviewDTO>> call, Response<List<RideReviewDTO>> response) {

                List<RideReviewDTO> rideReviewDTOList = response.body();
                Log.d("TAG", rideReviewDTOList.toString());

                LinearLayout mainLinear = (LinearLayout) v.findViewById(R.id.ridedetails_reviewsLayout);

                for (RideReviewDTO rideReviewDTO : rideReviewDTOList) {

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.setMargins(0, 40, 0, 0);
                    LinearLayout reviewLinear = new LinearLayout(getContext());
                    reviewLinear.setLayoutParams(params);
                    reviewLinear.setOrientation(LinearLayout.VERTICAL);

                    TextView passengerEmail = new TextView(getContext());
                    passengerEmail.setTypeface(Typeface.DEFAULT_BOLD);
                    reviewLinear.addView(passengerEmail);

                    VehicleReviewResponseDTO vehicleReview = rideReviewDTO.getVehicleReview();
                    if (vehicleReview != null) {
                        TextView vehicleReviewText = new TextView(getContext());
                        vehicleReviewText.setText("Vehicle review: " + vehicleReview.getComment() + " - rating: " + vehicleReview.getRating() + " ⭐");
                        passengerEmail.setText(vehicleReview.getPassenger().getEmail());
                        reviewLinear.addView(vehicleReviewText);
                    }

                    DriverReviewResponseDTO driverReview = rideReviewDTO.getDriverReview();
                    if (driverReview != null) {
                        TextView driverReviewText = new TextView(getContext());
                        driverReviewText.setText("Driver review: " + driverReview.getComment() + " - rating: " + driverReview.getRating() + " ⭐");
                        passengerEmail.setText(driverReview.getPassenger().getEmail());
                        reviewLinear.addView(driverReviewText);
                    }

                    mainLinear.addView(reviewLinear);
                }
            }

            @Override
            public void onFailure(Call<List<RideReviewDTO>> call, Throwable t) {
                Log.d("TAG", "greska u povlacenju reviewova o voznji", t);
            }
        });
    }

    private void setUpReviewButton() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
        Button btn = getView().findViewById(R.id.btn_ridedetails_rating);
        if (LocalDateTime.now().isAfter( LocalDateTime.parse(ride.getEndTime(), formatter).plusDays(3) ) ) {
            btn.setVisibility(View.INVISIBLE);
            return;
        }
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment makeReviewDialog = MakeReviewDialog.newInstance(ride.getId().intValue());
                makeReviewDialog.show(getParentFragmentManager(), "make_review_dialog");
            }
        });
    }

    private void setUpFavoriteButton() {
        Button btn = getView().findViewById(R.id.btn_ridedetails_addfav);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment addFavoriteDialog = AddFavoriteDialog.newInstance(ride, getActivity());
                addFavoriteDialog.show(getParentFragmentManager(), "dialog_add_favorite");
            }
        });
    }

    private void setUpRideAgainButton() {
        Button btn = getView().findViewById(R.id.btn_ridedetails_startnew);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), PassengerMainActivity.class);
                i.putExtra("DESTINATION", ride.getLocations().get(0).getDestination().getAddress());
                i.putExtra("DEPARTURE", ride.getLocations().get(0).getDeparture().getAddress());
                startActivity(i);
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        double departureLat = ride.getLocations().get(0).getDeparture().getLatitude();
        double departureLon = ride.getLocations().get(0).getDeparture().getLongitude();
        double destinationLat = ride.getLocations().get(0).getDestination().getLatitude();
        double destinationLon = ride.getLocations().get(0).getDestination().getLongitude();
        LatLng departure = new LatLng(departureLat, departureLon);
        mMap.addMarker(new MarkerOptions()
                .position(departure)
                .title("Departure: " + ride.getLocations().get(0).getDeparture().getAddress()));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(departure, 14));
        Log.d("TAG", "Departure: " + departure.toString());

        LatLng destination = new LatLng(destinationLat, destinationLon);
        mMap.addMarker(new MarkerOptions()
                .position(destination)
                .title("Destination: " + ride.getLocations().get(0).getDestination().getAddress()));
        Log.d("TAG", "Destination: " + destination.toString());

        String origin = "" + departureLat + "," + departureLon;
        String end = "" + destinationLat + "," + destinationLon;

        path = mapService.getPath(origin, end);

        //Draw the polyline
        if (path.size() > 0) {
            PolylineOptions opts = new PolylineOptions().addAll(path).color(Color.BLUE).width(5);
            mMap.addPolyline(opts);
        }
    }

}