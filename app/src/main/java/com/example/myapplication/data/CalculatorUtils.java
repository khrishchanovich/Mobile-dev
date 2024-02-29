package com.example.myapplication.data;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

public class CalculatorUtils {
    public static void setNumberClickListener(Button button, final TextView textView, final String number) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNumberToTextView(textView, number);
            }
        });
    }

    private static void addNumberToTextView(TextView textView, String number) {
        String currentText = textView.getText().toString();
        String[] tokens = currentText.split("(?<=[-+*/^()])|(?=[-+*/^()])");
        int lastTokenIndex = tokens.length - 1;
        if (lastTokenIndex >= 0 && tokens[lastTokenIndex].matches("-?\\d+(\\.\\d+)?")) {
            String lastNumber = tokens[lastTokenIndex];
            if (lastNumber.contains(".")) {
                String[] parts = lastNumber.split("\\.");
                if (parts[1].length() < 16) {
                    textView.setText(currentText + number);
                }
            } else {
                if (lastNumber.length() < 16) {
                    textView.setText(currentText + number);
                }
            }
        } else {
            textView.setText(currentText + number);
        }
    }



    public static void setDotClickListener(Button button, final TextView textView) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String expression = textView.getText().toString();
                String[] tokens = expression.split("(?<=[-+*/^()])|(?=[-+*/^()])");
                String lastToken = tokens[tokens.length - 1];
                boolean isAfterBracket = lastToken.equals(")");

                if (!lastToken.contains(".") && !isAfterBracket) {
                    if (lastToken.matches("sin|cos|tg|ctg|ln")) {
                        textView.setText(expression + "0.");
                    } else {
                        textView.setText(expression + ".");
                    }
                }
            }
        });
    }

    public static void setClearClickListener(Button button, final TextView textView, final TextView textViewSec) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText("");
                textViewSec.setText("");
            }
        });
    }

    public static void setBackspaceClickListener(Button button, final TextView textView) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String val = textView.getText().toString();
                if (!val.isEmpty()) {
                    val = val.substring(0, val.length() - 1);
                    textView.setText(val);
                }
            }
        });
    }

    public static void setOperatorClickListener(Button button, final TextView textView, final String operator) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleOperatorClick(textView, operator);
            }
        });
    }
    private static void handleOperatorClick(TextView textView, String operator) {
        String expression = textView.getText().toString();

        // Проверяем, если строка пустая, то не добавляем оператор
        if (expression.isEmpty()) {
            return;
        }

        // Удаляем последний введенный оператор, если он есть
        if (isOperator(expression.charAt(expression.length() - 1))) {
            expression = expression.substring(0, expression.length() - 1);
        }

        // Добавляем новый оператор к текущему выражению
        textView.setText(expression + operator);
    }

    public static void setInverseClickListener(Button button, final TextView textView) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleInverseClick(textView);
            }
        });
    }

    private static void handleInverseClick(TextView textView) {
        String val = textView.getText().toString().replace('÷', '/').replace('×', '*').replace(',', '.');
        if (!val.isEmpty()) {
            // Проверяем, содержит ли val только число (без операторов)
            if (val.matches("-?\\d+(\\.\\d+)?")) {
                // Если val содержит только число, меняем его знак
                double number = Double.parseDouble(val);
                double invertedNumber = -number;
                DecimalFormat decimalFormat = new DecimalFormat("#.##"); // Определение формата
                String formattedNumber = decimalFormat.format(invertedNumber); // Применение формата к числу
                textView.setText(formattedNumber);
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
                textView.setText(updatedVal.toString());
            }
        }
    }


    public static void setEqualClickListener(Button button, final TextView textView, final TextView textViewSec) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleEqualClick(textView, textViewSec);
            }
        });
    }

    private static void handleEqualClick(TextView textView, TextView textViewSec) {
        String val = textView.getText().toString();
        String replacedStr = val.replace('÷', '/').replace('×', '*').replace(',', '.');
        try {
            double result = eval(replacedStr);
            if (Double.isInfinite(result) || Double.isNaN(result)) {
                Toast.makeText(textView.getContext(), "Невозможно выполнить деление на ноль или получить корректный результат", Toast.LENGTH_SHORT).show();
            } else if (result > Double.MAX_VALUE || result < -Double.MAX_VALUE) {
                Toast.makeText(textView.getContext(), "Результат выходит за пределы допустимого диапазона", Toast.LENGTH_SHORT).show();
            } else {
                String formattedResult = String.format("%.2f", result);
                textView.setText(formattedResult);
                textViewSec.setText(val);
            }
        } catch (RuntimeException e) {
            Toast.makeText(textView.getContext(), "Hекорректное выражение", Toast.LENGTH_SHORT).show();
        }
    }


    public static void setSqrtClickListener(Button button, final TextView textViewMain, final TextView textViewSec) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSqrtClick(textViewMain, textViewSec);
            }
        });
    }

//    private static void handleSqrtClick(TextView textViewMain) {
//        String val = textViewMain.getText().toString();
//        if (!val.isEmpty()) {
//            // Разбиваем строку на числа и операторы
//            String[] tokens = val.split("(?<=[-+*/^()])|(?=[-+*/^()])");
//            String lastToken = tokens[tokens.length - 1];
//
//            // Проверяем, является ли последний токен числом
//            if (lastToken.matches("-?\\d+(\\.\\d+)?")) {
//                double number = Double.parseDouble(lastToken);
//                double result = Math.sqrt(number);
//                textViewMain.setText(val.substring(0, val.length() - lastToken.length()) + String.valueOf(result));
//            } else {
//                Toast.makeText(textViewMain.getContext(), "Введите сначала число для извлечения корня", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }

    private static void handleSqrtClick(TextView textViewMain, TextView textViewSec) {
        String val = textViewMain.getText().toString().replace('÷', '/').replace('×', '*').replace(',', '.');
        if (!val.isEmpty()) {
            String[] tokens = val.split("(?<=[-+*/^()])|(?=[-+*/^()])");
            String lastToken = tokens[tokens.length - 1];

            if (lastToken.matches("-?\\d+(\\.\\d+)?")) {
                double number = Double.parseDouble(lastToken);
                double result = Math.sqrt(number);

                DecimalFormat decimalFormat = new DecimalFormat("#.##");
                String formattedResult = decimalFormat.format(result);

                textViewMain.setText(val.substring(0, val.length() - lastToken.length()) + formattedResult);
                textViewSec.setText("√" + number);
            } else {
                Toast.makeText(textViewMain.getContext(), "Введите число для извлечения корня", Toast.LENGTH_SHORT).show();
            }
        }
    }

//    private static void handleSqrtClick(TextView textViewMain) {
//        String val = textViewMain.getText().toString();
//        if (!val.isEmpty()) {
//            // Разбиваем строку на числа и операторы
//            String[] tokens = val.split("(?<=[-+*/^()])|(?=[-+*/^()])");
//            String lastToken = tokens[tokens.length - 1];
//
//            // Проверяем, является ли последний токен числом
//            if (lastToken.matches("-?\\d+(\\.\\d+)?")) {
//                double number = Double.parseDouble(lastToken);
//                double result = Math.sqrt(number);
//
//                // Форматируем результат без использования научной записи
//                DecimalFormat decimalFormat = new DecimalFormat("#.##"); // Определение формата
//                String formattedResult = decimalFormat.format(result); // Применение формата к результату
//
//                textViewMain.setText(val.substring(0, val.length() - lastToken.length()) + formattedResult);
//            } else {
//                Toast.makeText(textViewMain.getContext(), "Введите сначала число для извлечения корня", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }


    public static void setBtb1ClickListener(Button button, final TextView textViewMain) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleBtb1Click(textViewMain);
            }
        });
    }

    public static void setBtb2ClickListener(Button button, final TextView textViewMain) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleBtb2Click(textViewMain);
            }
        });
    }

    private static void handleBtb1Click(TextView textViewMain) {
        try {
            String expression = textViewMain.getText().toString();

            if (expression.isEmpty() || expression.charAt(expression.length() - 1) == ')' || expression.charAt(expression.length() - 1) == '(' || expression.charAt(expression.length() - 1) == '-' || expression.charAt(expression.length() - 1) == '+' || expression.charAt(expression.length() - 1) == '*' || expression.charAt(expression.length() - 1) == '/' || expression.charAt(expression.length() - 1) == 's' || expression.charAt(expression.length() - 1) == 'n' || expression.charAt(expression.length() - 1) == 'g')
            {
                textViewMain.setText(expression + "(");
            } else {
                textViewMain.setText(expression + "*(");
            }
        } catch (Exception e) {
            System.out.println("Error occurred: " + e.getMessage());
        }
    }

    private static boolean isLastCharOperatorOrBracket(String expression) {
        char lastChar = expression.charAt(expression.length() - 1);
        return lastChar == ')' || lastChar == '(' || lastChar == '-' || lastChar == '+' || lastChar == '*' || lastChar == '/';
    }

    private static void handleBtb2Click(TextView textViewMain) {
        String expression = textViewMain.getText().toString();
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
            textViewMain.setText(expression + ")");
        }
    }

    public static void setBtSinClickListener(Button button, final TextView textViewMain) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleBtSinClick(textViewMain);
            }
        });
    }

    private static void handleBtSinClick(TextView textViewMain) {
        String expression = textViewMain.getText().toString();

        // '*' перед sin, если предыдущий символ - цифра
        if (!expression.isEmpty()) {
            char lastChar = expression.charAt(expression.length() - 1);
            if (Character.isDigit(lastChar) || lastChar == ')') {
                textViewMain.setText(expression + "*sin");
            } else {
                textViewMain.setText(expression + "sin");
            }
        } else {
            textViewMain.setText("sin");
        }
    }

    public static void setBtCosClickListener(Button button, final TextView textViewMain) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleBtCosClick(textViewMain);
            }
        });
    }

    private static void handleBtCosClick(TextView textViewMain) {
        String expression = textViewMain.getText().toString();

        if (!expression.isEmpty()) {
            char lastChar = expression.charAt(expression.length() - 1);
            if (Character.isDigit(lastChar) || lastChar == ')') {
                textViewMain.setText(expression + "*cos");
            } else {
                textViewMain.setText(expression + "cos");
            }
        } else {
            textViewMain.setText("cos");
        }
    }

    public static void setBtLogClickListener(Button button, final TextView textViewMain) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleBtLogClick(textViewMain);
            }
        });
    }

    private static void handleBtLogClick(TextView textViewMain) {
        String expression = textViewMain.getText().toString();

        if (!expression.isEmpty()) {
            char lastChar = expression.charAt(expression.length() - 1);
            if (Character.isDigit(lastChar) || lastChar == ')') {
                textViewMain.setText(expression + "*lg");
            } else {
                textViewMain.setText(expression + "lg");
            }
        } else {
            textViewMain.setText("lg");
        }
    }

    public static void setBtLnClickListener(Button button, final TextView textViewMain) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleBtLnClick(textViewMain);
            }
        });
    }

    private static void handleBtLnClick(TextView textViewMain) {
        String expression = textViewMain.getText().toString();

        if (!expression.isEmpty()) {
            char lastChar = expression.charAt(expression.length() - 1);
            if (Character.isDigit(lastChar) || lastChar == ')') {
                textViewMain.setText(expression + "*ln");
            } else {
                textViewMain.setText(expression + "ln");
            }
        } else {
            textViewMain.setText("ln");
        }
    }
    public static void setBtSquareClickListener(Button button, final TextView textViewMain, final TextView textViewSec) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleBtSquareClick(textViewMain, textViewSec);
            }
        });
    }

//    private static void handleBtSquareClick(TextView textViewMain, TextView textViewSec) {
//        String val = textViewMain.getText().toString();
//        if (!val.isEmpty()) {
//            // Разбиваем строку на числа и операторы
//            String[] tokens = val.split("(?<=[-+*/^()])|(?=[-+*/^()])");
//            String lastToken = tokens[tokens.length - 1];
//
//            // Проверяем, является ли последний токен числом
//            if (lastToken.matches("-?\\d+(\\.\\d+)?")) {
//                double number = Double.parseDouble(lastToken);
//                double result = Math.pow(number, 2);
//                textViewMain.setText(val.substring(0, val.length() - lastToken.length()) + String.valueOf(result));
//                textViewSec.setText(number + "²");
//            } else {
//                Toast.makeText(textViewMain.getContext(), "Введите сначала число для возведения в квадрат", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }

    private static void handleBtSquareClick(TextView textViewMain, TextView textViewSec) {
        String val = textViewMain.getText().toString().replace('÷', '/').replace('×', '*').replace(',', '.');
        if (!val.isEmpty()) {
            // Разбиваем строку на числа и операторы
            String[] tokens = val.split("(?<=[-+*/^()])|(?=[-+*/^()])");
            String lastToken = tokens[tokens.length - 1];

            // Проверяем, является ли последний токен числом
            if (lastToken.matches("-?\\d+(\\.\\d+)?")) {
                double number = Double.parseDouble(lastToken);
                double result = Math.pow(number, 2);

                // Форматируем результат без использования научной записи
                DecimalFormat decimalFormat = new DecimalFormat("#.##"); // Определение формата
                String formattedResult = decimalFormat.format(result); // Применение формата к результату

                textViewMain.setText(val.substring(0, val.length() - lastToken.length()) + formattedResult);
                textViewSec.setText(number + "²");
            } else {
                Toast.makeText(textViewMain.getContext(), "Введите сначала число для возведения в квадрат", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public static boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }

    public static boolean canAddSquareRootOrSquare(TextView textView) {
        String expression = textView.getText().toString();
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
                    else if (func.equals("lg")) x = Math.log10(x);
                    else if (func.equals("ln")) x = Math.log(x);
                    else throw new RuntimeException("Unknown function: " + func);
                } else {
                    throw new RuntimeException("Unexpected: " + (char)ch);
                }
                return x;
            }
        }.parse();
    }

}
