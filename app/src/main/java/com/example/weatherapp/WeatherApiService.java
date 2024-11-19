package com.example.weatherapp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Interfaz `WeatherApiService`:
 * Define los métodos para interactuar con la API de clima utilizando Retrofit.
 * Cada método en esta interfaz representa una solicitud HTTP específica.
 */
public interface WeatherApiService {

    /**
     * Método `getCurrentWeather`:
     * Realiza una solicitud GET a la API para obtener el clima actual de una
     * ubicación específica.
     *
     * @param city   Este parámetro especifica la ciudad o las coordenadas de la
     *               ubicación.
     *               - Puede ser el nombre de una ciudad, como "Santiago".
     *               - También puede ser una combinación de latitud y longitud en
     *               formato
     *               "latitud,longitud" (por ejemplo, "-33.4489,-70.6693").
     *
     * @param apiKey Clave de autenticación para la API proporcionada por el
     *               servicio.
     *               - Es un parámetro obligatorio que asegura el acceso autorizado
     *               a los datos.
     *
     * @return Un objeto {@link Call} que encapsula la solicitud HTTP y la
     *         respuesta.
     *         - El tipo genérico de `Call` es {@link WeatherResponse}, que es el
     *         modelo de datos
     *         utilizado para mapear la respuesta JSON de la API al formato de Java.
     *         - Este objeto permite realizar la solicitud de forma asincrónica o
     *         sincrónica.
     *
     *         Ejemplo de URL generada por Retrofit:
     *         - Si `city` = "Santiago" y `apiKey` = "tu_api_key", se enviará una
     *         solicitud GET a:
     *         `https://api.weatherapi.com/v1/current.json?q=Santiago&key=tu_api_key`
     */
    @GET("current.json")
    Call<WeatherResponse> getCurrentWeather(@Query("q") String city, @Query("key") String apiKey);
}
