package com.abraar.pathfinding.service.geographicalData;

import com.abraar.pathfinding.dto.NominatimAddress;
import org.springframework.beans.factory.annotation.Autowired;
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
            NominatimAddress response = restTemplate.getForObject(url, NominatimAddress.class);

            if (response != null && response.getAddress() != null) {
                // Get the city, town, or village from the nested address object
                return response.getCity();
            }
        } catch (Exception e) {
            // Handle exceptions (e.g., API is down, network issues, bad coordinates)
            System.err.println("Error calling Nominatim API: " + e.getMessage());
        }

        return null; // Return null if the city could not be found
    }
}