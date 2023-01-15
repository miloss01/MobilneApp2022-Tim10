package com.example.myapplication.dto;

import java.util.ArrayList;

public class RideCreationDTO {
    private ArrayList<DepartureDestinationLocationsDTO> locations;
    private ArrayList<UserDTO> passengers;
    private String startTime;
    private String vehicleType;
    private boolean babyTransport;
    private boolean petTransport;
    private int estimatedTimeMinutes;

    public RideCreationDTO() {
    }

    public RideCreationDTO(ArrayList<DepartureDestinationLocationsDTO> locations, ArrayList<UserDTO> passengers, String startTime, String vehicleType, boolean babyTransport, boolean petTransport, int estimatedTimeMinutes) {
        this.locations = locations;
        this.passengers = passengers;
        this.startTime = startTime;
        this.vehicleType = vehicleType;
        this.babyTransport = babyTransport;
        this.petTransport = petTransport;
        this.estimatedTimeMinutes = estimatedTimeMinutes;
    }

    public ArrayList<DepartureDestinationLocationsDTO> getLocations() {
        return locations;
    }

    public void setLocations(ArrayList<DepartureDestinationLocationsDTO> locations) {
        this.locations = locations;
    }

    public ArrayList<UserDTO> getPassengers() {
        return passengers;
    }

    public void setPassengers(ArrayList<UserDTO> passengers) {
        this.passengers = passengers;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
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

    public int getEstimatedTimeMinutes() {
        return estimatedTimeMinutes;
    }

    public void setEstimatedTimeMinutes(int estimatedTimeMinutes) {
        this.estimatedTimeMinutes = estimatedTimeMinutes;
    }

    @Override
    public String toString() {
        return "RideCreationDTO{" +
                "locations=" + locations +
                ", passengers=" + passengers +
                ", startTime='" + startTime + '\'' +
                ", vehicleType='" + vehicleType + '\'' +
                ", babyTransport=" + babyTransport +
                ", petTransport=" + petTransport +
                ", estimatedTimeMinutes=" + estimatedTimeMinutes +
                '}';
    }
}
