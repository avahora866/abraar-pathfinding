package com.abraar.pathfinding.controller;

import com.abraar.pathfinding.dto.Graph;
import com.abraar.pathfinding.service.geographicalData.GeographicalDataServiceVersion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RoutingController {

    @Autowired
    private GeographicalDataServiceVersion geographicalDataServiceVersion;

    @GetMapping("getRoutes")
    public Routes getRoutes(@RequestBody RouteRequest routeRequest) throws InterruptedException {
        // Given a start and destination return a graph representation of the geographical data to be used by algorithms
        Graph graph = geographicalDataServiceVersion.getGraph(routeRequest);
        return null;
    }
}
