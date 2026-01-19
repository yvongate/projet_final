package com.uici.classmanager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.uici.classmanager.models.POI;

public class MapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        POI poi = getIntent().getParcelableExtra("poi");
        if (poi == null) {
            finish();
            return;
        }

        try {
            String label = poi.getNom() + " - " + poi.getType();
            String uriString = "geo:" + poi.getLatitude() + "," + poi.getLongitude()
                    + "?q=" + poi.getLatitude() + "," + poi.getLongitude()
                    + "(" + Uri.encode(label) + ")";

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uriString));
            intent.setPackage("com.google.android.apps.maps");

            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            } else {
                intent.setPackage(null);
                startActivity(intent);
            }
            finish();
        } catch (Exception e) {
            Toast.makeText(this, "Erreur lors de l'ouverture de la carte: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
