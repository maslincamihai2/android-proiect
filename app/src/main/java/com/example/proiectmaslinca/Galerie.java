package com.example.proiectmaslinca;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Call;
import okhttp3.ResponseBody;

import android.Manifest;

public class Galerie extends AppCompatActivity {

    private Context context = this;
    private String API_URL = "https://api.pexels.com/v1/search?query=";
    private String AUTH_TOKEN = "zAaAIHSnT2MhXQReGR1cmyX3OE1E3PhZb6HUo1f3d3Alu5Bd5adlt0uZ";
    private int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 123; //cod random pentru permisiune

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
                // Se obtine obiectul Photo corespunzator imaginii apasate
                Photo photo = colectieObiectePhoto.get(position);
                // Se obtine url-ul imaginii din raspunsul primit de la API
                Src src = photo.src;
                String url = src.portrait;

                // Verifica daca permisiunea a fost data anterior
                int verificaPermisiune = ContextCompat.checkSelfPermission(context,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE);

                if (verificaPermisiune != PackageManager.PERMISSION_GRANTED) {
                    // Daca nu a fost data permisiunea anterior
                    // Se cere permisiunea acum folosind codul random din variabila MY_PERMISSION_REQUEST

                    // Metoda requestPermissions() cere ca permisiunile sa fie intr-un array
                    String[] permisiuni = new String[1];
                    permisiuni[0] = Manifest.permission.WRITE_EXTERNAL_STORAGE;

                    // Aici se cer permisiunile
                    // Si se apeleaza implicit onRequestPermissionsResult() definit mai jos
                    ActivityCompat.requestPermissions(Galerie.this,
                            permisiuni,
                            MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                } else {
                    // Daca permisiunea a fost acceptata anterior se incepe direct descarcarea
                    TaskDownloadImagine task = new TaskDownloadImagine(context);
                    task.execute(url, "exemplu");
                }
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Se verifica request codul si in functie de acesta se poate cere o permisiune sau alta
        if (requestCode == MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE) {
            // Daca permisiunea a fost acordata din interfata
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Se afiseaza un mesaj si trebuie reincercata descarcarea
                Toast.makeText(this, "Permisiune acordata. Reincercati descarcarea.",
                        Toast.LENGTH_SHORT).show();
            } else {
                // Altfel se afiseaza un alt mesaj de avertizare
                Toast.makeText(this, "Permisiunea nu a fost acordata",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}