package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editText);
    }

    public void clickNumber(View view) {
        String number = editText.getText().toString();
        int viewId = view.getId();
        if (viewId == R.id.btn1) {
            number += "1";
        } else if (viewId == R.id.btn2) {
            number += "2";
        } else if (viewId == R.id.btn3) {
            number += "3";
        } else if (viewId == R.id.btn4) {
            number += "4";
        } else if (viewId == R.id.btn5) {
            number += "5";
        } else if (viewId == R.id.btn6) {
            number += "6";
        } else if (viewId == R.id.btn7) {
            number += "7";
        } else if (viewId == R.id.btn8) {
            number += "8";
        } else if (viewId == R.id.btn9) {
            number += "0";
        } else if (viewId == R.id.btn0) {
            number += "0";
        }


        editText.setText(number);
    }
}