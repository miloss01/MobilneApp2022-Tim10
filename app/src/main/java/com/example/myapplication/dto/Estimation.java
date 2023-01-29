package com.example.myapplication.dto;

public class Estimation {
    private Double km;
    private Double timeInMin;

    public Estimation() {
    }

    public Estimation(Double km, Double timeInMin) {
        this.km = km;
        this.timeInMin = timeInMin;
    }

    public Double getKm() {
        return km;
    }

    public void setKm(Double km) {
        this.km = km;
    }

    public Double getTimeInMin() {
        return timeInMin;
    }

    public void setTimeInMin(Double timeInMin) {
        this.timeInMin = timeInMin;
    }
}
