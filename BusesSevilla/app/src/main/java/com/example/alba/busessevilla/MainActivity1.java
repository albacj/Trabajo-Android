package com.example.alba.busessevilla;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

public class MainActivity1 extends Activity {

    private Spinner puebloSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);

        String[] pueblos = {"Villamanrique","Pilas","Aznalcázar","Bollullos de la Mitación","Bormujos","Castilleja de la Cuesta","Sevilla"};

        ArrayAdapter adapterPueblo = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, pueblos);

        puebloSpinner = (Spinner)findViewById(R.id.puebloSpinner);
        puebloSpinner.setAdapter(adapterPueblo);
    }

    class PlacehoderFragment{

        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.activity_main1, container, false);

            // A. Creamos el arreglo de Strings para llenar la lista
            String[] lineas = new String[] { "M-142B", "M-169","M-120","M-168"};

            // B. Creamos un nuevo ArrayAdapter con nuestra lista de cosasPorHacer
            ArrayAdapter arrayAdapter = new ArrayAdapter(MainActivity1.this, android.R.layout.simple_list_item_1, lineas);

            // C. Seleccionamos la lista de nuestro layout
            ListView miLista = (ListView) rootView.findViewById(R.id.lineasListView);

            // D. Asignamos el adaptador a nuestra lista
            miLista.setAdapter(arrayAdapter);

            return rootView;
        }
    }
}
