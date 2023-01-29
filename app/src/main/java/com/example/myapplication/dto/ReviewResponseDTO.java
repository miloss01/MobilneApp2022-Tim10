package com.example.myapplication.dto;

public class ReviewResponseDTO {

    private Integer id;
    private Integer rating;
    private String comment;
    private UserResponseDTO passenger;

    public ReviewResponseDTO() {
    }

    public ReviewResponseDTO(Integer id, Integer rating, String comment, UserResponseDTO passenger) {
        this.id = id;
        this.rating = rating;
        this.comment = comment;
        this.passenger = passenger;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public UserResponseDTO getPassenger() {
        return passenger;
    }

    public void setPassenger(UserResponseDTO passenger) {
        this.passenger = passenger;
    }

    @Override
    public String toString() {
        return "ReviewResponseDTO{" +
                "id=" + id +
                ", rating=" + rating +
                ", comment='" + comment + '\'' +
                ", passenger=" + passenger +
                '}';
    }
}
