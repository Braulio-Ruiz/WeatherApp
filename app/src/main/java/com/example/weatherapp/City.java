package com.example.weatherapp;

// Clase City: Representa una ciudad con nombre, coordenadas geográficas y un ícono asociado al clima.
//Incluye validación de coordenadas y permite la representación textual de sus atos.
public class City {

    private final String name; // Nombre de la ciudad.
    private final double latitude; // Latitud geográfica de la ciudad.
    private final double longitude; // Longitud geográfica de la ciudad.
    private int weatherIcon; // ID del recurso drawable asociado al ícono del clima.

    // Constructor principal de la clase. Crea una instancia de `City` con el
    // nombre, latitud y longitud especificados.
    // Por defecto, asigna un ícono genérico al clima.
    // @param name: Nombre de la ciudad.
    // @param latitude: Latitud de la ciudad (debe estar entre -90 y 90 grados).
    // @param longitude: Longitud de la ciudad (debe estar entre -180 y 180 grados).
    // @throws IllegalArgumentException: Si las coordenadas son inválidas.
    public City(String name, double latitude, double longitude) {
        if (!isValidLatitude(latitude) || !isValidLongitude(longitude)) {
            throw new IllegalArgumentException("Coordenadas inválidas."); // Valida las coordenadas.
        }
        this.name = name; // Asigna el nombre de la ciudad.
        this.latitude = latitude; // Asigna la latitud de la ciudad.
        this.longitude = longitude; // Asigna la longitud de la ciudad.
        this.weatherIcon = R.drawable.default_weather; // Ícono por defecto para la ciudad.
    }

    // Constructor adicional de la clase.
    // Permite crear una instancia de `City` especificando un ícono de clima
    // personalizado.
    // @param name: Nombre de la ciudad.
    // @param latitude: Latitud de la ciudad (debe estar entre -90 y 90 grados).
    // @param longitude: Longitud de la ciudad (debe estar entre -180 y 180 grados).
    // @param weatherIcon: ID del recurso drawable que representa el ícono del
    // clima.
    public City(String name, double latitude, double longitude, int weatherIcon) {
        this(name, latitude, longitude); // Llama al constructor principal para inicializar los datos básicos.
        this.weatherIcon = weatherIcon; // Asigna el ícono de clima personalizado.
    }

    // Obtiene el nombre de la ciudad.
    // @return: Nombre de la ciudad como cadena de texto.
    public String getName() {
        return name;
    }

    // Obtiene la latitud de la ciudad.
    // @return: Latitud de la ciudad como un valor de tipo double.
    public double getLatitude() {
        return latitude;
    }

    // Obtiene la longitud de la ciudad.
    // @return: Longitud de la ciudad como un valor de tipo double.
    public double getLongitude() {
        return longitude;
    }

    // Obtiene el ícono del clima asociado a la ciudad.
    // @return: ID del recurso drawable del ícono del clima.
    public int getWeatherIcon() {
        return weatherIcon;
    }

    // Actualiza el ícono del clima asociado a la ciudad.
    // @param weatherIcon: ID del recurso drawable del nuevo ícono del clima.
    public void setWeatherIcon(int weatherIcon) {
        this.weatherIcon = weatherIcon;
    }

    // Representación textual de la ciudad.
    // Devuelve una cadena en formato: "Nombre (Latitud, Longitud)".
    // @return: Cadena representativa de la ciudad.
    @Override
    public String toString() {
        return String.format("%s (%.2f, %.2f)", name, latitude, longitude);
    }

    // Valida si una latitud está dentro de los rangos permitidos.
    // @param latitude: Latitud a validar.
    // @return `true`: si la latitud está entre -90 y 90 grados; de lo contrario,
    // `false`.
    private boolean isValidLatitude(double latitude) {
        return latitude >= -90 && latitude <= 90;
    }

    // Valida si una longitud está dentro de los rangos permitidos.
    // @param longitude: Longitud a validar.
    // @return `true`: si la longitud está entre -180 y 180 grados; de lo
    // contrario,`false`.
    private boolean isValidLongitude(double longitude) {
        return longitude >= -180 && longitude <= 180;
    }
}
