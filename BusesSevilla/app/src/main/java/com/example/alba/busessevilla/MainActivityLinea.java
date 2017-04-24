package com.example.alba.busessevilla;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivityLinea extends Activity {

    String id_linea;
    int seleccion;
    JSONObject datos_linea;
    JSONObject datos_paradas;
    JSONArray noticias;
    Map<String,ArrayList> ida = new HashMap<String,ArrayList>();
    Map<String,ArrayList> vuelta = new HashMap<String,ArrayList>();
    List<String> paradas_ida = new ArrayList<String>();
    List<String> paradas_vuelta = new ArrayList<String>();
    List<Bitmap> bmp = new ArrayList<Bitmap>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_linea);
        Intent actlinea = getIntent();
        id_linea = actlinea.getStringExtra("id_linea");
        bmp.clear();
        new ParseoDatosLinea().execute();
        Log.e("Parseo",id_linea);
    }

    private void cargarLinea() {
        String tag = "Parseo";
        String nombre = "";
        String operador = "";
        String tieneNoticias = "";
        String tieneIda = "";
        String imgIda = "";
        String tieneVuelta = "";
        String imgVuelta = "";
        String pmr = "";
        seleccion = 0;

        List<String> boletin = new ArrayList<String>();
        //Leyendo datos de la línea.
        try {
            nombre = datos_linea.getString("nombre");
            operador = datos_linea.getString("operadores");
            tieneNoticias = datos_linea.getString("hayNoticias");
            tieneIda = datos_linea.getString("tieneIda");
            if (tieneIda.equals("1")) {
                imgIda = datos_linea.getString("termometroIda");
            }
            tieneVuelta = datos_linea.getString("tieneVuelta");
            if (tieneVuelta.equals("1")) {
                imgVuelta = datos_linea.getString("termometroVuelta");
            }
            pmr = datos_linea.getString("pmr");
        } catch (Exception e) {
            Log.e(tag, "Error al leer el JSON" + e);
        }
        //Leyendo datos de las paradas.
        if (tieneIda.equals("1")) {
            try {
                JSONArray nombres_paradas = datos_paradas.getJSONArray("bloquesIda");
                JSONArray horarios = datos_paradas.getJSONArray("horarioIda");
                for (int i = 0; i < nombres_paradas.length(); i++) {
                    if (nombres_paradas.getJSONObject(i).getString("tipo").equals("0")) {
                        ArrayList<String> horas = new ArrayList<String>();
                        for (int j = 0; j < horarios.length(); j++) {
                            horas.add(horarios.getJSONObject(j).getJSONArray("horas").getString(i));
                        }
                        paradas_ida.add(nombres_paradas.getJSONObject(i).getString("nombre"));
                        ida.put(nombres_paradas.getJSONObject(i).getString("nombre"), horas);
                    }
                }
            } catch (Exception e) {
                Log.e(tag, "Error al leer el JSON" + e);
            }
        } else {
            paradas_ida.add("No hay servicios disponibles");
            seleccion = 1;
        }
        if (tieneVuelta.equals("1")) {
            try {
                JSONArray nombres_paradas = datos_paradas.getJSONArray("bloquesVuelta");
                JSONArray horarios = datos_paradas.getJSONArray("horarioVuelta");
                for (int i = 0; i < nombres_paradas.length(); i++) {
                    if (nombres_paradas.getJSONObject(i).getString("tipo").equals("0")) {
                        ArrayList<String> horas = new ArrayList<String>();
                        for (int j = 0; j < horarios.length(); j++) {
                            horas.add(horarios.getJSONObject(j).getJSONArray("horas").getString(i));
                        }
                        paradas_vuelta.add(nombres_paradas.getJSONObject(i).getString("nombre"));
                        vuelta.put(nombres_paradas.getJSONObject(i).getString("nombre"), horas);
                    }
                }
            } catch (Exception e) {
                Log.e(tag, "Error al leer el JSON" + e);
            }
        } else {
            paradas_vuelta.add("No hay servicios disponibles");
            seleccion = 0;
        }
        //Leyendo noticias de la línea.
        if (tieneNoticias.equals("1")) {
            for (int i = 0; i > noticias.length(); i++) {
                try {
                    boletin.add(datos_linea.getString("titulo"));
                } catch (Exception e) {
                    Log.e(tag, "Error al leer el JSON" + e);
                }
            }
        } else {
            boletin.add("No hay noticias que mostrar acerca de esta línea.");
        }
        if (boletin.isEmpty()){
            boletin.add("No hay noticias que mostrar acerca de esta línea.");
        }
        //Presentar datos.
        cargar_imagenes(imgIda, imgVuelta);
        TextView txtnombre = (TextView) findViewById(R.id.nombrelinea);
        TextView txtoperador = (TextView) findViewById(R.id.operador);
        TextView txtnoticias = (TextView) findViewById(R.id.noticias);
        txtnombre.setText(nombre);
        txtoperador.setText(operador);
        txtnoticias.setText(boletin.toString() + "\n" + pmr);
        actualiza_datos();
    }
    private void actualiza_datos(){
        Switch switchidavuelta = (Switch) findViewById(R.id.switchIdaVuelta);
        switchidavuelta.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                Log.e("Parseo", "Cambiado");
                if(isChecked){
                    seleccion=1;
                }else{
                    seleccion=0;
                }
            }
        });
        ArrayAdapter adapterParadas = null;
        switch (seleccion) {
            case 0:
                adapterParadas = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,paradas_ida);
                switchidavuelta.setChecked(false);
                break;
            case 1:
                adapterParadas = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,paradas_vuelta);
                switchidavuelta.setChecked(true);
                break;
        }
        ListView paradaslv = (ListView)findViewById(R.id.paradasListView);
        paradaslv.setAdapter(adapterParadas);

        paradaslv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        paradaslv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Abre una nueva Activity:
                Intent myIntent = new Intent(view.getContext(), MainActivity2.class);
                String salida = (String) parent.getItemAtPosition(position);
                List<String> paramtiempo = new ArrayList<String>();
                switch (seleccion) {
                    case 0:
                        paramtiempo = ida.get(salida);
                        break;
                    case 1:
                        paramtiempo = vuelta.get(salida);
                        break;
                }
                if (paramtiempo!=null){
                    Log.e("Parseo",parent.getItemAtPosition(position).toString());
                    Log.e("Parseo",paramtiempo.toString());
                    //myIntent.putExtra("tiempos_paradas", paramtiempo);
                    startActivity(myIntent);
                }
            }
        });
    }
    private void actualiza_bitmap(){
        if (bmp!=null){
            ImageView img = (ImageView) findViewById(R.id.recorrido);
            img.setImageBitmap(bmp.get(seleccion));
        }
    }
    private void cargar_imagenes(String url1,String url2){
        if (url1!=""){
            new DownloadImageTask().execute(url1);
        }
        if (url2!=""){
            new DownloadImageTask().execute(url2);
        }
        new DownloadImageTask();
    }
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap>
    {
        protected Bitmap doInBackground(String... urls)
        {
            return descarga_imagen(urls[0]);
        }
        protected void onPostExecute(Bitmap result)
        {
            //Toast.makeText(getApplicationContext(), "Descargado.", Toast.LENGTH_SHORT).show();
            actualiza_bitmap();
        }
    }
    private Bitmap descarga_imagen(String url){
        try {
            Bitmap bitmap = BitmapFactory.decodeStream((InputStream)new URL(url).getContent());
            bmp.add(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    private class ParseoDatosLinea extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Toast.makeText(MainActivityLinea.this, "Espere, por favor.", Toast.LENGTH_LONG).show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            String tag = "Parseo";
            String frecuencia = "";
            Calendar hoy = Calendar.getInstance();
            int diasem = hoy.get(Calendar.DAY_OF_WEEK);
            switch (diasem){
                case 6:
                    frecuencia = "2";
                    break;
                case 7:
                    frecuencia = "3";
                    break;
                default:
                    frecuencia = "1";
            }
            String jsonStr1 = "";
            String jsonStr2 = "";
            String jsonStr3 = "";
            String url1 = "http://api.ctan.es/v1/Consorcios/1/lineas/" + id_linea;
            String url2 = "http://api.ctan.es/v1/Consorcios/1/horarios_lineas?&idLinea=" + id_linea + "&idFrecuencia=" + frecuencia;
            String url3 = "http://api.ctan.es/v1/Consorcios/1/lineas/" + id_linea + "/noticias";
            try {
                jsonStr1 = clienteHttp(url1);
                jsonStr2 = clienteHttp(url2);
                jsonStr3 = clienteHttp(url3);
                Log.e(tag, "Respuesta de HTML");
            } catch (Exception e) {
                Log.e(tag, "No hubo respuesta de HTML.");
            }
            if (jsonStr1 != null && jsonStr2 != null && jsonStr3 != null) {
                try {
                    datos_linea = new JSONObject(jsonStr1);
                    datos_paradas = new JSONObject(jsonStr2).getJSONArray("planificadores").getJSONObject(0);
                    noticias = new JSONObject(jsonStr3).getJSONArray("noticias");
                } catch (final JSONException e) {
                    Log.e(tag, "Error al parsear: " + e.getMessage());
                }
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            Toast.makeText(MainActivityLinea.this, "Descargado.", Toast.LENGTH_LONG).show();
            super.onPostExecute(result);
            cargarLinea();
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
