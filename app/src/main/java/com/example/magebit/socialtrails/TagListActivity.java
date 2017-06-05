package com.example.magebit.socialtrails;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class TagListActivity extends AppCompatActivity {
    DBHandler dbHandler;
    private ListView tagList;
    ArrayAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_list);
        dbHandler = MapsActivity.dbHandler;
        tagList = (ListView) findViewById(R.id.TagList);
        outputTags();
    }
    public void toMap(View v) {
        startActivity(new Intent(TagListActivity.this, MapsActivity.class));
    }
    public void outputTags() {
        final List<Tag> tags = dbHandler.getAllTags();
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, tags);
        tagList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = ((Tag) adapter.getItem(position)).get_name();
                MapsActivity.filterTagId = name;
                startActivity(new Intent(TagListActivity.this, MapsActivity.class));
            }
        });
    /*    tagList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
          *//*      id = ((Route) adapter.getItem(position)).get_id();
                String idString = String.valueOf(id);
                dbHandler.deleteRoute(idString);
                adapter.notifyDataSetChanged();
                Toast.makeText(RouteListActivity.this, "Route deleted", Toast.LENGTH_LONG).show();
                startActivity(new Intent(RouteListActivity.this, MapsActivity.class));
                return true;*//*
            }
        });*/
        tagList.setAdapter(adapter);
    }

}
