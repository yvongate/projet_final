package com.uici.classmanager.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.uici.classmanager.models.Classe;

import java.util.ArrayList;
import java.util.List;

public class TableClasses {

    public static final String TABLE_NAME = "Classes";
    public static final String COL_ID = "_id";
    public static final String COL_NOM = "nom";
    public static final String COL_ANNEE_ACADEMIQUE = "annee_academique";
    public static final String COL_DESCRIPTION = "description";
    public static final String COL_ENSEIGNANT_ID = "enseignant_id";

    public static void create(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_NOM + " TEXT NOT NULL," +
                COL_ANNEE_ACADEMIQUE + " TEXT NOT NULL," +
                COL_DESCRIPTION + " TEXT," +
                COL_ENSEIGNANT_ID + " INTEGER NOT NULL," +
                "FOREIGN KEY(" + COL_ENSEIGNANT_ID + ") REFERENCES " + TableUsers.TABLE_NAME + "(" + TableUsers.COL_ID + "))");
    }

    public static void drop(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }

    public static long insert(SQLiteDatabase db, Classe classe) {
        db.execSQL(
                "INSERT INTO " + TABLE_NAME + " VALUES (null, ?, ?, ?, ?)",
                new Object[]{
                        classe.getNom(),
                        classe.getAnneeAcademique(),
                        classe.getDescription(),
                        classe.getEnseignantId()
                });

        Cursor cursor = db.rawQuery("SELECT last_insert_rowid()", null);
        long id = 0;
        if (cursor.moveToFirst()) {
            id = cursor.getLong(0);
        }
        cursor.close();
        return id;
    }

    public static void update(SQLiteDatabase db, long id, Classe classe) {
        db.execSQL(
                "UPDATE " + TABLE_NAME + " SET " +
                        COL_NOM + "=?, " +
                        COL_ANNEE_ACADEMIQUE + "=?, " +
                        COL_DESCRIPTION + "=? " +
                        "WHERE " + COL_ID + "=?",
                new Object[]{
                        classe.getNom(),
                        classe.getAnneeAcademique(),
                        classe.getDescription(),
                        id
                });
    }

    public static void delete(SQLiteDatabase db, long id) {
        db.execSQL(
                "DELETE FROM " + TABLE_NAME + " WHERE " + COL_ID + "=?",
                new Object[]{id});
    }

    public static List<Classe> selectAll(SQLiteDatabase db) {
        List<Classe> classes = new ArrayList<>();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_NAME + " ORDER BY " + COL_ANNEE_ACADEMIQUE + " DESC, " + COL_NOM,
                null);

        while (cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(COL_ID));
            String nom = cursor.getString(cursor.getColumnIndexOrThrow(COL_NOM));
            String anneeAcademique = cursor.getString(cursor.getColumnIndexOrThrow(COL_ANNEE_ACADEMIQUE));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(COL_DESCRIPTION));
            long enseignantId = cursor.getLong(cursor.getColumnIndexOrThrow(COL_ENSEIGNANT_ID));

            Classe classe = new Classe(id, nom, anneeAcademique, description, enseignantId);
            classes.add(classe);
        }
        cursor.close();
        return classes;
    }

    public static List<Classe> selectByEnseignant(SQLiteDatabase db, long enseignantId) {
        List<Classe> classes = new ArrayList<>();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_NAME + " WHERE " + COL_ENSEIGNANT_ID + "=? ORDER BY " + COL_ANNEE_ACADEMIQUE + " DESC, " + COL_NOM,
                new String[]{String.valueOf(enseignantId)});

        while (cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(COL_ID));
            String nom = cursor.getString(cursor.getColumnIndexOrThrow(COL_NOM));
            String anneeAcademique = cursor.getString(cursor.getColumnIndexOrThrow(COL_ANNEE_ACADEMIQUE));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(COL_DESCRIPTION));

            Classe classe = new Classe(id, nom, anneeAcademique, description, enseignantId);
            classes.add(classe);
        }
        cursor.close();
        return classes;
    }

    public static Classe selectById(SQLiteDatabase db, long id) {
        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_NAME + " WHERE " + COL_ID + "=?",
                new String[]{String.valueOf(id)});

        Classe classe = null;
        if (cursor.moveToFirst()) {
            String nom = cursor.getString(cursor.getColumnIndexOrThrow(COL_NOM));
            String anneeAcademique = cursor.getString(cursor.getColumnIndexOrThrow(COL_ANNEE_ACADEMIQUE));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(COL_DESCRIPTION));
            long enseignantId = cursor.getLong(cursor.getColumnIndexOrThrow(COL_ENSEIGNANT_ID));

            classe = new Classe(id, nom, anneeAcademique, description, enseignantId);
        }
        cursor.close();
        return classe;
    }
}
