package com.example.magebit.socialtrails;

/**
 * Created by magebit on 17.5.6.
 */

public class Tag {
    int _id;
    String _name;

    public Tag() {
    }

    public Tag(int _id, String _name) {
        this._id = _id;
        this._name = _name;
    }

    public Tag(String _name) {
        this._name = _name;
    }

    public int get_id() {
        return _id;
    }

    public String get_name() {
        return _name;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public void set_name(String _name) {
        this._name = _name;
    }
    @Override
    public String toString() {
        return this._name;
    }
}
