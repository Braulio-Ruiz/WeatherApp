package com.example.weatherapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // Declaración de variables para los elementos de la interfaz
    private Button btnCurrentForecast;
    private Button btnCityList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicialización de los elementos de la interfaz de usuario
        btnCurrentForecast = findViewById(R.id.btn_current_forecast);
        btnCityList = findViewById(R.id.btn_city_list);

        // Configuración de un listener para el botón de pronóstico actual
        btnCurrentForecast.setOnClickListener(view -> {
            // Inicia la actividad ForecastActivity al hacer clic en el botón
            Intent intent = new Intent(MainActivity.this, ForecastActivity.class);
            startActivity(intent);
        });

        // Configuración de un listener para el botón de lista de ciudades
        btnCityList.setOnClickListener(view -> {
            // Inicia la actividad ListActivity al hacer clic en el botón
            Intent intent = new Intent(MainActivity.this, ListActivity.class);
            startActivity(intent);
        });
    }
}
