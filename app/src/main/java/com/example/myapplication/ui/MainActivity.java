package com.example.myapplication.ui;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.preference.PreferenceManager;

import com.example.myapplication.R;
import com.example.myapplication.firebase.HistoryActivity;
import com.example.myapplication.firebase.SettingsActivity;
import com.example.myapplication.data.CalculatorUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {

    private final ActivityResultLauncher<String> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
        @Override
        public void onActivityResult(Boolean o) {
            if (o) {
                Toast.makeText(MainActivity.this, "Post notification permission granted!", Toast.LENGTH_SHORT).show();
            }
        }
    });

    boolean isShortPress = false;
    private static final String TAG = "MainActivity";
    private static final int SETTINGS_REQUEST_CODE = 100;

    boolean isLongPress = false;

    float x, downX, moveX;
    float y, downY, moveY;

    float SLOP_DISTANCE = 50;

    Button bt1, bt2, bt3, bt4, bt5, bt6, bt7, bt8, bt9, bt0, bt_dot,
            bt_equal, bt_plus, bt_min, bt_mul, bt_div, bt_sqrt, bt_square,
            bt_inv, bt_sin, bt_cos, bt_log, bt_ln, btb1, btb2, bt_ac, bt_c;

    MenuItem settings_menu, history_menu;
    TextView tv_main, tv_sec;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                activityResultLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        }

        toolbar = findViewById(R.id.toolbar);
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

        applySavedTheme();
    }


    private void applyTheme(int themeId) {
        int orientation = getResources().getConfiguration().orientation;
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        switch (themeId) {
            case 1:
                window.setStatusBarColor(getResources().getColor(R.color.blue));

                setToolbarColor(R.color.blue);
                setButtonColor(R.color.blue);
                setNumberColor(R.color.pink);
                setOperationColor(R.color.darkpink);

                if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    setFuncColor(R.color.violet);
                }
                break;
            case 2:
                window.setStatusBarColor(getResources().getColor(R.color.breeze));

                setToolbarColor(R.color.breeze);
                setButtonColor(R.color.breeze);
                setNumberColor(R.color.orange);
                setOperationColor(R.color.darkorange);

                if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    setFuncColor(R.color.darkbreeze);
                }
                break;
            case 3:
                window.setStatusBarColor(getResources().getColor(R.color.green));

                setToolbarColor(R.color.green);
                setButtonColor(R.color.green);
                setNumberColor(R.color.yellow);
                setOperationColor(R.color.darkyellow);

                if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    setFuncColor(R.color.darkgreen);
                }
                break;
            case 4:
                window.setStatusBarColor(getResources().getColor(R.color.purple));

                setToolbarColor(R.color.purple);
                setButtonColor(R.color.purple);
                setNumberColor(R.color.sky);
                setOperationColor(R.color.darksky);

                if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    setFuncColor(R.color.darkpurple);
                }
                break;
            default:
                window.setStatusBarColor(getResources().getColor(R.color.blue));

                setToolbarColor(R.color.blue);
                setButtonColor(R.color.blue);
                setNumberColor(R.color.pink);
                setOperationColor(R.color.darkpink);

                if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    setFuncColor(R.color.violet);
                }
                break;
        }
    }


    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {

            tv_main.setText("");
            tv_sec.setText("");

            isLongPress = true;
            isShortPress = false;
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
        settings_menu = menu.findItem(R.id.action_settings);
        history_menu = menu.findItem(R.id.action_history);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            intent.putExtra("toolbar_id", R.id.toolbar);
            startActivityForResult(intent, SETTINGS_REQUEST_CODE);

            return true;
        } else if (item.getItemId() == R.id.action_history) {
            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
            intent.putExtra("toolbar_id", R.id.toolbar);
            startActivityForResult(intent, SETTINGS_REQUEST_CODE);

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
        applyTheme(savedThemeId);

        FirebaseApp.initializeApp(MainActivity.this);
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
                            applyTheme(dbThemeId);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Failed to read theme ID from Firestore!", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    @Override
    protected void onResume() {
        super.onResume();
        applySavedTheme();
    }

    private void setAppTheme(int themeId) {
        switch (themeId) {
            case 1:
                toolbar.setBackgroundResource(R.color.blue);
                break;
            case 2:
                toolbar.setBackgroundResource(R.color.breeze);
                break;
            case 3:
                toolbar.setBackgroundResource(R.color.green);
                break;
            case 4:
                toolbar.setBackgroundResource(R.color.purple);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SETTINGS_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                int themeId = data.getIntExtra("theme_id", 1);
                setAppTheme(themeId);

                int windowColor = data.getIntExtra("statusbar_color", R.color.blue);
                setStatusBarColor(windowColor);

                int toolbarColor = data.getIntExtra("toolbar_color", R.color.blue);
                setToolbarColor(toolbarColor);

                int buttonColor = data.getIntExtra("button_color", R.color.blue);
                setButtonColor(buttonColor);

                int numberColor = data.getIntExtra("number_color", R.color.pink);
                setNumberColor(numberColor);

                int operationColor = data.getIntExtra("operation_color", R.color.darkpink);
                setOperationColor(operationColor);

                int orientation = getResources().getConfiguration().orientation;
                if (orientation == Configuration.ORIENTATION_LANDSCAPE) {

                    int funcColor = data.getIntExtra("function_color", R.color.violet);
                    setFuncColor(funcColor);
                }
            }
        }
    }


    public void setButtonColor(int colorResId) {
        bt_ac.setBackgroundColor(getResources().getColor(colorResId));
        bt_c.setBackgroundColor(getResources().getColor(colorResId));
    }

    public void setNumberColor(int colorResId) {
        bt0.setBackgroundColor(getResources().getColor(colorResId));
        bt1.setBackgroundColor(getResources().getColor(colorResId));
        bt2.setBackgroundColor(getResources().getColor(colorResId));
        bt3.setBackgroundColor(getResources().getColor(colorResId));
        bt4.setBackgroundColor(getResources().getColor(colorResId));
        bt5.setBackgroundColor(getResources().getColor(colorResId));
        bt6.setBackgroundColor(getResources().getColor(colorResId));
        bt7.setBackgroundColor(getResources().getColor(colorResId));
        bt8.setBackgroundColor(getResources().getColor(colorResId));
        bt9.setBackgroundColor(getResources().getColor(colorResId));
    }

    public void setOperationColor(int colorResId) {
        bt_equal.setBackgroundColor(getResources().getColor(colorResId));
        bt_dot.setBackgroundColor(getResources().getColor(colorResId));
        bt_inv.setBackgroundColor(getResources().getColor(colorResId));
        bt_min.setBackgroundColor(getResources().getColor(colorResId));
        bt_plus.setBackgroundColor(getResources().getColor(colorResId));
        bt_mul.setBackgroundColor(getResources().getColor(colorResId));
        bt_div.setBackgroundColor(getResources().getColor(colorResId));
    }

    public void setFuncColor(int colorResId) {
        bt_sqrt.setBackgroundColor(getResources().getColor(colorResId));
        bt_square.setBackgroundColor(getResources().getColor(colorResId));
        bt_sin.setBackgroundColor(getResources().getColor(colorResId));
        bt_cos.setBackgroundColor(getResources().getColor(colorResId));
        bt_ln.setBackgroundColor(getResources().getColor(colorResId));
        bt_log.setBackgroundColor(getResources().getColor(colorResId));
        btb1.setBackgroundColor(getResources().getColor(colorResId));
        btb2.setBackgroundColor(getResources().getColor(colorResId));
    }

    public void setToolbarColor(int colorResId) {
        toolbar.setBackgroundColor(getResources().getColor(colorResId));
    }

    private void setStatusBarColor(int colorResId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(colorResId));
        }
    }
}
