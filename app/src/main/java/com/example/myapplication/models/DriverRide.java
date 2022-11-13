package com.example.myapplication.models;

public class DriverRide {
    public String rating;
    public String comment;
    public String startTime;
    public String endTime;
    public String numOfPassengers;
    public String kilometers;
    public String price;
    public String departure;
    public String destination;

    public DriverRide(String rating, String comment, String startTime, String endTime, String numOfPassengers, String kilometers, String price, String departure, String destination) {
        this.rating = rating;
        this.comment = comment;
        this.startTime = startTime;
        this.endTime = endTime;
        this.numOfPassengers = numOfPassengers;
        this.kilometers = kilometers;
        this.price = price;
        this.departure = departure;
        this.destination = destination;
    }
}
