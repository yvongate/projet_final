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
import com.uici.classmanager.adapters.POIAdapter;
import com.uici.classmanager.database.DatabaseHelper;
import com.uici.classmanager.database.TablePOI;
import com.uici.classmanager.models.Classe;
import com.uici.classmanager.models.POI;
import com.uici.classmanager.models.User;

import java.util.ArrayList;
import java.util.List;

public class POIListActivity extends AppCompatActivity implements POIAdapter.OnPOIClickListener {

    private RecyclerView recyclerViewPOIs;
    private TextView textViewEmpty;
    private TextView textViewTitle;
    private FloatingActionButton fabAddPOI;

    private POIAdapter poiAdapter;
    private List<POI> pois;
    private SQLiteDatabase db;

    private Classe classe;
    private String userRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poi_list);

        classe = getIntent().getParcelableExtra("classe");
        if (classe == null) {
            finish();
            return;
        }

        SharedPreferences prefs = getSharedPreferences("ClassManager", MODE_PRIVATE);
        userRole = prefs.getString("userRole", "");

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        db = dbHelper.getReadableDatabase();

        recyclerViewPOIs = findViewById(R.id.recyclerViewPOIs);
        textViewEmpty = findViewById(R.id.textViewEmpty);
        textViewTitle = findViewById(R.id.textViewTitle);
        fabAddPOI = findViewById(R.id.fabAddPOI);

        textViewTitle.setText("Points d'Intérêt - " + classe.getNom());

        if (User.ROLE_ENSEIGNANT.equals(userRole)) {
            fabAddPOI.setVisibility(View.VISIBLE);
            fabAddPOI.setOnClickListener(v -> {
                Intent intent = new Intent(POIListActivity.this, CreatePOIActivity.class);
                intent.putExtra("classe", classe);
                startActivity(intent);
            });
        } else {
            fabAddPOI.setVisibility(View.GONE);
        }

        pois = new ArrayList<>();
        setupRecyclerView();
        loadPOIs();
    }

    private void setupRecyclerView() {
        poiAdapter = new POIAdapter(pois, this);
        recyclerViewPOIs.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewPOIs.setAdapter(poiAdapter);
    }

    private void loadPOIs() {
        pois.clear();
        try {
            pois.addAll(TablePOI.selectByClasse(db, classe.getId()));
            poiAdapter.notifyDataSetChanged();
            updateEmptyState();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateEmptyState() {
        if (pois.isEmpty()) {
            textViewEmpty.setVisibility(View.VISIBLE);
            recyclerViewPOIs.setVisibility(View.GONE);
        } else {
            textViewEmpty.setVisibility(View.GONE);
            recyclerViewPOIs.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPOIClick(POI poi) {
        Intent intent = new Intent(this, MapActivity.class);
        intent.putExtra("poi", poi);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPOIs();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (db != null) {
            db.close();
        }
    }
}
