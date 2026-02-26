package com.abraar.pathfinding.service.geographicalData;

import com.abraar.pathfinding.controller.RouteRequest;
import com.abraar.pathfinding.dto.Graph;
import crosby.binary.osmosis.OsmosisReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;

import static com.abraar.pathfinding.service.geographicalData.CityLookup.lookup;

@Service
public class GeographicalDataServiceVersion {

    @Autowired
    private GeocodingService geocodingService;

    @Autowired
    private PBFFileHandler pbfFileHandler;

    @Value("${file.directory}")
    private String fileDirectory;

    private String currentCity = null;
    private File currentFile = null;
    private Graph currentGraph = null;

    public Graph getGraph(RouteRequest routeRequest) {
        double[] start = routeRequest.getStart();
        double[] destination = routeRequest.getDestination();

        String startCity = geocodingService.getCityFromCoordinates(start[0], start[1]);
        String destinationCity = geocodingService.getCityFromCoordinates(destination[0], destination[1]);

        if (!startCity.equals(destinationCity)) {
            throw new IllegalArgumentException("Start and Destination must be in the same city");
        }

        synchronized (this) {
            if (startCity.equals(currentCity) && currentFile != null && currentFile.exists() && currentGraph != null) {
                return currentGraph;
            }

            if (currentFile != null && currentFile.exists()) {
                currentFile.delete();
            }

            currentFile = new File(fileDirectory, startCity + ".pbf");
            pbfFileHandler.downloadFile(lookup(startCity), currentFile.getAbsolutePath());
            currentCity = startCity;

            currentGraph = new Graph();
            OsmosisReader pbfReader = new OsmosisReader(currentFile);
            GraphBuilderSink graphBuilderSink = new GraphBuilderSink(currentGraph);

            pbfReader.setSink(graphBuilderSink);
            pbfReader.run();

            return currentGraph;
        }
    }
}