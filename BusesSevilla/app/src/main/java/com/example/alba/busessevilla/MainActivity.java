package com.example.alba.busessevilla;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.view.View;

public class MainActivity extends Activity {

    private Spinner puebloOrigenSpinner, paradaSpinner, puebloDestinoSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] pueblosOrigen = {"Pilas", "Aznalcázar", "Bollullos", "Bormujos"};
        String[] paradas = {"Pinichi", "Alambique", "Iglesia", "Blanca Paloma"};
        String[] pueblosDestino = {"Pilas", "Aznalcázar", "Bollullos", "Bormujos"};

        ArrayAdapter adapterPuebloOrigen = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, pueblosOrigen);
        ArrayAdapter adapterParada = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, paradas);
        ArrayAdapter adapterPuebloDestino = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, pueblosDestino);

        puebloOrigenSpinner = (Spinner)findViewById(R.id.puebloOrigenSpinner);
        paradaSpinner = (Spinner)findViewById(R.id.paradaSpinner);
        puebloDestinoSpinner = (Spinner)findViewById(R.id.puebloDestinoSpinner);

        puebloOrigenSpinner.setAdapter(adapterPuebloOrigen);
        paradaSpinner.setAdapter(adapterParada);
        puebloDestinoSpinner.setAdapter(adapterPuebloDestino);
   }

    public void btnClicked(View view){
        Intent myIntent;

        myIntent = new Intent(getApplicationContext(), Main2Activity.class);
        myIntent.putExtra("puebloOrigen", puebloOrigenSpinner.getSelectedItem().toString());
        myIntent.putExtra("parada", paradaSpinner.getSelectedItem().toString());
        myIntent.putExtra("puebloDestino", puebloDestinoSpinner.getSelectedItem().toString());

        startActivity(myIntent);
    }
}
