package com.example.myapplication.firebase;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class ChangePasswordActivity extends AppCompatActivity {

    FirebaseUser user;
    Toolbar toolbar;
    MaterialButton changePassword;
    TextInputLayout mailLayout;
    TextInputEditText mailEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        user = FirebaseAuth.getInstance().getCurrentUser();

        changePassword = findViewById(R.id.changePassword);

        mailLayout = findViewById(R.id.mailLayout);
        mailEdit = findViewById(R.id.mailEdit);

        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.pink));

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Objects.requireNonNull(mailEdit.getText()).toString().isEmpty()) {
                    mailLayout.setError("Поле пустое!");
                } else {
                    ProgressDialog progressDialog = new ProgressDialog(ChangePasswordActivity.this);
                    progressDialog.setMessage("Отправка письма для смены пароля...");
                    progressDialog.show();

                    String email = mailEdit.getText().toString();

                    FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        progressDialog.dismiss();
                                        Toast.makeText(ChangePasswordActivity.this, "Письмо для сброса пароля отправлено на указанный адрес", Toast.LENGTH_SHORT).show();
                                    } else {
                                        progressDialog.dismiss();
                                        Toast.makeText(ChangePasswordActivity.this, "Ошибка при отправке письма. Проверьте email.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }
}