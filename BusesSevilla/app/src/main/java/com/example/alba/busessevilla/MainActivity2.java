package com.example.alba.busessevilla;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

public class MainActivity2 extends Activity {

    ArrayList<String> tiempos_paradas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent actlinea = getIntent();
        tiempos_paradas = actlinea.getStringArrayListExtra("tiempos_paradas");
        setContentView(R.layout.activity_main2);

        ArrayList<Lista_entrada2> datos = new ArrayList<Lista_entrada2>();
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("Europe/Madrid"));
        int ahora = 60 * c.get(Calendar.HOUR_OF_DAY) + c.get(Calendar.MINUTE);

        /*datos.add(new Lista_entrada2(2));
        datos.add(new Lista_entrada2(15));*/
        for (String elemento: tiempos_paradas){
            String[] partes = elemento.split(":");
            int hora = Integer.parseInt(partes[0]);
            int minutos = Integer.parseInt(partes[1]);
            int horabus = 60 * hora + minutos;
            int tiempores = horabus - ahora;
            if (tiempores >= 0){
                int h = tiempores/60;
                int m = tiempores%60;
                String t = "";
                if (h>1){
                    t = t.concat(String.valueOf(h) + " horas y ");
                } else if (h==1){
                    t = t.concat(String.valueOf(h) + " hora y ");
                }
                if (m==1){
                    t = t.concat(String.valueOf(m) + " minuto");
                }else{
                    t = t.concat(String.valueOf(m) + " minutos");
                }
                datos.add(new Lista_entrada2(t + "\n(Sale a las " + elemento + ")"));
            }
        }
        if (datos.isEmpty()){
            datos.add(new Lista_entrada2("Sin estimaciones."));
        }

        ListView lista = (ListView) findViewById(R.id.tiempoListView);

        lista.setAdapter(new Lista_adaptador2(this, R.layout.entrada2, datos){
            @Override
            public void onEntrada(Object entrada, View view) {
                TextView texto_superior_entrada = (TextView) view.findViewById(R.id.tiempoTextView);
                //texto_superior_entrada.setText("hola");
                texto_superior_entrada.setText(((Lista_entrada2) entrada).getTextoTiempo());
            }
        });
    }
}
