package com.example.weatherapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

// MainActivity: Pantalla principal de la aplicación que ofrece dos opciones al usuario:
// - Ver el pronóstico del clima actual.
// - Consultar una lista de ciudades con sus pronósticos respectivos.
public class MainActivity extends AppCompatActivity {

    // Declaración de variables para los elementos de la interfaz gráfica.
    private Button btnCurrentForecast; // Botón para acceder al pronóstico actual.
    private Button btnCityList; // Botón para acceder a la lista de ciudades.
    private TextView welcome_text; // Texto de bienvenida para el usuario.

    /**
     * Método que se ejecuta al crear la actividad. Configura la interfaz de
     * usuario,
     * enlaza elementos del diseño con el código y define comportamientos.
     *
     * @param savedInstanceState Objeto Bundle que contiene el estado de la
     *                           actividad si
     *                           esta fue pausada o recreada anteriormente.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Asocia la actividad con el diseño XML `activity_main`.

        // Vinculación de elementos de la interfaz gráfica con sus identificadores en el
        // archivo XML.
        btnCurrentForecast = findViewById(R.id.btn_current_forecast); // Botón "Pronóstico actual".
        btnCityList = findViewById(R.id.btn_city_list); // Botón "Lista de ciudades".
        welcome_text = findViewById(R.id.welcome_text); // Texto de bienvenida.

        // Aplicar una animación de "fade in" (desvanecimiento) al texto de bienvenida y
        // a los botones.
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        welcome_text.startAnimation(fadeIn); // Animación para el texto de bienvenida.
        btnCurrentForecast.startAnimation(fadeIn); // Animación para el botón "Pronóstico actual".
        btnCityList.startAnimation(fadeIn); // Animación para el botón "Lista de ciudades".

        // Listener para el botón "Pronóstico actual".
        // Define el comportamiento al hacer clic en este botón.
        btnCurrentForecast.setOnClickListener(view -> {
            // Crear un Intent para iniciar la actividad ForecastActivity (pantalla del
            // pronóstico actual).
            Intent intent = new Intent(MainActivity.this, ForecastActivity.class);
            startActivity(intent); // Inicia la actividad ForecastActivity.
        });

        // Listener para el botón "Lista de ciudades".
        // Define el comportamiento al hacer clic en este botón.
        btnCityList.setOnClickListener(view -> {
            // Crear un Intent para iniciar la actividad ListActivity (pantalla de la lista
            // de ciudades).
            Intent intent = new Intent(MainActivity.this, ListActivity.class);
            startActivity(intent); // Inicia la actividad ListActivity.
        });
    }
}
