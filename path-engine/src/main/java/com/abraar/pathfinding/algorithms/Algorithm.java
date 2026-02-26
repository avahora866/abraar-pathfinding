package com.abraar.pathfinding.algorithms;

import com.abraar.pathfinding.dto.Graph;

import static com.abraar.pathfinding.controller.Routes.Route;

public interface Algorithm {
    Route getRoute(Graph graph, long startNodeId, long endNodeId);
}
