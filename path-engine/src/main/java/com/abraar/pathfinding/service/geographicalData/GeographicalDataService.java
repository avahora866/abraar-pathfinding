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

        double south= 52.334702;
        double west= 52.334702;
        double north= 52.334702;
        double east= 52.334702;
        String query = """
                [out:json];
                (
                  node(%s, %s, %s, %s);
                );
                out body;
                """.formatted(south, west, north, east);

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
                        while (parser.nextToken() != JsonToken.END_OBJECT) {
                            String fieldName = parser.getCurrentName();
                            parser.nextToken();
                            if ("id".equals(fieldName)) {
                                long id = parser.getLongValue();
                                System.out.println("Node ID: " + id);
                            } else {
                                parser.skipChildren(); // Skip other fields
                            }
                        }
                    }
                }
            }
        }
    }
}
