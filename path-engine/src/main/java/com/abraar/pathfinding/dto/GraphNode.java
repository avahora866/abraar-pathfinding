package com.abraar.pathfinding.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GraphNode {
    private long id;
    private double latitude;
    private double longitude;

    public GraphNode(long id, double latitude, double longitude) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}