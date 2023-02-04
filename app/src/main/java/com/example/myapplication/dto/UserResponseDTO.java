package com.example.myapplication.dto;

public class UserResponseDTO {

    private long id;
    private String email;

    public UserResponseDTO() {
    }

    public UserResponseDTO(long id, String email) {
        this.id = id;
        this.email = email;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "UserResponseDTO{" +
                "id=" + id +
                ", email='" + email + '\'' +
                '}';
    }
}
