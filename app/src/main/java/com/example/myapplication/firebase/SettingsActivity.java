package com.example.myapplication.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.myapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SettingsActivity extends AppCompatActivity {

    Button theme_default, theme_orange, theme_green, theme_purple;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        FirebaseApp.initializeApp(SettingsActivity.this);
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        theme_default = findViewById(R.id.theme1);
        theme_orange = findViewById(R.id.theme2);
        theme_green = findViewById(R.id.theme3);
        theme_purple = findViewById(R.id.theme4);

        theme_default.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAndReturnAppTheme(1);
            }
        });

        theme_orange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAndReturnAppTheme(2);
            }
        });

        theme_green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAndReturnAppTheme(3);
            }
        });

        theme_purple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAndReturnAppTheme(4);
            }
        });

        applySavedTheme();
    }

    private void saveThemeToSharedPreferences(int themeId) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("theme_id", themeId);
        editor.apply();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getTheme();
    }

    private void applySavedTheme() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        int savedThemeId = preferences.getInt("theme_id", 1);

        setAppTheme(savedThemeId);

        FirebaseApp.initializeApp(SettingsActivity.this);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("theme").document("current")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        int dbThemeId = documentSnapshot.getLong("theme_id").intValue();

                        if (dbThemeId != savedThemeId) {
                            saveThemeToSharedPreferences(dbThemeId);
                            setAppTheme(dbThemeId);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SettingsActivity.this, "Failed to read theme ID from Firestore!", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void setAndReturnAppTheme(int themeId) {
        FirebaseApp.initializeApp(SettingsActivity.this);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> themeData = new HashMap<>();
        themeData.put("theme_id", themeId);

        db.collection("theme").document("current")
                .set(themeData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(SettingsActivity.this, "Theme ID successfully written to Firestore!", Toast.LENGTH_SHORT).show();
                        setAppTheme(themeId);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SettingsActivity.this, "Theme ID NOT successfully written to Firestore!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setAppTheme(int themeId) {
        switch (themeId) {
            case 1:
                toolbar.setBackgroundResource(R.color.blue);
                getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.blue));
                break;
            case 2:
                toolbar.setBackgroundResource(R.color.breeze);
                getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.breeze));
                break;
            case 3:
                toolbar.setBackgroundResource(R.color.green);
                getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.green));
                break;
            case 4:
                toolbar.setBackgroundResource(R.color.purple);
                getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.purple));
                break;
        }
    }
}
