package com.uici.classmanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.uici.classmanager.adapters.StudentAdapter;
import com.uici.classmanager.database.DatabaseHelper;
import com.uici.classmanager.database.TableInscriptions;
import com.uici.classmanager.models.Classe;
import com.uici.classmanager.models.User;

import java.util.ArrayList;
import java.util.List;

public class StudentListActivity extends AppCompatActivity implements StudentAdapter.OnStudentActionListener {

    private RecyclerView recyclerViewStudents;
    private TextView textViewEmpty;
    private TextView textViewTitle;
    private FloatingActionButton fabAddStudent;

    private StudentAdapter studentAdapter;
    private List<User> students;
    private SQLiteDatabase db;

    private Classe classe;
    private String userRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);

        classe = getIntent().getParcelableExtra("classe");
        if (classe == null) {
            finish();
            return;
        }

        SharedPreferences prefs = getSharedPreferences("ClassManager", MODE_PRIVATE);
        userRole = prefs.getString("userRole", "");

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        db = dbHelper.getReadableDatabase();

        recyclerViewStudents = findViewById(R.id.recyclerViewStudents);
        textViewEmpty = findViewById(R.id.textViewEmpty);
        textViewTitle = findViewById(R.id.textViewTitle);
        fabAddStudent = findViewById(R.id.fabAddStudent);

        textViewTitle.setText("Étudiants - " + classe.getNom());

        if (User.ROLE_ENSEIGNANT.equals(userRole)) {
            fabAddStudent.setVisibility(View.VISIBLE);
            fabAddStudent.setOnClickListener(v -> {
                Intent intent = new Intent(StudentListActivity.this, EnrollStudentActivity.class);
                intent.putExtra("classe", classe);
                startActivity(intent);
            });
        } else {
            fabAddStudent.setVisibility(View.GONE);
        }

        students = new ArrayList<>();
        setupRecyclerView();
        loadStudents();
    }

    private void setupRecyclerView() {
        boolean showDeleteButton = User.ROLE_ENSEIGNANT.equals(userRole);
        studentAdapter = new StudentAdapter(students, this, showDeleteButton);
        recyclerViewStudents.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewStudents.setAdapter(studentAdapter);
    }

    private void loadStudents() {
        students.clear();
        try {
            students.addAll(TableInscriptions.getStudentsByClass(db, classe.getId()));
            studentAdapter.notifyDataSetChanged();
            updateEmptyState();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateEmptyState() {
        if (students.isEmpty()) {
            textViewEmpty.setVisibility(View.VISIBLE);
            recyclerViewStudents.setVisibility(View.GONE);
        } else {
            textViewEmpty.setVisibility(View.GONE);
            recyclerViewStudents.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDeleteStudent(User student) {
        new AlertDialog.Builder(this)
                .setTitle("Retirer l'étudiant")
                .setMessage("Voulez-vous retirer " + student.getNom() + " " + student.getPrenom() + " de cette classe ?")
                .setPositiveButton("Oui", (dialog, which) -> {
                    try {
                        TableInscriptions.delete(db, student.getId(), classe.getId());
                        Toast.makeText(this, "Étudiant retiré avec succès", Toast.LENGTH_SHORT).show();
                        loadStudents();
                    } catch (Exception e) {
                        Toast.makeText(this, "Erreur: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Non", null)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadStudents();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (db != null) {
            db.close();
        }
    }
}
