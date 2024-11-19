package com.example.weatherapp;

public class City {

    private String name;
    private double latitude;
    private double longitude;
    private int weatherIcon;

    public City(String name, double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.weatherIcon = R.drawable.default_weather;
    }

    // Constructor adicional
    public City(String name, double latitude, double longitude, int weatherIcon) {
        this(name, latitude, longitude);
        this.weatherIcon = weatherIcon;
    }

    public String getName() {
        return name;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public int getWeatherIcon() {
        return weatherIcon;
    }

    public void setWeatherIcon(int weatherIcon) {
        this.weatherIcon = weatherIcon;
    }

    @Override
    public String toString() {
        return name + " (" + latitude + ", " + longitude + ")";
    }
}