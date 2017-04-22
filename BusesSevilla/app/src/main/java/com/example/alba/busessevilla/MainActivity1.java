package com.example.alba.busessevilla;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MainActivity1 extends Activity {

    private Spinner puebloSpinner;
    String url;
    String opcion;
    JSONObject jObj = new JSONObject();
    String [] lista_municipios;
    Map<String, String> mapa_municipios = new HashMap<String, String>();
    Map<String, String> mapa_lineas = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main1);
        url = "http://api.ctan.es/v1/Consorcios/1/municipios";
        opcion = "municipios";
        new Parseo().execute();

        //String[] pueblos = {"Villamanrique","Pilas","Aznalcázar","Bollullos de la Mitación","Bormujos","Castilleja de la Cuesta","Sevilla"};

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

    private void recargarListaMunicipios (){
        String tag = "Parseo";
        JSONArray municipios = new JSONArray();
        try{
            municipios = jObj.getJSONArray("municipios");
        } catch (Exception e){
            Log.e(tag, "Error al parsear: " + e.getMessage());
        }
        if (municipios!= null) {
            lista_municipios = new String[municipios.length()];
            for (int i = 0; i < municipios.length(); i++) {
                try {
                    JSONObject obj = municipios.getJSONObject(i);
                    String municipio = obj.getString("datos");
                    String id = obj.getString("idMunicipio");
                    lista_municipios[i] = municipio;
                    mapa_municipios.put(municipio,id);
                } catch (Exception e) {
                    Log.e(tag, "Error al leer el JSON: " + e);
                }
            }
        } else {
            Log.e(tag, "Error al acceder a la página.");
        }
        ArrayAdapter adapterPueblo = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, lista_municipios);
        puebloSpinner = (Spinner)findViewById(R.id.puebloSpinner);
        puebloSpinner.setAdapter(adapterPueblo);
    }

    /*private void cargarListaLineas(){
        String tag = "Parseo";
        puebloSpinner = (Spinner)findViewById(R.id.puebloSpinner);
        String seleccionado = puebloSpinner.getSelectedItem().toString();
        //String seleccionado = "SEVILLA";
        url = "http://api.ctan.es/v1/Consorcios/1/municipios/";
        String id = mapa_municipios.get(seleccionado);
        opcion = "nucleos";
        if (id!= null){
            url = url.concat(id + "/nucleos");
            new Parseo().execute();
        } else {
            Log.e(tag, "Municipio seleccionado incorrecto.");
        }
    }

    private void recargarListaLineas(){
        String tag = "Parseo";
        JSONArray nucleos = new JSONArray();
        List<String> id_municipios = null;
        try{
            nucleos = jObj.getJSONArray("nucleos");
        } catch (Exception e){
            Log.e(tag, "Error al generar núcleos: " + e.getMessage());
        }
        if (nucleos!= null) {
            for (int i = 0; i < nucleos.length(); i++) {
                try {
                    JSONObject obj = nucleos.getJSONObject(i);
                    id_municipios.add(obj.getString("idNucleo"));
                } catch (Exception e) {
                    Log.e(tag, "Error al leer el JSON" + e);
                }
            }
            Log.e(tag, "Contenido lista: " + id_municipios);
            parsearLineas(id_municipios.iterator(), tag);
        } else {
            Log.e(tag, "Error al acceder a la página.");
        }
    }

    private void parsearLineas(Iterator<String> id, String tag)
    {
        JSONArray lineas = new JSONArray();
        while (id.hasNext()){
            String siguiente = id.next();
            url = "http://api.ctan.es/v1/Consorcios/1/nucleos/" + siguiente +"/lineas";
            new Parseo().execute();
            try{
                lineas = jObj.getJSONArray("lineas");
            } catch (Exception e){
                Log.e(tag, "Error al generar líneas: " + e.getMessage());
            }
            if (lineas!= null) {
                for (int i = 0; i < lineas.length(); i++) {
                    try {
                        JSONObject obj = lineas.getJSONObject(i);
                        if (obj.getString("modo").equals("Bus")){
                            String id_linea = obj.getString("idLinea");
                            String linea = obj.getString("nombre");
                            mapa_lineas.put(id_linea,linea);
                        }
                    } catch (Exception e) {
                        Log.e(tag, "Error al leer el JSON" + e);
                    }
                }
                Log.e(tag, "Contenido mapa: " + mapa_lineas);
            } else {
                Log.e(tag, "Error al acceder a la página.");
            }

        }
    }*/

    private class Parseo extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Toast.makeText(MainActivity1.this, "Espere, por favor.", Toast.LENGTH_LONG).show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            String tag = "Parseo";
            String jsonStr = "";
            try {
                jsonStr = clienteHttp(url);
                Log.e(tag, "Respuesta de " + url);
            } catch (Exception e) {
                Log.e(tag, "No hubo respuesta de " + url);
            }
            if (jsonStr != null) {
                try {
                    jObj = new JSONObject(jsonStr);
                } catch (final JSONException e) {
                    Log.e(tag, "Error al parsear: " + e.getMessage());
                }
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            //Toast.makeText(MainActivity1.this, "Descargado.", Toast.LENGTH_LONG).show();
            super.onPostExecute(result);
            switch (opcion){
                case "municipios":  recargarListaMunicipios();
                //case "nucleos":  recargarListaLineas();
            }
        }
    }

    private String clienteHttp(String dir_web) throws IOException {
        String body = "";
        try {
            URL url = new URL(dir_web);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            String codigoRespuesta = Integer.toString(urlConnection.getResponseCode());
            if(codigoRespuesta.equals("200")){//Vemos si es 200 OK y leemos el cuerpo del mensaje.
                InputStream in = urlConnection.getInputStream();
                BufferedReader r = new BufferedReader(new InputStreamReader(in));
                StringBuilder total = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) {
                    total.append(line);
                }
                if(r != null)
                    r.close();
                in.close();
                body = total.toString();
            }
            urlConnection.disconnect();
        } catch (MalformedURLException e) {
            body = e.toString(); //Error URL incorrecta
        } catch (SocketTimeoutException e){
            body = e.toString(); //Error: Finalizado el timeout esperando la respuesta del servidor.
        } catch (Exception e) {
            body = e.toString();//Error diferente a los anteriores.
        }
        Log.e("Parseo", "body" + body);
        return body;
    }


}
