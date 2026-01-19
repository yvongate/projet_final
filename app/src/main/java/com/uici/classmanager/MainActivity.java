package com.uici.classmanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;

public class MainActivity extends AppCompatActivity {

    private TextView textViewUserName;
    private MaterialCardView cardClasses;
    private Button buttonLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewUserName = findViewById(R.id.textViewUserName);
        cardClasses = findViewById(R.id.cardClasses);
        buttonLogout = findViewById(R.id.buttonLogout);

        SharedPreferences prefs = getSharedPreferences("ClassManager", MODE_PRIVATE);
        String userName = prefs.getString("userName", "Utilisateur");
        textViewUserName.setText(userName);

        cardClasses.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, ClassListActivity.class));
        });

        buttonLogout.setOnClickListener(v -> {
            prefs.edit().clear().apply();
            Intent intent = new Intent(MainActivity.this, SelectProfileActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}
