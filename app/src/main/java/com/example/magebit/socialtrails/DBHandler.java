package com.example.magebit.socialtrails;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.content.Context;
import android.content.ContentValues;

public class DBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "trails.db";
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
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP_TABLE_IF_EXISTS " + TABLE_ROUTES);
        onCreate(db);
    }

    //Function to add routes to DB
    public void addRoute(Route route) {
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
        db.insert(TABLE_ROUTES, null, values);
        db.close();

    }
    public void  deleteRoute(String routeName) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_ROUTES + " WHERE " + COLUMN_ROUTENAME + "=\"" + routeName + "\";");

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
                " WHERE 1 + ORDER BY "+
                COLUMN_YEAR + " , "  +
                COLUMN_MONTH + " , " +
                COLUMN_DAY + " , " +
                COLUMN_HOUR + " , " +
                COLUMN_MINUTE + " , " +
                "ASC;";

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
                dbString += c.getString(c.getColumnIndex(COLUMN_MINUTE));
                dbString += ":";
                dbString += c.getString(c.getColumnIndex(COLUMN_HOUR));
                dbString += "\n";
            }
            c.moveToNext();
        }
        db.close();
        return dbString;
    }


}
