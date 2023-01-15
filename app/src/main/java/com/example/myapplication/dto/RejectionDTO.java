package com.example.myapplication.dto;

import java.io.Serializable;

public class RejectionDTO implements Serializable {

    private String reason;
    private String timeOfRejection;

    public RejectionDTO() {
    }

    public RejectionDTO(String reason, String timeOfRejection) {
        this.reason = reason;
        this.timeOfRejection = timeOfRejection;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getTimeOfRejection() {
        return timeOfRejection;
    }

    public void setTimeOfRejection(String timeOfRejection) {
        this.timeOfRejection = timeOfRejection;
    }

    @Override
    public String toString() {
        return "RejectionDTO{" +
                "reason='" + reason + '\'' +
                ", timeOfRejection='" + timeOfRejection + '\'' +
                '}';
    }
}
