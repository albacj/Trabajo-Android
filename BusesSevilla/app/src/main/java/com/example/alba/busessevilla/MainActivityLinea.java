package com.example.alba.busessevilla;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

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

public class MainActivityLinea extends Activity {

    String id_linea;
    String seleccion;
    JSONObject datos_linea;
    JSONObject datos_bloques;
    JSONArray noticias;
    JSONArray datos_paradas;
    JSONArray bloques_ida;
    JSONArray bloques_vuelta;
    HashMap<String, String> mapa_municipios = new HashMap<String, String>();
    ArrayList<Parada> paradas_ida = new ArrayList<>();
    ArrayList<Parada> paradas_vuelta = new ArrayList<>();
    HashMap<String,Bitmap> bmp = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_linea);
        Intent actlinea = getIntent();
        id_linea = actlinea.getStringExtra("id_linea");
        mapa_municipios = (HashMap<String, String>) actlinea.getExtras().getSerializable("mapa_municipios");
        Switch swit = (Switch) findViewById(R.id.switchIdaVuelta);
        swit.setVisibility(View.INVISIBLE);
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
        seleccion = "0";
        ProgressBar progreso = (ProgressBar) findViewById(R.id.progreso);
        progreso.setVisibility(View.INVISIBLE);
        List<String> boletin = new ArrayList<>();
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
        if (datos_bloques!=null){
            if (tieneIda.equals("1")) {
                try {
                    JSONArray nombres_bloques = datos_bloques.getJSONArray("bloquesIda");
                    JSONArray horarios = datos_bloques.getJSONArray("horarioIda");
                    String ultimo = "";
                    for (int i = 0; i < datos_paradas.length(); i++){
                        if (datos_paradas.getJSONObject(i).getString("sentido").equals("1")){
                            String nombre_parada = datos_paradas.getJSONObject(i).getString("nombre");
                            String municipio_parada = datos_paradas.getJSONObject(i).getString("idNucleo");
                            String latitud_parada = datos_paradas.getJSONObject(i).getString("latitud");
                            String longitud_parada = datos_paradas.getJSONObject(i).getString("longitud");
                            Parada elemento = new Parada(nombre_parada,latitud_parada,longitud_parada,true);
                            if (!municipio_parada.equals(ultimo)){
                                elemento.setMunicipio(mapa_municipios.get(municipio_parada));
                                ultimo = municipio_parada;
                            }
                            paradas_ida.add(elemento);
                        }
                    }
                    List<Integer> orden = new ArrayList<>();
                    orden.add(0);
                    for (int i = 0; i < bloques_ida.length(); i++){
                        orden.add(Integer.valueOf(bloques_ida.getJSONObject(i).getString("orden")));
                    }
                    for (int i = 0; i < nombres_bloques.length(); i++) {
                        if (nombres_bloques.getJSONObject(i).getString("tipo").equals("0")) {
                            for (int j =  orden.get(i); j < orden.get(i+1); j++){
                                ArrayList<String> horas = new ArrayList<>();
                                for (int k = 0; k < horarios.length(); k++) {
                                    horas.add(horarios.getJSONObject(k).getJSONArray("horas").getString(i));
                                }
                                paradas_ida.get(j).setHorarios(horas);
                            }
                        }
                    }
                } catch (Exception e) {
                    Log.e(tag, "Error al leer el JSON" + e);
                }
            } else {
                paradas_ida.add(new Parada(getString(R.string.sin_servicios),"","",false));
                seleccion = "1";
            }
            if (tieneVuelta.equals("1")) {
                try {
                    JSONArray nombres_bloques = datos_bloques.getJSONArray("bloquesVuelta");
                    JSONArray horarios = datos_bloques.getJSONArray("horarioVuelta");
                    String ultimo = "";
                    for (int i = 0; i < datos_paradas.length(); i++){
                        if (datos_paradas.getJSONObject(i).getString("sentido").equals("2")){
                            String nombre_parada = datos_paradas.getJSONObject(i).getString("nombre");
                            String municipio_parada = datos_paradas.getJSONObject(i).getString("idNucleo");
                            String latitud_parada = datos_paradas.getJSONObject(i).getString("latitud");
                            String longitud_parada = datos_paradas.getJSONObject(i).getString("longitud");
                            Parada elemento = new Parada(nombre_parada,latitud_parada,longitud_parada,true);
                            if (!municipio_parada.equals(ultimo)){
                                elemento.setMunicipio(mapa_municipios.get(municipio_parada));
                                ultimo = municipio_parada;
                            }
                            paradas_vuelta.add(elemento);
                        }
                    }
                    List<Integer> orden = new ArrayList<>();
                    orden.add(0);
                    for (int i = 0; i < bloques_vuelta.length(); i++){
                        orden.add(Integer.valueOf(bloques_vuelta.getJSONObject(i).getString("orden")));
                    }
                    for (int i = 0; i < nombres_bloques.length(); i++) {
                        if (nombres_bloques.getJSONObject(i).getString("tipo").equals("0")) {
                            for (int j =  orden.get(i); j < orden.get(i+1); j++){
                                ArrayList<String> horas = new ArrayList<>();
                                for (int k = 0; k < horarios.length(); k++) {
                                    horas.add(horarios.getJSONObject(k).getJSONArray("horas").getString(i));
                                }
                                paradas_vuelta.get(j).setHorarios(horas);
                            }
                        }
                    }
                } catch (Exception e) {
                    Log.e(tag, "Error al leer el JSON" + e);
                }
            } else {
                paradas_vuelta.add(new Parada(getString(R.string.sin_servicios),"","",false));
                seleccion = "0";
            }
        } else {
            paradas_ida.add(new Parada(getString(R.string.sin_servicios),"","",false));
            paradas_vuelta.add(new Parada(getString(R.string.sin_servicios),"","",false));
        }
        //Leyendo noticias de la línea.
        if (tieneNoticias.equals("1")) {
            if (noticias!=null){
                for (int i = 0; i > noticias.length(); i++) {
                    try {
                        boletin.add(datos_linea.getString("titulo"));
                    } catch (Exception e) {
                        Log.e(tag, "Error al leer el JSON" + e);
                    }
                }
            }
        } else {
            boletin.add(getString(R.string.sin_noticias));
        }
        if (boletin.isEmpty()){
            boletin.add(getString(R.string.sin_noticias));
        }
        //Presentar datos.
        cargar_imagenes(imgIda, imgVuelta);
        TextView txtnombre = (TextView) findViewById(R.id.nombrelinea);
        TextView txtoperador = (TextView) findViewById(R.id.operador);
        TextView txtcabecera = (TextView) findViewById(R.id.cabeceranoticias);
        TextView txtnoticias = (TextView) findViewById(R.id.noticias);
        txtnombre.setText(nombre);
        txtoperador.setText(operador);
        txtcabecera.setText(getString(R.string.info));
        String texto = "";
        for (String elemento: boletin){
            texto = texto.concat(elemento + "\n");
        }
        txtnoticias.setText(pmr + ".\n" + texto);
        ArrayList<Parada> seleccionado = new ArrayList<>();
        Switch switchidavuelta = (Switch) findViewById(R.id.switchIdaVuelta);
        switch (seleccion) {
            case "0":
                seleccionado = paradas_ida;
                switchidavuelta.setChecked(false);
                break;
            case "1":
                seleccionado = paradas_vuelta;
                switchidavuelta.setChecked(true);
                break;
        }
        switchidavuelta.setVisibility(View.VISIBLE);
        ListView paradaslv = (ListView)findViewById(R.id.paradasListView);
        paradaslv.setVisibility(View.VISIBLE);
        paradaslv.setAdapter(new Lista_adaptador(this, R.layout.entrada3, seleccionado){
            @Override
            public void onEntrada(Object entrada, View view) {
                TextView texto_superior_entrada = (TextView) view.findViewById(R.id.municipioTextView);
                String municipio = ((Parada) entrada).getMunicipio();
                texto_superior_entrada.setText(municipio);
                TextView texto_inferior_entrada = (TextView) view.findViewById(R.id.paradaTextView);
                texto_inferior_entrada.setText(((Parada) entrada).getNombre());
            }
        });
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
                Parada salida = (Parada) parent.getItemAtPosition(position);
                if (salida.esValido()){
                    Log.e("Parseo",parent.getItemAtPosition(position).toString());
                    TextView txtnombre = (TextView) findViewById(R.id.nombrelinea);
                    myIntent.putExtra("nombre_linea", txtnombre.getText());
                    myIntent.putExtra("nombre_parada", salida.getNombre());
                    myIntent.putExtra("latitud", salida.getLatitud());
                    myIntent.putExtra("longitud", salida.getLongitud());
                    myIntent.putStringArrayListExtra("tiempos_paradas", salida.getHorarios());
                    startActivity(myIntent);
                }
            }
        });
        switchidavuelta.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                Log.e("Parseo", "Cambiado");
                if(isChecked){
                    seleccion="1";
                    actualiza_datos();
                }else{
                    seleccion="0";
                    actualiza_datos();
                }
            }
        });
    }
    private void actualiza_datos(){
        ArrayList<Parada> seleccionado = new ArrayList<>();
        switch (seleccion) {
            case "0":
                seleccionado = paradas_ida;
                break;
            case "1":
                seleccionado = paradas_vuelta;
                break;
        }
        ListView paradaslv = (ListView)findViewById(R.id.paradasListView);
        paradaslv.setAdapter(new Lista_adaptador(this, R.layout.entrada3, seleccionado){
            @Override
            public void onEntrada(Object entrada, View view) {
                TextView texto_superior_entrada = (TextView) view.findViewById(R.id.municipioTextView);
                String municipio = ((Parada) entrada).getMunicipio();
                texto_superior_entrada.setText(municipio);
                TextView texto_inferior_entrada = (TextView) view.findViewById(R.id.paradaTextView);
                texto_inferior_entrada.setText(((Parada) entrada).getNombre());
            }
        });
        if (!bmp.isEmpty()){
            actualiza_bitmap();
        }
    }
    private void actualiza_bitmap(){
        ImageView img = (ImageView) findViewById(R.id.recorrido);
        img.setImageBitmap(bmp.get(seleccion));
        Log.e("Parseo",bmp.toString() + "\n" + "seleccionado " + seleccion + "; " + bmp.get(seleccion));
    }
    private void cargar_imagenes(String url1,String url2){
        if (!url1.equals("")){
            new DownloadImageTask().execute(new String[]{url1, "0"});
        }
        if (!url2.equals("")){
            new DownloadImageTask().execute(new String[]{url2, "1"});
        }
        new DownloadImageTask();
    }
    private class DownloadImageTask extends AsyncTask<String[], Void, Bitmap>
    {
        protected Bitmap doInBackground(String[]... urls)
        {
            return descarga_imagen(urls[0]);
        }
        protected void onPostExecute(Bitmap result)
        {
            //Toast.makeText(getApplicationContext(), "Descargado.", Toast.LENGTH_SHORT).show();
            actualiza_bitmap();
        }
    }
    private Bitmap descarga_imagen(String[] array){
        try {
            Bitmap bitmap = BitmapFactory.decodeStream((InputStream)new URL(array[0]).getContent());

            bmp.put(array[1],bitmap);
            Log.e("Parseo",bmp.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    private class ParseoDatosLinea extends AsyncTask<Void, Void, Void> {
        String error = "";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Toast.makeText(MainActivityLinea.this, "Espere, por favor.", Toast.LENGTH_LONG).show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            String tag = "Parseo";
            String frecuencia;
            Calendar hoy = Calendar.getInstance();
            int diasem = hoy.get(Calendar.DAY_OF_WEEK);
            switch (diasem){
                case Calendar.SATURDAY:
                    frecuencia = "2";
                    break;
                case Calendar.SUNDAY:
                    frecuencia = "3";
                    break;
                default:
                    frecuencia = "1";
            }
            String jsonStr1 = "";
            String jsonStr2 = "";
            String jsonStr3 = "";
            String jsonStr4 = "";
            String jsonStr5 = "";
            String jsonStr6 = "";
            String url1 = "http://api.ctan.es/v1/Consorcios/1/lineas/" + id_linea;
            String url2 = "http://api.ctan.es/v1/Consorcios/1/horarios_lineas?&idLinea=" + id_linea + "&idFrecuencia=" + frecuencia;
            String url3 = "http://api.ctan.es/v1/Consorcios/1/lineas/" + id_linea + "/noticias";
            String url4 = "http://api.ctan.es/v1/Consorcios/1/lineas/" + id_linea + "/paradas";
            String url5 = "http://api.ctan.es/v1/Consorcios/1/lineas/" + id_linea + "/bloques?&sentido=1";
            String url6 = "http://api.ctan.es/v1/Consorcios/1/lineas/" + id_linea + "/bloques?&sentido=2";
            try {
                jsonStr1 = clienteHttp(url1);
                jsonStr2 = clienteHttp(url2);
                jsonStr3 = clienteHttp(url3);
                jsonStr4 = clienteHttp(url4);
                jsonStr5 = clienteHttp(url5);
                jsonStr6 = clienteHttp(url6);
                Log.e(tag, "Respuesta de HTML");
            } catch (Exception e) {
                Log.e(tag, "No hubo respuesta de HTML.");
                error = e.getMessage();
            }
            if (jsonStr1 != null && jsonStr2 != null && jsonStr3 != null) {
                try {
                    datos_linea = new JSONObject(jsonStr1);
                    noticias = new JSONObject(jsonStr3).getJSONArray("noticias");
                    datos_bloques = new JSONObject(jsonStr2).getJSONArray("planificadores").getJSONObject(0);
                    datos_paradas = new JSONObject(jsonStr4).getJSONArray("paradas");
                    bloques_ida = new JSONObject(jsonStr5).getJSONArray("bloques");
                    bloques_vuelta = new JSONObject(jsonStr6).getJSONArray("bloques");
                } catch (final JSONException e) {
                    Log.e(tag, "Error al parsear: " + e.getMessage());
                }
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            //Toast.makeText(MainActivityLinea.this, "Descargado.", Toast.LENGTH_LONG).show();
            super.onPostExecute(result);
            if (error.equals("")){
                cargarLinea();
            } else {
                mostraralerta(error);
            }
        }
    }
    private void mostraralerta(String mensaje){
        try{
            ProgressBar progreso = (ProgressBar) findViewById(R.id.progreso);
            progreso.setVisibility(View.INVISIBLE);
            AlertDialog.Builder dialogo = new AlertDialog.Builder(MainActivityLinea.this);
            dialogo.setTitle(getText(R.string.error0));
            dialogo.setMessage(mensaje);
            dialogo.setPositiveButton(getText(R.string.ok), new DialogInterface.OnClickListener() {
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
        if (body.equals("")){
            throw new RuntimeException(getString(R.string.error1));
        }
        return body;
    }

}
