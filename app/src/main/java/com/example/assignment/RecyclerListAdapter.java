package com.example.assignment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerListAdapter extends RecyclerView.Adapter<ListViewHolder> {

    private ArrayList<DataModel> list_array;

    LayoutInflater inflater;

    private final Context context;

    public RecyclerListAdapter(ArrayList<DataModel> list_array, Context context) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.list_array = list_array;
        this.context = context;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View rowView = inflater.inflate(R.layout.address_list, viewGroup, false);
        ListViewHolder nocview = new ListViewHolder(rowView);
        return nocview;
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder listViewHolder, int i) {
        final DataModel dataModel = list_array.get(i);
        Bitmap bitmap = BitmapFactory.decodeByteArray(dataModel.getImage(), 0, dataModel.getImage().length);
        listViewHolder.tv_address.setText(dataModel.getAddress());
        listViewHolder.tv_latitude.setText(dataModel.getLat());
        listViewHolder.tv_longitude.setText(dataModel.getLang());
        listViewHolder.imageView.setImageBitmap(bitmap);
        listViewHolder.clickLayoutitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,MapsActivity.class);
                intent.putExtra("latitude",dataModel.getLat());
                intent.putExtra("longitude",dataModel.getLang());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list_array.size();
    }

}
