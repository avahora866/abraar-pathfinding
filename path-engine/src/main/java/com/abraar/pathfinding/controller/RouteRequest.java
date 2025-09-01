package com.abraar.pathfinding.controller;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RouteRequest {
    private double[] start;
    private double[] destination;

    public double[] getStart() {
        return start;
    }

    public void setStart(double[] start) {
        this.start = start;
    }

    public double[] getDestination() {
        return destination;
    }

    public void setDestination(double[] destination) {
        this.destination = destination;
    }
}
