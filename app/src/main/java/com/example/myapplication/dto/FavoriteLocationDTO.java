package com.example.myapplication.dto;

import java.util.ArrayList;
import java.util.List;

public class FavoriteLocationDTO {

    private String favoriteName;

    private List<DepartureDestinationLocationsDTO> locations;

    private List<UserResponseDTO> passengers;

    private String vehicleType;

    private boolean babyTransport;

    private boolean petTransport;


    public FavoriteLocationDTO(String favoriteName, List<DepartureDestinationLocationsDTO> locations, String vehicleType, boolean babyTransport, boolean petTransport) {
        this.favoriteName = favoriteName;
        this.locations = locations;
        this.passengers = new ArrayList<>();
        this.vehicleType = vehicleType;
        this.babyTransport = babyTransport;
        this.petTransport = petTransport;
    }

    public String getFavoriteName() {
        return favoriteName;
    }

    public void setFavoriteName(String favoriteName) {
        this.favoriteName = favoriteName;
    }

    public List<DepartureDestinationLocationsDTO> getLocations() {
        return locations;
    }

    public void setLocations(List<DepartureDestinationLocationsDTO> locations) {
        this.locations = locations;
    }

    public List<UserResponseDTO> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<UserResponseDTO> passengers) {
        this.passengers = passengers;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public boolean isBabyTransport() {
        return babyTransport;
    }

    public void setBabyTransport(boolean babyTransport) {
        this.babyTransport = babyTransport;
    }

    public boolean isPetTransport() {
        return petTransport;
    }

    public void setPetTransport(boolean petTransport) {
        this.petTransport = petTransport;
    }
}
