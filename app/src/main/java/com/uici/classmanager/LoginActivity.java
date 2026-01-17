package com.uici.classmanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.uici.classmanager.database.DatabaseHelper;
import com.uici.classmanager.database.TableUsers;
import com.uici.classmanager.models.User;

public class LoginActivity extends AppCompatActivity {

    private TextView textViewRole;
    private TextInputEditText editTextEmail;
    private TextInputEditText editTextPassword;
    private Button buttonLogin;
    private TextView textViewRegister;

    private String role;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        role = getIntent().getStringExtra("role");

        textViewRole = findViewById(R.id.textViewRole);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        textViewRegister = findViewById(R.id.textViewRegister);

        textViewRole.setText(role);

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        db = dbHelper.getWritableDatabase();

        buttonLogin.setOnClickListener(v -> login());

        textViewRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            intent.putExtra("role", role);
            startActivity(intent);
        });
    }

    private void login() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (email.isEmpty()) {
            editTextEmail.setError("Email requis");
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("Mot de passe requis");
            return;
        }

        try {
            User user = TableUsers.login(db, email, password);

            if (user != null && user.getRole().equals(role)) {
                SharedPreferences prefs = getSharedPreferences("ClassManager", MODE_PRIVATE);
                prefs.edit()
                        .putLong("userId", user.getId())
                        .putString("userRole", user.getRole())
                        .putString("userName", user.getNom() + " " + user.getPrenom())
                        .apply();

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Email ou mot de passe incorrect", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Erreur de connexion", Toast.LENGTH_SHORT).show();
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
