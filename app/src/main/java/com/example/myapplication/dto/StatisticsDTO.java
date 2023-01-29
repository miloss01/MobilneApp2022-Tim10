package com.example.myapplication.dto;

public class StatisticsDTO {
    double acceptanceRate;
    double workingHours;
    double kilometers;
    double income;

    public StatisticsDTO(double acceptanceRate, double workingHours, double kilometers, double income) {
        this.acceptanceRate = acceptanceRate;
        this.workingHours = workingHours;
        this.kilometers = kilometers;
        this.income = income;
    }

    public StatisticsDTO() {
    }

    public double getAcceptanceRate() {
        return acceptanceRate;
    }

    public void setAcceptanceRate(double acceptanceRate) {
        this.acceptanceRate = acceptanceRate;
    }

    public double getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(double workingHours) {
        this.workingHours = workingHours;
    }

    public double getKilometers() {
        return kilometers;
    }

    public void setKilometers(double kilometers) {
        this.kilometers = kilometers;
    }

    public double getIncome() {
        return income;
    }

    public void setIncome(double income) {
        this.income = income;
    }

    @Override
    public String toString() {
        return "StatisticsDTO{" +
                "acceptanceRate=" + acceptanceRate +
                ", workingHours=" + workingHours +
                ", kilometers=" + kilometers +
                ", income=" + income +
                '}';
    }
}
