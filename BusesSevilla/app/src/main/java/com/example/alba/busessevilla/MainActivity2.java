package com.example.alba.busessevilla;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

public class MainActivity2 extends Activity implements OnMapReadyCallback {

    private GoogleMap mapa;
    String nombre_linea;
    String nombre_parada;
    double latitud;
    double longitud;
    ArrayList<String> tiempos_paradas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent actlinea = getIntent();
        nombre_linea = actlinea.getStringExtra("nombre_linea");
        nombre_parada = actlinea.getStringExtra("nombre_parada");
        latitud = Double.valueOf(actlinea.getStringExtra("latitud"));
        longitud = Double.valueOf(actlinea.getStringExtra("longitud"));
        tiempos_paradas = actlinea.getStringArrayListExtra("tiempos_paradas");
        setContentView(R.layout.activity_main2);
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.mapa);
        mapFragment.getMapAsync(this);
        ArrayList<Lista_entrada2> datos = new ArrayList<>();
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("Europe/Madrid"));
        int ahora = 60 * c.get(Calendar.HOUR_OF_DAY) + c.get(Calendar.MINUTE);

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
                    t = t.concat(String.valueOf(h) + getText(R.string.horas));
                } else if (h==1){
                    t = t.concat(String.valueOf(h) + getText(R.string.hora));
                }
                if (m==1){
                    t = t.concat(String.valueOf(m) + getText(R.string.minuto));
                }else{
                    t = t.concat(String.valueOf(m) + getText(R.string.minutos));
                }
                datos.add(new Lista_entrada2(t + "\n(Sale a las " + elemento + ")"));
            }
        }
        if (datos.isEmpty()){
            datos.add(new Lista_entrada2(getString(R.string.sin_estimaciones)));
        }

        TextView textolinea = (TextView) findViewById(R.id.nombrelinea);
        textolinea.setText(nombre_linea);
        TextView textoparada = (TextView) findViewById(R.id.infoTiempoTextView);
        textoparada.setText(getText(R.string.info_tiempo) + nombre_parada + ":");
        ListView lista = (ListView) findViewById(R.id.tiempoListView);
        lista.setAdapter(new Lista_adaptador2(this, R.layout.entrada2, datos){
            @Override
            public void onEntrada(Object entrada, View view) {
                TextView texto_superior_entrada = (TextView) view.findViewById(R.id.tiempoTextView);
                texto_superior_entrada.setText(((Lista_entrada2) entrada).getTextoTiempo());
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapa = googleMap;
        LatLng parada = new LatLng(latitud, longitud);
        CameraPosition camPos = new CameraPosition.Builder()
                .target(parada)
                .zoom(19)
                .bearing(0)
                .tilt(0)
                .build();
        CameraUpdate camUpd3 =
                CameraUpdateFactory.newCameraPosition(camPos);
        mapa.addMarker(new MarkerOptions()
                .position(parada)
                .title(nombre_parada));
        mapa.animateCamera(camUpd3);
    }
}
