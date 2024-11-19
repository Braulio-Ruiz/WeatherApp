package com.example.weatherapp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Interfaz para interactuar con la API de clima mediante Retrofit.
 */
public interface WeatherApiService {

    /**
     * Obtiene el clima actual para una ciudad específica.
     *
     * @param city   Nombre de la ciudad o coordenadas (latitud,longitud) en formato
     *               "q".
     * @param apiKey Clave de API para autenticación.
     * @return Un objeto {@link Call} que contiene la respuesta mapeada a
     *         {@link WeatherResponse}.
     */
    @GET("current.json")
    Call<WeatherResponse> getCurrentWeather(@Query("q") String city, @Query("key") String apiKey);
}
