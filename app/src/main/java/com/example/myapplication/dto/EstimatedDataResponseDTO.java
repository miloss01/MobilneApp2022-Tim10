package com.example.myapplication.dto;

public class EstimatedDataResponseDTO {
    private Integer estimatedTimeInMinutes;
    private Integer estimatedCost;

    public EstimatedDataResponseDTO() {
    }

    public EstimatedDataResponseDTO(Integer estimatedTimeInMinutes, Integer estimatedCost) {
        this.estimatedTimeInMinutes = estimatedTimeInMinutes;
        this.estimatedCost = estimatedCost;
    }

    public Integer getEstimatedTimeInMinutes() {
        return estimatedTimeInMinutes;
    }

    public void setEstimatedTimeInMinutes(Integer estimatedTimeInMinutes) {
        this.estimatedTimeInMinutes = estimatedTimeInMinutes;
    }

    public Integer getEstimatedCost() {
        return estimatedCost;
    }

    public void setEstimatedCost(Integer estimatedCost) {
        this.estimatedCost = estimatedCost;
    }

    @Override
    public String toString() {
        return "EstimatedDataResponseDTO{" +
                "estimatedTimeInMinutes=" + estimatedTimeInMinutes +
                ", estimatedCost=" + estimatedCost +
                '}';
    }
}
