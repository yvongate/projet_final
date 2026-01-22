package com.uici.classmanager.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.uici.classmanager.models.POI;

import java.util.ArrayList;
import java.util.List;

public class TablePOI {

    public static final String TABLE_NAME = "POI";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NOM = "nom";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_LONGITUDE = "longitude";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_CLASSE_ID = "classe_id";

    public static void create(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_NOM + " TEXT NOT NULL, "
                + COLUMN_TYPE + " TEXT NOT NULL, "
                + COLUMN_LATITUDE + " REAL NOT NULL, "
                + COLUMN_LONGITUDE + " REAL NOT NULL, "
                + COLUMN_DESCRIPTION + " TEXT, "
                + COLUMN_CLASSE_ID + " INTEGER NOT NULL, "
                + "FOREIGN KEY(" + COLUMN_CLASSE_ID + ") REFERENCES " + TableClasses.TABLE_NAME + "(" + TableClasses.COL_ID + ")"
                + ")";
        db.execSQL(query);
    }

    public static void drop(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }

    public static long insert(SQLiteDatabase db, POI poi) {
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_NOM, poi.getNom());
            values.put(COLUMN_TYPE, poi.getType());
            values.put(COLUMN_LATITUDE, poi.getLatitude());
            values.put(COLUMN_LONGITUDE, poi.getLongitude());
            values.put(COLUMN_DESCRIPTION, poi.getDescription());
            values.put(COLUMN_CLASSE_ID, poi.getClasseId());
            return db.insert(TABLE_NAME, null, values);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static void update(SQLiteDatabase db, long id, POI poi) {
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_NOM, poi.getNom());
            values.put(COLUMN_TYPE, poi.getType());
            values.put(COLUMN_LATITUDE, poi.getLatitude());
            values.put(COLUMN_LONGITUDE, poi.getLongitude());
            values.put(COLUMN_DESCRIPTION, poi.getDescription());
            values.put(COLUMN_CLASSE_ID, poi.getClasseId());
            db.update(TABLE_NAME, values, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void delete(SQLiteDatabase db, long id) {
        try {
            db.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<POI> selectByClasse(SQLiteDatabase db, long classeId) {
        List<POI> pois = new ArrayList<>();
        try {
            Cursor cursor = db.query(TABLE_NAME,
                    null,
                    COLUMN_CLASSE_ID + " = ?",
                    new String[]{String.valueOf(classeId)},
                    null, null,
                    COLUMN_NOM + " ASC");

            if (cursor.moveToFirst()) {
                do {
                    long id = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID));
                    String nom = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOM));
                    String type = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TYPE));
                    double latitude = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_LATITUDE));
                    double longitude = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_LONGITUDE));
                    String description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION));
                    long classe_id = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_CLASSE_ID));

                    POI poi = new POI(nom, type, latitude, longitude, description, classe_id);
                    poi.setId(id);
                    pois.add(poi);
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pois;
    }

    public static POI selectById(SQLiteDatabase db, long id) {
        POI poi = null;
        try {
            Cursor cursor = db.query(TABLE_NAME,
                    null,
                    COLUMN_ID + " = ?",
                    new String[]{String.valueOf(id)},
                    null, null, null);

            if (cursor.moveToFirst()) {
                String nom = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOM));
                String type = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TYPE));
                double latitude = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_LATITUDE));
                double longitude = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_LONGITUDE));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION));
                long classeId = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_CLASSE_ID));

                poi = new POI(nom, type, latitude, longitude, description, classeId);
                poi.setId(id);
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return poi;
    }
}
