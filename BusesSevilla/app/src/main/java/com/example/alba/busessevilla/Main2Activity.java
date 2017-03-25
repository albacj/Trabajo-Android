package com.example.alba.busessevilla;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class Main2Activity extends Activity {

    private TextView puebloTextView;
    private TextView trayectoTextView;
    private TextView paradaTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        puebloTextView = (TextView)findViewById(R.id.puebloDatoTextView);
        trayectoTextView = (TextView)findViewById(R.id.trayectoDatoTextView);
        paradaTextView = (TextView)findViewById(R.id.paradaDatoTextView);

        String puebloDato = getIntent().getStringExtra("pueblo");
        String trayectoDato = getIntent().getStringExtra("sentido");
        String paradaDato = getIntent().getStringExtra("parada");

        puebloTextView.setText(puebloDato);
        trayectoTextView.setText(trayectoDato);
        paradaTextView.setText(paradaDato);
    }
}
