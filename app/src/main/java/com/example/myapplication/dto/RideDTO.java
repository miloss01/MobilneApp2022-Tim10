package com.example.myapplication.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;

public class RideDTO {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long id;
    private ArrayList<DepartureDestinationLocationsDTO> locations;
    private String startTime;
    private String endTime;
    private int totalCost;
    private UserDTO driver;
    private ArrayList<UserDTO> passengers;
    private int estimatedTimeInMinutes;
    private String vehicleType;
    private boolean babyTransport;
    private boolean petTransport;
    @JsonInclude(JsonInclude.Include. NON_NULL)
    private String status;
    private RejectionDTO rejection;

    public RideDTO() {
    }

    public RideDTO(Long id, ArrayList<DepartureDestinationLocationsDTO> locations, String startTime, String endTime, int totalCost, UserDTO driver, ArrayList<UserDTO> passengers, int estimatedTimeInMinutes, String vehicleType, boolean babyTransport, boolean petTransport, String status, RejectionDTO rejection) {
        this.id = id;
        this.locations = locations;
        this.startTime = startTime;
        this.endTime = endTime;
        this.totalCost = totalCost;
        this.driver = driver;
        this.passengers = passengers;
        this.estimatedTimeInMinutes = estimatedTimeInMinutes;
        this.vehicleType = vehicleType;
        this.babyTransport = babyTransport;
        this.petTransport = petTransport;
        this.status = status;
        this.rejection = rejection;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ArrayList<DepartureDestinationLocationsDTO> getLocations() {
        return locations;
    }

    public void setLocations(ArrayList<DepartureDestinationLocationsDTO> locations) {
        this.locations = locations;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(int totalCost) {
        this.totalCost = totalCost;
    }

    public UserDTO getDriver() {
        return driver;
    }

    public void setDriver(UserDTO driver) {
        this.driver = driver;
    }

    public ArrayList<UserDTO> getPassengers() {
        return passengers;
    }

    public void setPassengers(ArrayList<UserDTO> passengers) {
        this.passengers = passengers;
    }

    public int getEstimatedTimeInMinutes() {
        return estimatedTimeInMinutes;
    }

    public void setEstimatedTimeInMinutes(int estimatedTimeInMinutes) {
        this.estimatedTimeInMinutes = estimatedTimeInMinutes;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public RejectionDTO getRejection() {
        return rejection;
    }

    public void setRejection(RejectionDTO rejection) {
        this.rejection = rejection;
    }

    @Override
    public String toString() {
        return "RideDTO{" +
                "id=" + id +
                ", locations=" + locations +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", totalCost=" + totalCost +
                ", driver=" + driver +
                ", passengers=" + passengers +
                ", estimatedTimeInMinutes=" + estimatedTimeInMinutes +
                ", vehicleType='" + vehicleType + '\'' +
                ", babyTransport=" + babyTransport +
                ", petTransport=" + petTransport +
                ", status='" + status + '\'' +
                ", rejection=" + rejection +
                '}';
    }
}
