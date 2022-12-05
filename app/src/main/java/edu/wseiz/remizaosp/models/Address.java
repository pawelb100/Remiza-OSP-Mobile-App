package edu.wseiz.remizaosp.models;

public class Address {

    private String street;
    private String region;

    public Address(String street, String region) {
        this.street = street;
        this.region = region;
    }

    public Address() {}

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getStreetAndRegion() {
        return street + ", " + region;
    }


}
