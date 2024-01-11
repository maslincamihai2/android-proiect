package com.example.proiectmaslinca;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Call;
import okhttp3.ResponseBody;


public class Galerie extends AppCompatActivity {

    private String API_URL = "https://api.pexels.com/v1/search?query=";
    private String AUTH_TOKEN = "zAaAIHSnT2MhXQReGR1cmyX3OE1E3PhZb6HUo1f3d3Alu5Bd5adlt0uZ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galerie);

        Intent intent = getIntent();
        String cuvantCheie = intent.getStringExtra("PARAMETRU_CAUTARE");
        // Concatenam cuvantul cheie la URL-ul catre care se face cererea
        API_URL += cuvantCheie;

        makeRequest();
    }

    private void makeRequest() {
        // Se creeaza o instanta OkHttp
        OkHttpClient client = new OkHttpClient();

        // Se construieste cererea folosind cheia pentru accesarea API-ului
        Request request = new Request.Builder()
                .url(API_URL)
                .addHeader("Authorization", AUTH_TOKEN)
                .build();

        // Se face apelul asincron cu enqueue()
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    // Se parseaza sirul de caractere in format JSON obtinut de la API
                    ResponseBody responseBody = response.body();
                    if (responseBody != null) {
                        String json = responseBody.string();
                        System.out.println(json);

                        Gson gson = new Gson();
                        RaspunsAPI raspunsAPI = gson.fromJson(json, RaspunsAPI.class);

                        // Se acceseaza colectia de obiecte Photo returnata API
                        // din obiectul clasei RaspunsAPI
                        ArrayList<Photo> colectieObiectePhoto = raspunsAPI.photos;

                        // Se actualizeaza interfata cu imaginile returnate de API
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setupGridView(colectieObiectePhoto);
                            }
                        });
                    }
                } else {
                    // Exista situatia cand apelul este reusit, dar nu se returneaza datele
                    // De exemplu cand se foloseste un API key incorect
                    System.out.println("Apel reusit, cod returnat: " + response.code());
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                System.out.println("Cererea catre API a esuat");
            }
        });
    }

    private void setupGridView(ArrayList<Photo> colectieObiectePhoto){
        // GridView unde vor fi afisate imaginile
        GridView gridView = findViewById(R.id.gridView);

        // Adapter ce contine colectia de imagini si layout pentru afisarea unei imagini din grid
        CustomAdapter adapter = new CustomAdapter(this, R.layout.grid_view_item, colectieObiectePhoto);
        gridView.setAdapter(adapter);

        // Listener atingere imagine din grid
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }
}