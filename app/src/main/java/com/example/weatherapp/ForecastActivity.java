package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ForecastActivity extends AppCompatActivity {

    // Variables para los elementos de interfaz
    private TextView forecastDescription;

    // Reemplaza "{API key}" con tu clave API de OpenWeatherMap
    private final String API_KEY = "221ca3aedf3d0701ec5a7bf2b75a7efd";
    private final double LATITUDE = -33.4489; // Ejemplo: Latitud de Santiago
    private final double LONGITUDE = -70.6693; // Ejemplo: Longitud de Santiago

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);

        // Asignación de elementos de interfaz
        forecastDescription = findViewById(R.id.forecast_description);

        // Llamada a la función para obtener el pronóstico del clima
        getWeatherData(LATITUDE, LONGITUDE);
    }

    private void getWeatherData(double lat, double lon) {
        // URL de la API de OpenWeatherMap con parámetros
        String url = "https://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lon + "&appid=" + API_KEY
                + "&units=metric" + "&lang=es";

        // Configuración del cliente HTTP
        OkHttpClient client = new OkHttpClient();

        // Creación de la solicitud HTTP
        Request request = new Request.Builder()
                .url(url)
                .build();

        // Enviar la solicitud de manera asíncrona
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Manejo de error en caso de fallo en la llamada
                runOnUiThread(() -> Toast
                        .makeText(ForecastActivity.this, "Error: Datos no disponibles.", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();

                    try {
                        // Parseo de los datos de JSON recibidos
                        JSONObject json = new JSONObject(responseData);
                        JSONObject main = json.getJSONObject("main");
                        double temperature = main.getDouble("temp");
                        String weatherDescription = json.getJSONArray("weather").getJSONObject(0)
                                .getString("description");

                        // Actualización del TextView con los datos de clima
                        runOnUiThread(() -> forecastDescription
                                .setText("Clima: " + weatherDescription + "\nTemperatura: " + temperature + "°C"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    // Mensaje de error si la respuesta no fue exitosa
                    runOnUiThread(() -> Toast
                            .makeText(ForecastActivity.this, "Error: Datos no disponibles.", Toast.LENGTH_SHORT)
                            .show());
                }
            }
        });
    }
}
