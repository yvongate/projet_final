package com.uici.classmanager.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.uici.classmanager.models.User;

import java.util.ArrayList;
import java.util.List;

public class TableInscriptions {

    public static final String TABLE_NAME = "Inscriptions";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_CLASS_ID = "class_id";
    public static final String COLUMN_DATE_INSCRIPTION = "date_inscription";

    public static void create(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_USER_ID + " INTEGER NOT NULL, "
                + COLUMN_CLASS_ID + " INTEGER NOT NULL, "
                + COLUMN_DATE_INSCRIPTION + " TEXT, "
                + "FOREIGN KEY(" + COLUMN_USER_ID + ") REFERENCES " + TableUsers.TABLE_NAME + "(" + TableUsers.COLUMN_ID + "), "
                + "FOREIGN KEY(" + COLUMN_CLASS_ID + ") REFERENCES " + TableClasses.TABLE_NAME + "(" + TableClasses.COLUMN_ID + "), "
                + "UNIQUE(" + COLUMN_USER_ID + ", " + COLUMN_CLASS_ID + ")"
                + ")";
        db.execSQL(query);
    }

    public static void drop(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }

    public static long insert(SQLiteDatabase db, long userId, long classId) {
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_USER_ID, userId);
            values.put(COLUMN_CLASS_ID, classId);
            values.put(COLUMN_DATE_INSCRIPTION, java.text.DateFormat.getDateTimeInstance().format(new java.util.Date()));
            return db.insert(TABLE_NAME, null, values);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static void delete(SQLiteDatabase db, long userId, long classId) {
        try {
            db.delete(TABLE_NAME,
                    COLUMN_USER_ID + " = ? AND " + COLUMN_CLASS_ID + " = ?",
                    new String[]{String.valueOf(userId), String.valueOf(classId)});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<User> getStudentsByClass(SQLiteDatabase db, long classId) {
        List<User> students = new ArrayList<>();
        try {
            String query = "SELECT u.* FROM " + TableUsers.TABLE_NAME + " u "
                    + "INNER JOIN " + TABLE_NAME + " i ON u." + TableUsers.COLUMN_ID + " = i." + COLUMN_USER_ID + " "
                    + "WHERE i." + COLUMN_CLASS_ID + " = ? "
                    + "ORDER BY u." + TableUsers.COLUMN_NOM;

            Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(classId)});

            if (cursor.moveToFirst()) {
                do {
                    long id = cursor.getLong(cursor.getColumnIndexOrThrow(TableUsers.COLUMN_ID));
                    String nom = cursor.getString(cursor.getColumnIndexOrThrow(TableUsers.COLUMN_NOM));
                    String prenom = cursor.getString(cursor.getColumnIndexOrThrow(TableUsers.COLUMN_PRENOM));
                    String email = cursor.getString(cursor.getColumnIndexOrThrow(TableUsers.COLUMN_EMAIL));
                    String role = cursor.getString(cursor.getColumnIndexOrThrow(TableUsers.COLUMN_ROLE));

                    User user = new User(nom, prenom, email, "", role);
                    user.setId(id);
                    students.add(user);
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return students;
    }

    public static List<User> getAvailableStudents(SQLiteDatabase db, long classId) {
        List<User> students = new ArrayList<>();
        try {
            String query = "SELECT * FROM " + TableUsers.TABLE_NAME + " "
                    + "WHERE " + TableUsers.COLUMN_ROLE + " = ? "
                    + "AND " + TableUsers.COLUMN_ID + " NOT IN ("
                    + "SELECT " + COLUMN_USER_ID + " FROM " + TABLE_NAME + " WHERE " + COLUMN_CLASS_ID + " = ?"
                    + ") ORDER BY " + TableUsers.COLUMN_NOM;

            Cursor cursor = db.rawQuery(query, new String[]{User.ROLE_ETUDIANT, String.valueOf(classId)});

            if (cursor.moveToFirst()) {
                do {
                    long id = cursor.getLong(cursor.getColumnIndexOrThrow(TableUsers.COLUMN_ID));
                    String nom = cursor.getString(cursor.getColumnIndexOrThrow(TableUsers.COLUMN_NOM));
                    String prenom = cursor.getString(cursor.getColumnIndexOrThrow(TableUsers.COLUMN_PRENOM));
                    String email = cursor.getString(cursor.getColumnIndexOrThrow(TableUsers.COLUMN_EMAIL));
                    String role = cursor.getString(cursor.getColumnIndexOrThrow(TableUsers.COLUMN_ROLE));

                    User user = new User(nom, prenom, email, "", role);
                    user.setId(id);
                    students.add(user);
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return students;
    }

    public static boolean isEnrolled(SQLiteDatabase db, long userId, long classId) {
        try {
            Cursor cursor = db.query(TABLE_NAME,
                    new String[]{COLUMN_ID},
                    COLUMN_USER_ID + " = ? AND " + COLUMN_CLASS_ID + " = ?",
                    new String[]{String.valueOf(userId), String.valueOf(classId)},
                    null, null, null);
            boolean exists = cursor.getCount() > 0;
            cursor.close();
            return exists;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
