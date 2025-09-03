package com.abraar.pathfinding.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NominatimAddress {

    @JsonProperty("city")
    private String city;

    @JsonProperty("town")
    private String town;

    @JsonProperty("village")
    private String village;

    // The entire "address" object in the JSON
    @JsonProperty("address")
    private NominatimAddressDetails address;


    public String getCity() {
        if (address != null) {
            return address.getCity() != null ? address.getCity() : address.getTown() != null ? address.getTown() : address.getVillage();
        }
        return null;
    }

    public void setCity(String city) {
        this.city = city;
    }
    
    // Nested class to handle the "address" part of the JSON response
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class NominatimAddressDetails {

        @JsonProperty("city")
        private String city;

        @JsonProperty("town")
        private String town;

        @JsonProperty("village")
        private String village;

        // Getters and setters
        // ...

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getTown() {
            return town;
        }

        public void setTown(String town) {
            this.town = town;
        }

        public String getVillage() {
            return village;
        }

        public void setVillage(String village) {
            this.village = village;
        }
    }

    public NominatimAddressDetails getAddress() {
        return address;
    }

    public void setAddress(NominatimAddressDetails address) {
        this.address = address;
    }
}