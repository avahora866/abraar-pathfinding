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

    @Value("${file}")
    private String filePath;

    public Graph getGraph(RouteRequest routeRequest) {
        double[] start = routeRequest.getStart();
        double[] destination = routeRequest.getDestination();
        String city = geocodingService.getCityFromCoordinates(start[0], start[1]);
        if (city.equals(geocodingService.getCityFromCoordinates(destination[0], destination[1])))
        {
            throw new IllegalArgumentException("Start and Destination must be in the same city");
        }

        pbfFileHandler.downloadFile(lookup(city));

        Graph graph = new Graph();
        File pbfFile = new File(filePath);
        OsmosisReader pbfReader = new OsmosisReader(pbfFile);

        GraphBuilderSink graphBuilderSink = new GraphBuilderSink(graph);

        pbfReader.setSink(graphBuilderSink);
        pbfReader.run();

        pbfFileHandler.removeFile();
        return graph;

    }
}
