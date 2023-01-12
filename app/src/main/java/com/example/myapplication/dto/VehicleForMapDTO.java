package com.example.myapplication.dto;

public class VehicleForMapDTO {
    private LocationDTO currentLocation;
    boolean active;

    public VehicleForMapDTO() {
    }

    public VehicleForMapDTO(LocationDTO currentLocation, boolean active) {
        this.currentLocation = currentLocation;
        this.active = active;
    }

    public LocationDTO getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(LocationDTO currentLocation) {
        this.currentLocation = currentLocation;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "VehicleForMapDTO{" +
                "currentLocation=" + currentLocation +
                ", active=" + active +
                '}';
    }
}
