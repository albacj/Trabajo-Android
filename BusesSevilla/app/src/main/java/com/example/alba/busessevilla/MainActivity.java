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

        String[] lineas = {"M160", "M161", "M162", "M169"};
        String [] trayectos = {"Sevilla - Pueblo", "Pueblo - Sevilla"};
        String [] paradas = {"Pinichi", "Alambique", "Iglesia", "Blanca Paloma"};

        ArrayAdapter adapterLinea = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, lineas);
        ArrayAdapter adapterTrayecto = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, trayectos);
        ArrayAdapter adapterParada = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, paradas);

        Spinner lineaSpinner = (Spinner)findViewById(R.id.lineaSpinner);
        Spinner trayectoSpinner = (Spinner)findViewById(R.id.trayectoSpinner);
        Spinner paradaSpinner = (Spinner)findViewById(R.id.paradaSpinner);

        lineaSpinner.setAdapter(adapterLinea);
        trayectoSpinner.setAdapter(adapterTrayecto);
        paradaSpinner.setAdapter(adapterParada);
   }

    public void btnClicked(View view){
        Intent myIntent;
        myIntent = new Intent(getApplicationContext(), Main2Activity.class);
        startActivity(myIntent);
    }
}
