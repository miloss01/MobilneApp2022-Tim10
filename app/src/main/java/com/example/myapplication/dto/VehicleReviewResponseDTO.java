package com.example.myapplication.dto;

public class VehicleReviewResponseDTO extends ReviewResponseDTO {

    public VehicleReviewResponseDTO(){
    }

    public VehicleReviewResponseDTO(Integer id, Integer rating, String comment, UserResponseDTO passenger) {
        super(id, rating, comment, passenger);
    }

}
