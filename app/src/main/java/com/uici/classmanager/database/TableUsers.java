package com.uici.classmanager.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.uici.classmanager.models.User;

public class TableUsers {

    public static final String TABLE_NAME = "Users";
    public static final String COL_ID = "_id";
    public static final String COL_NOM = "nom";
    public static final String COL_PRENOM = "prenom";
    public static final String COL_EMAIL = "email";
    public static final String COL_PASSWORD = "password";
    public static final String COL_ROLE = "role";

    public static void create(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_NOM + " TEXT NOT NULL," +
                COL_PRENOM + " TEXT NOT NULL," +
                COL_EMAIL + " TEXT NOT NULL UNIQUE," +
                COL_PASSWORD + " TEXT NOT NULL," +
                COL_ROLE + " TEXT NOT NULL)");
    }

    public static void drop(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }

    public static long insert(SQLiteDatabase db, User user) {
        db.execSQL(
                "INSERT INTO " + TABLE_NAME + " VALUES (null, ?, ?, ?, ?, ?)",
                new Object[]{
                        user.getNom(),
                        user.getPrenom(),
                        user.getEmail(),
                        user.getPassword(),
                        user.getRole()
                });

        Cursor cursor = db.rawQuery("SELECT last_insert_rowid()", null);
        long id = 0;
        if (cursor.moveToFirst()) {
            id = cursor.getLong(0);
        }
        cursor.close();
        return id;
    }

    public static User getUserByEmail(SQLiteDatabase db, String email) {
        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_NAME + " WHERE " + COL_EMAIL + "=?",
                new String[]{email});

        User user = null;
        if (cursor.moveToFirst()) {
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(COL_ID));
            String nom = cursor.getString(cursor.getColumnIndexOrThrow(COL_NOM));
            String prenom = cursor.getString(cursor.getColumnIndexOrThrow(COL_PRENOM));
            String userEmail = cursor.getString(cursor.getColumnIndexOrThrow(COL_EMAIL));
            String password = cursor.getString(cursor.getColumnIndexOrThrow(COL_PASSWORD));
            String role = cursor.getString(cursor.getColumnIndexOrThrow(COL_ROLE));

            user = new User(id, nom, prenom, userEmail, password, role);
        }
        cursor.close();
        return user;
    }

    public static boolean emailExists(SQLiteDatabase db, String email) {
        Cursor cursor = db.rawQuery(
                "SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE " + COL_EMAIL + "=?",
                new String[]{email});

        boolean exists = false;
        if (cursor.moveToFirst()) {
            exists = cursor.getInt(0) > 0;
        }
        cursor.close();
        return exists;
    }

    public static User login(SQLiteDatabase db, String email, String password) {
        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_NAME + " WHERE " + COL_EMAIL + "=? AND " + COL_PASSWORD + "=?",
                new String[]{email, password});

        User user = null;
        if (cursor.moveToFirst()) {
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(COL_ID));
            String nom = cursor.getString(cursor.getColumnIndexOrThrow(COL_NOM));
            String prenom = cursor.getString(cursor.getColumnIndexOrThrow(COL_PRENOM));
            String userEmail = cursor.getString(cursor.getColumnIndexOrThrow(COL_EMAIL));
            String userPassword = cursor.getString(cursor.getColumnIndexOrThrow(COL_PASSWORD));
            String role = cursor.getString(cursor.getColumnIndexOrThrow(COL_ROLE));

            user = new User(id, nom, prenom, userEmail, userPassword, role);
        }
        cursor.close();
        return user;
    }
}
