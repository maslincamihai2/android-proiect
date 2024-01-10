package com.example.proiectmaslinca;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

public class CustomAdapter extends ArrayAdapter<Integer> {
    private final Context context;

    CustomAdapter(Context context, int resource, Integer[] objects) {
        super(context, resource, objects);
        this.context = context;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        // Creem un obiect software folosind fisierul XML  grid_view_item
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.grid_view_item, parent, false);
            System.out.println("***");
        }

        ImageView imageView = view.findViewById(R.id.imageView);
        // incarcare imagine
        // Metoda getItem() e mostenita de la superclasa
        imageView.setImageResource(getItem(position));

        return view;
    }
}
