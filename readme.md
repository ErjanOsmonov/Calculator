# Calculator Project

## Project Description

This project is a command-line calculator implemented in Java that evaluates arithmetic expressions entered by the user. It supports basic arithmetic operations (`+`, `-`, `*`, `/`) as well as mathematical functions such as `abs()`, `sqrt()`, `power()`, and `round()`. The calculator maintains a history of expressions entered during a session, allows users to view this history, and provides a continuous interaction loop until the user chooses to exit. The program handles errors gracefully and provides meaningful feedback to the user.

The primary goal of this project is to create a robust and user-friendly calculator that can process complex expressions involving parentheses and functions while adhering to mathematical precedence rules.

## Design Choices

1. **Modular Structure**:
   - The code is broken into distinct methods (`performCalculation`, `evaluateExpression`, `replaceFunctions`, `evaluateArithmetic`, etc.) to enhance readability, maintainability, and reusability.
   - Each method has a single responsibility, such as parsing functions, evaluating arithmetic, or handling user prompts.

2. **Input Handling**:
   - The `Scanner` class is used to read user input from the console, providing a simple and interactive interface.
   - Expressions are processed as strings, allowing flexibility in parsing and evaluating user input.

3. **Function Replacement**:
   - Mathematical functions (e.g., `abs()`, `sqrt()`) are replaced with their computed values before arithmetic evaluation. This simplifies the subsequent arithmetic parsing by reducing the expression to a basic form.

4. **Expression Evaluation**:
   - A stack-based approach is used to evaluate arithmetic expressions, supporting operator precedence and parentheses.
   - Two stacks are employed: one for numbers (`Stack<Double>`) and one for operators (`Stack<Character>`).

5. **Error Handling**:
   - Exceptions are caught and handled within a `try-catch` block in the main loop, ensuring the program doesn’t crash on invalid input.
   - Specific error messages are provided for common issues like division by zero, unmatched parentheses, or invalid function arguments.

6. **History Feature**:
   - An `ArrayList<String>` stores the history of expressions, chosen for its dynamic sizing and ease of access by index.

7. **User Interaction**:
   - A `continuePrompt()` method provides an interactive loop, asking users if they want to continue and whether to view the history, with input validation to ensure valid responses (`y` or `n`).
  
  ### Data Structures

1. **ArrayList (`List<String> history`)**:
   - **Purpose**: Stores the history of user-entered expressions.
   - **Why Chosen**: Dynamic size, fast indexed access (O(1)), and simple iteration for displaying history.
   - **Usage**: Appended to in `performCalculation` and displayed in `showHistory`.

2. **Stack (`Stack<Double> numbers` and `Stack<Character> operators`)**:
   - **Purpose**: Manages numbers and operators during arithmetic evaluation.
   - **Why Chosen**: LIFO (Last-In-First-Out) behavior is ideal for handling nested operations and operator precedence in a postfix-like evaluation.
   - **Usage**: In `evaluateArithmetic` to process the expression.

3. **StringBuilder**:
   - **Purpose**: Builds numeric strings (e.g., `12.34`) from the expression during parsing.
   - **Why Chosen**: Efficient for string concatenation compared to using `String` directly (O(1) append vs. O(n) for string concatenation).
   - **Usage**: In `evaluateArithmetic` to extract numbers.
