package com.example.proiectmaslinca;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.appcompat.app.AppCompatActivity;


public class Galerie extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galerie);

        Intent intent = getIntent();
        String cuvantCheie = intent.getStringExtra("PARAMETRU_CAUTARE");

        // Colectie imagini
        Integer[] imageIds = {R.drawable.ic_launcher_background, R.drawable.ic_launcher_foreground};

        // GridView unde vor fi afisate imaginile
        GridView gridView = findViewById(R.id.gridView);

        // Adapter ce contine colectia de imagini si layout pentru afisarea unei imagini din grid
        CustomAdapter adapter = new CustomAdapter(this, R.layout.grid_view_item, imageIds);
        gridView.setAdapter(adapter);

        // Listener atingere imagine din grid
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int imageId = imageIds[position];
                System.out.println(imageId);
            }
        });
    }
}