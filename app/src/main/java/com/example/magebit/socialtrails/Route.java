package com.example.magebit.socialtrails;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by magebit on 17.21.5.
 */

public class Route {
    int _id;
    String _name;
    Double _startLat;
    Double _startLng;
    Double _finishLat;
    Double _finishLng;
    String _description;
    int minute_x,hour_x,day_x,month_x,year_x;

    public Route() {

    }

    public Route(String _name, Double _startLat, Double _startLng, Double _finishLat, Double _finishLng, String _description, int minute_x, int hour_x, int day_x, int month_x, int year_x) {
        this._name = _name;
        this._startLat = _startLat;
        this._startLng = _startLng;
        this._finishLat = _finishLat;
        this._finishLng = _finishLng;
        this._description = _description;
        this.minute_x = minute_x;
        this.hour_x = hour_x;
        this.day_x = day_x;
        this.month_x = month_x;
        this.year_x = year_x;
    }

    public int get_id() {
        return _id;
    }

    public String get_name() {
        return _name;
    }

    public Double get_startLat() {
        return _startLat;
    }

    public Double get_startLng() {
        return _startLng;
    }

    public Double get_finishLat() {
        return _finishLat;
    }

    public Double get_finishLng() {
        return _finishLng;
    }

    public String get_description() {
        return _description;
    }

    public int getMinute_x() {
        return minute_x;
    }

    public int getHour_x() {
        return hour_x;
    }

    public int getDay_x() {
        return day_x;
    }

    public int getMonth_x() {
        return month_x;
    }

    public int getYear_x() {
        return year_x;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public void set_startLat(Double _startLat) {
        this._startLat = _startLat;
    }

    public void set_startLng(Double _startLng) {
        this._startLng = _startLng;
    }

    public void set_finishLat(Double _finishLat) {
        this._finishLat = _finishLat;
    }

    public void set_finishLng(Double _finishLng) {
        this._finishLng = _finishLng;
    }

    public void set_description(String _description) {
        this._description = _description;
    }

    public void setMinute_x(int minute_x) {
        this.minute_x = minute_x;
    }

    public void setHour_x(int hour_x) {
        this.hour_x = hour_x;
    }

    public void setDay_x(int day_x) {
        this.day_x = day_x;
    }

    public void setMonth_x(int month_x) {
        this.month_x = month_x;
    }

    public void setYear_x(int year_x) {
        this.year_x = year_x;
    }
}
