package com.example.myapplication.dto;

public class DepartureDestinationLocationsDTO {
    private LocationDTO departure;
    private LocationDTO destination;

    public DepartureDestinationLocationsDTO() {
    }

    @Override
    public String toString() {
        return "DepartureDestinationLocationsDTO{" +
                "departure=" + departure +
                ", destination=" + destination +
                '}';
    }

    public DepartureDestinationLocationsDTO(LocationDTO departure, LocationDTO destination) {
        this.departure = departure;
        this.destination = destination;
    }

    public LocationDTO getDeparture() {
        return departure;
    }

    public void setDeparture(LocationDTO departure) {
        this.departure = departure;
    }

    public LocationDTO getDestination() {
        return destination;
    }

    public void setDestination(LocationDTO destination) {
        this.destination = destination;
    }
}