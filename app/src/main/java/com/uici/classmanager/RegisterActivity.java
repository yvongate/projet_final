package com.uici.classmanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.uici.classmanager.database.DatabaseHelper;
import com.uici.classmanager.database.TableUsers;
import com.uici.classmanager.models.User;

public class RegisterActivity extends AppCompatActivity {

    private TextView textViewRole;
    private TextInputEditText editTextNom;
    private TextInputEditText editTextPrenom;
    private TextInputEditText editTextEmail;
    private TextInputEditText editTextPassword;
    private TextInputEditText editTextConfirmPassword;
    private Button buttonRegister;
    private TextView textViewLogin;

    private String role;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        role = getIntent().getStringExtra("role");

        textViewRole = findViewById(R.id.textViewRole);
        editTextNom = findViewById(R.id.editTextNom);
        editTextPrenom = findViewById(R.id.editTextPrenom);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        buttonRegister = findViewById(R.id.buttonRegister);
        textViewLogin = findViewById(R.id.textViewLogin);

        textViewRole.setText(role);

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        db = dbHelper.getWritableDatabase();

        buttonRegister.setOnClickListener(v -> register());

        textViewLogin.setOnClickListener(v -> {
            finish();
        });
    }

    private void register() {
        String nom = editTextNom.getText().toString().trim();
        String prenom = editTextPrenom.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String confirmPassword = editTextConfirmPassword.getText().toString().trim();

        if (nom.isEmpty()) {
            editTextNom.setError("Nom requis");
            return;
        }

        if (prenom.isEmpty()) {
            editTextPrenom.setError("Prénom requis");
            return;
        }

        if (email.isEmpty()) {
            editTextEmail.setError("Email requis");
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Email invalide");
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("Mot de passe requis");
            return;
        }

        if (password.length() < 6) {
            editTextPassword.setError("Mot de passe trop court (minimum 6 caractères)");
            return;
        }

        if (!password.equals(confirmPassword)) {
            editTextConfirmPassword.setError("Les mots de passe ne correspondent pas");
            return;
        }

        try {
            if (TableUsers.emailExists(db, email)) {
                editTextEmail.setError("Cet email est déjà utilisé");
                return;
            }

            User user = new User(nom, prenom, email, password, role);
            long userId = TableUsers.insert(db, user);

            if (userId > 0) {
                SharedPreferences prefs = getSharedPreferences("ClassManager", MODE_PRIVATE);
                prefs.edit()
                        .putLong("userId", userId)
                        .putString("userRole", role)
                        .putString("userName", nom + " " + prenom)
                        .apply();

                Toast.makeText(this, "Inscription réussie", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
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
