package com.example.proiectmaslinca;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Referinte butoane
        Button butonNatura = findViewById(R.id.button_natura);
        Button butonAnimale = findViewById(R.id.button_animale);

        // Listener butoane
        butonNatura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                afiseazaGalerie("nature");
            }
        });

        butonAnimale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                afiseazaGalerie("animal");
            }
        });
    }

    // Metoda pentru lansarea activitatii urmatoare
    private void afiseazaGalerie(String cuvantCheie){
        Intent intent = new Intent(this, Galerie.class);
        // Transmiterea datelor catre urmatoarea activitate prin intent
        intent.putExtra("PARAMETRU_CAUTARE", cuvantCheie);
        startActivity(intent);
    }
}