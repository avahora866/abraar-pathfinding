package com.abraar.pathfinding.algorithms;

import com.abraar.pathfinding.controller.Routes;
import com.abraar.pathfinding.dto.Graph;
import com.abraar.pathfinding.dto.GraphEdge;
import com.abraar.pathfinding.dto.GraphNode;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class BFS implements Algorithm {
    @Override
    public Routes.Route getRoute(Graph graph, long startNodeId, long endNodeId) {
        Routes.Route route = new Routes.Route();
        long startTime = System.currentTimeMillis();
        double[][] bfs = bfs(graph, startNodeId, endNodeId);
        long endTime = System.currentTimeMillis();
        long durationMs = endTime - startTime;

        route.setAlgorithm("BFS");
        route.setRoute(bfs);
        route.setAlgorithmTime(durationMs);
        route.setDistance(0);

        return route;
    }

    private double[][] bfs(Graph graph, long startNodeId, long endNodeId) {
        Queue<Long> queue = new LinkedList<>();
        Set<Long> visited = new HashSet<>();
        Map<Long, Long> parentMap = new HashMap<>();

        queue.add(startNodeId);
        visited.add(startNodeId);

        boolean pathFound = false;

        while (!queue.isEmpty()) {
            long current = queue.poll();

            if (current == endNodeId) {
                pathFound = true;
                break;
            }

            List<GraphEdge> edges = graph.getAdjacencyList().getOrDefault(current, Collections.emptyList());

            for (GraphEdge edge : edges) {
                long neighbor = edge.getToNodeId();
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    parentMap.put(neighbor, current);
                    queue.add(neighbor);
                }
            }
        }

        if (!pathFound) {
            return new double[0][0];
        }

        return reconstructPath(graph, parentMap, endNodeId);
    }

    private double[][] reconstructPath(Graph graph, Map<Long, Long> parentMap, long endNodeId) {
        List<double[]> pathCoords = new ArrayList<>();
        Long currentId = endNodeId;

        while (currentId != null) {
            GraphNode node = graph.getNodes().get(currentId);
            pathCoords.add(new double[]{node.getLatitude(), node.getLongitude()});
            currentId = parentMap.get(currentId);
        }

        Collections.reverse(pathCoords);
        return pathCoords.toArray(new double[0][0]);
    }
}
