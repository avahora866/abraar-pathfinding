package com.abraar.pathfinding.controller;

import com.abraar.pathfinding.service.geographicalData.GeographicalDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class RoutingController {

    @Autowired
    private GeographicalDataService geographicalDataService;

    @GetMapping("getRoutes")
    public Routes getRoutes(@RequestBody RouteRequest routeRequest)
    {
        // Given a start and destination return a graph represnetation of the geographical data to be used by algorithms
        geographicalDataService.getGraph(routeRequest);
        return null;
    }
}
