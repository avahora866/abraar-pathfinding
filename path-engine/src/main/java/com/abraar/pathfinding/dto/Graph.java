package com.abraar.pathfinding.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class Graph {
    private Map<Long, GraphNode> nodes = new HashMap<>();
    private Map<Long, List<GraphEdge>> adjacencyList = new HashMap<>();

    public void addNode(GraphNode node) {
        nodes.putIfAbsent(node.getId(), node);
    }
    
    public void addEdge(GraphEdge edge) {
        adjacencyList.computeIfAbsent(edge.getFromNodeId(), k -> new LinkedList<>()).add(edge);
        GraphEdge reverseEdge = new GraphEdge(edge.getId(), edge.getToNodeId(), edge.getFromNodeId());
        adjacencyList.computeIfAbsent(reverseEdge.getFromNodeId(), k -> new LinkedList<>()).add(reverseEdge);
    }

    public long getNearestNode(double targetLat, double targetLon) {
        long nearestNodeId = -1;
        double minDistance = Double.MAX_VALUE;

        for (GraphNode node : nodes.values()) {
            double distance = calculateHaversineDistance(
                    targetLat, targetLon, node.getLatitude(), node.getLongitude()
            );

            if (distance < minDistance) {
                minDistance = distance;
                nearestNodeId = node.getId();
            }
        }

        return nearestNodeId;
    }

    private double calculateHaversineDistance(double lat1, double lon1, double lat2, double lon2) {
        final double EARTH_RADIUS_METERS = 6371000.0;

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS_METERS * c;
    }
}