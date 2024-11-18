package com.example.weatherapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import android.widget.ImageView;

// MyAdapter: Adaptador para manejar y mostrar la lista de ciudades con sus temperaturas.
public class MyAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<City> cities;
    private OkHttpClient client;
    private final String API_KEY = "221ca3aedf3d0701ec5a7bf2b75a7efd";

    // Constructor del adaptador.
    public MyAdapter(Context context, ArrayList<City> cities) {
        // @param context Contexto de la actividad.
        this.context = context;
        // @param cities Lista de ciudades a mostrar.
        this.cities = cities;
        this.client = new OkHttpClient();
    }

    @Override
    public int getCount() {
        return cities.size();
    }

    @Override
    public Object getItem(int position) {
        return cities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        }

        City city = cities.get(position);
        TextView cityName = convertView.findViewById(R.id.city_name);
        // Añade un TextView en el layout para mostrar la temperatura
        TextView cityTemperature = convertView.findViewById(R.id.city_temperature);
        ImageView weatherIcon = convertView.findViewById(R.id.weather_image);

        // Muestra el nombre de la ciudad
        cityName.setText(city.getName());
        // Realiza la solicitud a la API para obtener la temperatura
        fetchWeatherData(city, cityTemperature, weatherIcon);

        return convertView;
    }

    // Solicita la temperatura de la ciudad usando la API de OpenWeather.
    // @param city Ciudad para obtener datos. @param cityTemperature TextView para
    // mostrar la temperatura.
    private void fetchWeatherData(City city, TextView cityTemperature, ImageView weatherIcon) {
        String url = "https://api.openweathermap.org/data/2.5/weather?lat=" + city.getLatitude() +
                "&lon=" + city.getLongitude() + "&appid=" + API_KEY + "&units=metric" + "&lang=es";

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Manejo de error en la llamada a la API
                ((ListActivity) context).runOnUiThread(() -> Toast.makeText(context,
                        "Error: No se pudieron cargar los datos para " + city.getName(), Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    try {
                        JSONObject json = new JSONObject(responseData);
                        JSONObject main = json.getJSONObject("main");
                        double temperature = main.getDouble("temp");
                        String weatherDescription = json.getJSONArray("weather").getJSONObject(0)
                                .getString("description");
                        int weatherIconRes = getWeatherIcon(weatherDescription);

                        ((ListActivity) context).runOnUiThread(() -> {
                            cityTemperature.setText(String.format("%.1f°C", temperature));

                            // Aquí puedes asignar un ícono dependiendo de la descripción del clima
                            weatherIcon.setImageResource(weatherIconRes);
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    // Retorna el ícono correspondiente basado en la descripción del clima.
    // @param description Descripción del clima. @return ID del recurso de imagen.
    private int getWeatherIcon(String weatherDescription) {
        // Convertir a minúsculas para evitar errores por mayúsculas/minúsculas
        weatherDescription = weatherDescription.toLowerCase();

        if (weatherDescription.contains("claro"))
            return R.drawable.sun;
        if (weatherDescription.contains("nubes"))
            return R.drawable.cloudy;
        if (weatherDescription.contains("lluvia"))
            return R.drawable.rain;
        if (weatherDescription.contains("tormenta"))
            return R.drawable.thunderstorm;
        // Ícono por defecto
        return R.drawable.default_weather;
    }
}
