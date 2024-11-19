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
import java.util.HashMap;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import android.widget.ImageView;

// MyAdapter: Adaptador para manejar y mostrar la lista de ciudades con sus temperaturas.
public class MyAdapter extends BaseAdapter {

    private final Context context;
    private final ArrayList<City> cities;
    private final OkHttpClient client;
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

    private static class ViewHolder {
        TextView cityName;
        TextView cityTemperature;
        ImageView weatherIcon;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
            holder = new ViewHolder();
            holder.cityName = convertView.findViewById(R.id.city_name);
            holder.cityTemperature = convertView.findViewById(R.id.city_temperature);
            holder.weatherIcon = convertView.findViewById(R.id.weather_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        City city = cities.get(position);
        holder.cityName.setText(city.getName());

        // Solicita los datos climáticos
        fetchWeatherData(city, holder.cityTemperature, holder.weatherIcon);

        return convertView;
    }

    private HashMap<String, String> weatherCache = new HashMap<>();

    // Solicita la temperatura de la ciudad usando la API de OpenWeather.
    // @param city Ciudad para obtener datos. @param cityTemperature TextView para
    // mostrar la temperatura.
    private void fetchWeatherData(City city, TextView cityTemperature, ImageView weatherIcon) {
        if (weatherCache.containsKey(city.getName())) {
            String cachedData = weatherCache.get(city.getName());
            cityTemperature.setText(cachedData);
            return;
        }

        String url = "https://api.openweathermap.org/data/2.5/weather?lat=" + city.getLatitude() +
                "&lon=" + city.getLongitude() + "&appid=" + API_KEY + "&units=metric" + "&lang=es";

        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ((ListActivity) context).runOnUiThread(() -> Toast.makeText(context,
                        "Error al cargar datos para " + city.getName(), Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    try {
                        JSONObject json = new JSONObject(responseData);
                        JSONObject main = json.getJSONObject("main");
                        double temperature = main.getDouble("temp");
                        String weatherDescription = json.getJSONArray("weather").getJSONObject(0).getString("description");
                        int weatherIconRes = getWeatherIcon(weatherDescription);

                        ((ListActivity) context).runOnUiThread(() -> {
                            cityTemperature.setText(String.format("%.1f°C", temperature));
                            weatherIcon.setImageResource(weatherIconRes);
                        });

                    } catch (JSONException e) {
                        ((ListActivity) context).runOnUiThread(() -> Toast.makeText(context,
                                "Error al procesar datos para " + city.getName(), Toast.LENGTH_SHORT).show());
                    }
                } else {
                    ((ListActivity) context).runOnUiThread(() -> Toast.makeText(context,
                            "Error en la respuesta de la API: " + response.message(), Toast.LENGTH_SHORT).show());
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