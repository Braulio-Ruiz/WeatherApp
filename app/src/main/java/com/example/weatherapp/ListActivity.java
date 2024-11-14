package com.example.weatherapp;

import android.os.Bundle;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Arrays;

public class ListActivity extends AppCompatActivity {

    // Variables para el ListView y la lista de ciudades
    private ListView cityListView;
    private ArrayList<City> cities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        // Inicialización del ListView
        cityListView = findViewById(R.id.city_list_view);

        // Inicialización de la lista de ciudades con sus coordenadas
        cities = new ArrayList<>(Arrays.asList(
                new City("Arica", -18.4783, -70.3126),
                new City("Iquique", -20.2307, -70.1357),
                new City("Antofagasta", -23.6500, -70.4000),
                new City("Copiapó", -27.3667, -70.3333),
                new City("La Serena", -29.9045, -71.2489),
                new City("Valparaíso", -33.0472, -71.6127),
                new City("Santiago", -33.4489, -70.6693),
                new City("Rancagua", -34.1654, -70.7399),
                new City("Talca", -35.4264, -71.6554),
                new City("Chillán", -36.6066, -72.1034),
                new City("Concepción", -36.8201, -73.0444),
                new City("Temuco", -38.7359, -72.5904),
                new City("Valdivia", -39.8142, -73.2459),
                new City("Puerto Montt", -41.4693, -72.9424),
                new City("Coyhaique", -45.5712, -72.0683),
                new City("Punta Arenas", -53.1638, -70.9171)
        ));

        // Creación y configuración del adaptador personalizado para mostrar la lista
        MyAdapter adapter = new MyAdapter(this, cities);
        cityListView.setAdapter(adapter);
    }
}
