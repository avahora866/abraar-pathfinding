package com.abraar.pathfinding.service.geographicalData;

import com.abraar.pathfinding.dto.NominatimAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class GeocodingService {

    private final RestTemplate restTemplate;

    @Autowired
    public GeocodingService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getCityFromCoordinates(double latitude, double longitude) {
        // Nominatim API endpoint
        String apiUrl = "https://nominatim.openstreetmap.org/reverse";

        // Build the URL with query parameters
        String url = UriComponentsBuilder.fromHttpUrl(apiUrl)
                .queryParam("format", "json")
                .queryParam("lat", latitude)
                .queryParam("lon", longitude)
                .queryParam("zoom", 18) // High zoom level to get a precise result
                .toUriString();

        try {
            // Make the GET request and map the response to our DTO
            HttpHeaders headers = new HttpHeaders();
            // Replace with your actual app name or contact email
            headers.set("User-Agent", "AbraarPathfindingApp/1.0 (avahora1@hotmail.com)");

            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<NominatimAddress> responseEntity = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    NominatimAddress.class
            );

            NominatimAddress response = responseEntity.getBody();

            if (response != null && response.getAddress() != null) {
                return response.getCity();
            }
        } catch (Exception e) {
            // Handle exceptions (e.g., API is down, network issues, bad coordinates)
            throw new RuntimeException("Error calling Nominatim API: " + e.getMessage());
        }

        throw new NullPointerException("Could not find city");
    }
}