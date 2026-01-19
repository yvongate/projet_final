package com.uici.classmanager;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.textfield.TextInputEditText;
import com.uici.classmanager.database.DatabaseHelper;
import com.uici.classmanager.database.TablePOI;
import com.uici.classmanager.models.Classe;
import com.uici.classmanager.models.POI;

public class CreatePOIActivity extends AppCompatActivity {

    private TextInputEditText editTextNom;
    private TextInputEditText editTextType;
    private TextInputEditText editTextDescription;
    private TextView textViewCoordinates;
    private Button buttonGetLocation;
    private Button buttonCreate;
    private Button buttonCancel;

    private SQLiteDatabase db;
    private Classe classe;
    private FusedLocationProviderClient fusedLocationClient;
    private ActivityResultLauncher<String[]> locationPermissionLauncher;

    private double currentLatitude = 0;
    private double currentLongitude = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_poi);

        classe = getIntent().getParcelableExtra("classe");
        if (classe == null) {
            finish();
            return;
        }

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        db = dbHelper.getWritableDatabase();

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        editTextNom = findViewById(R.id.editTextNom);
        editTextType = findViewById(R.id.editTextType);
        editTextDescription = findViewById(R.id.editTextDescription);
        textViewCoordinates = findViewById(R.id.textViewCoordinates);
        buttonGetLocation = findViewById(R.id.buttonGetLocation);
        buttonCreate = findViewById(R.id.buttonCreate);
        buttonCancel = findViewById(R.id.buttonCancel);

        setupPermissionLauncher();

        buttonGetLocation.setOnClickListener(v -> getCurrentLocation());
        buttonCreate.setOnClickListener(v -> createPOI());
        buttonCancel.setOnClickListener(v -> finish());
    }

    private void setupPermissionLauncher() {
        locationPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(),
                result -> {
                    Boolean fineLocationGranted = result.get(Manifest.permission.ACCESS_FINE_LOCATION);
                    Boolean coarseLocationGranted = result.get(Manifest.permission.ACCESS_COARSE_LOCATION);

                    if ((fineLocationGranted != null && fineLocationGranted) ||
                        (coarseLocationGranted != null && coarseLocationGranted)) {
                        getLocationNow();
                    } else {
                        Toast.makeText(this, "Permission de localisation refusée", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getCurrentLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            getLocationNow();
        } else {
            locationPermissionLauncher.launch(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            });
        }
    }

    private void getLocationNow() {
        try {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                fusedLocationClient.getLastLocation()
                        .addOnSuccessListener(this, location -> {
                            if (location != null) {
                                currentLatitude = location.getLatitude();
                                currentLongitude = location.getLongitude();
                                textViewCoordinates.setText("Lat: " + currentLatitude + ", Lon: " + currentLongitude);
                                Toast.makeText(this, "Position obtenue", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(this, "Impossible d'obtenir la position", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(this, e -> {
                            Toast.makeText(this, "Erreur: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            }
        } catch (SecurityException e) {
            Toast.makeText(this, "Erreur de sécurité: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void createPOI() {
        String nom = editTextNom.getText().toString().trim();
        String type = editTextType.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();

        if (nom.isEmpty()) {
            editTextNom.setError("Nom requis");
            return;
        }

        if (type.isEmpty()) {
            editTextType.setError("Type requis");
            return;
        }

        if (currentLatitude == 0 && currentLongitude == 0) {
            Toast.makeText(this, "Veuillez obtenir la position GPS d'abord", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            POI poi = new POI(nom, type, currentLatitude, currentLongitude, description, classe.getId());
            long id = TablePOI.insert(db, poi);

            if (id > 0) {
                Toast.makeText(this, "POI créé avec succès", Toast.LENGTH_SHORT).show();
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
