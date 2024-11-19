package com.example.weatherapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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

public class MyAdapter extends BaseAdapter {

    private final Context context;
    private final ArrayList<City> cities;
    private final OkHttpClient client;
    private final String API_KEY = "221ca3aedf3d0701ec5a7bf2b75a7efd";
    private final HashMap<String, WeatherData> weatherCache = new HashMap<>();

    // Constructor del adaptador.
    public MyAdapter(Context context, ArrayList<City> cities) {
        this.context = context;
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

        // Verifica si los datos ya están en caché
        String cacheKey = city.getName() + city.getLatitude() + city.getLongitude();
        if (weatherCache.containsKey(cacheKey)) {
            WeatherData cachedData = weatherCache.get(cacheKey);
            holder.cityTemperature.setText(cachedData.getTemperature());
            holder.weatherIcon.setImageResource(cachedData.getIcon());
        } else {
            // Solicita los datos climáticos
            fetchWeatherData(city, cacheKey, holder.cityTemperature, holder.weatherIcon);
        }

        return convertView;
    }

    private void fetchWeatherData(City city, String cacheKey, TextView cityTemperature, ImageView weatherIcon) {
        String url = "https://api.openweathermap.org/data/2.5/weather?lat=" + city.getLatitude() +
                "&lon=" + city.getLongitude() + "&appid=" + API_KEY + "&units=metric";

        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> Toast
                        .makeText(context, "Error al cargar datos para " + city.getName(), Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        JSONObject json = new JSONObject(responseData);
                        JSONObject main = json.optJSONObject("main");
                        String weatherDescription = json.getJSONArray("weather").getJSONObject(0)
                                .optString("description", "Desconocido");
                        double temperature = main != null ? main.optDouble("temp", 0) : 0;
                        int weatherIconRes = getWeatherIcon(weatherDescription);

                        // Guarda en caché los datos obtenidos
                        WeatherData data = new WeatherData(String.format("%.1f°C", temperature), weatherIconRes);
                        weatherCache.put(cacheKey, data);

                        runOnUiThread(() -> {
                            cityTemperature.setText(data.getTemperature());
                            weatherIcon.setImageResource(data.getIcon());
                        });

                    } catch (JSONException e) {
                        runOnUiThread(() -> Toast
                                .makeText(context, "Error al procesar datos para " + city.getName(), Toast.LENGTH_SHORT)
                                .show());
                    }
                } else {
                    runOnUiThread(() -> Toast.makeText(context,
                            "Error en la respuesta de la API: " + response.message(), Toast.LENGTH_SHORT).show());
                }
            }
        });
    }

    private void runOnUiThread(Runnable action) {
        if (context instanceof android.app.Activity) {
            ((android.app.Activity) context).runOnUiThread(action);
        }
    }

    private int getWeatherIcon(String weatherDescription) {
        weatherDescription = weatherDescription.toLowerCase();

        if (weatherDescription.contains("clear"))
            return R.drawable.sun;
        if (weatherDescription.contains("clouds"))
            return R.drawable.cloudy;
        if (weatherDescription.contains("rain"))
            return R.drawable.rain;
        if (weatherDescription.contains("thunderstorm"))
            return R.drawable.thunderstorm;
        if (weatherDescription.contains("drizzle"))
            return R.drawable.drizzle;
        if (weatherDescription.contains("snow"))
            return R.drawable.snow;
        if (weatherDescription.contains("mist"))
            return R.drawable.mist;

        return R.drawable.default_weather;
    }

    private static class WeatherData {
        private final String temperature;
        private final int icon;

        public WeatherData(String temperature, int icon) {
            this.temperature = temperature;
            this.icon = icon;
        }

        public String getTemperature() {
            return temperature;
        }

        public int getIcon() {
            return icon;
        }
    }
}
