package com.example.weatherapp;

import android.os.Bundle;
import android.widget.ListView;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

/**
 * ListActivity: Actividad que muestra una lista de ciudades en un ListView.
 * Cada ciudad contiene un nombre, una latitud y una longitud predefinidas.
 */
public class ListActivity extends AppCompatActivity {

    // Variables para manejar el ListView y la lista de ciudades.
    private ListView cityListView; // Componente visual para mostrar la lista de ciudades.
    private ArrayList<City> cities; // Lista de objetos City que se mostrará en el ListView.

    /**
     * Método `onCreate`: Inicializa la actividad.
     * Configura el diseño, el ListView, aplica una animación y asigna un adaptador
     * personalizado.
     *
     * @param savedInstanceState Estado previo de la actividad (si aplica).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list); // Vincula la actividad con el diseño XML `activity_list`.

        // Vincula el ListView con su componente en el diseño XML.
        cityListView = findViewById(R.id.city_list_view);

        // Aplica una animación de "fade in" al ListView para mejorar la experiencia
        // visual.
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        cityListView.startAnimation(fadeIn);

        // Inicializa la lista de ciudades con valores predefinidos.
        initializeCities();

        // Configura el adaptador personalizado que conecta la lista de ciudades con el
        // ListView.
        MyAdapter adapter = new MyAdapter(this, cities);
        cityListView.setAdapter(adapter); // Asigna el adaptador al ListView.
    }

    /**
     * Inicializa la lista de ciudades con sus respectivas coordenadas.
     * Cada ciudad se agrega a un ArrayList de objetos `City`.
     */
    private void initializeCities() {
        cities = new ArrayList<>(); // Crea una nueva instancia de ArrayList para almacenar las ciudades.

        // Agrega cada ciudad con su nombre, latitud y longitud.
        cities.add(new City("Arica", -18.4783, -70.3126));
        cities.add(new City("Iquique", -20.2307, -70.1357));
        cities.add(new City("Antofagasta", -23.6500, -70.4000));
        cities.add(new City("Copiapó", -27.3667, -70.3314));
        cities.add(new City("La Serena", -29.9045, -71.2489));
        cities.add(new City("Valparaíso", -33.0472, -71.6127));
        cities.add(new City("Rancagua", -34.1654, -70.7399));
        cities.add(new City("Talca", -35.4264, -71.6554));
        cities.add(new City("Chillán", -36.6066, -72.1034));
        cities.add(new City("Concepción", -36.8201, -73.0444));
        cities.add(new City("Temuco", -38.7359, -72.5904));
        cities.add(new City("Valdivia", -39.8142, -73.2459));
        cities.add(new City("Puerto Montt", -41.4693, -72.9424));
        cities.add(new City("Coyhaique", -45.5712, -72.0683));
        cities.add(new City("Punta Arenas", -53.1638, -70.9171));
    }
}
