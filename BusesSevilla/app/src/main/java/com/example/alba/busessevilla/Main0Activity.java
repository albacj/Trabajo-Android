package com.example.alba.busessevilla;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class Main0Activity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main0);
        cargaSpinnerIdioma();
    }

    public void cargaSpinnerIdioma(){
        final Spinner idiomaspinner = (Spinner) findViewById(R.id.idiomaSpinner);
        final HashMap<Integer, String> mapa_idiomas = new HashMap<>();
        mapa_idiomas.put(R.mipmap.es, "es");
        mapa_idiomas.put(R.mipmap.en, "en");
        mapa_idiomas.put(R.mipmap.fr, "fr");
        mapa_idiomas.put(R.mipmap.it, "it");
        mapa_idiomas.put(R.mipmap.ja, "ja");
        ArrayList<Integer> lista_idiomas = new ArrayList<>();
        lista_idiomas.add(R.mipmap.idioma);
        lista_idiomas.add(R.mipmap.es);
        lista_idiomas.add(R.mipmap.en);
        lista_idiomas.add(R.mipmap.fr);
        lista_idiomas.add(R.mipmap.it);
        lista_idiomas.add(R.mipmap.ja);
        Lista_adaptador adapter_idiomas = new Lista_adaptador(this, R.layout.entrada4, lista_idiomas) {
            @Override
            public void onEntrada(Object entrada, View view) {
                ImageView bandera = (ImageView) view.findViewById(R.id.bandera);
                bandera.setImageResource((Integer) entrada);
            }
        };
        idiomaspinner.setAdapter(adapter_idiomas);
        idiomaspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cambiarIdioma(mapa_idiomas.get((Integer) idiomaspinner.getSelectedItem()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void btnWelcomeClicked(View view){
        Intent myIntent;
        myIntent = new Intent(getApplicationContext(), MainActivity1.class);
        startActivity(myIntent);

        TextView textLink = (TextView) findViewById(R.id.linkTextView);
        textLink.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void cambiarIdioma(String idioma) {
        if (idioma!=null) {
            Locale locale = new Locale(idioma);
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.setLocale(locale);
            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
            //getApplicationContext().createConfigurationContext(config);
            this.setContentView(R.layout.activity_main0);
            Toast.makeText(getApplicationContext(), getString(R.string.cambio_idioma), Toast.LENGTH_SHORT).show();
            cargaSpinnerIdioma();
        }
    }

    public void irAPaginaCTSE(View view){
        try {
            Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.consorciotransportes-sevilla.com/"));
            startActivity(myIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, getString(R.string.sin_navegador),  Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}
