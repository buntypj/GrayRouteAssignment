package com.example.assignment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class ListDisplayActivity extends AppCompatActivity {

    RecyclerView recyclerview_display;
    RecyclerListAdapter adapter;
    private ArrayList<DataModel> fieldValues;
    DBHelper myDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_display);
        recyclerview_display = (RecyclerView)findViewById(R.id.recyclerview_display);

        myDB = new DBHelper(ListDisplayActivity.this);
        fieldValues = myDB.getAlldata();
        adapter = new RecyclerListAdapter(fieldValues,ListDisplayActivity.this);
        recyclerview_display.setAdapter(adapter);
        recyclerview_display.setLayoutManager(new LinearLayoutManager(ListDisplayActivity.this));
    }
}
