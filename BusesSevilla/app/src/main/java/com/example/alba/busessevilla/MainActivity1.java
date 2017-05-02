package com.example.alba.busessevilla;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity1 extends Activity {

    String url;
    JSONObject jObj = new JSONObject();
    String [] lista_municipios;
    ArrayList<Lista_entrada> lista_lineas = new ArrayList<>();
    HashMap<String, String> mapa_municipios = new HashMap<>();
    HashMap<String, String> mapa_municipios2 = new HashMap<>();
    HashMap<String, String> mapa_lineas = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        TextView texto1 = (TextView) findViewById(R.id.infoTextView);
        texto1.setVisibility(View.INVISIBLE);
        TextView texto2 = (TextView) findViewById(R.id.infoLineasTextView);
        texto2.setVisibility(View.INVISIBLE);
        ImageView imglineas = (ImageView) findViewById(R.id.imglineas);
        imglineas.setVisibility(View.INVISIBLE);
        Spinner puebloSpinner = (Spinner)findViewById(R.id.puebloSpinner);
        puebloSpinner.setVisibility(View.INVISIBLE);
        ProgressBar progreso2 = (ProgressBar) findViewById(R.id.progreso2);
        progreso2.setVisibility(View.INVISIBLE);
        url = "http://api.ctan.es/v1/Consorcios/1/municipios";
        new ParseoMunicipio().execute();
    }

    private void cargarListaMunicipios (){

        ProgressBar progreso1 = (ProgressBar) findViewById(R.id.progreso1);
        progreso1.setVisibility(View.INVISIBLE);
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
                    mapa_municipios2.put(id,municipio);
                } catch (Exception e) {
                    Log.e(tag, "Error al leer el JSON: " + e);
                }
            }
        } else {
            Log.e(tag, "Error al acceder a la página.");
        }
        ArrayAdapter adapterPueblo = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, lista_municipios);
        final Spinner puebloSpinner = (Spinner)findViewById(R.id.puebloSpinner);
        puebloSpinner.setAdapter(adapterPueblo);
        puebloSpinner.setVisibility(View.VISIBLE);
        TextView texto1 = (TextView) findViewById(R.id.infoTextView);
        texto1.setVisibility(View.VISIBLE);
        ImageView imglineas = (ImageView) findViewById(R.id.imglineas);
        imglineas.setVisibility(View.VISIBLE);
        TextView texto2 = (TextView) findViewById(R.id.infoLineasTextView);
        texto2.setVisibility(View.VISIBLE);
        puebloSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapter, View view,
                                       int position, long id) {
                url = "http://api.ctan.es/v1/Consorcios/1/municipios/";
                String tag = "Parseo";
                String seleccionado = mapa_municipios.get(puebloSpinner.getSelectedItem());
                if (seleccionado!= null){
                    ImageView imglineas = (ImageView) findViewById(R.id.imglineas);
                    imglineas.setVisibility(View.INVISIBLE);
                    ListView lista = (ListView) findViewById(R.id.lineasListView);
                    lista.setVisibility(View.INVISIBLE);
                    ProgressBar progreso2 = (ProgressBar) findViewById(R.id.progreso2);
                    progreso2.setVisibility(View.VISIBLE);
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

        ProgressBar progreso2 = (ProgressBar) findViewById(R.id.progreso2);
        progreso2.setVisibility(View.INVISIBLE);
        String tag = "Parseo";
        ImageView imglineas = (ImageView) findViewById(R.id.imglineas);
        imglineas.setVisibility(View.VISIBLE);
        ListView lista = (ListView) findViewById(R.id.lineasListView);
        lista.setVisibility(View.VISIBLE);
        Collections.sort(lista_lineas,new Comparator<Lista_entrada>() {
            @Override
            public int compare(Lista_entrada o1, Lista_entrada o2) {
                return o1.getTextoNumLinea().compareTo(o2.getTextoNumLinea());
            }
        });
        lista.setAdapter(new Lista_adaptador(this, R.layout.entrada, lista_lineas){
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
                Intent myIntent = new Intent(view.getContext(), MainActivityLinea.class);

                Lista_entrada salida = (Lista_entrada) parent.getItemAtPosition(position);
                String paramlinea = salida.getId_linea();

                if (!paramlinea.equals("-1")){
                    myIntent.putExtra("id_linea", paramlinea);
                    myIntent.putExtra("mapa_municipios", mapa_municipios2);
                    startActivity(myIntent);
                }
            }
        }
        );
    }

    private class ParseoMunicipio extends AsyncTask<Void, Void, Void>{
        private String error = "";
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
                error = e.getMessage();
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
            if (error.equals("")) {
                cargarListaMunicipios();
            } else {
                mostraralerta(error);
            }
        }
    }

    private class ParseoLinea extends AsyncTask<Void, Void, Void> {
        List<String> id_municipios = new ArrayList();
        String error = "";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            List<String> l = new ArrayList<String>();
            ArrayAdapter<String> adapterVacio = new ArrayAdapter<String>(getApplicationContext(), R.layout.entrada, l);
            ListView listalv = (ListView)findViewById(R.id.lineasListView);
            listalv.setAdapter(adapterVacio);
            //Toast.makeText(MainActivity1.this, "Espere, por favor.", Toast.LENGTH_LONG).show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            String tag = "Parseo";
            String jsonStr = "";
            lista_lineas.clear();
            mapa_lineas.clear();
            JSONArray nucleos = new JSONArray();
            try {
                jsonStr = clienteHttp(url);
                Log.e(tag, "Respuesta de " + url);
            } catch (Exception e) {
                Log.e(tag, "No hubo respuesta de " + url);
                error = e.getMessage();
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
                                                        if (!mapa_lineas.containsKey(linea)){
                                                            mapa_lineas.put(linea,id_linea);
                                                            lista_lineas.add(new Lista_entrada(cod_linea,nombre,id_linea));
                                                        }
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
            //Toast.makeText(MainActivity1.this, "Descargado.", Toast.LENGTH_LONG).show();
            super.onPostExecute(result);
            if (error.equals("")) {
                cargarListaLineas();
            } else {
                mostraralerta(error);
            }
        }
    }

    private void mostraralerta(String mensaje){
        try{
            ProgressBar progreso1 = (ProgressBar) findViewById(R.id.progreso1);
            progreso1.setVisibility(View.INVISIBLE);
            ProgressBar progreso2 = (ProgressBar) findViewById(R.id.progreso2);
            progreso2.setVisibility(View.INVISIBLE);
            AlertDialog.Builder dialogo = new AlertDialog.Builder(MainActivity1.this);
            dialogo.setTitle(getText(R.string.error0));
            dialogo.setMessage(mensaje);
            dialogo.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    finish();
                }
            });
            dialogo.create();
            dialogo.show();
        } catch (Exception e){
            Log.e("Parseo", e.getMessage());
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
            throw new RuntimeException(getString(R.string.error1));
        } catch (SocketTimeoutException e){
            body = e.toString(); //Error: Finalizado el timeout esperando la respuesta del servidor.
            throw new RuntimeException(getString(R.string.error1));
        } catch (Exception e) {
            body = e.toString();//Error diferente a los anteriores.
            throw new RuntimeException(getString(R.string.error2));
        }
        Log.e("Parseo", "body" + body);
        if (body==""){
            throw new RuntimeException(getString(R.string.error1));
        }
        return body;
    }


}
