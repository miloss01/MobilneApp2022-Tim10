package com.example.myapplication.dto;

import java.util.ArrayList;

public class VehicleResponceDTO {
    private int size;
    private ArrayList<VehicleForMapDTO> vehicles;

    public VehicleResponceDTO() {
    }

    public VehicleResponceDTO(int size, ArrayList<VehicleForMapDTO> vehicles) {
        this.size = size;
        this.vehicles = vehicles;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public ArrayList<VehicleForMapDTO> getVehicles() {
        return vehicles;
    }

    public void setVehicles(ArrayList<VehicleForMapDTO> vehicles) {
        this.vehicles = vehicles;
    }
}
