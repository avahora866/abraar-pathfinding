package com.abraar.pathfinding.service.geographicalData;

import com.abraar.pathfinding.controller.RouteRequest;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Service
public class GeographicalDataService {
    private static final String OVERPASS_ENDPOINT = "https://overpass-api.de/api/interpreter";

    public void getGraph(RouteRequest routeRequest) {
        // Call overpass API to return a json representation of the geographical data
        RestTemplate restTemplate = new RestTemplate();

        ResponseExtractor<Void> responseExtractor = response -> {
            try (InputStream inputStream = response.getBody()) {
                processStreamedJson(inputStream);
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
        double longitutdeDifference = Math.abs(startLong - destinationLong);
        double north = startLat < destinationLat ? startLat + latitudeDifference : destinationLat + latitudeDifference;
        double south = startLat < destinationLat ? startLat - latitudeDifference : destinationLat - latitudeDifference;
        double east = startLong < destinationLong ? startLong + longitutdeDifference : destinationLong + longitutdeDifference;
        double west = startLong < destinationLong ? startLong - longitutdeDifference : destinationLong - longitutdeDifference;
//        String query = """
//                [out:json];
//                (
//                  node(%s, %s, %s, %s);
//                );
//                out body;
//                """.formatted(south, west, north, east);
        // temporary for dev purposes
        String query = """
                [out:json];
                (
                  node(52.486, -1.905, 52.487, -1.904);
                );
                out body;
                """;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED); // required by Overpass API
        String requestBody = "data=" + query;
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        RequestCallback requestCallback = request -> {
            request.getHeaders().setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            request.getBody().write(requestBody.getBytes(StandardCharsets.UTF_8));
        };
        restTemplate.execute(OVERPASS_ENDPOINT, HttpMethod.POST, requestCallback, responseExtractor);

        // Convert received json into a .pbf file (Potentially cache to improve performance)

        // Convert created .pbd file into a graph structure
    }


    private static void processStreamedJson(InputStream inputStream) throws Exception {
        JsonFactory jsonFactory = new JsonFactory();
        try (JsonParser parser = jsonFactory.createParser(inputStream)) {
            while (!parser.isClosed()) {
                JsonToken token = parser.nextToken();
                if (token == JsonToken.FIELD_NAME && "elements".equals(parser.getCurrentName())) {
                    parser.nextToken(); // Move to START_ARRAY
                    while (parser.nextToken() != JsonToken.END_ARRAY) {
                        // Process each element object one by one
                        System.out.println("Next array object => " + parser.currentToken());
                        while (parser.nextToken() != JsonToken.END_OBJECT) {
                            String fieldName = parser.getCurrentName();
                            System.out.println("Next: " + fieldName);
                            if (fieldName != null) {
                                switch (fieldName) {
                                    case "type":
                                        System.out.println("Node Type: " + parser.getText());
                                        break;
                                    case "id":
                                        if (parser.getCurrentToken().isNumeric())
                                        {
                                            System.out.println("Node Id: " + parser.getLongValue());
                                        }
                                        else
                                        {
                                            System.out.println("Node id not numeric?");
                                        }
                                        break;
                                    case "lat":
                                        if (parser.getCurrentToken().isNumeric())
                                        {
                                            System.out.println("Node Latitude: " + parser.getDoubleValue());
                                        }
                                        else
                                        {
                                            System.out.println("Node Latitude not numeric?");
                                        }
                                        break;
                                    case "lon":
                                        if (parser.getCurrentToken().isNumeric())
                                        {
                                            System.out.println("Node Longitude: " + parser.getDoubleValue());
                                        }
                                        else
                                        {
                                            System.out.println("Node Longitude not numeric?");
                                        }
                                        break;
                                    case "tags":
                                        System.out.println("Node has tags");
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
                    }
                }
            }
        }
    }
}
