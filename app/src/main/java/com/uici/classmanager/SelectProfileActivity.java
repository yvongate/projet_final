package com.uici.classmanager;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;
import com.uici.classmanager.models.User;

public class SelectProfileActivity extends AppCompatActivity {

    private MaterialCardView cardEnseignant;
    private MaterialCardView cardEtudiant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_profile);

        cardEnseignant = findViewById(R.id.cardEnseignant);
        cardEtudiant = findViewById(R.id.cardEtudiant);

        cardEnseignant.setOnClickListener(v -> {
            Intent intent = new Intent(SelectProfileActivity.this, LoginActivity.class);
            intent.putExtra("role", User.ROLE_ENSEIGNANT);
            startActivity(intent);
        });

        cardEtudiant.setOnClickListener(v -> {
            Intent intent = new Intent(SelectProfileActivity.this, LoginActivity.class);
            intent.putExtra("role", User.ROLE_ETUDIANT);
            startActivity(intent);
        });
    }
}
