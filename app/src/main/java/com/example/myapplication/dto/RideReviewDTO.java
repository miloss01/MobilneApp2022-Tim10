package com.example.myapplication.dto;

public class RideReviewDTO {

    private VehicleReviewResponseDTO vehicleReview;
    private DriverReviewResponseDTO driverReview;

    public RideReviewDTO() {
    }

    public RideReviewDTO(VehicleReviewResponseDTO vehicleReview, DriverReviewResponseDTO driverReview) {
        this.vehicleReview = vehicleReview;
        this.driverReview = driverReview;
    }

    public VehicleReviewResponseDTO getVehicleReview() {
        return vehicleReview;
    }

    public void setVehicleReview(VehicleReviewResponseDTO vehicleReview) {
        this.vehicleReview = vehicleReview;
    }

    public DriverReviewResponseDTO getDriverReview() {
        return driverReview;
    }

    public void setDriverReview(DriverReviewResponseDTO driverReview) {
        this.driverReview = driverReview;
    }

    @Override
    public String toString() {
        return "RideReviewDTO{" +
                "vehicleReview=" + vehicleReview +
                ", driverReview=" + driverReview +
                '}';
    }
}
