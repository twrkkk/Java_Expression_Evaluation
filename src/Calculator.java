import java.util.Scanner;
import java.util.Stack;

/**
 * Программа для вычисления значения математического выражения.
 */
public class Calculator {

    /**
     * Вычисляет значение математического выражения.
     *
     * @param expression математическое выражение в виде строки
     * @return результат вычисления выражения
     */
    public static double evaluateExpression(String expression) {
        try {
            if (!isValidParentheses(expression)) {
                throw new IllegalArgumentException("Invalid parentheses arrangement");
            }

            Stack<Double> numbers = new Stack<>();
            Stack<Character> operators = new Stack<>();
            int index = 0;
            boolean lastTokenWasOperator = true;
            while (index < expression.length()) {
                char ch = expression.charAt(index);
                if (ch == ' ') {
                    index++;
                    continue;
                }
                if (Character.isDigit(ch) || ch == '-') {
                    StringBuilder numStr = new StringBuilder();
                    while (index < expression.length() && (Character.isDigit(expression.charAt(index)) || expression.charAt(index) == '.' || (expression.charAt(index) == '-' && (lastTokenWasOperator || index == 0)))) {
                        numStr.append(expression.charAt(index));
                        index++;
                    }
                    numbers.push(Double.parseDouble(numStr.toString()));
                    lastTokenWasOperator = false;
                } else if (ch == '(') {
                    operators.push(ch);
                    index++;
                    lastTokenWasOperator = true;
                } else if (ch == ')') {
                    while (operators.peek() != '(') {
                        double result = applyOperation(operators.pop(), numbers.pop(), numbers.pop());
                        numbers.push(result);
                    }
                    operators.pop();
                    index++;
                    lastTokenWasOperator = false;
                } else if (isOperator(ch)) {
                    while (!operators.isEmpty() && precedence(operators.peek()) >= precedence(ch)) {
                        double result = applyOperation(operators.pop(), numbers.pop(), numbers.pop());
                        numbers.push(result);
                    }
                    operators.push(ch);
                    index++;
                    lastTokenWasOperator = true;
                } else {
                    throw new IllegalArgumentException("Invalid character: " + ch);
                }
            }
            while (!operators.isEmpty()) {
                double result = applyOperation(operators.pop(), numbers.pop(), numbers.pop());
                numbers.push(result);
            }
            return numbers.pop();
        } catch (Exception e) {
            System.out.println("Error evaluating expression: " + e.getMessage());
            return Double.NaN;
        }
    }

    /**
     * Проверяет корректность расположения скобок в выражении.
     *
     * @param expression математическое выражение в виде строки
     * @return {@code true}, если расположение скобок корректное, {@code false} в противном случае
     */
    private static boolean isValidParentheses(String expression) {
        int count = 0;
        for (char ch : expression.toCharArray()) {
            if (ch == '(') {
                count++;
            } else if (ch == ')') {
                count--;
                if (count < 0) {
                    return false;
                }
            }
        }
        return count == 0;
    }

    /**
     * Проверяет, является ли символ оператором.
     *
     * @param ch символ
     * @return {@code true}, если символ является оператором, {@code false} в противном случае
     */
    private static boolean isOperator(char ch) {
        return ch == '+' || ch == '-' || ch == '*' || ch == '/';
    }

    /**
     * Возвращает приоритет оператора.
     *
     * @param op оператор
     * @return приоритет оператора
     */
    private static int precedence(char op) {
        if (op == '+' || op == '-') return 1;
        if (op == '*' || op == '/') return 2;
        return 0;
    }

    /**
     * Применяет операцию к двум операндам.
     *
     * @param op оператор
     * @param b  второй операнд
     * @param a  первый операнд
     * @return результат операции
     */
    private static double applyOperation(char op, double b, double a) {
        return switch (op) {
            case '+' -> a + b;
            case '-' -> a - b;
            case '*' -> a * b;
            case '/' -> {
                if (b == 0) {
                    throw new ArithmeticException("Division by zero");
                }
                yield a / b;
            }
            default -> throw new IllegalArgumentException("Invalid operator: " + op);
        };
    }

    /**
     * Точка входа в программу.
     *
     * @param args аргументы командной строки (не используются)
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter an expression: ");
        String expression = scanner.nextLine();
        double result = evaluateExpression(expression);
        System.out.println("Result: " + result);
    }
}
