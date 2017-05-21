package com.example.magebit.socialtrails;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by magebit on 17.21.5.
 */

public class Route {
    int _id;
    String _name;
    Float _startLat;
    Float _startLng;
    Float _finishLat;
    Float _finishLng;

    public Route() {

    }

    public Route(String _name, Float _startLat, Float _startLng, Float _finishLat, Float _finishLng) {
        this._name = _name;
        this._startLat = _startLat;
        this._startLng = _startLng;
        this._finishLat = _finishLat;
        this._finishLng = _finishLng;
    }

    public int get_id() {
        return _id;
    }

    public String get_name() {
        return _name;
    }

    public Float get_startLat() {
        return _startLat;
    }

    public Float get_startLng() {
        return _startLng;
    }

    public Float get_finishLat() {
        return _finishLat;
    }

    public Float get_finishLng() {
        return _finishLng;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public void set_startLat(Float _startLat) {
        this._startLat = _startLat;
    }

    public void set_startLng(Float _startLng) {
        this._startLng = _startLng;
    }

    public void set_finishLat(Float _finishLat) {
        this._finishLat = _finishLat;
    }

    public void set_finishLng(Float _finishLng) {
        this._finishLng = _finishLng;
    }
}
