package com.abraar.pathfinding.controller;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Routes {
    private List<Route> routes = new ArrayList<>();

    @Getter
    @Setter
    public static class Route {
        private String algorithm;
        private int[][] route;
        private int timeTaken;
        private int time;
    }
}

