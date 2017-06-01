package com.example.magebit.socialtrails;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.GoogleMap;

public class FiletMenuActivity extends AppCompatActivity {
    static int filterParameter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filet_menu);

    }
    public void filterDay(View v) {
        filterParameter = 1;
        startActivity(new Intent(FiletMenuActivity.this, MapsActivity.class));
    }
    public void filterWeek(View v) {
        filterParameter = 2;
        startActivity(new Intent(FiletMenuActivity.this, MapsActivity.class));
    }
    public void filterMonth(View v) {
        filterParameter = 3;
        startActivity(new Intent(FiletMenuActivity.this, MapsActivity.class));
    }
    public void filterAll(View v) {
        filterParameter = 4;
        startActivity(new Intent(FiletMenuActivity.this, MapsActivity.class));
    }
    public void filterRoutes(int option) {

    }


    public void toMap(View v) {
        startActivity(new Intent(FiletMenuActivity.this, MapsActivity.class));
    }
}
