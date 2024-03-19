package com.example.myapplication.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.view.Menu;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.example.myapplication.R;
import com.example.myapplication.data.CalculatorUtils;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener{
    boolean isShortPress = false;
    private static final String TAG = "MainActivity";

    boolean isLongPress = false;

    float x, downX, moveX;
    float y, downY, moveY;

    private static final float SLOP_DISTANCE = 50;



    Button bt1, bt2, bt3, bt4, bt5, bt6, bt7, bt8, bt9, bt0, bt_dot,
            bt_equal, bt_plus, bt_min, bt_mul, bt_div, bt_sqrt, bt_square,
            bt_inv, bt_sin, bt_cos, bt_log, bt_ln, btb1, btb2, bt_ac, bt_c;

    TextView tv_main, tv_sec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        int orientation = getResources().getConfiguration().orientation;

        tv_main = findViewById(R.id.calculatorText);
        tv_sec = findViewById(R.id.memoryText);

        bt1 = findViewById(R.id.btn1);
        bt2 = findViewById(R.id.btn2);
        bt3 = findViewById(R.id.btn3);
        bt4 = findViewById(R.id.btn4);
        bt5 = findViewById(R.id.btn5);
        bt6 = findViewById(R.id.btn6);
        bt7 = findViewById(R.id.btn7);
        bt8 = findViewById(R.id.btn8);
        bt9 = findViewById(R.id.btn9);
        bt0 = findViewById(R.id.btn0);

        bt_dot = findViewById(R.id.btnDot);
        bt_equal = findViewById(R.id.btnEqual);
        bt_plus = findViewById(R.id.btnPlus);
        bt_min = findViewById(R.id.btnMinus);
        bt_mul = findViewById(R.id.btnMultiply);
        bt_div = findViewById(R.id.btnDivide);

        bt_inv = findViewById(R.id.btnPlusMinus);
        bt_ac = findViewById(R.id.btnAC);
        bt_c = findViewById(R.id.btnC);

        CalculatorUtils.setNumberClickListener(bt0, tv_main, "0");
        CalculatorUtils.setNumberClickListener(bt1, tv_main, "1");
        CalculatorUtils.setNumberClickListener(bt2, tv_main, "2");
        CalculatorUtils.setNumberClickListener(bt3, tv_main, "3");
        CalculatorUtils.setNumberClickListener(bt4, tv_main, "4");
        CalculatorUtils.setNumberClickListener(bt5, tv_main, "5");
        CalculatorUtils.setNumberClickListener(bt6, tv_main, "6");
        CalculatorUtils.setNumberClickListener(bt7, tv_main, "7");
        CalculatorUtils.setNumberClickListener(bt8, tv_main, "8");
        CalculatorUtils.setNumberClickListener(bt9, tv_main, "9");

        CalculatorUtils.setDotClickListener(bt_dot, tv_main);

        CalculatorUtils.setClearClickListener(bt_ac, tv_main, tv_sec);
        CalculatorUtils.setBackspaceClickListener(bt_c, tv_main);

        CalculatorUtils.setOperatorClickListener(bt_plus, tv_main, "+");
        CalculatorUtils.setOperatorClickListener(bt_min, tv_main, "-");
        CalculatorUtils.setOperatorClickListener(bt_div, tv_main, "/");
        CalculatorUtils.setOperatorClickListener(bt_mul, tv_main, "*");

        CalculatorUtils.setInverseClickListener(bt_inv, tv_main);

        CalculatorUtils.setEqualClickListener(bt_equal, tv_main, tv_sec);

        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            bt_sqrt = findViewById(R.id.btnSqr);
            bt_square = findViewById(R.id.btnExp);

            bt_sin = findViewById(R.id.btnSin);
            bt_cos = findViewById(R.id.btnCos);

            bt_log = findViewById(R.id.btnLg);
            bt_ln = findViewById(R.id.btnLn);
            btb1 = findViewById(R.id.btnOpen);
            btb2 = findViewById(R.id.btnClose);

            CalculatorUtils.setSqrtClickListener(bt_sqrt, tv_main, tv_sec);

            CalculatorUtils.setBtb1ClickListener(btb1, tv_main);
            CalculatorUtils.setBtb2ClickListener(btb2, tv_main);

            CalculatorUtils.setBtSinClickListener(bt_sin, tv_main);
            CalculatorUtils.setBtCosClickListener(bt_cos, tv_main);

            CalculatorUtils.setBtLogClickListener(bt_log, tv_main);
            CalculatorUtils.setBtLnClickListener(bt_ln, tv_main);

            CalculatorUtils.setBtSquareClickListener(bt_square, tv_main, tv_sec);
        }
        View rootView = findViewById(android.R.id.content);
        rootView.setOnTouchListener(this);
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {

            tv_main.setText("");
            tv_sec.setText("");

            isShortPress = false;
            isLongPress = true;
            return true;
        }
        return super.onKeyLongPress(keyCode, event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            event.startTracking();
            if (isLongPress == true) {
                isShortPress = false;
            } else {
                isShortPress = true;
                isLongPress = false;
            }
            return true;
        }
        else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            String val = tv_main.getText().toString();
            if (!val.isEmpty()) {
                val = val.substring(0, val.length() - 1);
                tv_main.setText(val);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            event.startTracking();
            if (isShortPress) {
                CalculatorUtils.handleEqualClick(tv_main, tv_sec);
            }
            isShortPress = true;
            isLongPress = false;
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        x = event.getX();
        y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = x;
                downY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                moveX = x;
                moveY = y;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (downY > moveY + SLOP_DISTANCE) {
                    CalculatorUtils.handleOperatorClick(tv_main, "+");
                } else if (downY < moveY - SLOP_DISTANCE) {
                    CalculatorUtils.handleOperatorClick(tv_main, "-");
                } else if (downX > moveX + SLOP_DISTANCE) {
                    CalculatorUtils.handleOperatorClick(tv_main, "*");
                } else if (downX < moveX - SLOP_DISTANCE) {
                    CalculatorUtils.handleOperatorClick(tv_main, "/");
                }
                break;
        }
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

}