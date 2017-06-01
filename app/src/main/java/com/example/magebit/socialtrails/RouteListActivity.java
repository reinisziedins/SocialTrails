package com.example.magebit.socialtrails;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class RouteListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_list);
    }
    public void toMap(View v) {
        startActivity(new Intent(RouteListActivity.this, MapsActivity.class));
    }
}
