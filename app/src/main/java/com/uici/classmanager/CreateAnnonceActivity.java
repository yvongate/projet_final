package com.uici.classmanager;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.uici.classmanager.database.DatabaseHelper;
import com.uici.classmanager.database.TableAnnonces;
import com.uici.classmanager.models.Annonce;
import com.uici.classmanager.models.Classe;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CreateAnnonceActivity extends AppCompatActivity {

    private TextInputEditText editTextTitre;
    private TextInputEditText editTextContenu;
    private Button buttonCreate;
    private Button buttonCancel;
    private Button buttonAddImage;
    private Button buttonAddAudio;
    private Button buttonAddVideo;
    private TextView textViewSelectedFile;

    private SQLiteDatabase db;
    private Classe classe;
    private String selectedFileType = Annonce.TYPE_AUCUNE;
    private String selectedFilePath = null;

    private ActivityResultLauncher<String> imagePickerLauncher;
    private ActivityResultLauncher<String> audioPickerLauncher;
    private ActivityResultLauncher<String> videoPickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_annonce);

        classe = getIntent().getParcelableExtra("classe");
        if (classe == null) {
            finish();
            return;
        }

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        db = dbHelper.getWritableDatabase();

        editTextTitre = findViewById(R.id.editTextTitre);
        editTextContenu = findViewById(R.id.editTextContenu);
        buttonCreate = findViewById(R.id.buttonCreate);
        buttonCancel = findViewById(R.id.buttonCancel);
        buttonAddImage = findViewById(R.id.buttonAddImage);
        buttonAddAudio = findViewById(R.id.buttonAddAudio);
        buttonAddVideo = findViewById(R.id.buttonAddVideo);
        textViewSelectedFile = findViewById(R.id.textViewSelectedFile);

        setupFilePickers();

        buttonCreate.setOnClickListener(v -> createAnnonce());
        buttonCancel.setOnClickListener(v -> finish());
        buttonAddImage.setOnClickListener(v -> imagePickerLauncher.launch("image/*"));
        buttonAddAudio.setOnClickListener(v -> audioPickerLauncher.launch("audio/*"));
        buttonAddVideo.setOnClickListener(v -> videoPickerLauncher.launch("video/*"));
    }

    private void setupFilePickers() {
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        selectedFileType = Annonce.TYPE_IMAGE;
                        selectedFilePath = uri.toString();
                        textViewSelectedFile.setText("Image sélectionnée: " + uri.getLastPathSegment());
                    }
                });

        audioPickerLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        selectedFileType = Annonce.TYPE_AUDIO;
                        selectedFilePath = uri.toString();
                        textViewSelectedFile.setText("Audio sélectionné: " + uri.getLastPathSegment());
                    }
                });

        videoPickerLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        selectedFileType = Annonce.TYPE_VIDEO;
                        selectedFilePath = uri.toString();
                        textViewSelectedFile.setText("Vidéo sélectionnée: " + uri.getLastPathSegment());
                    }
                });
    }

    private void createAnnonce() {
        String titre = editTextTitre.getText().toString().trim();
        String contenu = editTextContenu.getText().toString().trim();

        if (titre.isEmpty()) {
            editTextTitre.setError("Titre requis");
            return;
        }

        if (contenu.isEmpty()) {
            editTextContenu.setError("Contenu requis");
            return;
        }

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            String datePublication = sdf.format(new Date());

            Annonce annonce = new Annonce(titre, contenu, datePublication, classe.getId());
            annonce.setTypeRessource(selectedFileType);
            annonce.setCheminRessource(selectedFilePath);

            long id = TableAnnonces.insert(db, annonce);

            if (id > 0) {
                Toast.makeText(this, "Annonce créée avec succès", Toast.LENGTH_SHORT).show();
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
