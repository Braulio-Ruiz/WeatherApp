package com.example.weatherapp;

import com.google.gson.annotations.SerializedName;

public class WeatherResponse {

    @SerializedName("location")
    private Location location;

    @SerializedName("current")
    private Current current;

    public String getWeatherDescription() {
        return "Ubicación: " + location.getName() + ", Clima: " + current.getCondition().getText() +
                ", Temperatura: " + current.getTempC() + "°C";
    }

    public class Location {
        private String name;

        public String getName() {
            return name;
        }
    }

    public class Current {
        private double tempC;
        private Condition condition;

        public double getTempC() {
            return tempC;
        }

        public Condition getCondition() {
            return condition;
        }
    }

    public class Condition {
        private String text;

        public String getText() {
            return text;
        }
    }
}
