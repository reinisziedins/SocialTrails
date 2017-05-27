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

    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_ROUTES +  "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_ROUTENAME + " TEXT, " +
                COLUMN_STARTLAT + " double, " +
                COLUMN_STARTLNG + " double, " +
                COLUMN_FINISHLAT + " double, " +
                COLUMN_FINISHLNG + " doubledada" +
                ");";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP_TABLE_IF_EXISTS " + TABLE_ROUTES);
        onCreate(db);
    }

    public void addRoute(Route route) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_ROUTENAME, route.get_name());
        values.put(COLUMN_STARTLAT, route.get_startLat());
        values.put(COLUMN_STARTLNG, route.get_startLng());
        values.put(COLUMN_FINISHLAT, route.get_finishLat());
        values.put(COLUMN_FINISHLNG, route.get_finishLng());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_ROUTES, null, values);
        db.close();

    }
    public void  deleteRoute(String routeName) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_ROUTES + " WHERE " + COLUMN_ROUTENAME + "=\"" + routeName + "\";");

    }

    public String databaseToString() {
        String dbString = "";
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT " + COLUMN_ROUTENAME + ", " + COLUMN_STARTLAT + " FROM " + TABLE_ROUTES + " WHERE 1";

        Cursor c = db.rawQuery(query, null);

        c.moveToFirst();

        while(!c.isAfterLast()) {
            if(c.getString(c.getColumnIndex(COLUMN_ROUTENAME))!= null) {
                dbString += c.getString(c.getColumnIndex(COLUMN_ROUTENAME));
                dbString += ",";
                dbString += c.getString(c.getColumnIndex(COLUMN_STARTLAT));
                dbString += "\n";
            }
            c.moveToNext();
        }
        db.close();
        return dbString;
    }




}
