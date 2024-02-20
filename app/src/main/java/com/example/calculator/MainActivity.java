package com.example.calculator;
import java.util.SplittableRandom;
import java.util.Stack;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

//    EditText editText;
    TextView calculatorText, memoryText;
    Button btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btn0,
            btnAC, btnC, btnDivide, btnMultiply, btnPlus, btnMinus, btnPlusMinus, btnEqual,
            btnDot, btnPercent,
            btnExp, btnSqr, btnLg, btnLn, btnSin, btnCos, btnOpen, btnClose;
    Boolean isEnter = true;
    String oldNumber;
    String operator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        btn4 = findViewById(R.id.btn4);
        btn5 = findViewById(R.id.btn5);
        btn6 = findViewById(R.id.btn6);
        btn7 = findViewById(R.id.btn7);
        btn8 = findViewById(R.id.btn8);
        btn9 = findViewById(R.id.btn9);
        btn0 = findViewById(R.id.btn0);

        btnAC = findViewById(R.id.btnAC);
        btnC = findViewById(R.id.btnC);
        btnDivide = findViewById(R.id.btnDivide);
        btnMultiply = findViewById(R.id.btnMultiply);
        btnPlus = findViewById(R.id.btnPlus);
        btnMinus = findViewById(R.id.btnMinus);
        btnPlusMinus = findViewById(R.id.btnPlusMinus);
        btnEqual = findViewById(R.id.btnEqual);
        btnDot = findViewById(R.id.btnDot);
        btnPercent = findViewById(R.id.btnPercent);
        btnExp = findViewById(R.id.btnExp);
        btnSqr = findViewById(R.id.btnSqr);
        btnLg = findViewById(R.id.btnLg);
        btnLn = findViewById(R.id.btnLn);
        btnSin = findViewById(R.id.btnSin);
        btnCos = findViewById(R.id.btnCos);
        btnOpen = findViewById(R.id.btnOpen);
        btnClose = findViewById(R.id.btnClose);

        calculatorText = findViewById(R.id.calculatorText);
        memoryText = findViewById(R.id.memoryText);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calculatorText.setText(calculatorText.getText()+"1");
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calculatorText.setText(calculatorText.getText()+"2");
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calculatorText.setText(calculatorText.getText()+"3");
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calculatorText.setText(calculatorText.getText()+"4");
            }
        });
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calculatorText.setText(calculatorText.getText()+"5");
            }
        });
        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calculatorText.setText(calculatorText.getText()+"6");
            }
        });
        btn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calculatorText.setText(calculatorText.getText()+"7");
            }
        });
        btn8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calculatorText.setText(calculatorText.getText()+"8");
            }
        });
        btn9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calculatorText.setText(calculatorText.getText()+"9");
            }
        });
        btn0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calculatorText.setText(calculatorText.getText()+"0");
            }
        });
        btnAC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calculatorText.setText("");
                memoryText.setText("");
            }
        });
        btnC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String expression = calculatorText.getText().toString();
                if (!expression.isEmpty()) {
                    expression = expression.substring(0, expression.length() - 1);
                    calculatorText.setText(expression);
                }
            }
        });
        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mathOperatorClick("+");
            }
        });
        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mathOperatorClick("-");
            }
        });
        btnDivide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mathOperatorClick("/");
            }
        });
        btnMultiply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mathOperatorClick("*");
            }
        });
        btnPercent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mathOperatorClick("%");
            }
        });
        btnEqual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        btnDot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        btnSqr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        btnExp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        btnSin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        btnCos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        btnLg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        btnLn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        btnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        btnPlusMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }

    private Boolean isOperator(char operator) {
        return operator == '+' || operator == '-' || operator == '*' || operator == '/' || operator == '%';
    }

    private void mathOperatorClick(String operator) {
        String expression = calculatorText.getText().toString();

        if (expression.isEmpty() || expression.charAt(expression.length() - 1) == '(') {
            return;
        }

        if (isOperator(expression.charAt(expression.length() - 1))) {
            expression = expression.substring(0, expression.length() - 1);
        }

        calculatorText.setText(expression + operator);
    }
}