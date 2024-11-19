package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class ForecastActivity extends AppCompatActivity {

    private TextView forecastDescription;
    private ImageView weatherIcon;

    private final String API_KEY = "221ca3aedf3d0701ec5a7bf2b75a7efd";
    private final double LATITUDE = -33.4489; // Latitud de Santiago
    private final double LONGITUDE = -70.6693; // Longitud de Santiago

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);

        forecastDescription = findViewById(R.id.forecast_description);
        weatherIcon = findViewById(R.id.weather_image);

        // Mostrar ícono de carga por defecto mientras se obtienen los datos
        weatherIcon.setImageResource(R.drawable.default_weather);

        // Llamada para obtener el pronóstico del clima
        getWeatherData(LATITUDE, LONGITUDE);
    }

    private void getWeatherData(double lat, double lon) {
        String url = "https://api.openweathermap.org/data/2.5/weather?lat=" + lat
                + "&lon=" + lon + "&appid=" + API_KEY
                + "&units=metric" + "&lang=es";

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> Toast.makeText(ForecastActivity.this,
                        "Error: No se pudo conectar con el servidor.",
                        Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        JSONObject json = new JSONObject(responseData);

                        // Validación y extracción de datos
                        String weatherDescription = json.getJSONArray("weather")
                                .getJSONObject(0)
                                .optString("description", "Sin descripción");
                        double temperature = json.getJSONObject("main").optDouble("temp", Double.NaN);
                        int weatherIconRes = getWeatherIcon(weatherDescription);

                        // Actualizar la interfaz en el hilo principal
                        runOnUiThread(() -> {
                            forecastDescription.setText(String.format("Clima: %s\nTemperatura: %.1f°C",
                                    weatherDescription, temperature));
                            weatherIcon.setImageResource(weatherIconRes);

                            // Animación del ícono una vez cargado
                            Animation fadeIn = AnimationUtils.loadAnimation(ForecastActivity.this, R.anim.fade_in);
                            weatherIcon.startAnimation(fadeIn);
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                        runOnUiThread(() -> Toast.makeText(ForecastActivity.this,
                                "Error al procesar los datos.",
                                Toast.LENGTH_SHORT).show());
                    }
                } else {
                    runOnUiThread(() -> Toast.makeText(ForecastActivity.this,
                            "Error: Respuesta no válida del servidor.",
                            Toast.LENGTH_SHORT).show());
                }
            }
        });
    }

    private int getWeatherIcon(String weatherDescription) {
        weatherDescription = weatherDescription.toLowerCase();

        if (weatherDescription.contains("claro"))
            return R.drawable.sun;
        if (weatherDescription.contains("nubes"))
            return R.drawable.cloudy;
        if (weatherDescription.contains("lluvia"))
            return R.drawable.rain;
        if (weatherDescription.contains("tormenta"))
            return R.drawable.thunderstorm;
        if (weatherDescription.contains("llovizna"))
            return R.drawable.drizzle;
        if (weatherDescription.contains("nieve"))
            return R.drawable.snow;
        if (weatherDescription.contains("neblina"))
            return R.drawable.mist;

        return R.drawable.default_weather;
    }
}
