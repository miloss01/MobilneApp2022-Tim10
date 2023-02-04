package com.example.myapplication.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

public class WorkingHourDTO {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer id;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String start;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String end;

    public WorkingHourDTO(Integer id, String start, String end) {
        this.id = id;
        this.start = start;
        this.end = end;
    }

    public WorkingHourDTO() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }
}
