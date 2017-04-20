package com.example.alba.busessevilla;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

public class Main0Activity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main0);
    }

    public void btnWelcomeClicked(View view){
        Intent myIntent;
        myIntent = new Intent(getApplicationContext(), MainActivity1.class);
        startActivity(myIntent);

        TextView textLink = (TextView) findViewById(R.id.linkTextView);
        textLink.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
