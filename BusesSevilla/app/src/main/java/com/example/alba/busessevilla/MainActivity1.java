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
import java.util.List;
import java.util.Map;

public class MainActivity1 extends Activity {

    private Spinner puebloSpinner;
    String url;
    JSONObject jObj = new JSONObject();
    String [] lista_municipios;
    ArrayList<Lista_entrada> lista_lineas = new ArrayList<Lista_entrada>();
    Map<String, String> mapa_municipios = new HashMap<String, String>();
    Map<String, String> mapa_lineas = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main1);
        url = "http://api.ctan.es/v1/Consorcios/1/municipios";
        new ParseoMunicipio().execute();

        //String[] pueblos = {"Villamanrique","Pilas","Aznalcázar","Bollullos de la Mitación","Bormujos","Castilleja de la Cuesta","Sevilla"};
    }

    private void cargarListaMunicipios (){
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

        puebloSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapter, View view,
                                       int position, long id) {
                url = "http://api.ctan.es/v1/Consorcios/1/municipios/";
                String tag = "Parseo";
                String seleccionado = mapa_municipios.get(puebloSpinner.getSelectedItem());
                if (seleccionado!= null){
                    url = url.concat(seleccionado + "/nucleos");
                    new ParseoLinea().execute();
                } else {
                    Log.e(tag, "Municipio seleccionado incorrecto.");
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void cargarListaLineas(){

        /*datos.add(new Lista_entrada("M-120", "Bollullos de la Mitación"));
        datos.add(new Lista_entrada("M-163", "Camas"));
        datos.add(new Lista_entrada("M-142B", "Palomares del Río"));
        datos.add(new Lista_entrada("M-169", "Villamanrique"));*/

        String tag = "Parseo";
        ListView lista = (ListView) findViewById(R.id.lineasListView);
        lista.setAdapter(new Lista_adaptador(this, R.layout.entrada, lista_lineas){
            @Override
            public void onEntrada(Object entrada, View view) {
                TextView texto_superior_entrada = (TextView) view.findViewById(R.id.busTextView);
                texto_superior_entrada.setText(((Lista_entrada) entrada).getTextoNumLinea());

                TextView texto_inferior_entrada = (TextView) view.findViewById(R.id.busPuebloTextView);
                texto_inferior_entrada.setText(((Lista_entrada) entrada).getTextoPuebloLinea());
            }
        });
        Log.e(tag, "Selector generado: " + lista_lineas.toArray().toString());

        //Selecciona un ítem y lleva a la activity2 con el tiempo que queda para que pase el autobús

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Abre una nueva Activity:
                Intent myIntent = new Intent(view.getContext(), MainActivityLinea.class);

                Lista_entrada salida = (Lista_entrada) parent.getItemAtPosition(position);
                String paramlinea = salida.getId_linea();

                if (!paramlinea.equals("-1")){
                    myIntent.putExtra("id_linea", paramlinea);
                    startActivity(myIntent);
                }
            }
        }
        );
    }

    private class ParseoMunicipio extends AsyncTask<Void, Void, Void> {
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
            cargarListaMunicipios();
        }
    }

    private class ParseoLinea extends AsyncTask<Void, Void, Void> {
        List<String> id_municipios = new ArrayList();
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Toast.makeText(MainActivity1.this, "Espere, por favor.", Toast.LENGTH_LONG).show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            String tag = "Parseo";
            String jsonStr = "";
            lista_lineas.clear();
            JSONArray nucleos = new JSONArray();
            try {
                jsonStr = clienteHttp(url);
                Log.e(tag, "Respuesta de " + url);
            } catch (Exception e) {
                Log.e(tag, "No hubo respuesta de " + url);
            }
            if (jsonStr != null) {
                try {
                    jObj = new JSONObject(jsonStr);
                    nucleos = jObj.getJSONArray("nucleos");
                    JSONArray lineas = new JSONArray();
                    if (nucleos!= null) {
                        for (int i = 0; i < nucleos.length(); i++) {
                            try {
                                JSONObject obj = nucleos.getJSONObject(i);
                                String siguiente = (obj.getString("idNucleo"));
                                url = "http://api.ctan.es/v1/Consorcios/1/nucleos/" + siguiente +"/lineas";
                            } catch (Exception e) {
                                Log.e(tag, "Error al leer el JSON" + e);
                            }
                            try {
                                jsonStr = clienteHttp(url);
                                Log.e(tag, "Respuesta de " + url);
                                if (jsonStr != null) {
                                    try {
                                        JSONObject jObj = new JSONObject(jsonStr);
                                        lineas = jObj.getJSONArray("lineas");
                                        if (lineas!= null) {
                                            for (int j = 0; j < lineas.length(); j++) {
                                                try {
                                                    JSONObject obj = lineas.getJSONObject(j);
                                                    if (obj.getString("modo").equals("Bus")){
                                                        String id_linea = obj.getString("idLinea");
                                                        String linea = obj.getString("nombre");
                                                        String nombre = "";
                                                        String[] partes = linea.split(" ");
                                                        String cod_linea = partes[0];
                                                        for(int k=1;k<partes.length;k++) {
                                                            nombre = nombre.concat(partes[k] + " ");
                                                        }
                                                        mapa_lineas.put(linea,id_linea);
                                                        lista_lineas.add(new Lista_entrada(cod_linea,nombre,id_linea));
                                                    }
                                                } catch (Exception e) {
                                                    Log.e(tag, "Error al leer el JSON" + e);
                                                }
                                            }
                                            if (lista_lineas.size() == 0){
                                                lista_lineas.add(new Lista_entrada("","No hay servicios disponibles.","-1"));
                                            }
                                        } else {
                                            Log.e(tag, "Error al acceder a la página.");
                                        }
                                    } catch (Exception e) {
                                        Log.e(tag, "Error al generar líneas: " + e.getMessage());
                                    }
                                }
                            } catch (Exception e) {
                                    Log.e(tag, "No hubo respuesta de " + url);
                            }
                        }
                        Log.e(tag, "Contenido lista: " + id_municipios);
                        Log.e(tag, "Contenido mapa: " + mapa_lineas);}
                } catch (final JSONException e) {
                    Log.e(tag, "Error al parsear: " + e.getMessage());
                }
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            Toast.makeText(MainActivity1.this, "Descargado.", Toast.LENGTH_LONG).show();
            super.onPostExecute(result);
            cargarListaLineas();
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
