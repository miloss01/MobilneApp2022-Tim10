package com.example.myapplication.dto;

public class ChangeRequestDTO {
    private UserExpandedDTO userDTO;
    private VehicleDTO vehicleDTO;
    private String date;

    public ChangeRequestDTO() {
    }

    public ChangeRequestDTO(UserExpandedDTO userDTO, VehicleDTO vehicleDTO, String date) {
        this.userDTO = userDTO;
        this.vehicleDTO = vehicleDTO;
        this.date = date;
    }

    public UserExpandedDTO getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(UserExpandedDTO userDTO) {
        this.userDTO = userDTO;
    }

    public VehicleDTO getVehicleDTO() {
        return vehicleDTO;
    }

    public void setVehicleDTO(VehicleDTO vehicleDTO) {
        this.vehicleDTO = vehicleDTO;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "ChangeRequestDTO{" +
                "userDTO=" + userDTO +
                ", vehicleDTO=" + vehicleDTO +
                ", date='" + date + '\'' +
                '}';
    }
}
