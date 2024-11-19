package com.example.weatherapp;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * ApiClient: Configura y proporciona una instancia de Retrofit para interactuar
 * con la API.
 */
public class ApiClient {

    private static final String BASE_URL = "https://api.weatherapi.com/v1/";
    private static Retrofit retrofit;

    /**
     * Retorna una instancia singleton de Retrofit.
     * 
     * @return Retrofit - Cliente de Retrofit configurado.
     */
    public static Retrofit getClient() {
        if (retrofit == null) {
            // Configuración de un cliente HTTP con logging
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor)
                    .build();

            // Inicialización de Retrofit
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client) // Usa el cliente HTTP configurado
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
