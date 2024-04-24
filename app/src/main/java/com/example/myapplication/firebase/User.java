package com.example.myapplication.firebase;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;

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

import java.util.Objects;
import java.util.concurrent.Executors;

public class User extends AppCompatActivity {

    FirebaseAuth auth;
    MaterialButton login, createAccount, changePassword;
    TextInputLayout mailLayout, passwordLayout;

    Toolbar toolbar;

    ImageButton bioButton;

    TextInputEditText mailEdit, passwordEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        auth = FirebaseAuth.getInstance();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Toolbar.LayoutParams toolbarParams = new Toolbar.LayoutParams(
                Toolbar.LayoutParams.WRAP_CONTENT,
                Toolbar.LayoutParams.WRAP_CONTENT
        );
        toolbarParams.gravity = Gravity.END;

        bioButton = new ImageButton(this);
        bioButton.setImageResource(android.R.drawable.ic_secure);
        bioButton.setBackgroundColor(getResources().getColor(R.color.pink));
        bioButton.setLayoutParams(toolbarParams);
        bioButton.setPadding(0, 0, 32, 0);

        toolbar.addView(bioButton);

        login = findViewById(R.id.login);
        createAccount = findViewById(R.id.createAccount);
        changePassword = findViewById(R.id.changePassword);

        mailLayout = findViewById(R.id.mailLayout);
        passwordLayout = findViewById(R.id.passwordLayout);

        mailEdit = findViewById(R.id.mailEdit);
        passwordEdit = findViewById(R.id.passwordEdit);

        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.pink));

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Objects.requireNonNull(mailEdit.getText()).toString().isEmpty()) {
                    mailLayout.setError("Поле пустое!");
                } else if (Objects.requireNonNull(passwordEdit.getText()).toString().isEmpty()) {
                    passwordLayout.setError("Поле пустое!");
                } else {
                    ProgressDialog progressDialog = new ProgressDialog(User.this);
                    progressDialog.setMessage("Вход в аккаунт...");
                    progressDialog.show();

                    auth.signInWithEmailAndPassword(mailEdit.getText().toString(), passwordEdit.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            Toast.makeText(User.this, "Вход в аккаунт завершен успешно!", Toast.LENGTH_SHORT).show();

                            saveLoginStatus();

                            startActivity(new Intent(User.this, MainActivity.class));
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(User.this, "Произошла ошибка", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(User.this, CreateAccountActivity.class));
            }
        });

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(User.this, ChangePasswordActivity.class));
            }
        });

        bioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authenticateUserWithBiometrics();
            }
        });
    }

    private void saveLoginStatus() {
        SharedPreferences sharedPreferences = getSharedPreferences("login_status", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isUserLoggedIn", true);
        editor.apply();
    }

    private void saveBiotatus() {
        SharedPreferences sharedPreferences = getSharedPreferences("login_status", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isBio", true);
        editor.apply();
    }

    private void authenticateUserWithBiometrics() {
        BiometricManager biometricManager = BiometricManager.from(this);
        if (biometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS) {
            BiometricPrompt biometricPrompt = new BiometricPrompt(this, Executors.newSingleThreadExecutor(),
                    new BiometricPrompt.AuthenticationCallback() {
                        @Override
                        public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                            super.onAuthenticationSucceeded(result);
                            startActivity(new Intent(User.this, MainActivity.class));
                            saveBiotatus();
                            finish();
                        }
                        @Override
                        public void onAuthenticationFailed() {
                            super.onAuthenticationFailed();

                            Toast.makeText(User.this, "Биометрическая аутентификация не удалась!", Toast.LENGTH_SHORT).show();
                        }
                    });

            BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                    .setTitle("Аутентификация с помощью биометрии")
                    .setNegativeButtonText("Использовать пароль")
                    .build();

            biometricPrompt.authenticate(promptInfo);
        } else {
            Toast.makeText(this, "Биометрия недоступна на вашем устройстве!", Toast.LENGTH_SHORT).show();
        }
    }

}