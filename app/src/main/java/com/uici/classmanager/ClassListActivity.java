package com.uici.classmanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.uici.classmanager.adapters.ClassAdapter;
import com.uici.classmanager.database.DatabaseHelper;
import com.uici.classmanager.database.TableClasses;
import com.uici.classmanager.models.Classe;
import com.uici.classmanager.models.User;

import java.util.ArrayList;
import java.util.List;

import android.widget.TextView;

public class ClassListActivity extends AppCompatActivity implements ClassAdapter.OnClassClickListener {

    private RecyclerView recyclerViewClasses;
    private TextView textViewEmpty;
    private TextView textViewTitle;
    private FloatingActionButton fabAddClass;

    private ClassAdapter classAdapter;
    private List<Classe> classes;
    private SQLiteDatabase db;

    private String userRole;
    private long userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_list);

        SharedPreferences prefs = getSharedPreferences("ClassManager", MODE_PRIVATE);
        userRole = prefs.getString("userRole", "");
        userId = prefs.getLong("userId", -1);

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        db = dbHelper.getReadableDatabase();

        recyclerViewClasses = findViewById(R.id.recyclerViewClasses);
        textViewEmpty = findViewById(R.id.textViewEmpty);
        textViewTitle = findViewById(R.id.textViewTitle);
        fabAddClass = findViewById(R.id.fabAddClass);

        if (User.ROLE_ENSEIGNANT.equals(userRole)) {
            textViewTitle.setText("Mes Classes");
            fabAddClass.setVisibility(View.VISIBLE);
            fabAddClass.setOnClickListener(v -> {
                startActivity(new Intent(ClassListActivity.this, CreateClassActivity.class));
            });
        } else {
            textViewTitle.setText("Classes Disponibles");
            fabAddClass.setVisibility(View.GONE);
        }

        classes = new ArrayList<>();
        setupRecyclerView();
        loadClasses();
    }

    private void setupRecyclerView() {
        classAdapter = new ClassAdapter(classes, this);
        recyclerViewClasses.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewClasses.setAdapter(classAdapter);
    }

    private void loadClasses() {
        classes.clear();
        try {
            if (User.ROLE_ENSEIGNANT.equals(userRole)) {
                classes.addAll(TableClasses.selectByEnseignant(db, userId));
            } else {
                classes.addAll(TableClasses.selectAll(db));
            }
            classAdapter.notifyDataSetChanged();
            updateEmptyState();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateEmptyState() {
        if (classes.isEmpty()) {
            textViewEmpty.setVisibility(View.VISIBLE);
            recyclerViewClasses.setVisibility(View.GONE);
        } else {
            textViewEmpty.setVisibility(View.GONE);
            recyclerViewClasses.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClassClick(Classe classe) {
        Intent intent = new Intent(this, ClassDetailActivity.class);
        intent.putExtra("classe", classe);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadClasses();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (db != null) {
            db.close();
        }
    }
}
