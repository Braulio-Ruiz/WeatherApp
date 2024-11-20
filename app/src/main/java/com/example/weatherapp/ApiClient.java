package com.example.weatherapp;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//Clase ApiClient: Esta clase configura y proporciona una instancia singleton de 
//Retrofit para realizar solicitudes HTTP a la API del clima.
public class ApiClient {

    // URL base para las solicitudes a la API.
    private static final String BASE_URL = "https://api.weatherapi.com/v1/";

    // Instancia estática de Retrofit para reutilización.
    private static Retrofit retrofit;

    // Obtiene una instancia singleton de Retrofit configurada con la URL base, un
    // cliente HTTP,
    // y un convertidor JSON (Gson).
    // @return Retrofit: Instancia configurada para realizar solicitudes a la API.
    public static Retrofit getClient() {
        // Comprueba si la instancia de Retrofit ya existe.
        if (retrofit == null) {
            // Configuración del interceptor de logging para depuración.
            // Este interceptor registra las solicitudes y respuestas HTTP en el logcat.
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            // Configuración del cliente HTTP con el interceptor de logging.
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor) // Añade el interceptor al cliente.
                    .build();

            // Configuración de Retrofit.
            // - Define la URL base para todas las solicitudes.
            // - Asocia el cliente HTTP configurado.
            // - Añade un convertidor de JSON (Gson) para mapear respuestas a objetos de
            // Java.
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL) // URL base para las llamadas a la API.
                    .client(client) // Asocia el cliente HTTP configurado.
                    .addConverterFactory(GsonConverterFactory.create()) // Conversión JSON <-> Objetos Java.
                    .build();
        }
        return retrofit; // Devuelve la instancia configurada de Retrofit.
    }
}
