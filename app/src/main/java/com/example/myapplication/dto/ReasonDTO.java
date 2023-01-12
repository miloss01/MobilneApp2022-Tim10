package com.example.myapplication.dto;

public class ReasonDTO {

    private String reason;

    public ReasonDTO() {
    }

    public ReasonDTO(String reason) {
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return "ReasonDTO{" +
                "reason='" + reason + '\'' +
                '}';
    }
}
