package com.uici.classmanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.uici.classmanager.adapters.AnnonceAdapter;
import com.uici.classmanager.database.DatabaseHelper;
import com.uici.classmanager.database.TableAnnonces;
import com.uici.classmanager.models.Annonce;
import com.uici.classmanager.models.Classe;
import com.uici.classmanager.models.User;

import java.util.ArrayList;
import java.util.List;

public class AnnonceListActivity extends AppCompatActivity implements AnnonceAdapter.OnAnnonceClickListener {

    private RecyclerView recyclerViewAnnonces;
    private TextView textViewEmpty;
    private TextView textViewTitle;
    private FloatingActionButton fabAddAnnonce;

    private AnnonceAdapter annonceAdapter;
    private List<Annonce> annonces;
    private SQLiteDatabase db;

    private Classe classe;
    private String userRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_annonce_list);

        classe = getIntent().getParcelableExtra("classe");
        if (classe == null) {
            finish();
            return;
        }

        SharedPreferences prefs = getSharedPreferences("ClassManager", MODE_PRIVATE);
        userRole = prefs.getString("userRole", "");

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        db = dbHelper.getReadableDatabase();

        recyclerViewAnnonces = findViewById(R.id.recyclerViewAnnonces);
        textViewEmpty = findViewById(R.id.textViewEmpty);
        textViewTitle = findViewById(R.id.textViewTitle);
        fabAddAnnonce = findViewById(R.id.fabAddAnnonce);

        textViewTitle.setText("Annonces - " + classe.getNom());

        if (User.ROLE_ENSEIGNANT.equals(userRole)) {
            fabAddAnnonce.setVisibility(View.VISIBLE);
            fabAddAnnonce.setOnClickListener(v -> {
                Intent intent = new Intent(AnnonceListActivity.this, CreateAnnonceActivity.class);
                intent.putExtra("classe", classe);
                startActivity(intent);
            });
        } else {
            fabAddAnnonce.setVisibility(View.GONE);
        }

        annonces = new ArrayList<>();
        setupRecyclerView();
        loadAnnonces();
    }

    private void setupRecyclerView() {
        annonceAdapter = new AnnonceAdapter(annonces, this);
        recyclerViewAnnonces.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAnnonces.setAdapter(annonceAdapter);
    }

    private void loadAnnonces() {
        annonces.clear();
        try {
            annonces.addAll(TableAnnonces.selectByClasse(db, classe.getId()));
            annonceAdapter.notifyDataSetChanged();
            updateEmptyState();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateEmptyState() {
        if (annonces.isEmpty()) {
            textViewEmpty.setVisibility(View.VISIBLE);
            recyclerViewAnnonces.setVisibility(View.GONE);
        } else {
            textViewEmpty.setVisibility(View.GONE);
            recyclerViewAnnonces.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onAnnonceClick(Annonce annonce) {
        Intent intent = new Intent(this, AnnonceDetailActivity.class);
        intent.putExtra("annonce", annonce);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAnnonces();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (db != null) {
            db.close();
        }
    }
}
