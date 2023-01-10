package com.example.myapplication.dto;

public class IsActiveDTO {

    private boolean active;

    public IsActiveDTO() {}

    public IsActiveDTO(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "IsActiveDTO{" +
                "active=" + active +
                '}';
    }
}
