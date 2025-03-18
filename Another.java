import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;


public class Another {
    private static List<String> history = new ArrayList<>();
    private static File myObj = new File("input.txt");
    // private static Scanner scanner = new Scanner(myObj);


    public static void main(String[] args) {
        System.out.println("Welcome to the Calculator!");
        
        while (true) {
            try {
                performCalculation();
                if (!continuePrompt()) {
                    break;
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        
        System.out.println("Thank you for using the Calculator!");
        // scanner.close();
    }

    private static void performCalculation() {
        try {
            Scanner scanner = new Scanner(myObj);
            while(scanner.hasNextLine()) {
                System.out.print("Please enter your arithmetic expression: ");
                String expression = scanner.nextLine().trim();
                
                if (expression.isEmpty()) {
                    throw new IllegalArgumentException("empty string");
                }
                
                // Store in history
                history.add(expression);
                
                double result = evaluateExpression(expression);
                System.out.println("Result: " + result);
            }
        } catch (FileNotFoundException e) {
            System.out.println("An error occured.");
            e.printStackTrace();
        }
    }

    private static double evaluateExpression(String expression) {
        // Replace function calls with their values
        expression = replaceFunctions(expression);
        
        // Evaluate the resulting arithmetic expression
        return evaluateArithmetic(expression);
    }

    private static String replaceFunctions(String expression) {
        while (true) {
            int absIndex = expression.indexOf("abs(");
            if (absIndex != -1) {
                int endIndex = findClosingBracket(expression, absIndex + 4);
                if (endIndex == -1) throw new IllegalArgumentException("Unmatched parenthesis in abs()");
                String arg = expression.substring(absIndex + 4, endIndex);
                double value = Math.abs(Double.parseDouble(arg));
                expression = expression.substring(0, absIndex) + value + expression.substring(endIndex + 1);
                continue;
            }

            int sqrtIndex = expression.indexOf("sqrt(");
            if (sqrtIndex != -1) {
                int endIndex = findClosingBracket(expression, sqrtIndex + 5);
                if (endIndex == -1) throw new IllegalArgumentException("Unmatched parenthesis in sqrt()");
                String arg = expression.substring(sqrtIndex + 5, endIndex);
                double value = Math.sqrt(Double.parseDouble(arg));
                if (value < 0) throw new IllegalArgumentException("Square root of negative number is not allowed");
                expression = expression.substring(0, sqrtIndex) + value + expression.substring(endIndex + 1);
                continue;
            }

            int powerIndex = expression.indexOf("power(");
            if (powerIndex != -1) {
                int endIndex = findClosingBracket(expression, powerIndex + 6);
                if (endIndex == -1) throw new IllegalArgumentException("Unmatched parenthesis in power()");
                String[] args = expression.substring(powerIndex + 6, endIndex).split(",");
                if (args.length != 2) throw new IllegalArgumentException("power() requires two arguments");
                double base = Double.parseDouble(args[0].trim());
                double exponent = Double.parseDouble(args[1].trim());
                double value = Math.pow(base, exponent);
                expression = expression.substring(0, powerIndex) + value + expression.substring(endIndex + 1);
                continue;
            }

            int roundIndex = expression.indexOf("round(");
            if (roundIndex != -1) {
                int endIndex = findClosingBracket(expression, roundIndex + 6);
                if (endIndex == -1) throw new IllegalArgumentException("Unmatched parenthesis in round()");
                String arg = expression.substring(roundIndex + 6, endIndex);
                double value = Math.round(Double.parseDouble(arg));
                expression = expression.substring(0, roundIndex) + value + expression.substring(endIndex + 1);
                continue;
            }
            break;
        }
        return expression;
    }

    private static int findClosingBracket(String s, int start) {
        int count = 1;
        for (int i = start; i < s.length(); i++) {
            if (s.charAt(i) == '(') count++;
            if (s.charAt(i) == ')') count--;
            if (count == 0) return i;
        }
        return -1;
    }

    private static double evaluateArithmetic(String expression) {
        // Use a stack-based approach to handle precedence and parentheses
        Stack<Double> numbers = new Stack<>();
        Stack<Character> operators = new Stack<>();

        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);

            if (Character.isDigit(c) || c == '.') {
                StringBuilder num = new StringBuilder();
                while (i < expression.length() && (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
                    num.append(expression.charAt(i++));
                }
                i--;
                numbers.push(Double.parseDouble(num.toString()));
            } else if (c == '(') {
                operators.push(c);
            } else if (c == ')') {
                while (!operators.isEmpty() && operators.peek() != '(') {
                    numbers.push(applyOperation(operators.pop(), numbers.pop(), numbers.pop()));
                }
                operators.pop(); // Remove '('
            } else if (isOperator(c)) {
                while (!operators.isEmpty() && precedence(operators.peek()) >= precedence(c)) {
                    numbers.push(applyOperation(operators.pop(), numbers.pop(), numbers.pop()));
                }
                operators.push(c);
            }
        }

        while (!operators.isEmpty()) {
            numbers.push(applyOperation(operators.pop(), numbers.pop(), numbers.pop()));
        }

        return numbers.pop();
    }

    private static boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }

    private static int precedence(char op) {
        if (op == '+' || op == '-') return 1;
        if (op == '*' || op == '/') return 2;
        return 0;
    }

    private static double applyOperation(char op, double b, double a) {
        switch (op) {
            case '+': return a + b;
            case '-': return a - b;
            case '*': return a * b;
            case '/':
                if (b == 0) throw new ArithmeticException("Division by zero is not allowed");
                return a / b;
        }
        return 0;
    }

    private static boolean continuePrompt() {
        while (true) {
            try {
                Scanner scanner = new Scanner(myObj);
                System.out.print("Do you want to continue? (y/n): ");
                String response = scanner.nextLine().trim().toLowerCase();
                if (response.equals("y")) {
                    System.out.print("View history? (y/n): ");
                    String historyResponse = scanner.nextLine().trim().toLowerCase();
                    if (historyResponse.equals("y")) {
                        showHistory();
                    }
                    return true;
                } else if (response.equals("n")) {
                    return false;
                }
                System.out.println("Please enter 'y' or 'n'");
                
            } catch (FileNotFoundException e) {
                System.out.println("An error occured.");
                e.printStackTrace();
            }
        }
    }

    private static void showHistory() {
        System.out.println("\nCalculation History:");
        for (int i = 0; i < history.size(); i++) {
            System.out.println((i + 1) + ". " + history.get(i));
        }
        System.out.println();
    }
}