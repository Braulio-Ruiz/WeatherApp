package com.example.weatherapp;

public class City {

    private final String name;
    private final double latitude;
    private final double longitude;
    private int weatherIcon;

    // Constructor principal
    public City(String name, double latitude, double longitude) {
        if (!isValidLatitude(latitude) || !isValidLongitude(longitude)) {
            throw new IllegalArgumentException("Coordenadas inválidas.");
        }
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
        return String.format("%s (%.2f, %.2f)", name, latitude, longitude);
    }

    // Validación de latitud
    private boolean isValidLatitude(double latitude) {
        return latitude >= -90 && latitude <= 90;
    }

    // Validación de longitud
    private boolean isValidLongitude(double longitude) {
        return longitude >= -180 && longitude <= 180;
    }
}
