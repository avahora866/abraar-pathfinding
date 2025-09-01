package com.abraar.pathfinding.service.geographicalData;

import com.abraar.pathfinding.controller.RouteRequest;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class GeographicalDataService {
    private static final String OVERPASS_ENDPOINT = "https://overpass-api.de/api/interpreter";

    public void getGraph(RouteRequest routeRequest) throws InterruptedException {
        // Call overpass API to return a json representation of the geographical data
        RestTemplate restTemplate = new RestTemplate();
        Graph graph = new Graph(new HashMap<>(), new HashMap<>());

        ResponseExtractor<Void> responseExtractor = response -> {
            try (InputStream inputStream = response.getBody()) {
                processStreamedJson(inputStream, graph);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return null;
        };

        HttpEntity<String> requestEntity = getHttpEntity(routeRequest, graph);
        RequestCallback requestCallback = getRequestCallback(requestEntity.getBody());
        query(restTemplate, requestCallback, responseExtractor);
        graph.printGraph();

        // Convert received json into a .pbf file (Potentially cache to improve performance)
        System.out.println("Converting received json into .pbf file");
        // Convert created .pbd file into a graph structure
    }

    private HttpEntity<String> getHttpEntity(RouteRequest routeRequest, Graph graph) {
        ResponseExtractor<Void> responseExtractor = response -> {
            try (InputStream inputStream = response.getBody()) {
                processStreamedJson(inputStream, graph);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return null;
        };
        double startLat = routeRequest.getStart()[0];
        double destinationLat = routeRequest.getDestination()[0];
        double latitudeDifference = Math.abs(startLat - destinationLat);
        double startLong = routeRequest.getStart()[1];
        double destinationLong = routeRequest.getDestination()[1];
        double longitudeDifference = Math.abs(startLong - destinationLong);
        double north = startLat < destinationLat ? startLat + latitudeDifference : destinationLat + latitudeDifference;
        double south = startLat < destinationLat ? startLat - latitudeDifference : destinationLat - latitudeDifference;
        double east = startLong < destinationLong ? startLong + longitudeDifference : destinationLong + longitudeDifference;
        double west = startLong < destinationLong ? startLong - longitudeDifference : destinationLong - longitudeDifference;
        String query = """
                [out:json];
                (
                  way(%s, %s, %s, %s)["highway"];
                );
                out body;
                >;
                out skel qt;
                """.formatted(south, west, north, east);
        // temporary for dev purposes
//        String query = """
//                [out:json];
//                (
//                  node(52.486, -1.905, 52.487, -1.904);
//                );
//                out body;
//                """;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED); // required by Overpass API
        String requestBody = "data=" + query;
        return new HttpEntity<>(requestBody, headers);
    }

    private static RequestCallback getRequestCallback(String requestBody) {
        return request -> {
            request.getHeaders().setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            request.getBody().write(requestBody.getBytes(StandardCharsets.UTF_8));
        };
    }

    private static void query(RestTemplate restTemplate, RequestCallback requestCallback, ResponseExtractor<Void> responseExtractor) throws InterruptedException {
        try {
            restTemplate.execute(OVERPASS_ENDPOINT, HttpMethod.POST, requestCallback, responseExtractor);
        } catch (HttpServerErrorException exception) {
            System.out.println("Failed due to timeout. Waiting for 5 seconds and trying again");
            Thread.sleep(5000);
            restTemplate.execute(OVERPASS_ENDPOINT, HttpMethod.POST, requestCallback, responseExtractor);
        }
    }


    private static void processStreamedJson(InputStream inputStream, Graph graph) throws Exception {
        JsonFactory jsonFactory = new JsonFactory();
        try (JsonParser parser = jsonFactory.createParser(inputStream)) {
            while (!parser.isClosed()) {
                JsonToken token = parser.nextToken();
                if (token == JsonToken.FIELD_NAME && "elements".equals(parser.getCurrentName())) {
                    parser.nextToken(); // Move to START_ARRAY
                    while (parser.nextToken() != JsonToken.END_ARRAY) {
                        // Process each element object one by one
                        System.out.println("Next array object => " + parser.currentToken());
                        Node node = null;
                        while (parser.nextToken() != JsonToken.END_OBJECT) {
                            String fieldName = parser.getCurrentName();
                            System.out.println("FieldName: " + fieldName);
                            System.out.println("Token: " + parser.getCurrentToken());
                            if (fieldName != null) {
                                switch (fieldName) {
                                    case "type":
                                        System.out.println("Node Type: " + parser.getText());
                                        break;
                                    case "id":
                                        if (parser.getCurrentToken().isNumeric()) {
                                            System.out.println("Node Id: " + parser.getLongValue());
                                            node = graph.addVertex(parser.getLongValue());
                                        } else {
                                            System.out.println("Node id not numeric?");
                                        }
                                        break;
                                    case "lat":
                                        if (parser.getCurrentToken().isNumeric()) {
                                            System.out.println("Node Latitude: " + parser.getDoubleValue());
                                            if (node != null)
                                            {
                                                node.setLat(parser.getDoubleValue());
                                            }
                                            else
                                            {
                                                System.out.println("Could not fine node for Latitutde");
                                            }
                                        } else {
                                            System.out.println("Node Latitude not numeric?");
                                        }
                                        break;
                                    case "lon":
                                        if (parser.getCurrentToken().isNumeric()) {
                                            System.out.println("Node Longitude: " + parser.getDoubleValue());
                                            if (node != null)
                                            {
                                                node.setLon(parser.getDoubleValue());
                                            }
                                            else
                                            {
                                                System.out.println("Could not fine node for Longitutde");
                                            }
                                        } else {
                                            System.out.println("Node Longitude not numeric?");
                                        }
                                        break;
                                    case "tags":
                                        System.out.println("Node has tags");
                                        break;
                                    case "nodes":
                                        System.out.println("Node has members");
                                        populateMembers(parser, graph);
                                        break;
                                    default:
                                        System.out.println("Node not matching type: " + parser.getText());
                                        break;
                                }
                            }
                            System.out.println("---------");
//                            if ("id".equals(fieldName)) {
//                                long id = parser.getLongValue();
//                                System.out.println("Node ID: " + id);
//                            } else {
//                                parser.skipChildren(); // Skip other fields
//                            }
                        }
                        graph.addVertex(node);
                    }
                }
            }
        }
    }

    private static void populateMembers(JsonParser parser, Graph graph) throws IOException {
        while(parser.getCurrentToken() != JsonToken.START_ARRAY)
        {
            parser.nextToken();
        }

        List<Long> nodeIds = new ArrayList<>();
        while (parser.nextToken() != JsonToken.END_ARRAY)
        {
            long nodeId = parser.getLongValue();
            graph.addVertex(nodeId);
            nodeIds.add(nodeId);
        }
        graph.addEdges(nodeIds);
    }
}
