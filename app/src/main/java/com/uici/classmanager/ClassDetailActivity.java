package com.uici.classmanager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;
import com.uici.classmanager.models.Classe;

public class ClassDetailActivity extends AppCompatActivity {

    private TextView textViewNom;
    private TextView textViewAnnee;
    private TextView textViewDescription;
    private MaterialCardView cardStudents;
    private MaterialCardView cardAnnonces;
    private MaterialCardView cardPOI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_detail);

        textViewNom = findViewById(R.id.textViewNom);
        textViewAnnee = findViewById(R.id.textViewAnnee);
        textViewDescription = findViewById(R.id.textViewDescription);
        cardStudents = findViewById(R.id.cardStudents);
        cardAnnonces = findViewById(R.id.cardAnnonces);
        cardPOI = findViewById(R.id.cardPOI);

        Classe classe = getIntent().getParcelableExtra("classe");

        if (classe != null) {
            textViewNom.setText(classe.getNom());
            textViewAnnee.setText(classe.getAnneeAcademique());
            textViewDescription.setText(classe.getDescription());

            cardStudents.setOnClickListener(v -> {
                Intent intent = new Intent(ClassDetailActivity.this, StudentListActivity.class);
                intent.putExtra("classe", classe);
                startActivity(intent);
            });

            cardAnnonces.setOnClickListener(v -> {
                Intent intent = new Intent(ClassDetailActivity.this, AnnonceListActivity.class);
                intent.putExtra("classe", classe);
                startActivity(intent);
            });

            cardPOI.setOnClickListener(v -> {
                Intent intent = new Intent(ClassDetailActivity.this, POIListActivity.class);
                intent.putExtra("classe", classe);
                startActivity(intent);
            });
        }
    }
}
