package com.example.myapplication.dto;

import java.util.HashMap;

public class ReportDTO {
    private HashMap<String, Double> values;
    private double total;
    private double average;

    public ReportDTO() {
        values = new HashMap<>();
        total = 0;
        average = 0;
    }

    public ReportDTO(HashMap<String, Double> values, double total, double average) {
        this.values = values;
        this.total = total;
        this.average = average;
    }

    public HashMap<String, Double> getValues() {
        return values;
    }

    public void setValues(HashMap<String, Double> values) {
        this.values = values;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getAverage() {
        return average;
    }

    public void setAverage(double average) {
        this.average = average;
    }

    @Override
    public String toString() {
        return "ReportDTO{" +
                "values=" + values.size() +
                ", total=" + total +
                ", average=" + average +
                '}';
    }
}
