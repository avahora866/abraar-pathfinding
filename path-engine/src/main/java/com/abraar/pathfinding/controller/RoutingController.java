package com.abraar.pathfinding.controller;

import com.abraar.pathfinding.algorithms.Algorithm;
import com.abraar.pathfinding.dto.Graph;
import com.abraar.pathfinding.service.geographicalData.GeographicalDataServiceVersion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class RoutingController {

    @Autowired
    private GeographicalDataServiceVersion geographicalDataServiceVersion;

    @Autowired
    private List<Algorithm> algorithms;

    @PostMapping("getRoutes")
    public Routes getRoutes(@RequestBody RouteRequest routeRequest) throws InterruptedException {
        // Given a start and destination return a graph representation of the geographical data to be used by algorithms
        Graph graph = geographicalDataServiceVersion.getGraph(routeRequest);
        Routes routes = new Routes();
        List<Routes.Route> routeList = new ArrayList<>();

        double[] startCoords = {routeRequest.getStart()[0], routeRequest.getStart()[1]};
        double[] endCoords = {routeRequest.getDestination()[0], routeRequest.getDestination()[1]};

        long startNodeId = graph.getNearestNode(startCoords[0], startCoords[1]);
        long endNodeId = graph.getNearestNode(endCoords[0], endCoords[1]);


        for (Algorithm algorithm : algorithms) {
            routeList.add(algorithm.getRoute(graph, startNodeId, endNodeId));
        }

        routes.setRoutes(routeList);
        return routes;
    }
}
