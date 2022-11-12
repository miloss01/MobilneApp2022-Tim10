package com.example.myapplication.models;

import java.time.LocalDateTime;
import java.util.List;

public class Ride {

    private double price;
    private TimeSlot time;
    private User driver;
    private List<User> passengers;
    private boolean babyFlag;
    private boolean petsFlag;
    private boolean splitFareFlag;
    // status
    // vehicletype
    // payment
    // route

    public Ride() {}

    public Ride(double price, LocalDateTime start, LocalDateTime end) {
        this.price = price;
        this.time = new TimeSlot(start, end);
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public TimeSlot getTime() {
        return time;
    }

    public void setTime(TimeSlot time) {
        this.time = time;
    }

    public User getDriver() {
        return driver;
    }

    public void setDriver(User driver) {
        this.driver = driver;
    }

    public List<User> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<User> passengers) {
        this.passengers = passengers;
    }

    public boolean isBabyFlag() {
        return babyFlag;
    }

    public void setBabyFlag(boolean babyFlag) {
        this.babyFlag = babyFlag;
    }

    public boolean isPetsFlag() {
        return petsFlag;
    }

    public void setPetsFlag(boolean petsFlag) {
        this.petsFlag = petsFlag;
    }

    public boolean isSplitFareFlag() {
        return splitFareFlag;
    }

    public void setSplitFareFlag(boolean splitFareFlag) {
        this.splitFareFlag = splitFareFlag;
    }
}
