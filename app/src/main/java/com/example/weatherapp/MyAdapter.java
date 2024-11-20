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

///MyAdapter: Adaptador personalizado para mostrar una lista de ciudades con sus datos climáticos.
//Gestiona las vistas de cada elemento y la obtención de datos desde la API de OpenWeatherMap.
public class MyAdapter extends BaseAdapter {

    private final Context context; // Contexto de la actividad donde se utiliza el adaptador.
    private final ArrayList<City> cities; // Lista de ciudades a mostrar.
    private final OkHttpClient client; // Cliente HTTP para realizar las solicitudes de red.
    private final String API_KEY = "221ca3aedf3d0701ec5a7bf2b75a7efd"; // Clave de acceso a la API de OpenWeatherMap.

    // Caché para almacenar datos climáticos y reducir llamadas a la API.
    private final HashMap<String, WeatherData> weatherCache = new HashMap<>();

    // Constructor del adaptador.
    // @param context: Contexto de la actividad.
    // @param cities: Lista de ciudades a manejar.
    public MyAdapter(Context context, ArrayList<City> cities) {
        this.context = context;
        this.cities = cities;
        this.client = new OkHttpClient(); // Inicializa el cliente HTTP.
    }

    @Override
    public int getCount() {
        // Devuelve la cantidad de elementos en la lista.
        return cities.size();
    }

    @Override
    public Object getItem(int position) {
        // Devuelve el objeto City en la posición especificada.
        return cities.get(position);
    }

    @Override
    public long getItemId(int position) {
        // Devuelve el ID del elemento en la posición especificada (en este caso, su
        // posición).
        return position;
    }

    // ViewHolder: Clase interna para almacenar las vistas asociadas a un elemento
    // del ListView, mejorando el rendimiento al reutilizar vistas.
    private static class ViewHolder {
        TextView cityName; // Nombre de la ciudad.
        TextView cityTemperature; // Temperatura de la ciudad.
        ImageView weatherIcon; // Icono que representa el estado del clima.
    }

    // Método `getView`: Genera y personaliza las vistas para cada elemento del
    // ListView.
    // @param position: Posición del elemento en la lista.
    // @param convertView: Vista existente reutilizable (si aplica).
    // @param parent: Grupo padre que contiene las vistas.
    // @return: Vista personalizada para el elemento.
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        // Verifica si la vista puede reutilizarse o debe inflarse desde el diseño XML.
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
            holder = new ViewHolder();
            holder.cityName = convertView.findViewById(R.id.city_name);
            holder.cityTemperature = convertView.findViewById(R.id.city_temperature);
            holder.weatherIcon = convertView.findViewById(R.id.weather_image);
            convertView.setTag(holder); // Asocia el ViewHolder a la vista para reutilizarlo.
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Configura los datos de la ciudad en las vistas.
        City city = cities.get(position);
        holder.cityName.setText(city.getName());

        // Genera una clave para la caché basada en los datos de la ciudad.
        String cacheKey = city.getName() + city.getLatitude() + city.getLongitude();

        // Verifica si los datos están en caché, de lo contrario, solicita los datos.
        if (weatherCache.containsKey(cacheKey)) {
            WeatherData cachedData = weatherCache.get(cacheKey);
            holder.cityTemperature.setText(cachedData.getTemperature());
            holder.weatherIcon.setImageResource(cachedData.getIcon());
        } else {
            fetchWeatherData(city, cacheKey, holder.cityTemperature, holder.weatherIcon);
        }

        return convertView;
    }

    // Método `fetchWeatherData`: Solicita datos climáticos desde la API y actualiza
    // la vista.
    // @param city: Ciudad para la cual se solicitan los datos.
    // @param cacheKey: Clave única para almacenar en caché los datos.
    // @param cityTemperature: TextView para mostrar la temperatura.
    // @param weatherIcon: ImageView para mostrar el ícono del clima.
    private void fetchWeatherData(City city, String cacheKey, TextView cityTemperature, ImageView weatherIcon) {
        String url = "https://api.openweathermap.org/data/2.5/weather?lat=" + city.getLatitude() +
                "&lon=" + city.getLongitude() + "&appid=" + API_KEY + "&units=metric";

        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> Toast.makeText(context,
                        "Error al cargar datos para " + city.getName(), Toast.LENGTH_SHORT).show());
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

                        // Almacena los datos en caché y actualiza la vista en el hilo principal.
                        WeatherData data = new WeatherData(String.format("%.1f°C", temperature), weatherIconRes);
                        weatherCache.put(cacheKey, data);

                        runOnUiThread(() -> {
                            cityTemperature.setText(data.getTemperature());
                            weatherIcon.setImageResource(data.getIcon());
                        });

                    } catch (JSONException e) {
                        runOnUiThread(() -> Toast.makeText(context,
                                "Error al procesar datos para " + city.getName(), Toast.LENGTH_SHORT).show());
                    }
                } else {
                    runOnUiThread(() -> Toast.makeText(context,
                            "Error en la respuesta de la API: " + response.message(), Toast.LENGTH_SHORT).show());
                }
            }
        });
    }

    // Método `runOnUiThread`: Ejecuta una acción en el hilo principal para
    // actualizar la UI.
    // @param action: Acción a ejecutar.
    private void runOnUiThread(Runnable action) {
        if (context instanceof android.app.Activity) {
            ((android.app.Activity) context).runOnUiThread(action);
        }
    }

    // Método `getWeatherIcon`: Devuelve el recurso drawable correspondiente a la
    // descripción del clima.
    // @param weatherDescription: Descripción textual del clima.
    // @return: ID del recurso drawable.
    private int getWeatherIcon(String weatherDescription) {
        weatherDescription = weatherDescription.toLowerCase();

        if (weatherDescription.contains("clear"))
            return R.drawable.sun;
        if (weatherDescription.contains("clouds"))
            return R.drawable.cloudy;
        if (weatherDescription.contains("cloudy"))
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

        return R.drawable.default_weather; // Ícono por defecto si no coincide.
    }

    // Clase interna `WeatherData`: Representa datos climáticos individuales para
    // caché.
    private static class WeatherData {
        private final String temperature; // Texto de la temperatura (e.g., "25.0°C").
        private final int icon; // ID del ícono del clima.

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
