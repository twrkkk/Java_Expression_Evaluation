import java.util.Scanner;
import java.util.Stack;

public class Calculator {

    public static double evaluateExpression(String expression) {
        try {
            if (!isValidParentheses(expression)) {
                throw new IllegalArgumentException("Invalid parentheses arrangement");
            }

            Stack<Double> numbers = new Stack<>();
            Stack<Character> operators = new Stack<>();
            int index = 0;
            boolean lastTokenWasOperator = true; // Added flag to track if last token was an operator
            while (index < expression.length()) {
                char ch = expression.charAt(index);
                if (ch == ' ') {
                    index++;
                    continue;
                }
                if (Character.isDigit(ch) || ch == '-') { // Added support for negative numbers
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
                    operators.pop(); // Remove '('
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

    private static boolean isOperator(char ch) {
        return ch == '+' || ch == '-' || ch == '*' || ch == '/';
    }

    private static int precedence(char op) {
        if (op == '+' || op == '-') return 1;
        if (op == '*' || op == '/') return 2;
        return 0;
    }

    private static double applyOperation(char op, double b, double a) {
        switch (op) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
                if (b == 0) {
                    throw new ArithmeticException("Division by zero");
                }
                return a / b;
            default:
                throw new IllegalArgumentException("Invalid operator: " + op);
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter an expression: ");
        String expression = scanner.nextLine();
        double result = evaluateExpression(expression);
        System.out.println("Result: " + result);
    }
}
