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

// ForecastActivity: Muestra el pronóstico del clima de una ubicación específica (Santiago).
public class ForecastActivity extends AppCompatActivity {

    // Elementos de la interfaz gráfica
    private TextView forecastDescription; // Muestra la descripción del clima y la temperatura.
    private ImageView weatherIcon; // Muestra el ícono asociado al estado del clima.

    // Constantes para la API de OpenWeather
    private final String API_KEY = "221ca3aedf3d0701ec5a7bf2b75a7efd"; // Clave de la API.
    private final double LATITUDE = -33.4489; // Latitud de Santiago, Chile.
    private final double LONGITUDE = -70.6693; // Longitud de Santiago, Chile.

    /// Método `onCreate`: Configura la actividad al ser creada.
    // Inicializa las vistas y realiza la solicitud para obtener datos del clima.
    // @param savedInstanceState: Estado previo de la actividad (si aplica).
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast); // Asocia la actividad con el diseño XML `activity_forecast`.

        // Vinculación de vistas desde el diseño XML.
        forecastDescription = findViewById(R.id.forecast_description); // Descripción del clima.
        weatherIcon = findViewById(R.id.weather_image); // Ícono del clima.

        // Configura un ícono de carga predeterminado mientras se obtienen los datos.
        weatherIcon.setImageResource(R.drawable.default_weather);

        // Llama al método para obtener el pronóstico del clima usando las coordenadas
        // especificadas.
        getWeatherData(LATITUDE, LONGITUDE);
    }

    // Realiza una solicitud a la API de OpenWeather para obtener los datos
    // climáticos de una ubicación.
    // @param lat: Latitud de la ubicación.
    // @param lon: Longitud de la ubicación.
    private void getWeatherData(double lat, double lon) {
        // Construye la URL de la solicitud con los parámetros necesarios.
        String url = "https://api.openweathermap.org/data/2.5/weather?lat=" + lat
                + "&lon=" + lon + "&appid=" + API_KEY
                + "&units=metric" + "&lang=es"; // Unidades métricas y lenguaje español.

        // Cliente HTTP para realizar la solicitud.
        OkHttpClient client = new OkHttpClient();

        // Crea una solicitud GET con la URL generada.
        Request request = new Request.Builder().url(url).build();

        // Encola la solicitud de forma asíncrona.
        client.newCall(request).enqueue(new Callback() {

            // Maneja los errores de la solicitud HTTP.
            // @param call: Objeto que representa la solicitud fallida.
            // @param e: Excepción que describe el error ocurrido.
            @Override
            public void onFailure(Call call, IOException e) {
                // Muestra un mensaje al usuario indicando el error.
                runOnUiThread(() -> Toast.makeText(ForecastActivity.this,
                        "Error: No se pudo conectar con el servidor.",
                        Toast.LENGTH_SHORT).show());
            }

            // Maneja la respuesta exitosa o fallida del servidor.
            // @param call: Objeto que representa la solicitud.
            // @param response: Respuesta del servidor.
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        // Extrae los datos de la respuesta como una cadena JSON.
                        String responseData = response.body().string();
                        JSONObject json = new JSONObject(responseData);

                        // Extrae la descripción del clima y la temperatura del JSON.
                        String weatherDescription = json.getJSONArray("weather")
                                .getJSONObject(0)
                                .optString("description", "Sin descripción");
                        double temperature = json.getJSONObject("main").optDouble("temp", Double.NaN);
                        int weatherIconRes = getWeatherIcon(weatherDescription); // Obtiene el ícono correspondiente.

                        // Actualiza la interfaz gráfica en el hilo principal.
                        runOnUiThread(() -> {
                            // Convierte la primera letra a mayúscula y el resto a minúscula.
                            String formattedDescription = weatherDescription.substring(0, 1).toUpperCase()
                                    + weatherDescription.substring(1).toLowerCase();

                            forecastDescription.setText(String.format("Clima: %s\nTemperatura: %.1f°C",
                                    formattedDescription, temperature)); // Actualiza el texto.
                            weatherIcon.setImageResource(weatherIconRes); // Cambia el ícono del clima.

                            // Aplica una animación de "fade in" al ícono del clima.
                            Animation fadeIn = AnimationUtils.loadAnimation(ForecastActivity.this, R.anim.fade_in);
                            weatherIcon.startAnimation(fadeIn);
                        });

                    } catch (JSONException e) {
                        // Maneja errores al procesar el JSON.
                        e.printStackTrace();
                        runOnUiThread(() -> Toast.makeText(ForecastActivity.this,
                                "Error al procesar los datos.",
                                Toast.LENGTH_SHORT).show());
                    }
                } else {
                    // Maneja respuestas no exitosas del servidor.
                    runOnUiThread(() -> Toast.makeText(ForecastActivity.this,
                            "Error: Respuesta no válida del servidor.",
                            Toast.LENGTH_SHORT).show());
                }
            }
        });
    }

    // Obtiene el recurso de imagen correspondiente según la descripción del clima.
    // @param weatherDescription: Descripción del clima en texto.
    // @return: ID del recurso drawable que representa el estado del clima.
    private int getWeatherIcon(String weatherDescription) {
        // Convierte la descripción a minúsculas para una comparación más robusta.
        weatherDescription = weatherDescription.toLowerCase();

        // Compara la descripción con palabras clave y retorna el ícono correspondiente.
        if (weatherDescription.contains("claro"))
            return R.drawable.sun;
        if (weatherDescription.contains("nubes"))
            return R.drawable.clouds;
        if (weatherDescription.contains("nuboso"))
            return R.drawable.cloudy;
        if (weatherDescription.contains("llovizna"))
            return R.drawable.drizzle;
        if (weatherDescription.contains("lluvia"))
            return R.drawable.rain;
        if (weatherDescription.contains("tormenta"))
            return R.drawable.thunderstorm;
        if (weatherDescription.contains("nieve"))
            return R.drawable.snow;
        if (weatherDescription.contains("nieblina"))
            return R.drawable.mist;

        // Retorna un ícono predeterminado si no hay coincidencias.
        return R.drawable.default_weather;
    }
}
