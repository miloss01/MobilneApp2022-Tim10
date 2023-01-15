package com.example.myapplication.dto;

public class NotificationDTO {

    private String message;
    private Integer rideId;
    private String reason;

    public NotificationDTO() {
    }

    public NotificationDTO(String message, Integer rideId, String reason) {
        this.message = message;
        this.rideId = rideId;
        this.reason = reason;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getRideId() {
        return rideId;
    }

    public void setRideId(Integer rideId) {
        this.rideId = rideId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return "NotificationDTO{" +
                "message='" + message + '\'' +
                ", rideId=" + rideId +
                ", reason='" + reason + '\'' +
                '}';
    }
}
