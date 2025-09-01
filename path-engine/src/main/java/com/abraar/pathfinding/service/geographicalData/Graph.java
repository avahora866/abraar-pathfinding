package com.abraar.pathfinding.service.geographicalData;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class Graph {
    private Map<Long, Node> nodes;
    private Map<Long, List<Node>> adjacencyList;

    public Graph(Map<Long, Node> nodes, Map<Long, List<Node>> adjacencyList) {
        this.nodes = nodes;
        this.adjacencyList = adjacencyList;
    }

    public Node addVertex(Node vertex) {
        nodes.put(vertex.getId(), vertex);
        adjacencyList.putIfAbsent(vertex.getId(), new ArrayList<>());
        return vertex;
    }

    public Node addVertex(Long vertexId) {
        return addVertex(new Node(vertexId));
    }

    public void addEdge(Node from, Node to) {
        adjacencyList.get(from.getId()).add(to);
        adjacencyList.get(to.getId()).add(from);
    }

    public void addEdges(List<Long> nodeIds) {
        for (int i = 0; i < nodeIds.size(); i++) {
            Long current = nodeIds.get(i);
            for (int j = 1; j < nodeIds.size(); j++) {
                Long next = nodeIds.get(j);
                addEdge(nodes.get(current), nodes.get(next));
            }
        }
    }

    public void printGraph() {
        for (Long vertex : nodes.keySet()) {
            System.out.println(nodes.get(vertex) + " -> " + adjacencyList.get(vertex).stream().map(Node::getId).toList());
        }
    }

    public boolean doesVertexExist(Long id) {
        if (adjacencyList.get(id) != null) {
            return true;
        }
        return false;
    }

    public Node findById(Long id) {
        return nodes.get(id);
    }

}
