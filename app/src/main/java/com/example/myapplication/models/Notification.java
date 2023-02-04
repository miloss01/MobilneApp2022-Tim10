package com.example.myapplication.models;

import java.time.LocalDateTime;

public class Notification {

    private String message;
    private LocalDateTime timeOfReceiving;

    public Notification() {
    }

    public Notification(String message) {
        this.message = message;
        this.timeOfReceiving = LocalDateTime.now();
    }

    public Notification(String message, LocalDateTime timeOfReceiving) {
        this.message = message;
        this.timeOfReceiving = timeOfReceiving;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public LocalDateTime getTimeOfReceiving() {
        return timeOfReceiving;
    }

    public void setTimeOfReceiving(LocalDateTime timeOfReceiving) {
        this.timeOfReceiving = timeOfReceiving;
    }
}
