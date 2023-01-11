package com.example.myapplication.activities;

import static ua.naiksoftware.stomp.dto.LifecycleEvent.Type.OPENED;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.myapplication.Constants;
import com.example.myapplication.R;
import com.example.myapplication.dto.ErrorMessage;
import com.example.myapplication.dto.LoginDTO;
import com.example.myapplication.dto.RideDTO;
import com.example.myapplication.dto.TokenResponseDTO;
import com.example.myapplication.services.AuthService;
import com.example.myapplication.services.IRideService;
import com.example.myapplication.services.MapService;
import com.example.myapplication.tools.Retrofit;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.EncodedPolyline;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Action;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.StompMessage;

public class DriverMainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private AuthService authService;
    private MapService mapService;
    private GoogleMap mMap;
    private List<LatLng> path = new ArrayList();
    private Marker simMarker;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_main);

        authService = new AuthService(this);
        mapService = new MapService();

        Toolbar toolbar = findViewById(R.id.driver_main_toolbar);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Uber");
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.driver_map);
        mapFragment.getMapAsync(this);

        Switch toggle = findViewById(R.id.driver_main_toggle);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                TextView t = findViewById(R.id.driver_main_label_active);
                if (isChecked) {
                    t.setText("ACTIVE");
                } else {
                    t.setText("NOT ACTIVE");
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.driver_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.driver_menu_history) {
            Intent intent1 = new Intent(this, DriverRideHistoryActivity.class);
            this.startActivity(intent1);
            return true;
        }
        if (id == R.id.driver_menu_account) {
            Intent intent1 = new Intent(this, DriverAccountActivity.class);
            this.startActivity(intent1);
            return true;
        }
        if (id == R.id.driver_menu_inbox) {
            Intent intent1 = new Intent(this, DriverInboxActivity.class);
            this.startActivity(intent1);
            return true;
        }
        if (id == R.id.driver_logout_account) {
            authService.logout();
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

//        String driverId = Retrofit.sharedPreferences.getString("user_id", null);
//
//        IRideService rideService = Retrofit.retrofit.create(IRideService.class);
//        Call<RideDTO> activeRideResponseCall = rideService.getDriverActiveRide(Integer.parseInt(driverId));
//
//        activeRideResponseCall.enqueue(new Callback<RideDTO>() {
//            @Override
//            public void onResponse(Call<RideDTO> call, Response<RideDTO> response) {
//                if (response.code() != 200)
//                    return;
//
//                RideDTO rideDTO = response.body();
//                Log.d("TAG", rideDTO.toString());
//
//                String departureAddress = rideDTO.getLocations().get(0).getDeparture().getAddress();
//                double departureLat = rideDTO.getLocations().get(0).getDeparture().getLatitude();
//                double departureLon = rideDTO.getLocations().get(0).getDeparture().getLongitude();
//                String destinationAddress = rideDTO.getLocations().get(0).getDestination().getAddress();
//                double destinationLat = rideDTO.getLocations().get(0).getDestination().getLatitude();
//                double destinationLon = rideDTO.getLocations().get(0).getDestination().getLongitude();
//
//                LatLng departure = new LatLng(departureLat, departureLon);
//                mMap.addMarker(new MarkerOptions()
//                        .position(departure)
//                        .title(departureAddress));
//                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(departure, 14));
//
//                LatLng destination = new LatLng(destinationLat, destinationLon);
//                mMap.addMarker(new MarkerOptions()
//                        .position(destination)
//                        .title(destinationAddress));
//
//                String origin = "" + departureLat + "," + departureLon;
//                String end = "" + destinationLat + "," + destinationLon;
//
//                path = mapService.getPath(origin, end);
//
//                //Draw the polyline
//                if (path.size() > 0) {
//                    Log.d("TAG", "duzina" + path.size());
//                    PolylineOptions opts = new PolylineOptions().addAll(path).color(Color.BLUE).width(5);
//                    mMap.addPolyline(opts);
//                }
//
//                simMarker = mMap.addMarker(new MarkerOptions().position(path.get(0)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
//
//                startSimulation();
//            }
//
//            @Override
//            public void onFailure(Call<RideDTO> call, Throwable t) {
//                Log.d("TAG", "greska", t);
//            }
//        });

//        StompClient stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, Constants.websocketBaseUrl);
//        stompClient.connect();



//        stompClient.topic("/vehicle-location").subscribe(topicMessage -> {
//            Log.d("TAG", topicMessage.getPayload());
//        });

        Retrofit.stompClient.send("/vehicle-location", "data").subscribe(new Action() {
            @Override
            public void run() throws Exception {
                Log.d("TAG", "poslato iz koda");
            }
        });

    }

    private void showMarker(Integer i) {

        if (i > path.size() - 1)
            return;

        new android.os.Handler(Looper.getMainLooper()).postDelayed(
                new Runnable() {
                    public void run() {
                        moveMarker(path.get(i));

                        if (i > path.size() - 1 - 5 && i < path.size() - 1)
                            showMarker(path.size() - 1);
                        else
                            showMarker(i + 5);

                        Log.d("TAG", i + " od " + path.size());
                    }
                },
                2000);
    }

    private void startSimulation() {
        showMarker(0);
    }

    private void moveMarker(LatLng loc) {
        simMarker.remove();
        simMarker = mMap.addMarker(new MarkerOptions().position(loc).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
    }
}
