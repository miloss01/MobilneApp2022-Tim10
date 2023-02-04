package com.example.myapplication.dto;

public class DriverReviewResponseDTO extends ReviewResponseDTO {

    public DriverReviewResponseDTO(){
    }

    public DriverReviewResponseDTO(Integer id, Integer rating, String comment, UserResponseDTO passenger) {
        super(id, rating, comment, passenger);
    }

}
