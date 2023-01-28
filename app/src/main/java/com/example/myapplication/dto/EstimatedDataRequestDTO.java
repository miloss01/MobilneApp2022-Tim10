package com.example.myapplication.dto;

import java.util.List;

public class EstimatedDataRequestDTO {
    private List<DepartureDestinationLocationsDTO> locations;
    private String vehicleType;
    private Boolean babyTransport;
    private Boolean petTransport;
    private Double distance;

    public EstimatedDataRequestDTO() {
    }

    public EstimatedDataRequestDTO(List<DepartureDestinationLocationsDTO> locations, String vehicleType, Boolean babyTransport, Boolean petTransport, Double distance) {
        this.locations = locations;
        this.vehicleType = vehicleType;
        this.babyTransport = babyTransport;
        this.petTransport = petTransport;
        this.distance = distance;
    }

    public List<DepartureDestinationLocationsDTO> getLocations() {
        return locations;
    }

    public void setLocations(List<DepartureDestinationLocationsDTO> locations) {
        this.locations = locations;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public Boolean getBabyTransport() {
        return babyTransport;
    }

    public void setBabyTransport(Boolean babyTransport) {
        this.babyTransport = babyTransport;
    }

    public Boolean getPetTransport() {
        return petTransport;
    }

    public void setPetTransport(Boolean petTransport) {
        this.petTransport = petTransport;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }
}
