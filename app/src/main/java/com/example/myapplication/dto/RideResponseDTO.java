package com.example.myapplication.dto;

import java.util.ArrayList;

public class RideResponseDTO {

    private int totalCount;
    private ArrayList<RideDTO> results;

    public RideResponseDTO() {
        this.results = new ArrayList<>();
    }

    public RideResponseDTO(int totalCount, ArrayList<RideDTO> results) {
        this.totalCount = totalCount;
        this.results = results;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public ArrayList<RideDTO> getResults() {
        return results;
    }

    public void setResults(ArrayList<RideDTO> results) {
        this.results = results;
    }

    @Override
    public String toString() {
        return "RideResponseDTO{" +
                "totalCount=" + totalCount +
                ", results=" + results +
                '}';
    }
}
