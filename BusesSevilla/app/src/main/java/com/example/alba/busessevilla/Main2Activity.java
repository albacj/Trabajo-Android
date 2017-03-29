package com.example.alba.busessevilla;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class Main2Activity extends Activity {

    private TextView puebloOrigenTextView;
    private TextView paradaTextView;
    private TextView puebloDestinoTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        puebloOrigenTextView = (TextView)findViewById(R.id.puebloDatoOrigenTextView);
        paradaTextView = (TextView)findViewById(R.id.puebloDatoDestinoTextView);
        puebloDestinoTextView = (TextView)findViewById(R.id.paradaDatoTextView);

        String puebloOrigenDato = getIntent().getStringExtra("puebloOrigen");
        String paradaDato = getIntent().getStringExtra("parada");
        String puebloDestinoDato = getIntent().getStringExtra("puebloDestino");

        puebloOrigenTextView.setText(puebloOrigenDato);
        paradaTextView.setText(paradaDato);
        puebloDestinoTextView.setText(puebloDestinoDato);
    }
}
