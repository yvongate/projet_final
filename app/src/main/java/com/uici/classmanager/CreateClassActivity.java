package com.uici.classmanager;

import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.uici.classmanager.database.DatabaseHelper;
import com.uici.classmanager.database.TableClasses;
import com.uici.classmanager.models.Classe;

public class CreateClassActivity extends AppCompatActivity {

    private TextInputEditText editTextNom;
    private TextInputEditText editTextAnnee;
    private TextInputEditText editTextDescription;
    private Button buttonCreate;
    private Button buttonCancel;

    private SQLiteDatabase db;
    private long enseignantId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_class);

        SharedPreferences prefs = getSharedPreferences("ClassManager", MODE_PRIVATE);
        enseignantId = prefs.getLong("userId", -1);

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        db = dbHelper.getWritableDatabase();

        editTextNom = findViewById(R.id.editTextNom);
        editTextAnnee = findViewById(R.id.editTextAnnee);
        editTextDescription = findViewById(R.id.editTextDescription);
        buttonCreate = findViewById(R.id.buttonCreate);
        buttonCancel = findViewById(R.id.buttonCancel);

        buttonCreate.setOnClickListener(v -> createClass());
        buttonCancel.setOnClickListener(v -> finish());
    }

    private void createClass() {
        String nom = editTextNom.getText().toString().trim();
        String annee = editTextAnnee.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();

        if (nom.isEmpty()) {
            editTextNom.setError("Nom requis");
            return;
        }

        if (annee.isEmpty()) {
            editTextAnnee.setError("Année académique requise");
            return;
        }

        try {
            Classe classe = new Classe(nom, annee, description, enseignantId);
            long id = TableClasses.insert(db, classe);

            if (id > 0) {
                Toast.makeText(this, "Classe créée avec succès", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Erreur lors de la création", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Erreur: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (db != null) {
            db.close();
        }
    }
}
