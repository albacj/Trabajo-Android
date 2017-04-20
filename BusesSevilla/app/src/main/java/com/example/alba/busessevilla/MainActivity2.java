package com.example.alba.busessevilla;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity2 extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        ArrayList<Lista_entrada2> datos = new ArrayList<Lista_entrada2>();

        datos.add(new Lista_entrada2(2));
        datos.add(new Lista_entrada2(15));

        ListView lista = (ListView) findViewById(R.id.tiempoListView);

        lista.setAdapter(new Lista_adaptador2(this, R.layout.entrada2, datos){
            @Override
            public void onEntrada(Object entrada, View view) {
                TextView texto_superior_entrada = (TextView) view.findViewById(R.id.tiempoTextView);
                //texto_superior_entrada.setText("hola");
                int tmp =((Lista_entrada2) entrada).getTextoTiempo();
                texto_superior_entrada.setText(tmp + " minutos");
            }
        });
    }
}
