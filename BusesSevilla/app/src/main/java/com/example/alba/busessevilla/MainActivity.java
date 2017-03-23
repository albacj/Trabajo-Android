package com.example.alba.busessevilla;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.view.View;

public class MainActivity extends Activity {

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

        Spinner puebloSpinner = (Spinner)findViewById(R.id.puebloSpinner);
        Spinner trayectoSpinner = (Spinner)findViewById(R.id.trayectoSpinner);
        Spinner paradaSpinner = (Spinner)findViewById(R.id.paradaSpinner);

        puebloSpinner.setAdapter(adapterPueblo);
        trayectoSpinner.setAdapter(adapterTrayecto);
        paradaSpinner.setAdapter(adapterParada);
   }

    public void btnClicked(View view){
        Intent myIntent;
        myIntent = new Intent(getApplicationContext(), Main2Activity.class);
        startActivity(myIntent);
    }
}
