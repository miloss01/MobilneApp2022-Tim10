package com.example.myapplication.activities;

import static ua.naiksoftware.stomp.dto.LifecycleEvent.Type.OPENED;

import android.app.Activity;
import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RemoteViews;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.RemoteInput;

import com.example.myapplication.Constants;
import com.example.myapplication.R;
import com.example.myapplication.dto.DepartureDestinationLocationsDTO;
import com.example.myapplication.dto.ErrorMessage;
import com.example.myapplication.dto.LocationDTO;
import com.example.myapplication.dto.LoginDTO;
import com.example.myapplication.dto.NotificationDTO;
import com.example.myapplication.dto.RideDTO;
import com.example.myapplication.dto.TokenResponseDTO;
import com.example.myapplication.dto.VehicleDTO;
import com.example.myapplication.fragments.DriverAcceptedRideFragment;
import com.example.myapplication.fragments.DriverActiveRideFragment;
import com.example.myapplication.fragments.DriverNoRideFragment;
import com.example.myapplication.dto.UserDTO;
import com.example.myapplication.models.User;
import com.example.myapplication.receiver.AcceptRideNotificationReceiver;
import com.example.myapplication.services.AuthService;
import com.example.myapplication.services.IDriverService;
import com.example.myapplication.services.IRideService;
import com.example.myapplication.services.MapService;
import com.example.myapplication.tools.FragmentTransition;
import com.example.myapplication.tools.Retrofit;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private LocationDTO currentLocation;

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

//        Switch toggle = findViewById(R.id.driver_main_toggle);
//        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                TextView t = findViewById(R.id.driver_main_label_active);
//                if (isChecked) {
//                    t.setText("ACTIVE");
//                } else {
//                    t.setText("NOT ACTIVE");
//                }
//            }
//        });

        subscribeToAcceptRide();

        // ovaj deo je samo za testiranje, notifikacija na ovaj kanal se salje kad vozac
        // dobije voznju da prihvati ili odbije (verovatno ce se dobiti sa beka pri zakazivanju)
        RideDTO rideDTO = new RideDTO();
        rideDTO.setId(1L);
        DepartureDestinationLocationsDTO departureDestinationLocationsDTO = new DepartureDestinationLocationsDTO(new LocationDTO("adresa1", 0, 0), new LocationDTO("adresa2", 0, 0));
        ArrayList<DepartureDestinationLocationsDTO> locations = new ArrayList<>();
        locations.add(departureDestinationLocationsDTO);
        rideDTO.setLocations(locations);
        rideDTO.setEstimatedTimeInMinutes(25);
        rideDTO.setTotalCost(250);
        ArrayList<UserDTO> passengers = new ArrayList<>();
        passengers.add(new UserDTO(2L, "asd@asd.com"));
        passengers.add(new UserDTO(3L, "Asd2asd.com"));
        rideDTO.setPassengers(passengers);
        rideDTO.setDriver(new UserDTO(2L, "dr@asd.com"));
        ObjectMapper objectMapper = new ObjectMapper();
        String json = "asd";
        try {
            json = objectMapper.writeValueAsString(rideDTO);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        String driverId = Retrofit.sharedPreferences.getString("user_id", null);
        Retrofit.stompClient.send("/ride-notification-driver-request-mob/" + driverId, json).subscribe();
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

        showNoRide();
        showActiveRide();

//        Retrofit.stompClient.send("/vehicle-location", "data").subscribe(new Action() {
//            @Override
//            public void run() throws Exception {
//                Log.d("TAG", "poslato iz koda");
//            }
//        });

    }

    private void showMarker(Integer i) {

        if (i > path.size() - 1) {
            return;
        }

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

    private void showActiveRide() {

        String driverId = Retrofit.sharedPreferences.getString("user_id", null);

        IRideService rideService = Retrofit.retrofit.create(IRideService.class);
        Call<RideDTO> activeRideResponseCall = rideService.getDriverActiveRide(Integer.parseInt(driverId));

        activeRideResponseCall.enqueue(new Callback<RideDTO>() {
            @Override
            public void onResponse(Call<RideDTO> call, Response<RideDTO> response) {
                if (response.code() != 200) {
                    Log.d("TAG", "No active ride from != 200 : ");
                    showAcceptedRide();
                    return;
                }

                RideDTO rideDTO = response.body();
                Log.d("TAG", "Active ride: " + rideDTO.toString());
                transitionToActiveRideFragment(rideDTO);

                String departureAddress = rideDTO.getLocations().get(0).getDeparture().getAddress();
                double departureLat = rideDTO.getLocations().get(0).getDeparture().getLatitude();
                double departureLon = rideDTO.getLocations().get(0).getDeparture().getLongitude();
                String destinationAddress = rideDTO.getLocations().get(0).getDestination().getAddress();
                double destinationLat = rideDTO.getLocations().get(0).getDestination().getLatitude();
                double destinationLon = rideDTO.getLocations().get(0).getDestination().getLongitude();

                mMap.clear();
                LatLng departure = new LatLng(departureLat, departureLon);
                mMap.addMarker(new MarkerOptions()
                        .position(departure)
                        .title("Departure: " +departureAddress));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(departure, 14));

                LatLng destination = new LatLng(destinationLat, destinationLon);
                mMap.addMarker(new MarkerOptions()
                        .position(destination)
                        .title("Destination: " +destinationAddress));

                String origin = "" + departureLat + "," + departureLon;
                String end = "" + destinationLat + "," + destinationLon;

                path = mapService.getPath(origin, end);

                //Draw the polyline
                if (path.size() > 0) {
                    Log.d("TAG", "duzina" + path.size());
                    PolylineOptions opts = new PolylineOptions().addAll(path).color(Color.BLUE).width(5);
                    mMap.addPolyline(opts);
                }

                simMarker = mMap.addMarker(new MarkerOptions().position(path.get(0)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

                startSimulation();
            }

            @Override
            public void onFailure(Call<RideDTO> call, Throwable t) {
                Log.d("TAG", "greska", t);
                Log.d("TAG", "No active ride from onFailure :");
                showAcceptedRide();
            }
        });

        StompClient stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, Constants.websocketBaseUrl);
        stompClient.connect();


        stompClient.topic("/vehicle-location").subscribe(topicMessage -> {
            Log.d("TAG", topicMessage.getPayload());

        });
    }

    private void showAcceptedRide() {

        String driverId = Retrofit.sharedPreferences.getString("user_id", null);

        IRideService rideService = Retrofit.retrofit.create(IRideService.class);
        Call<RideDTO> activeRideResponseCall = rideService.getDriverAcceptedRide(Integer.parseInt(driverId));

        activeRideResponseCall.enqueue(new Callback<RideDTO>() {
            @Override
            public void onResponse(Call<RideDTO> call, Response<RideDTO> response) {
                if (response.code() != 200) {
                    Log.d("TAG", "No accepted ride");
                    return;
                }

                RideDTO rideDTO = response.body();
                Log.d("TAG", "Accepted ride: " + rideDTO.toString());
                transitionToAcceptedRideFragment(rideDTO);

                String departureAddress = rideDTO.getLocations().get(0).getDeparture().getAddress();
                double departureLat = rideDTO.getLocations().get(0).getDeparture().getLatitude();
                double departureLon = rideDTO.getLocations().get(0).getDeparture().getLongitude();
                String destinationAddress = rideDTO.getLocations().get(0).getDestination().getAddress();
                double destinationLat = rideDTO.getLocations().get(0).getDestination().getLatitude();
                double destinationLon = rideDTO.getLocations().get(0).getDestination().getLongitude();

                LatLng departure = new LatLng(departureLat, departureLon);
                mMap.addMarker(new MarkerOptions()
                        .position(departure)
                        .title("Departure: " + departureAddress));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(departure, 14));
                Log.d("TAG", "Departure: " + departure.toString());

                LatLng destination = new LatLng(destinationLat, destinationLon);
                mMap.addMarker(new MarkerOptions()
                        .position(destination)
                        .title("Destination: " + destinationAddress));
                Log.d("TAG", "Destination: " + destination.toString());

                String origin = "" + currentLocation.getLatitude() + "," + currentLocation.getLongitude();
                String end = "" + departureLat + "," + departureLon;

                path = mapService.getPath(origin, end);

                //Draw the polyline
                if (path.size() > 0) {
                    Log.d("TAG", "duzina" + path.size());
                    PolylineOptions opts = new PolylineOptions().addAll(path).color(Color.BLUE).width(5);
                    mMap.addPolyline(opts);
                }

                simMarker = mMap.addMarker(new MarkerOptions().position(path.get(0)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

                startSimulation();

                NotificationDTO notificationDTO = new NotificationDTO("Driver arrived at departure location.", rideDTO.getId().intValue(), "DRIVER_ARRIVED");
                ObjectMapper objectMapper = new ObjectMapper();
                String json = "asd";
                try {
                    json = objectMapper.writeValueAsString(notificationDTO);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                for (UserDTO passenger : rideDTO.getPassengers()) {
                    Log.d("DEBUG", "sending notification - driver arrived - to passengerId:" + passenger.getId());
                    Retrofit.stompClient.send("/ride-notification-passenger/" + passenger.getId(), json);
                }

            }

            @Override
            public void onFailure(Call<RideDTO> call, Throwable t) {
                Log.d("TAG", "greska", t);
                showNoRide();
            }
        });
//
//        StompClient stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, Constants.websocketBaseUrl);
//        stompClient.connect();
//
//
//        stompClient.topic("/vehicle-location").subscribe(topicMessage -> {
//            Log.d("TAG", topicMessage.getPayload());
//        });
    }

    private void transitionToNoRideFragment() {
        FragmentTransition.to(DriverNoRideFragment.newInstance(authService.getUserData().get("user_id")),
                this, false, R.id.driver_ride_details_container);
    }

    private void transitionToAcceptedRideFragment(RideDTO rideDTO) {
        FragmentTransition.to(DriverAcceptedRideFragment.newInstance(rideDTO),
                this, false, R.id.driver_ride_details_container);
    }

    private void transitionToActiveRideFragment(RideDTO rideDTO) {
        FragmentTransition.to(DriverActiveRideFragment.newInstance(rideDTO),
                this, false, R.id.driver_ride_details_container);
    }

    private void showNoRide() {
        String driverId = Retrofit.sharedPreferences.getString("user_id", null);

        IDriverService driverService = Retrofit.retrofit.create(IDriverService.class);
        Call<VehicleDTO> vehicleResponseCall = driverService.getVehicle(Integer.parseInt(driverId));

        vehicleResponseCall.enqueue(new Callback<VehicleDTO>() {
            @Override
            public void onResponse(Call<VehicleDTO> call, Response<VehicleDTO> response) {


                VehicleDTO vehicle = response.body();
//                Log.d("TAG", "Vehicle for map: " + VehicleDTO);
                transitionToNoRideFragment();

                if (vehicle == null) return;

                currentLocation = vehicle.getCurrentLocation();

                LatLng vehicleLatLng = new LatLng(vehicle.getCurrentLocation().getLatitude(), vehicle.getCurrentLocation().getLongitude());
                mMap.addMarker(new MarkerOptions()
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                        .position(vehicleLatLng)
                        .title("You are here: " + vehicle.getCurrentLocation().getAddress()));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(vehicleLatLng, 14));
            }

            @Override
            public void onFailure(Call<VehicleDTO> call, Throwable t) {
                Log.d("TAG", "greska", t);
                transitionToNoRideFragment();
            }
        });

        StompClient stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, Constants.websocketBaseUrl);
        stompClient.connect();


        stompClient.topic("/vehicle-location").subscribe(topicMessage -> {
            Log.d("TAG", topicMessage.getPayload());
        });
    }

    @SuppressLint("CheckResult")
    private void subscribeToAcceptRide() {
        String driverId = Retrofit.sharedPreferences.getString("user_id", null);
        Retrofit.stompClient.topic("/ride-notification-driver-request-mob/" + driverId).subscribe(topicMessage -> {
            Log.d("TAG", "doslo" + topicMessage.getPayload());

            ObjectMapper objectMapper = new ObjectMapper();
            RideDTO rideDTO = objectMapper.readValue(topicMessage.getPayload(), RideDTO.class);

            if (rideDTO.getDriver().getId() == Long.parseLong(driverId)) {
                Log.d("TAG", "u if");

//                RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.notification_accept_ride);

                String text = "From: " + rideDTO.getLocations().get(0).getDeparture().getAddress() + "\n";
                text += "To: " + rideDTO.getLocations().get(0).getDestination().getAddress() + "\n";
                text += "Estimated time: " + rideDTO.getEstimatedTimeInMinutes() + "min\n";
                text += "Price: " + rideDTO.getTotalCost() + "din\n";
                text += "Number of passengers: " + rideDTO.getPassengers().size();

                Intent acceptIntent = new Intent(this, AcceptRideNotificationReceiver.class);
                acceptIntent.setAction("ACCEPT_RIDE");
                acceptIntent.putExtra("rideId", rideDTO.getId());
                PendingIntent acceptPendingIntent =
                        PendingIntent.getBroadcast(this, 0, acceptIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                Intent denyIntent = new Intent(this, AcceptRideNotificationReceiver.class);
                denyIntent.setAction("DENY_RIDE");
                denyIntent.putExtra("rideId", rideDTO.getId());

                RemoteInput remoteInput = new RemoteInput.Builder("DENY_RIDE_INPUT")
                        .setLabel("Reason")
                        .build();

                PendingIntent replyPendingIntent =
                        PendingIntent.getBroadcast(this, 2, denyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                NotificationCompat.Action denyAction =
                        new NotificationCompat.Action.Builder(R.drawable.ic_message_icon,
                                "DENY", replyPendingIntent)
                                .addRemoteInput(remoteInput)
                                .build();

                NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "NOTIFICATION_CHANNEL")
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(text))
                        .setContentTitle("Ride request")
                        .setSmallIcon(R.drawable.ic_message_icon)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .addAction(R.drawable.ic_message_icon, "ACCEPT", acceptPendingIntent)
                        .addAction(denyAction)
                        .setAutoCancel(true);

                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                notificationManager.notify(654234, builder.build());

            }
        });
    }
}
