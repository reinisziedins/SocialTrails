package com.example.magebit.socialtrails;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RouteListActivity extends AppCompatActivity {
    DBHandler dbHandler;
    private ListView routeList;
    private ArrayList<String> Alist = new ArrayList<>();
    ArrayAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_list);
        dbHandler = MapsActivity.dbHandler;
        printDatabase();
        routeList = (ListView) findViewById(R.id.RouteList);
        outputRoute();
    }
    public void toMap(View v) {
        startActivity(new Intent(RouteListActivity.this, MapsActivity.class));
    }

    public void outputRoute() {
        final Object[] list = dbHandler.getRoute(0);
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
        routeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                id = ((Route) adapter.getItem(position)).get_id();
                Integer i = (int) (long) id;
                AddRouteActivity.editRouteId = i;
                startActivity(new Intent(RouteListActivity.this, AddRouteActivity.class));
            }
        });
        routeList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                id = ((Route) adapter.getItem(position)).get_id();
                String idString = String.valueOf(id);
                dbHandler.deleteRoute(idString);
                adapter.notifyDataSetChanged();
                Toast.makeText(RouteListActivity.this, "Route deleted", Toast.LENGTH_LONG).show();
                startActivity(new Intent(RouteListActivity.this, MapsActivity.class));
                return true;
            }
        });
        routeList.setAdapter(adapter);
    }
    public void printDatabase() {
        String dbString = dbHandler.databaseToString();
    }
}
