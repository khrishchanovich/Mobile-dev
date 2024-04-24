package com.example.myapplication.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    Toolbar toolbar;
    ImageButton deleteAllButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LinearLayout linearLayout = findViewById(R.id.linearLayout);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(32, 16, 32, 16);

        Toolbar.LayoutParams toolbarParams = new Toolbar.LayoutParams(
                Toolbar.LayoutParams.WRAP_CONTENT,
                Toolbar.LayoutParams.WRAP_CONTENT
        );
        toolbarParams.gravity = Gravity.END;

        deleteAllButton = new ImageButton(this);
        deleteAllButton.setImageResource(android.R.drawable.ic_menu_delete);
        deleteAllButton.setBackgroundColor(getResources().getColor(R.color.blue));
        deleteAllButton.setLayoutParams(toolbarParams);
        deleteAllButton.setPadding(0, 0, 32, 0);

        toolbar.addView(deleteAllButton);

        FirebaseApp.initializeApp(HistoryActivity.this);
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        CollectionReference equationsRef = db.collection("history");
        equationsRef.orderBy("timestamp", Query.Direction.DESCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    String equation = documentSnapshot.getString("equation");
                    String result = documentSnapshot.getString("result");

                    CardView cardView = new CardView(HistoryActivity.this);

                    cardView.setLayoutParams(layoutParams);
                    cardView.setRadius(32);
                    cardView.setCardElevation(8);

                    LinearLayout levelsCard = new LinearLayout(HistoryActivity.this);
                    levelsCard.setOrientation(LinearLayout.VERTICAL);

                    TextView textViewEq = new TextView(HistoryActivity.this);
                    TextView textViewRes = new TextView(HistoryActivity.this);

                    textViewEq.setText(equation);
                    textViewRes.setText(" = " + result);

                    textViewEq.setTextSize(20);
                    textViewEq.setTextColor(getResources().getColor(R.color.gray));
                    textViewEq.setPadding(32, 32, 32, 32);

                    textViewRes.setTextSize(27);
                    textViewRes.setPadding(32, 16, 32, 32);

                    levelsCard.addView(textViewEq);
                    levelsCard.addView(textViewRes);

                    cardView.addView(levelsCard);

                    linearLayout.addView(cardView);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(HistoryActivity.this, "Добавление примера в базу данных провалено!", Toast.LENGTH_SHORT).show();
            }
        });

        deleteAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CollectionReference equationsRef = db.collection("history");

                List<View> viewsToRemove = new ArrayList<>();

                for (int i = 0; i < linearLayout.getChildCount(); i++) {
                    View child = linearLayout.getChildAt(i);
                    if (child instanceof CardView) {
                        viewsToRemove.add(child);
                    }
                }

                List<String> documentIdsToRemove = new ArrayList<>();
                equationsRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            String documentId = documentSnapshot.getId();
                            documentIdsToRemove.add(documentId);
                        }

                        for (String documentId : documentIdsToRemove) {
                            equationsRef.document(documentId).delete();
                        }

                        for (View view : viewsToRemove) {
                            linearLayout.removeView(view);
                        }

                        Toast.makeText(HistoryActivity.this, "Успешное удаление истории из базы данных!", Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(HistoryActivity.this, "Удаление провалено!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        applySavedTheme();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveThemeToSharedPreferences(int themeId) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("theme_id", themeId);
        editor.apply();
    }

    private void applySavedTheme() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        int savedThemeId = preferences.getInt("theme_id", 1);

        setAppTheme(savedThemeId);

        FirebaseApp.initializeApp(HistoryActivity.this);
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
                        Toast.makeText(HistoryActivity.this, "Добавление theme_id в базу данных не прошло!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setAppTheme(int themeId) {
        switch (themeId) {
            case 1:
                toolbar.setBackgroundResource(R.color.blue);
                deleteAllButton.setBackgroundColor(getResources().getColor(R.color.blue));
                getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.blue));
                break;
            case 2:
                toolbar.setBackgroundResource(R.color.breeze);
                deleteAllButton.setBackgroundColor(getResources().getColor(R.color.breeze));
                getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.breeze));
                break;
            case 3:
                toolbar.setBackgroundResource(R.color.green);
                deleteAllButton.setBackgroundColor(getResources().getColor(R.color.green));
                getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.green));
                break;
            case 4:
                toolbar.setBackgroundResource(R.color.purple);
                deleteAllButton.setBackgroundColor(getResources().getColor(R.color.purple));
                getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.purple));
                break;
        }
    }
}