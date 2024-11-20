package com.example.weatherapp;

import com.google.gson.annotations.SerializedName;

//Clase `WeatherResponse`: Representa la estructura de la respuesta JSON obtenida de la API de clima.
//Esta clase utiliza Gson para mapear automáticamente las claves del JSON a las propiedades de los objetos de Java.
public class WeatherResponse {

    // Mapea la clave "location" del JSON a esta propiedad.
    @SerializedName("location")
    private Location location;

    // Mapea la clave "current" del JSON a esta propiedad.
    @SerializedName("current")
    private Current current;

    // Método `getWeatherDescription`: Genera una descripción formateada del clima
    // con datos de ubicación, condición
    // climática, y temperatura. Valida nulos para evitar errores en caso de que
    // alguna clave del JSON esté ausente.
    // @return Una cadena que describe el clima en formato:
    // "Ubicación: [nombre], Clima: [condición], Temperatura: [valor]".
    public String getWeatherDescription() {
        // Obtiene el nombre de la ubicación, o usa "Desconocida" si no está disponible.
        String locationName = (location != null && location.getName() != null) ? location.getName() : "Desconocida";

        // Obtiene la descripción de la condición climática, o usa "No disponible" si no
        // está disponible.
        String conditionText = (current != null && current.getCondition() != null)
                ? current.getCondition().getText()
                : "No disponible";

        // Obtiene la temperatura en grados Celsius, o usa "No disponible" si no está
        // disponible.
        String temperature = (current != null)
                ? String.format("%.1f°C", current.getTempC())
                : "No disponible";

        // Combina los datos para formar la descripción completa.
        return "Ubicación: " + locationName + ", Clima: " + conditionText + ", Temperatura: " + temperature;
    }

    // Clase interna `Location`: Representa los datos de ubicación incluidos en la
    // respuesta JSON bajo la clave "location".
    public class Location {
        // El nombre de la ubicación (ciudad o lugar), mapeado desde el JSON.
        private String name;

        // Método `getName`: Devuelve el nombre de la ubicación.
        // @return: El nombre de la ubicación o `null` si no está definido.
        public String getName() {
            return name;
        }
    }

    // Clase interna `Current`: Representa los datos del clima actual incluidos en
    // la respuesta JSON bajo la clave "current".
    public class Current {
        // La temperatura actual en grados Celsius, mapeada desde "temp_c" en el JSON.
        @SerializedName("temp_c")
        private double tempC;

        // Objeto que representa la condición climática actual, mapeado desde
        // "condition" en el JSON.
        @SerializedName("condition")
        private Condition condition;

        // Método `getTempC`: Devuelve la temperatura actual en grados Celsius.
        // @return La temperatura como un valor decimal.
        public double getTempC() {
            return tempC;
        }

        // Método `getCondition`: Devuelve el objeto `Condition` que describe la
        // condición climática actual.
        // @return: Un objeto `Condition` o `null` si no está definido.
        public Condition getCondition() {
            return condition;
        }
    }

    // Clase interna `Condition`: Representa la descripción de la condición
    // climática incluida en la clave "condition" del JSON.
    public class Condition {
        // Texto descriptivo de la condición climática, como "Clear" o "Rainy".
        private String text;

        // Método `getText`: Devuelve el texto descriptivo de la condición climática.
        // @return Una cadena que describe la condición climática o `null` si no está
        // definido.
        public String getText() {
            return text;
        }
    }
}
