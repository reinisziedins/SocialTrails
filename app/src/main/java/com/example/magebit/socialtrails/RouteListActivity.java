package com.example.magebit.socialtrails;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

public class RouteListActivity extends AppCompatActivity {
    DBHandler dbHandler;
    TextView outputRoute;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_list);
        dbHandler = MapsActivity.dbHandler;
        outputRoute = (TextView) findViewById(R.id.outputRouteField);
        printDatabase();
    }
    public void toMap(View v) {
        startActivity(new Intent(RouteListActivity.this, MapsActivity.class));
    }
    public void printDatabase() {
        String dbString = dbHandler.databaseToString();
        if (dbString != null) {
            outputRoute.setText(dbString);
        }
    }
}
