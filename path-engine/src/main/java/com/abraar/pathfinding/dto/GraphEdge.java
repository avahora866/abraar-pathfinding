package com.abraar.pathfinding.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GraphEdge {
    private long id; // OSM way ID
    private long fromNodeId;
    private long toNodeId;
    private double length;

    public GraphEdge(long id, long fromNodeId, long toNodeId) {
        this.id = id;
        this.fromNodeId = fromNodeId;
        this.toNodeId = toNodeId;
    }
}