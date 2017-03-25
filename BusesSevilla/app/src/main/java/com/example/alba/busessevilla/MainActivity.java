package com.example.alba.busessevilla;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.view.View;

import java.text.BreakIterator;

public class MainActivity extends Activity {

    private Spinner puebloSpinner, trayectoSpinner, paradaSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] pueblos = {"Pilas", "Aznalcázar", "Bollullos", "Bormujos"};
        String[] trayectos = {"Sevilla - Pueblo", "Pueblo - Sevilla"};
        String[] paradas = {"Pinichi", "Alambique", "Iglesia", "Blanca Paloma"};

        ArrayAdapter adapterPueblo = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, pueblos);
        ArrayAdapter adapterTrayecto = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, trayectos);
        ArrayAdapter adapterParada = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, paradas);

        puebloSpinner = (Spinner)findViewById(R.id.puebloSpinner);
        trayectoSpinner = (Spinner)findViewById(R.id.trayectoSpinner);
        paradaSpinner = (Spinner)findViewById(R.id.paradaSpinner);

        puebloSpinner.setAdapter(adapterPueblo);
        trayectoSpinner.setAdapter(adapterTrayecto);
        paradaSpinner.setAdapter(adapterParada);
   }

    public void btnClicked(View view){
        Intent myIntent;

        myIntent = new Intent(getApplicationContext(), Main2Activity.class);
        myIntent.putExtra("pueblo", puebloSpinner.getSelectedItem().toString());
        myIntent.putExtra("sentido", trayectoSpinner.getSelectedItem().toString());
        myIntent.putExtra("parada", paradaSpinner.getSelectedItem().toString());

        startActivity(myIntent);
    }
}
