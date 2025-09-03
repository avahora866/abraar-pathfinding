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
}