package com.example.calculator;
import java.util.Stack;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    TextView calculationTextView;
    Boolean isEnter = true;
    String oldNumber;
    String operator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editText);
    }

    public void clickNumber(View view) {
        if (isEnter) editText.setText("");
        isEnter = false;

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
            number += "9";
        } else if (viewId == R.id.btn0) {
            number += "0";
        } else if (viewId == R.id.btnDot) {
            number += ".";
        } else if (viewId == R.id.btnPlusMinus) {
            number = "-" + number;
        }

        editText.setText(number);
    }

    public void operation(View view) {
        isEnter = true;
        oldNumber = editText.getText().toString();
        int viewId = view.getId();
        if (viewId == R.id.btnMinus) {
            operator = "-";
        } else if (viewId == R.id.btnPlus) {
            operator = "+";
        } else if (viewId == R.id.btnDivide) {
            operator = "/";
        } else if (viewId == R.id.btnMultiply) {
            operator = "*";
        }
    }

    public void resultEqual(View view) {
        String newNumber = editText.getText().toString();
        Double result = 0.0;
        int viewId = view.getId();
        if (operator.equals("-")) {
            result = Double.parseDouble(oldNumber) - Double.parseDouble(newNumber);
        } else if (viewId == R.id.btnPlus) {
            result = Double.parseDouble(oldNumber) + Double.parseDouble(newNumber);
        } else if (viewId == R.id.btnDivide) {
            result = Double.parseDouble(oldNumber) / Double.parseDouble(newNumber);
        } else if (viewId == R.id.btnMultiply) {
            result = Double.parseDouble(oldNumber) * Double.parseDouble(newNumber);
        }
    }



}