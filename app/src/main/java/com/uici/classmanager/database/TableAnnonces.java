package com.uici.classmanager.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.uici.classmanager.models.Annonce;

import java.util.ArrayList;
import java.util.List;

public class TableAnnonces {

    public static final String TABLE_NAME = "Annonces";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITRE = "titre";
    public static final String COLUMN_CONTENU = "contenu";
    public static final String COLUMN_DATE_PUBLICATION = "date_publication";
    public static final String COLUMN_CLASSE_ID = "classe_id";
    public static final String COLUMN_TYPE_RESSOURCE = "type_ressource";
    public static final String COLUMN_CHEMIN_RESSOURCE = "chemin_ressource";

    public static void create(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_TITRE + " TEXT NOT NULL, "
                + COLUMN_CONTENU + " TEXT NOT NULL, "
                + COLUMN_DATE_PUBLICATION + " TEXT NOT NULL, "
                + COLUMN_CLASSE_ID + " INTEGER NOT NULL, "
                + COLUMN_TYPE_RESSOURCE + " TEXT, "
                + COLUMN_CHEMIN_RESSOURCE + " TEXT, "
                + "FOREIGN KEY(" + COLUMN_CLASSE_ID + ") REFERENCES " + TableClasses.TABLE_NAME + "(" + TableClasses.COLUMN_ID + ")"
                + ")";
        db.execSQL(query);
    }

    public static void drop(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }

    public static long insert(SQLiteDatabase db, Annonce annonce) {
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_TITRE, annonce.getTitre());
            values.put(COLUMN_CONTENU, annonce.getContenu());
            values.put(COLUMN_DATE_PUBLICATION, annonce.getDatePublication());
            values.put(COLUMN_CLASSE_ID, annonce.getClasseId());
            values.put(COLUMN_TYPE_RESSOURCE, annonce.getTypeRessource());
            values.put(COLUMN_CHEMIN_RESSOURCE, annonce.getCheminRessource());
            return db.insert(TABLE_NAME, null, values);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static void update(SQLiteDatabase db, long id, Annonce annonce) {
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_TITRE, annonce.getTitre());
            values.put(COLUMN_CONTENU, annonce.getContenu());
            values.put(COLUMN_DATE_PUBLICATION, annonce.getDatePublication());
            values.put(COLUMN_CLASSE_ID, annonce.getClasseId());
            values.put(COLUMN_TYPE_RESSOURCE, annonce.getTypeRessource());
            values.put(COLUMN_CHEMIN_RESSOURCE, annonce.getCheminRessource());
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

    public static List<Annonce> selectByClasse(SQLiteDatabase db, long classeId) {
        List<Annonce> annonces = new ArrayList<>();
        try {
            Cursor cursor = db.query(TABLE_NAME,
                    null,
                    COLUMN_CLASSE_ID + " = ?",
                    new String[]{String.valueOf(classeId)},
                    null, null,
                    COLUMN_DATE_PUBLICATION + " DESC");

            if (cursor.moveToFirst()) {
                do {
                    long id = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID));
                    String titre = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITRE));
                    String contenu = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENU));
                    String datePublication = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE_PUBLICATION));
                    long classe_id = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_CLASSE_ID));
                    String typeRessource = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TYPE_RESSOURCE));
                    String cheminRessource = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CHEMIN_RESSOURCE));

                    Annonce annonce = new Annonce(titre, contenu, datePublication, classe_id);
                    annonce.setId(id);
                    annonce.setTypeRessource(typeRessource);
                    annonce.setCheminRessource(cheminRessource);
                    annonces.add(annonce);
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return annonces;
    }

    public static Annonce selectById(SQLiteDatabase db, long id) {
        Annonce annonce = null;
        try {
            Cursor cursor = db.query(TABLE_NAME,
                    null,
                    COLUMN_ID + " = ?",
                    new String[]{String.valueOf(id)},
                    null, null, null);

            if (cursor.moveToFirst()) {
                String titre = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITRE));
                String contenu = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENU));
                String datePublication = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE_PUBLICATION));
                long classeId = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_CLASSE_ID));
                String typeRessource = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TYPE_RESSOURCE));
                String cheminRessource = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CHEMIN_RESSOURCE));

                annonce = new Annonce(titre, contenu, datePublication, classeId);
                annonce.setId(id);
                annonce.setTypeRessource(typeRessource);
                annonce.setCheminRessource(cheminRessource);
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return annonce;
    }
}
