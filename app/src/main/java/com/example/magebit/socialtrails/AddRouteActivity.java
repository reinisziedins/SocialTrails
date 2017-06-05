package com.example.magebit.socialtrails;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class AddRouteActivity extends AppCompatActivity {
    Button dateButton, timeButton;
    int year_x, month_x, day_x, hour_x, minute_x;
    static int currentY, currentM, currentD;
    static final int DATEDILOG_ID = 0;
    static final int TIMEDILOG_ID = 1;
    static int editRouteId = 0;
    DBHandler dbHandler;
    EditText routeName;
    EditText tagName;
    EditText routeDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_route);
        year_x = currentY = MapsActivity.currentY;
        month_x = currentM = MapsActivity.currentM;
        day_x = currentD = MapsActivity.currentD;
        showDialogOnClick();
        routeName = (EditText) findViewById(R.id.RouteName);
        tagName = (EditText) findViewById(R.id.RouteTag);
        routeDescription = (EditText) findViewById(R.id.RouteDescription);
        dbHandler = MapsActivity.dbHandler;
        if (editRouteId != 0) {
            final Object[] list = dbHandler.getRoute(0);
            for (Object route : list) {
                if (editRouteId == ((Route) route).get_id()) {
                    routeName.setText(((Route) route).get_name());
                    routeDescription.setText(((Route) route).get_description());
                    year_x = ((Route) route).getYear_x();
                    month_x = ((Route) route).getMonth_x();
                    day_x = ((Route) route).getDay_x();
                    hour_x = ((Route) route).getHour_x();
                    minute_x = ((Route) route).getMinute_x();
                }
            }
        }
    }

    public void toMap(View v) {
        editRouteId = 0;
        startActivity(new Intent(AddRouteActivity.this, MapsActivity.class));
    }

    public void showDialogOnClick() {
        dateButton = (Button) findViewById(R.id.dateButton);
        timeButton = (Button) findViewById(R.id.timeButton);
        timeButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDialog(TIMEDILOG_ID);
                    }
                }
        );
        dateButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDialog(DATEDILOG_ID);
                    }
                }
        );
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == DATEDILOG_ID)
            return new DatePickerDialog(this, dpickerListener, year_x, month_x, day_x);
        else if (id == TIMEDILOG_ID)
            return new TimePickerDialog(this, tpickerListener, hour_x, minute_x, true);
        return null;
    }

    private DatePickerDialog.OnDateSetListener dpickerListener
            = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            if (currentY > year || (currentY == year && currentM > month) || (currentM == month && currentD > dayOfMonth)) {
                Toast.makeText(AddRouteActivity.this, "Please select a future date", Toast.LENGTH_LONG).show();
            } else {
                year_x = year;
                month_x = month;
                day_x = dayOfMonth;
                Toast.makeText(AddRouteActivity.this, year_x + "/" + month_x + "/" + day_x, Toast.LENGTH_LONG).show();
            }
        }
    };
    private TimePickerDialog.OnTimeSetListener tpickerListener
            = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            hour_x = hourOfDay;
            minute_x = minute;
            Toast.makeText(AddRouteActivity.this, hour_x + ":" + minute_x, Toast.LENGTH_LONG).show();
        }
    };

    public void addRoute(View view) {
        Route route = new Route(0, routeName.getText().toString(),
                MapsActivity.startMarkerLat,
                MapsActivity.startMarkerLng,
                MapsActivity.finishMarkerLat,
                MapsActivity.finishMarkerLng,
                routeDescription.getText().toString(),
                minute_x,
                hour_x,
                day_x,
                month_x,
                year_x
        );
        Tag tag = new Tag(0, tagName.getText().toString());
        if (editRouteId == 0 && MapsActivity.isRoute) {
            int tagId = dbHandler.addTag(tag);
            dbHandler.addRoute(route, tagId);
            Toast.makeText(AddRouteActivity.this, "Route " + routeName.getText().toString() + " added", Toast.LENGTH_LONG).show();
        } else {
            route.set_id(editRouteId);
            dbHandler.editRoute(route);
            editRouteId = 0;
            Toast.makeText(AddRouteActivity.this, "Route edited", Toast.LENGTH_LONG).show();
        }
        startActivity(new Intent(AddRouteActivity.this, MapsActivity.class));

    }


}
