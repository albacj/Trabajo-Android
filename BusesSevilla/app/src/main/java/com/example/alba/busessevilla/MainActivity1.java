package com.example.alba.busessevilla;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

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

        ArrayList<Lista_entrada> datos = new ArrayList<Lista_entrada>();

        datos.add(new Lista_entrada("M-120", "Bollullos de la Mitación"));
        datos.add(new Lista_entrada("M-163", "Camas"));
        datos.add(new Lista_entrada("M-142B", "Palomares del Río"));
        datos.add(new Lista_entrada("M-169", "Villamanrique"));

        ListView lista = (ListView) findViewById(R.id.lineasListView);
        lista.setAdapter(new Lista_adaptador(this, R.layout.entrada, datos){
            @Override
            public void onEntrada(Object entrada, View view) {
                TextView texto_superior_entrada = (TextView) view.findViewById(R.id.busTextView);
                texto_superior_entrada.setText(((Lista_entrada) entrada).getTextoNumLinea());

                TextView texto_inferior_entrada = (TextView) view.findViewById(R.id.busPuebloTextView);
                texto_inferior_entrada.setText(((Lista_entrada) entrada).getTextoPuebloLinea());
            }
        });

        //Selecciona un ítem y lleva a la activity2 con el tiempo que queda para que pase el autobús

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        // Abre una nueva Activity:
                        Intent myIntent = new Intent(view.getContext(), MainActivity2.class);
                        startActivity(myIntent);

                    }
                }
        );

    }
}
