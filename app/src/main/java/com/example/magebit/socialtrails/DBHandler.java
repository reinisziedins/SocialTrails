package com.example.magebit.socialtrails;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.content.Context;
import android.content.ContentValues;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "trails.db";
    //Route table varaibles
    public static final String TABLE_ROUTES = "routes";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_ROUTENAME = "routename";
    public static final String COLUMN_STARTLAT = "startlat";
    public static final String COLUMN_STARTLNG = "startlng";
    public static final String COLUMN_FINISHLAT = "finishlat";
    public static final String COLUMN_FINISHLNG = "finishlng";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_MINUTE = "minute";
    public static final String COLUMN_HOUR = "hour";
    public static final String COLUMN_DAY = "day";
    public static final String COLUMN_MONTH = "month";
    public static final String COLUMN_YEAR = "year";
    //Tag table variables
    public static final String TABLE_TAGS = "tags";
    public static final String COLUMN_TAGNAME = "tagname";
    //Route tag table variables
    public static final String COLUMN_TAGID = "tagid";
    public static final String COULUMN_ROUTEID = "routeid";
    public static final String TABLE_ROUTETAG = "routetag";




    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    //Creates the route table (Note to self: remember to keep a look out for commas)
    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_ROUTES +  "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_ROUTENAME + " TEXT, " +
                COLUMN_STARTLAT + " double, " +
                COLUMN_STARTLNG + " double, " +
                COLUMN_FINISHLAT + " double, " +
                COLUMN_FINISHLNG + " double," +
                COLUMN_DESCRIPTION + " TEXT," +
                COLUMN_MINUTE + " INT," +
                COLUMN_HOUR + " INT," +
                COLUMN_DAY + " INT," +
                COLUMN_MONTH + " INT," +
                COLUMN_YEAR + " INT" +
                ");";
        db.execSQL(query);
        query = "CREATE TABLE " + TABLE_TAGS  +  "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_TAGNAME + " TEXT" +
                ");";
        db.execSQL(query);
        query = "CREATE TABLE " + TABLE_ROUTETAG  +  "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_TAGID + " INT, " +
                COULUMN_ROUTEID + " INT" +
                ");";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ROUTES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TAGS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ROUTETAG);
        onCreate(db);
    }

    //Function to add routes to DB
    public int addRoute(Route route, int tagId) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_ROUTENAME, route.get_name());
        values.put(COLUMN_STARTLAT, route.get_startLat());
        values.put(COLUMN_STARTLNG, route.get_startLng());
        values.put(COLUMN_FINISHLAT, route.get_finishLat());
        values.put(COLUMN_FINISHLNG, route.get_finishLng());
        values.put(COLUMN_DESCRIPTION, route.get_description());
        values.put(COLUMN_MINUTE, route.getMinute_x());
        values.put(COLUMN_HOUR, route.getHour_x());
        values.put(COLUMN_DAY, route.getDay_x());
        values.put(COLUMN_MONTH, route.getMonth_x());
        values.put(COLUMN_YEAR, route.getYear_x());
        SQLiteDatabase db = getWritableDatabase();
        long idLong = db.insert(TABLE_ROUTES, null, values);
        int id = (int) idLong;
        createRouteTag(id, tagId);
        db.close();
        return id;
    }
    public int addTag(Tag tag) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TAGNAME, tag.get_name().toString());
        SQLiteDatabase db = getWritableDatabase();
        long idLong = db.insert(TABLE_TAGS, null, values);
        int id = (int) idLong;
        db.close();
        return id;
    }
    public int createRouteTag(int route_id, int tag_id) {
        ContentValues values = new ContentValues();
        values.put(COULUMN_ROUTEID, route_id);
        values.put(COLUMN_TAGID, tag_id);
        SQLiteDatabase db = getWritableDatabase();
        long idLong = db.insert(TABLE_ROUTETAG, null, values);
        db.close();
        int id = (int) idLong;
        return id;
    }
    public List<Tag> getAllTags() {
        List<Tag> tags = new ArrayList<Tag>();
        String selectQuery = "SELECT distinct "+ COLUMN_TAGNAME + ", " + COLUMN_ID  + " FROM " + TABLE_TAGS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Tag t = new Tag();
                t.set_id(c.getInt((c.getColumnIndex(COLUMN_ID))));
                t.set_name(c.getString(c.getColumnIndex(COLUMN_TAGNAME)));

                // adding to tags list
                tags.add(t);
            } while (c.moveToNext());
        }
        return tags;
    }
    public Route[] getTagRoutes(String tag_name) {
        String selectQuery = "SELECT  * FROM " + TABLE_ROUTES + " td, "
                + TABLE_TAGS + " tg, " + TABLE_ROUTETAG + " tt WHERE tg."
                + COLUMN_TAGNAME + " = '" + tag_name + "'" + " AND tg." + COLUMN_ID
                + " = " + "tt." + COLUMN_TAGID + " AND td." + COLUMN_ID + " = "
                + "tt." + COULUMN_ROUTEID;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        c.moveToFirst();
        int i = 0;
        while(!c.isAfterLast()) {
            i++;
            c.moveToNext();
        }
        Route[] routes = new Route[i];
        c.moveToFirst();
        int j = 0;
        while(!c.isAfterLast()) {
            if(c.getString(c.getColumnIndex(COLUMN_STARTLAT))!= null) {
                routes[j] = new Route();
                routes[j].set_id(c.getInt(c.getColumnIndex(COLUMN_ID)));
                routes[j].set_name(c.getString(c.getColumnIndex(COLUMN_ROUTENAME)));
                routes[j].set_startLat(c.getDouble(c.getColumnIndex(COLUMN_STARTLAT)));
                routes[j].set_startLng(c.getDouble(c.getColumnIndex(COLUMN_STARTLNG)));
                routes[j].set_finishLat(c.getDouble(c.getColumnIndex(COLUMN_FINISHLAT)));
                routes[j].set_finishLng(c.getDouble(c.getColumnIndex(COLUMN_FINISHLNG)));
                routes[j].set_description(c.getString(c.getColumnIndex(COLUMN_DESCRIPTION)));
                routes[j].setMinute_x(c.getInt(c.getColumnIndex(COLUMN_MINUTE)));
                routes[j].setHour_x(c.getInt(c.getColumnIndex(COLUMN_HOUR)));
                routes[j].setDay_x(c.getInt(c.getColumnIndex(COLUMN_DAY)));
                routes[j].setMonth_x(c.getInt(c.getColumnIndex(COLUMN_MONTH)));
                routes[j].setYear_x(c.getInt(c.getColumnIndex(COLUMN_YEAR)));
            }
            j++;
            c.moveToNext();
        }

        return routes;
    }
    public void  deleteRoute(String routeId) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_ROUTES + " WHERE " + COLUMN_ID + " = " + routeId + ";");

    }
    public void editRoute(Route route) {
        SQLiteDatabase db = getWritableDatabase();
        String routeId = String.valueOf(route.get_id());
        db.execSQL("UPDATE " + TABLE_ROUTES + " SET " +
                COLUMN_DESCRIPTION + " = '" + route.get_description() + "' , " +
                COLUMN_ROUTENAME + " = '" + route.get_name() + "' , " +
                COLUMN_MINUTE + " = " + route.getMinute_x() + " , " +
                COLUMN_HOUR + " = " + route.getHour_x() + " , " +
                COLUMN_DAY + " = " + route.getDay_x() + " , " +
                COLUMN_MONTH + " = " + route.getMonth_x() + " , " +
                COLUMN_YEAR + " = " + route.getYear_x() + " WHERE " + COLUMN_ID + " = " + routeId);
    }

    //Gets trails
    public Route[] getRoute(int parameter) {
        int year = MapsActivity.currentY;
        int month = MapsActivity.currentM;
        int day = MapsActivity.currentD;
        String query = "SELECT * FROM " + TABLE_ROUTES + " WHERE 1;";
        if (parameter == 1) {
             query = "SELECT * FROM " + TABLE_ROUTES + " WHERE " + COLUMN_YEAR + " = " + year +
                    " AND " + COLUMN_MONTH + " = " + month + " AND " +  COLUMN_DAY + " = " + day + ";";
        }
        else if (parameter == 2) {
            int maxDay = day+7;
             query = "SELECT * FROM " + TABLE_ROUTES + " WHERE " + COLUMN_YEAR + " = " + year +
                    " AND " + COLUMN_MONTH + " = " + month + " AND " +  COLUMN_DAY + " <= " + maxDay +
                     " AND " + COLUMN_DAY + " >= " + day + ";";
        }
        else if (parameter == 3) {
            int maxMonth = month+1;
            query = "SELECT * FROM " + TABLE_ROUTES + " WHERE " + COLUMN_YEAR + " = " + year +
                    " AND " + COLUMN_MONTH + " >= " + month +   " AND " + COLUMN_MONTH + " <= " + maxMonth +  ";";
        }

        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        int i = 0;
        while(!c.isAfterLast()) {
            i++;
            c.moveToNext();
        }
        Route[] routes = new Route[i];
        c.moveToFirst();
        int j = 0;
        while(!c.isAfterLast()) {
            if(c.getString(c.getColumnIndex(COLUMN_STARTLAT))!= null) {
                 routes[j] = new Route();
                routes[j].set_id(c.getInt(c.getColumnIndex(COLUMN_ID)));
                routes[j].set_name(c.getString(c.getColumnIndex(COLUMN_ROUTENAME)));
                routes[j].set_startLat(c.getDouble(c.getColumnIndex(COLUMN_STARTLAT)));
                routes[j].set_startLng(c.getDouble(c.getColumnIndex(COLUMN_STARTLNG)));
                routes[j].set_finishLat(c.getDouble(c.getColumnIndex(COLUMN_FINISHLAT)));
                routes[j].set_finishLng(c.getDouble(c.getColumnIndex(COLUMN_FINISHLNG)));
                routes[j].set_description(c.getString(c.getColumnIndex(COLUMN_DESCRIPTION)));
                routes[j].setMinute_x(c.getInt(c.getColumnIndex(COLUMN_MINUTE)));
                routes[j].setHour_x(c.getInt(c.getColumnIndex(COLUMN_HOUR)));
                routes[j].setDay_x(c.getInt(c.getColumnIndex(COLUMN_DAY)));
                routes[j].setMonth_x(c.getInt(c.getColumnIndex(COLUMN_MONTH)));
                routes[j].setYear_x(c.getInt(c.getColumnIndex(COLUMN_YEAR)));
            }
            j++;
            c.moveToNext();
        }
        db.close();
        return routes;
    }
    // Gets all info of trails
    public String databaseToString() {
        String dbString = "";
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_ROUTES +
                " WHERE 1 ORDER BY " +
                COLUMN_YEAR + ", "  +
                COLUMN_MONTH + ", " +
                COLUMN_DAY + ", " +
                COLUMN_MINUTE + ", " +
                COLUMN_HOUR +
        " ASC";

        Cursor c = db.rawQuery(query, null);

        c.moveToFirst();

        while(!c.isAfterLast()) {
            if(c.getString(c.getColumnIndex(COLUMN_ROUTENAME))!= null) {
                dbString += c.getString(c.getColumnIndex(COLUMN_ID));
                dbString += ". ";
                dbString += c.getString(c.getColumnIndex(COLUMN_ROUTENAME));
                dbString += ", ";
                dbString += c.getString(c.getColumnIndex(COLUMN_DESCRIPTION));
                dbString += ", ";
                dbString += c.getString(c.getColumnIndex(COLUMN_DAY));
                dbString += "/";
                dbString += c.getString(c.getColumnIndex(COLUMN_MONTH));
                dbString += "/";
                dbString += c.getString(c.getColumnIndex(COLUMN_YEAR));
                dbString += ", ";
                dbString += c.getString(c.getColumnIndex(COLUMN_HOUR));
                dbString += ":";
                dbString += c.getString(c.getColumnIndex(COLUMN_MINUTE));
                dbString += "\n";
            }
            c.moveToNext();
        }
        db.close();
        return dbString;
    }


}
