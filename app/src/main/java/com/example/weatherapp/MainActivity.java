package com.example.weatherapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

// MainActivity: Pantalla principal que permite navegar a las opciones de pronóstico actual y lista de ciudades.
public class MainActivity extends AppCompatActivity {

    // Declaración de variables para los elementos de la interfaz
    private Button btnCurrentForecast;
    private Button btnCityList;
    private TextView welcome_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Vincula los botones con sus elementos en el diseño
        btnCurrentForecast = findViewById(R.id.btn_current_forecast);
        btnCityList = findViewById(R.id.btn_city_list);
        welcome_text = findViewById(R.id.welcome_text);

        // Aplicar animación al texto de bienvenida
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        welcome_text.startAnimation(fadeIn);

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
