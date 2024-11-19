package com.example.weatherapp;

import com.google.gson.annotations.SerializedName;

/**
 * Representa la respuesta JSON de la API de clima.
 */
public class WeatherResponse {

    @SerializedName("location")
    private Location location;

    @SerializedName("current")
    private Current current;

    /**
     * Devuelve una descripción del clima.
     * Valida nulos para evitar errores.
     *
     * @return Descripción formateada del clima.
     */
    public String getWeatherDescription() {
        String locationName = (location != null && location.getName() != null) ? location.getName() : "Desconocida";
        String conditionText = (current != null && current.getCondition() != null) ? current.getCondition().getText()
                : "No disponible";
        String temperature = (current != null) ? String.format("%.1f°C", current.getTempC()) : "No disponible";

        return "Ubicación: " + locationName + ", Clima: " + conditionText + ", Temperatura: " + temperature;
    }

    /**
     * Representa la ubicación en la respuesta JSON.
     */
    public class Location {
        private String name;

        public String getName() {
            return name;
        }
    }

    /**
     * Representa los datos actuales del clima en la respuesta JSON.
     */
    public class Current {
        @SerializedName("temp_c")
        private double tempC;

        @SerializedName("condition")
        private Condition condition;

        public double getTempC() {
            return tempC;
        }

        public Condition getCondition() {
            return condition;
        }
    }

    /**
     * Representa la condición del clima en la respuesta JSON.
     */
    public class Condition {
        private String text;

        public String getText() {
            return text;
        }
    }
}
