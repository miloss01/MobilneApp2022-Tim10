package com.example.myapplication.dto;

public class DriverReviewRequestDTO {

    private Integer rating;
    private String comment;

    public DriverReviewRequestDTO() {
    }

    public DriverReviewRequestDTO(Integer rating, String comment) {
        this.rating = rating;
        this.comment = comment;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "VehicleReviewRequestDTO{" +
                "rating=" + rating +
                ", comment='" + comment + '\'' +
                '}';
    }

}
