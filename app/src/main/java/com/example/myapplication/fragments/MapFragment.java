package com.example.myapplication.fragments;

import static androidx.core.content.ContextCompat.getSystemService;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.GnssAntennaInfo;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.myapplication.BuildConfig;
import com.example.myapplication.R;
import com.example.myapplication.dialogs.LocationDialog;
import com.example.myapplication.dto.DepartureDestinationLocationsDTO;
import com.example.myapplication.dto.EstimatedDataRequestDTO;
import com.example.myapplication.dto.Estimation;
import com.example.myapplication.dto.LocationDTO;
import com.example.myapplication.dto.VehicleForMapDTO;
import com.example.myapplication.dto.VehicleResponceDTO;
import com.example.myapplication.services.IVehicleService;
import com.example.myapplication.services.MapService;
import com.example.myapplication.tools.Retrofit;
import com.google.android.gms.maps.CameraUpdate;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment implements LocationListener, OnMapReadyCallback {

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    //private LocationManager locationManager;
    //private String provider;
    private SupportMapFragment mMapFragment;
    private AlertDialog dialog;
    private Marker home;
    private GoogleMap map;
    private List<LatLng> path = new ArrayList();
    private MapService mapService;

    public static MapFragment newInstance() {
        MapFragment mpf = new MapFragment();
        return mpf;
    }

    /**
     * Prilikom kreidanja fragmenta preuzimamo sistemski servis za rad sa lokacijama
     * */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        mapService = new MapService();
    }

    /**
     * Kada zelmo da dobijamo informacije o lokaciji potrebno je da specificiramo
     * po kom kriterijumu zelimo da dobijamo informacije GSP, MOBILNO(WIFI, MObilni internet), GPS+MOBILNO
     * **/
    private void createMapFragmentAndInflate() {

        //kreiramo novu instancu fragmenta
        mMapFragment = SupportMapFragment.newInstance();

        //i vrsimo zamenu trenutnog prikaza sa prikazom mape
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.map_container, mMapFragment).commit();

        mMapFragment.getMapAsync(this);
    }

    private void showLocatonDialog() {
        if (dialog == null) {
            dialog = new LocationDialog(getActivity()).prepareDialog();
        } else {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
        dialog.show();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onResume() {
        super.onResume();

        createMapFragmentAndInflate();


//        if (!gps && !wifi) {
        if (false) {
            Log.i("ASD", "ASDresumemap");
            showLocatonDialog();
        } else {
            if (checkLocationPermission()) {
                if (ContextCompat.checkSelfPermission(requireContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {

                    //Request location updates:
                    //locationManager.requestLocationUpdates(provider, 2000, 0, this);
                    Toast.makeText(getContext(), "ACCESS_FINE_LOCATION", Toast.LENGTH_SHORT).show();
                }else if(ContextCompat.checkSelfPermission(getContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){

                    //Request location updates:
                    //locationManager.requestLocationUpdates(provider, 2000, 0, this);
                    Toast.makeText(getContext(), "ACCESS_COARSE_LOCATION", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup vg, Bundle data) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_map, vg, false);
        return view;
    }

    /**
     * Svaki put kada uredjaj dobijee novu informaciju o lokaciji ova metoda se poziva
     * i prosledjuje joj se nova informacija o kordinatamad
     * */
    @Override
    public void onLocationChanged(Location location) {
//        Toast.makeText(getActivity(), "NEW LOCATION", Toast.LENGTH_SHORT).show();
//        if (map != null) {
//            addMarker(location);
//        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(getActivity())
                        .setTitle("Allow user location")
                        .setMessage("To continue working we need your locations....Allow now?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{
                                                Manifest.permission.ACCESS_FINE_LOCATION,
                                                Manifest.permission.ACCESS_COARSE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode,
//                                           String permissions[], int[] grantResults) {
//        switch (requestCode) {
//            case MY_PERMISSIONS_REQUEST_LOCATION: {
//                // If request is cancelled, the result arrays are empty.
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//                    // permission was granted, yay! Do the
//                    // location-related task you need to do.
//                    if (ContextCompat.checkSelfPermission(requireActivity(),
//                            Manifest.permission.ACCESS_FINE_LOCATION)
//                            == PackageManager.PERMISSION_GRANTED) {
//
//                        //Request location updates:
//                        locationManager.requestLocationUpdates(provider, 0, 0, this);
//                    }
//
//                } else if (grantResults.length > 0
//                        && grantResults[1] == PackageManager.PERMISSION_GRANTED){
//
//                    // permission denied, boo! Disable the
//                    // functionality that depends on this permission.
//                    if (ContextCompat.checkSelfPermission(getActivity(),
//                            Manifest.permission.ACCESS_COARSE_LOCATION)
//                            == PackageManager.PERMISSION_GRANTED) {
//
//                        //Request location updates:
//                        locationManager.requestLocationUpdates(provider, 0, 0, this);
//                    }
//
//                }
//                return;
//            }
//
//        }
//    }


    /**
     * KAda je mapa spremna mozemo da radimo sa njom.
     * Mozemo reagovati na razne dogadjaje dodavanje markera, pomeranje markera,klik na mapu,...
     * */
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
//        map.setMyLocationEnabled(true);
        LatLng location = null;
        String provider = "d";
        if (provider == null) {
            Log.i("ASD", "Onmapre");

            showLocatonDialog();
        }else {
            if (checkLocationPermission()) {
                Log.i("ASD", "str" + provider);


                if (ContextCompat.checkSelfPermission(getContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    //Request location updates:
                    //location = locationManager.getLastKnownLocation(provider);
                    location = new LatLng(45.24619939901454, 19.85162169815072);
                    Log.i("LOCATION", location.toString());

                } else if (ContextCompat.checkSelfPermission(getContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    //Request location updates:
                    //location = locationManager.getLastKnownLocation(provider);
                    location = new LatLng(45.24619939901454, 19.85162169815072);
                    Log.i("LOCATION", location.toString());

                }
            }
        }

        //ako zelimo da rucno postavljamo markere to radimo
        //dodavajuci click listener
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
//                map.addMarker(new MarkerOptions()
//                        .title("YOUR_POSITON")
//                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
//                        .position(latLng));
//                home.setFlat(true);
//
//                CameraPosition cameraPosition = new CameraPosition.Builder()
//                        .target(latLng).zoom(14).build();
//
//                map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });

        //ako zelmo da reagujemo na klik markera koristimo marker click listener
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Toast.makeText(getActivity(), marker.getTitle(), Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Log.i("ASD", "ASDASDASDSA");
            }
        });

        //ako je potrebno da reagujemo na pomeranje markera koristimo marker drag listener
        map.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
                Toast.makeText(getActivity(), "Drag started", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onMarkerDrag(Marker marker) {
                Toast.makeText(getActivity(), "Dragging", Toast.LENGTH_SHORT).show();
                map.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                Toast.makeText(getActivity(), "Drag ended", Toast.LENGTH_SHORT).show();
            }
        });

        if (location != null) {
            addMarker(location);
        }
    }

    private void addMarker(LatLng location) {
        LatLng loc = location;

        if (home != null) {
            home.remove();
        }

        home = map.addMarker(new MarkerOptions()
                .title("YOUR_POSITON")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                .position(loc));
        home.setFlat(true);

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(loc).zoom(14).build();

        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(45.24619939901454, 19.85162169815072), 15));
        this.loadVehicles();
    }

    /**
     *
     * Rad sa lokacja izuzetno trosi bateriju.Obavezno osloboditi kada vise ne koristmo
     * */
    @Override
    public void onPause() {
        super.onPause();

        //locationManager.removeUpdates(this);
    }

    public Estimation drawRoute(LocationDTO departureDTO, LocationDTO destinationDTO) {
        map.clear();
        //loadVehicles();
        String origin = setMark(departureDTO, "departure");
        String end = setMark(destinationDTO, "destination");
        path = mapService.getPath(origin, end);

        //Draw the polyline
        if (path.size() > 0) {
            Log.d("TAG", "duzina" + path.size());
            PolylineOptions opts = new PolylineOptions().addAll(path).color(Color.BLUE).width(5);
            map.addPolyline(opts);
        }
        return getDistance2(destinationDTO.getAddress(), departureDTO.getAddress());

    }

    public Estimation getDistance2(String address1, String address2) {
        final Integer[] distance = new Integer[1];
        final Integer[] duration = new Integer[1];
        Log.e("DEBUG", "Usao je u ucitavanje");
        final String[] parsedDistance = new String[1];
        final String[] response = new String[1];
        String key = BuildConfig.MAPS_API_KEY;
        String origiin = address1.replace(" ", "+");
        origiin = origiin.replace(",", "");
        String destination = address2.replace(" ", "+");
        destination = destination.replace(",", "");
        String urlStr = String.format("https://maps.googleapis.com/maps/api/distancematrix/json" +
                "?language=sr?&units=metric&mode=driving" +
                "&origins=%s" +
                "&destinations=%s" +
                "&key=%s", origiin, destination, key);
        Log.e("DEBUG", urlStr);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.e("DEBUG", "Usao je u try");
                    URL url = new URL(urlStr);
                    Log.v("urldirection", url.toString());
                    final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    InputStream in = new BufferedInputStream(conn.getInputStream());
//                    response[0] = org.apache.commons.io.IOUtils.toString(in, "UTF-8");
                    response[0] = new BufferedReader(
                            new InputStreamReader(in, StandardCharsets.UTF_8))
                            .lines()
                            .collect(Collectors.joining("\n"));
                    Log.e("DEBUG", "RESPONCEEEE");


                    JSONObject jsonObject = new JSONObject(response[0]);
                    Log.e("DEBUG", jsonObject.toString());
                    // {"destination_addresses":["Pasterova 4, Novi Sad, Serbia"],
                    // "origin_addresses":["Pasterova 7, Novi Sad, Serbia"],
                    // "rows":[{"elements":
                    // [{"distance":{"text":"0.2 mi","value":308},
                    // "duration":{"text":"1 min","value":73},"status":"OK"}]
                    // }],
                    // "status":"OK"}

                    JSONObject row = jsonObject.getJSONArray("rows").getJSONObject(0);
                    JSONObject element = row.getJSONArray("elements").getJSONObject(0);
                    distance[0] = element.getJSONObject("distance").getInt("value");
                    duration[0] = element.getJSONObject("duration").getInt("value");
                    Log.e("DEBUG", String.valueOf(distance[0]));
                    Log.e("DEBUG", String.valueOf(duration[0]));

                } catch (JSONException | IOException e) {
                    Log.v("TAG", e.toString());
                }
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            Log.v("DistanceGoogleAPi", "Interrupted!" + e);
            Thread.currentThread().interrupt();
        }
        return new Estimation(distance[0] /1000.0, duration[0] /60.0);
    }

    private String setMark(LocationDTO departureDTO, String tagName) {
        Geocoder geocoder = new Geocoder(getContext());
        String coordinates = "";
        try {
            List<Address> departures = geocoder.getFromLocationName(departureDTO.getAddress(), 1);
            departureDTO.setLatitude(departures.get(0).getLatitude());
            departureDTO.setLongitude(departures.get(0).getLongitude());
            LatLng latLng = new LatLng(departures.get(0).getLatitude(), departures.get(0).getLongitude());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.title(tagName);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
            markerOptions.position(latLng);
            map.addMarker(markerOptions);
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
            map.animateCamera(cameraUpdate);
            coordinates = "" + departureDTO.getLatitude() + "," + departureDTO.getLongitude();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return coordinates;
    }

    public void loadVehicles() {
//        Log.e("DEBUG", "Usao je u ucitavanje");
        IVehicleService vehicleService = Retrofit.retrofit.create(IVehicleService.class);
        Call<VehicleResponceDTO> vehicleResponce = vehicleService.getVehicles();
        vehicleResponce.enqueue(new Callback<VehicleResponceDTO>() {
            @Override
            public void onResponse(Call<VehicleResponceDTO> call, Response<VehicleResponceDTO> response) {
                if (response.code() != 200)
                    return;
                assert response.body() != null;
                for (VehicleForMapDTO vehicle: response.body().getVehicles()) {
                    Log.d("TAG", vehicle.toString());
                    LatLng location = new LatLng(vehicle.getCurrentLocation().getLatitude(), vehicle.getCurrentLocation().getLongitude());
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.title("Non Active Vehicle");
                    markerOptions.position(location);
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                    if(vehicle.isActive()) {
                        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                        markerOptions.title("Active Vehicle");
                    }
                    map.addMarker(markerOptions);
                }
            }

            @Override
            public void onFailure(Call<VehicleResponceDTO> call, Throwable t) {
                Log.d("TAG", "greska", t);
            }
        });
    }
}