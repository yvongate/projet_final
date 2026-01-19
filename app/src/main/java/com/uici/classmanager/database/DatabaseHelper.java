package com.uici.classmanager.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "classmanager.db";
    private static final int DATABASE_VERSION = 6;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        TableUsers.create(db);
        TableClasses.create(db);
        TableInscriptions.create(db);
        TableAnnonces.create(db);
        TablePOI.create(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        TableUsers.drop(db);
        TableClasses.drop(db);
        TableInscriptions.drop(db);
        TableAnnonces.drop(db);
        TablePOI.drop(db);
        onCreate(db);
    }
}
