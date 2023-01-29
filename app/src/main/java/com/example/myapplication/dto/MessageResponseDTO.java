package com.example.myapplication.dto;

import java.util.ArrayList;

public class MessageResponseDTO {

    private int totalCount;
    private ArrayList<MessageReceivedDTO> results;

    public MessageResponseDTO(int totalCount, ArrayList<MessageReceivedDTO> results) {
        this.totalCount = totalCount;
        this.results = results;
    }

    public MessageResponseDTO() {
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public ArrayList<MessageReceivedDTO> getResults() {
        return results;
    }

    public void setResults(ArrayList<MessageReceivedDTO> results) {
        this.results = results;
    }
}
