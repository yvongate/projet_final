package com.uici.classmanager;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.uici.classmanager.adapters.EnrollAdapter;
import com.uici.classmanager.database.DatabaseHelper;
import com.uici.classmanager.database.TableInscriptions;
import com.uici.classmanager.models.Classe;
import com.uici.classmanager.models.User;

import java.util.ArrayList;
import java.util.List;

public class EnrollStudentActivity extends AppCompatActivity implements EnrollAdapter.OnEnrollClickListener {

    private RecyclerView recyclerViewAvailableStudents;
    private TextView textViewEmpty;
    private TextView textViewTitle;

    private EnrollAdapter enrollAdapter;
    private List<User> availableStudents;
    private SQLiteDatabase db;

    private Classe classe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enroll_student);

        classe = getIntent().getParcelableExtra("classe");
        if (classe == null) {
            finish();
            return;
        }

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        db = dbHelper.getWritableDatabase();

        recyclerViewAvailableStudents = findViewById(R.id.recyclerViewAvailableStudents);
        textViewEmpty = findViewById(R.id.textViewEmpty);
        textViewTitle = findViewById(R.id.textViewTitle);

        textViewTitle.setText("Inscrire à " + classe.getNom());

        availableStudents = new ArrayList<>();
        setupRecyclerView();
        loadAvailableStudents();
    }

    private void setupRecyclerView() {
        enrollAdapter = new EnrollAdapter(availableStudents, this);
        recyclerViewAvailableStudents.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAvailableStudents.setAdapter(enrollAdapter);
    }

    private void loadAvailableStudents() {
        availableStudents.clear();
        try {
            availableStudents.addAll(TableInscriptions.getAvailableStudents(db, classe.getId()));
            enrollAdapter.notifyDataSetChanged();
            updateEmptyState();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateEmptyState() {
        if (availableStudents.isEmpty()) {
            textViewEmpty.setVisibility(View.VISIBLE);
            recyclerViewAvailableStudents.setVisibility(View.GONE);
        } else {
            textViewEmpty.setVisibility(View.GONE);
            recyclerViewAvailableStudents.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onEnrollClick(User student) {
        try {
            long result = TableInscriptions.insert(db, student.getId(), classe.getId());
            if (result > 0) {
                Toast.makeText(this, student.getNom() + " " + student.getPrenom() + " inscrit avec succès", Toast.LENGTH_SHORT).show();
                loadAvailableStudents();
            } else {
                Toast.makeText(this, "Erreur lors de l'inscription", Toast.LENGTH_SHORT).show();
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
