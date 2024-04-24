package com.example.myapplication.firebase;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.myapplication.R;
import com.example.myapplication.ui.MainActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.Objects;

public class CreateAccountActivity extends AppCompatActivity {

    FirebaseAuth auth;

    Toolbar toolbar;
    MaterialButton createAccount;
    TextInputLayout nameLayout, mailLayout, passwordLayout;
    TextInputEditText nameEdit, mailEdit, passwordEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        auth = FirebaseAuth.getInstance();

        createAccount = findViewById(R.id.createAccount);

        nameLayout = findViewById(R.id.nameLayout);
        mailLayout = findViewById(R.id.mailLayout);
        passwordLayout = findViewById(R.id.passwordLayout);

        nameEdit = findViewById(R.id.nameEdit);
        mailEdit = findViewById(R.id.mailEdit);
        passwordEdit = findViewById(R.id.passwordEdit);

        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.pink));

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Objects.requireNonNull(nameEdit.getText()).toString().isEmpty()) {
                    nameLayout.setError("Вы не ввели имя!");
                } else if (Objects.requireNonNull(mailEdit.getText()).toString().isEmpty()) {
                    mailLayout.setError("Вы не ввели почту!");
                } else if (Objects.requireNonNull(passwordEdit.getText()).toString().isEmpty()) {
                    passwordLayout.setError("Вы не ввели пароль!");
                } else {
                    ProgressDialog progressDialog = new ProgressDialog(CreateAccountActivity.this);
                    progressDialog.setMessage("Создание аккаунта...");
                    progressDialog.show();

                    auth.createUserWithEmailAndPassword(mailEdit.getText().toString(), passwordEdit.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {

                            UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(nameEdit.getText().toString())
                                    .build();
                            Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).updateProfile(request).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    progressDialog.dismiss();
                                    Toast.makeText(CreateAccountActivity.this, "Аккаунт успешно создан", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(CreateAccountActivity.this, MainActivity.class));
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(CreateAccountActivity.this, "Произошла ошибка при создании аккаунта", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(CreateAccountActivity.this, "Произошла ошибка при создании аккаунта", Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}