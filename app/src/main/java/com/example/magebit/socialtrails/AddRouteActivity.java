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
import android.widget.TimePicker;
import android.widget.Toast;

public class AddRouteActivity extends AppCompatActivity {
    Button dateButton, timeButton;
    int year_x,month_x,day_x,hour_x,minute_x, currentY, currentM, currentD;
    static final int DATEDILOG_ID = 0;
    static final int TIMEDILOG_ID = 1;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_route);

        final Calendar cal = Calendar.getInstance();
        year_x = currentY = cal.get(Calendar.YEAR);
        month_x = currentM = cal.get(Calendar.MONTH);
        day_x = currentD = cal.get(Calendar.DAY_OF_MONTH);
        showDialogOnClick();
    }
    public void toMap(View v) {
        startActivity(new Intent(AddRouteActivity.this, MapsActivity.class));
    }
    public void showDialogOnClick() {
        dateButton = (Button)findViewById(R.id.dateButton);
        timeButton = (Button)findViewById(R.id.timeButton);
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
        if(id == DATEDILOG_ID)
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
            }
            else {
                year_x = year;
                month_x = month + 1;
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


}
