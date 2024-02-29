package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button bt1, bt2, bt3, bt4, bt5, bt6, bt7, bt8, bt9, bt0, bt_dot,
            bt_equal, bt_plus, bt_min, bt_mul, bt_div, bt_sqrt, bt_square,
            bt_inv, bt_sin, bt_cos, bt_log, bt_ln, btb1, btb2, bt_ac, bt_c;

//    TextView tv_main, tv_sec;
    EditText tv_main, tv_sec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int orientation = getResources().getConfiguration().orientation;

        tv_main = findViewById(R.id.calculatorText);
        tv_sec = findViewById(R.id.memoryText);

        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
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
            bt_sqrt = findViewById(R.id.btnSqr);
            bt_square = findViewById(R.id.btnExp);


            bt_sin = findViewById(R.id.btnSin);
            bt_cos = findViewById(R.id.btnCos);

            bt_log = findViewById(R.id.btnLg);
            bt_ln = findViewById(R.id.btnLn);
            btb1 = findViewById(R.id.btnOpen);
            btb2= findViewById(R.id.btnClose);

            bt_inv = findViewById(R.id.btnPlusMinus);
            bt_ac= findViewById(R.id.btnAC);
            bt_c = findViewById(R.id.btnC);

            tv_main = findViewById(R.id.calculatorText);
            tv_sec = findViewById(R.id.memoryText);

            bt1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tv_main.setText(tv_main.getText()+"1");
                }
            });
            bt2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tv_main.setText(tv_main.getText()+"2");
                }
            });
            bt3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tv_main.setText(tv_main.getText()+"3");
                }
            });
            bt4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tv_main.setText(tv_main.getText()+"4");
                }
            });
            bt5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tv_main.setText(tv_main.getText()+"5");
                }
            });
            bt6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tv_main.setText(tv_main.getText()+"6");
                }
            });
            bt7.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tv_main.setText(tv_main.getText()+"7");
                }
            });
            bt8.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tv_main.setText(tv_main.getText()+"8");
                }
            });
            bt9.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tv_main.setText(tv_main.getText()+"9");
                }
            });
            bt0.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tv_main.setText(tv_main.getText()+"0");
                }
            });
            bt_dot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String expression = tv_main.getText().toString();

                    // Разделяем выражение на отдельные числа и операторы
                    String[] tokens = expression.split("(?<=[-+*/^()])|(?=[-+*/^()])");

                    // Проверяем последний токен на наличие десятичной точки
                    String lastToken = tokens[tokens.length - 1];
                    boolean isAfterBracket = lastToken.equals(")");

                    if (!lastToken.contains(".") && !isAfterBracket) {
                        //После триг. функций хочу, чтобы писалась не просто точка, а "0."
                        if (lastToken.matches("sin|cos|log||ln")) {
                            tv_main.setText(expression + "0.");
                        } else {
                            tv_main.setText(expression + ".");
                        }
                    }
                }
            });
            bt_ac.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tv_main.setText("");
                    tv_sec.setText("");
                }
            });
            bt_c.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String val = tv_main.getText().toString();
                    if(!val.isEmpty()){
                        val = val.substring(0, val.length()-1);
                        tv_main.setText(val);
                    }
                }
            });
            bt_plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleOperatorClick("+");
                }
            });
            bt_min.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleOperatorClick("-");
                }
            });
            bt_div.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleOperatorClick("/");
                }
            });
            bt_mul.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleOperatorClick("*");
                }
            });

            bt_sqrt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (canAddSquareRootOrSquare()) {
                        String val = tv_main.getText().toString();
                        if (val.isEmpty()) {
                            Toast.makeText(MainActivity.this, "Введите сначала число для извлечения корня", Toast.LENGTH_SHORT).show();
                        } else {
                            double res = Math.sqrt(Double.parseDouble(val));
                            tv_main.setText(String.valueOf(res));
                        }
                    }
                }
            });
            btb1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        String expression = tv_main.getText().toString();

                        if (expression.isEmpty() || expression.charAt(expression.length() - 1) == ')' || expression.charAt(expression.length() - 1) == '(' || expression.charAt(expression.length() - 1) == '-' || expression.charAt(expression.length() - 1) == '+' || expression.charAt(expression.length() - 1) == '*' || expression.charAt(expression.length() - 1) == '/') {
                            tv_main.setText(expression + "(");
                        } else {
                            tv_main.setText(expression + "*(");
                        }
                    } catch (Exception e) {
                        System.out.println("Error occurred: " + e.getMessage());
                    }
                }
            });

            btb2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String expression = tv_main.getText().toString();
                    int openBracketCount = 0;
                    int closeBracketCount = 0;

                    // Подсчет количества открывающих и закрывающих скобок
                    for (char c : expression.toCharArray()) {
                        if (c == '(') {
                            openBracketCount++;
                        } else if (c == ')') {
                            closeBracketCount++;
                        }
                    }

                    // Проверка на то, что количество закрывающих скобок не превышает количество открывающих
                    if (openBracketCount > closeBracketCount && (expression.isEmpty() || Character.isDigit(expression.charAt(expression.length() - 1)) || expression.charAt(expression.length() - 1) == ')')) {
                        tv_main.setText(expression + ")");
                    }
                }
            });
            bt_sin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String expression = tv_main.getText().toString();

                    //  '*' перед sin, если предыдущий символ - цифра
                    if (!expression.isEmpty()) {
                        char lastChar = expression.charAt(expression.length() - 1);
                        if (Character.isDigit(lastChar) || lastChar==')') {
                            tv_main.setText(expression + "×sin");
                        } else {
                            tv_main.setText(expression + "sin");
                        }
                    } else {
                        tv_main.setText("sin");
                    }
                }
            });
            bt_cos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String expression = tv_main.getText().toString();

                    if (!expression.isEmpty()) {
                        char lastChar = expression.charAt(expression.length() - 1);
                        if (Character.isDigit(lastChar) || lastChar==')') {
                            tv_main.setText(expression + "×cos");
                        } else {
                            tv_main.setText(expression + "cos");
                        }
                    } else {
                        tv_main.setText("cos");
                    }
                }
            });
            bt_log.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String expression = tv_main.getText().toString();

                    if (!expression.isEmpty()) {
                        char lastChar = expression.charAt(expression.length() - 1);
                        if (Character.isDigit(lastChar) || lastChar==')') {
                            tv_main.setText(expression + "×log");
                        } else {
                            tv_main.setText(expression + "log");
                        }
                    } else {
                        tv_main.setText("log");
                    }
                }
            });
            bt_ln.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String expression = tv_main.getText().toString();

                    if (!expression.isEmpty()) {
                        char lastChar = expression.charAt(expression.length() - 1);
                        if (Character.isDigit(lastChar) || lastChar==')') {
                            tv_main.setText(expression + "×ln");
                        } else {
                            tv_main.setText(expression + "ln");
                        }
                    } else {
                        tv_main.setText("ln");
                    }
                }
            });
            bt_square.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (canAddSquareRootOrSquare()) {
                        String val = tv_main.getText().toString().replace(',', '.');
                        if (val.isEmpty()) {
                            Toast.makeText(MainActivity.this, "Введите сначала число для возведения в квадрат", Toast.LENGTH_SHORT).show();
                        } else {
                            double number = Double.parseDouble(val);
                            tv_main.setText(String.valueOf(Math.pow(number, 2)));
                            tv_sec.setText(number + "²");
                        }
                    }
                }
            });
//        bt_sqrt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                double number = Double.parseDouble(tv_main.getText().toString());
//                tv_main.setText(String.valueOf(Math.sqrt(number)));
//                tv_sec.setText("√"+number);
//            }
//        });
            bt_inv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String val = tv_main.getText().toString();
                    if (!val.isEmpty()) {
                        double number = Double.parseDouble(val);
                        // Инвертируем знак числа
                        double invertedNumber = -number;
                        // Обновляем tv_main с новым значением
                        tv_main.setText(String.valueOf(invertedNumber));
                    }
                }
            });
            bt_equal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String val = tv_main.getText().toString();
                    String replacedStr = val.replace('÷', '/').replace(',', '.');
                    try {
                        double result = eval(replacedStr);
                        if (Double.isInfinite(result) || Double.isNaN(result)) {
                            Toast.makeText(MainActivity.this, "Невозможно выполнить деление на ноль", Toast.LENGTH_SHORT).show();
                        } else {
                            String formattedResult = String.format("%.2f", result);
                            tv_main.setText(formattedResult);
                            tv_sec.setText(val);
                        }
                    } catch (RuntimeException e) {
                        Toast.makeText(MainActivity.this, "Hекорректное выражение", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
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
            bt_ac= findViewById(R.id.btnAC);
            bt_c = findViewById(R.id.btnC);

            bt1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tv_main.setText(tv_main.getText()+"1");
                }
            });
            bt2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tv_main.setText(tv_main.getText()+"2");
                }
            });
            bt3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tv_main.setText(tv_main.getText()+"3");
                }
            });
            bt4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tv_main.setText(tv_main.getText()+"4");
                }
            });
            bt5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tv_main.setText(tv_main.getText()+"5");
                }
            });
            bt6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tv_main.setText(tv_main.getText()+"6");
                }
            });
            bt7.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tv_main.setText(tv_main.getText()+"7");
                }
            });
            bt8.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tv_main.setText(tv_main.getText()+"8");
                }
            });
            bt9.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tv_main.setText(tv_main.getText()+"9");
                }
            });
            bt0.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tv_main.setText(tv_main.getText()+"0");
                }
            });
            bt_dot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String expression = tv_main.getText().toString();

                    // Разделяем выражение на отдельные числа и операторы
                    String[] tokens = expression.split("(?<=[-+*/^()])|(?=[-+*/^()])");

                    // Проверяем последний токен на наличие десятичной точки
                    String lastToken = tokens[tokens.length - 1];
                    boolean isAfterBracket = lastToken.equals(")");

                    if (!lastToken.contains(".") && !isAfterBracket) {
                        //После триг. функций хочу, чтобы писалась не просто точка, а "0."
                        if (lastToken.matches("sin|cos|tg|ctg|ln")) {
                            tv_main.setText(expression + "0.");
                        } else {
                            tv_main.setText(expression + ".");
                        }
                    }
                }
            });
            bt_ac.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tv_main.setText("");
                    tv_sec.setText("");
                }
            });
            bt_c.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String val = tv_main.getText().toString();
                    if(!val.isEmpty()){
                        val = val.substring(0, val.length()-1);
                        tv_main.setText(val);
                    }
                }
            });
            bt_plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleOperatorClick("+");
                }
            });
            bt_min.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleOperatorClick("-");
                }
            });
            bt_div.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleOperatorClick("÷");
                }
            });
            bt_mul.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleOperatorClick("×");
                }
            });

            bt_inv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String val = tv_main.getText().toString();
                    if (!val.isEmpty()) {
                        // Проверяем, содержит ли val только число (без операторов)
                        if (val.matches("-?\\d+(\\.\\d+)?")) {
                            // Если val содержит только число, меняем его знак
                            double number = Double.parseDouble(val);
                            double invertedNumber = -number;
                            tv_main.setText(String.valueOf(invertedNumber));
                        } else {
                            // Если val содержит операторы, меняем знак последнего числа
                            // Разбиваем val на числа и операторы
                            String[] tokens = val.split("(?<=[-+*/^()])|(?=[-+*/^()])");
                            StringBuilder updatedVal = new StringBuilder();
                            for (int i = tokens.length - 1; i >= 0; i--) {
                                if (tokens[i].matches("-?\\d+(\\.\\d+)?")) {
                                    // Нашли последнее число, меняем его знак
                                    double number = Double.parseDouble(tokens[i]);
                                    double invertedNumber = -number;
                                    tokens[i] = String.valueOf(invertedNumber);
                                    break;
                                }
                            }
                            // Собираем обновленное значение с учетом знака последнего числа
                            for (String token : tokens) {
                                updatedVal.append(token);
                            }
                            tv_main.setText(updatedVal.toString());
                        }
                    }
                }
            });

            bt_equal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String val = tv_main.getText().toString();
                    String replacedStr = val.replace('÷', '/').replace('×','*').replace(',', '.');
                    try {
                        double result = eval(replacedStr);
                        if (Double.isInfinite(result) || Double.isNaN(result)) {
                            Toast.makeText(MainActivity.this, "Невозможно выполнить деление на ноль", Toast.LENGTH_SHORT).show();
                        } else {
                            String formattedResult = String.format("%.2f", result);
                            tv_main.setText(formattedResult);
                            tv_sec.setText(val);
                        }
                    } catch (RuntimeException e) {
                        Toast.makeText(MainActivity.this, "Hекорректное выражение", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }

    }

    private void handleOperatorClick(String operator) {
        String expression = tv_main.getText().toString();

        // Проверяем, если строка пустая или у нас (, то не добавляем оператор
        if (expression.isEmpty() || expression.charAt(expression.length() - 1) == '(') {
            return;
        }

        // Проверяем, если последний символ в строке является оператором,
        // то заменяем его на новый оператор
        if (isOperator(expression.charAt(expression.length() - 1))) {
            // Удаляем последний символ (старый оператор)
            expression = expression.substring(0, expression.length() - 1);
        }

        // Добавляем новый оператор к текущему выражению
        tv_main.setText(expression + operator);

    }
    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '×' || c == '÷';
    }

    private boolean canAddSquareRootOrSquare() {
        String expression = tv_main.getText().toString();
        if (!expression.isEmpty()) {
            char lastChar = expression.charAt(expression.length() - 1);
            return !(lastChar == 'n' || lastChar == 's' || lastChar == 'g' || lastChar == 't');
        }
        return true;
    }

    public static Double eval(final String str) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < str.length()) ? str.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            Double parse() {
                nextChar();
                Double x = parseExpression();
                if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char)ch);
                return x;
            }

            Double parseExpression() {
                Double x = parseTerm();
                for (;;) {
                    if      (eat('+')) x += parseTerm(); // addition
                    else if (eat('-')) x -= parseTerm(); // subtraction
                    else return x;
                }
            }

            Double parseTerm() {
                Double x = parseFactor();
                for (;;) {
                    if      (eat('*')) x *= parseFactor(); // multiplication
                    else if (eat('/')) x /= parseFactor(); // division
                    else return x;
                }
            }

            Double parseFactor() {
                if (eat('+')) return parseFactor(); // unary plus
                if (eat('-')) return -parseFactor(); // unary minus

                Double x;
                int startPos = this.pos;
                if (eat('(')) { // parentheses
                    x = parseExpression();
                    eat(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(str.substring(startPos, this.pos));
                } else if (ch >= 'a' && ch <= 'z') { // functions
                    while (ch >= 'a' && ch <= 'z') nextChar();
                    String func = str.substring(startPos, this.pos);
                    x = parseFactor();
                    if (func.equals("sqrt")) x = Math.sqrt(x);
                    else if (func.equals("sin")) x = Math.sin(Math.toRadians(x));
                    else if (func.equals("cos")) x = Math.cos(Math.toRadians(x));
                    else if (func.equals("tg")) x = Math.tan(Math.toRadians(x));
                    else if (func.equals("ctg")) x = 1/Math.tan(Math.toRadians(x));
                    else if (func.equals("log")) x = Math.log10(x);
                    else if (func.equals("ln")) x = Math.log(x);
                    else throw new RuntimeException("Unknown function: " + func);
                } else {
                    throw new RuntimeException("Unexpected: " + (char)ch);
                }

                if (eat('^')) x = Math.pow(x, parseFactor()); // exponentiation

                return x;
            }
        }.parse();
    }
}