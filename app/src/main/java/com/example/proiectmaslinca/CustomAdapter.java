package com.example.proiectmaslinca;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<Integer> {

    private Context context;
    private ArrayList<Photo> colectieObiectePhoto;

    CustomAdapter(Context context, int resource, ArrayList<Photo> colectieObiectePhoto) {
        super(context, resource);
        this.context = context;
        this.colectieObiectePhoto = colectieObiectePhoto;
    }

    // Daca nu functioneaza corect getCount() atunci
    // Nu se apeleaza getView()
    @Override
    public int getCount(){
        return colectieObiectePhoto.size();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        // Creem un obiect software folosind fisierul XML  grid_view_item
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.grid_view_item, parent, false);
        }

        ImageView imageView = view.findViewById(R.id.imageView);
        // incarcare imagine folosind Picasso
        Photo photo = colectieObiectePhoto.get(position);
        Src src = photo.src;
        String url = src.portrait;

        Picasso instantaPicasso = Picasso.get();
        // Metoda load() incarca imaginea asincron
        instantaPicasso.load(url).into(imageView);
        System.out.println(url);

        return view;
    }
}
