package com.example.assignment;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ListViewHolder extends RecyclerView.ViewHolder {

    public TextView tv_address,tv_latitude,tv_longitude;
    public ImageView imageView;
    public LinearLayout clickLayoutitem;
    public ListViewHolder(@NonNull View itemView) {
        super(itemView);
        tv_address = (TextView) itemView.findViewById(R.id.tv_address);
        tv_latitude = (TextView) itemView.findViewById(R.id.tv_latitude);
        tv_longitude = (TextView)  itemView.findViewById(R.id.tv_longitude);
        imageView = (ImageView)  itemView.findViewById(R.id.imageView);
        clickLayoutitem = (LinearLayout)  itemView.findViewById(R.id.clickLayoutitem);
    }
}
