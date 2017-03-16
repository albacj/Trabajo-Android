package com.example.alba.busessevilla;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Main0Activity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main0);
    }

    public void btnWelcomeClicked(View view){
        Intent myIntent;
        myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(myIntent);
    }
}
