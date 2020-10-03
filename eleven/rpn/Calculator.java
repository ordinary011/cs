package com.shpp.p2p.cs.ldebryniuk.assignment11.rpn;

import java.util.HashMap;
import java.util.Stack;

public class Calculator {

    private final Stack<String> nums = new Stack<>();
    private final Stack<String> operators = new Stack<>();
    private final HashMap<String, String> vars = new HashMap<>();

    /**
     * The following method prepares formula and variables for further actions.
     * It removes redundant spaces and replaces comas with dots
     */
    private void formatArgs(String[] args) {
        for (int i = 0; i < args.length; i++) {
            args[i] = args[i].replaceAll(" ", "");
            args[i] = args[i].replaceAll(",", ".");
        }
    }

    public String runCalc(String[] args) {
        formatArgs(args);
        String formula = args[0];

        try {
            parseVars(args);

            parseFormula(formula);

            // calculate the rest of the stack
            String n2 = nums.pop();
            while (!operators.empty()) {
                String n1 = nums.pop();
                String operation = operators.pop();

                n2 = calcOperation(n1, n2, operation);
            }

            return String.format("%.5f", Double.parseDouble(n2));
        } catch (Exception e) {
            System.out.println("Input mistake. Please check your formula and variables");
//            e.printStackTrace();
        }

        return null;
    }

    private void parseVars(String[] args) {
        if (args.length > 1) { // if there is at least one variable
            for (int i = 1; i < args.length; i++) {
                String var = args[i]; // example of var -> "a=5"
                int equalInd = var.indexOf('=');
                String varName = var.substring(0, equalInd);
                String varValue = var.substring(equalInd + 1);

                vars.put(varName, varValue);
            }
        }
    }

    private void parseFormula(String formula) throws Exception {
        int numInd = 0; // start index of a number before operation
        char ch = formula.charAt(0);
        if (ch == '(') {
            operators.push("(");
            numInd++; // i points to the "(". next num will have an index of i + 1;
        }

        for (int i = 1; i < formula.length(); i++) {
            ch = formula.charAt(i);

            if (isOp(ch)) {
                if (isOp(formula.charAt(i - 1))) continue; // e.g. 10*-3. Here "i" points to "-" after "*"

                pushToNumsStack(formula, numInd, i);

                String formulaOp = formula.substring(i, i + 1);
                if (!operators.empty()) {
                    int formOpPriority = getPriority(formulaOp);
                    String topOfStackOp = operators.peek();
                    int topOfStackOpPriority = getPriority(topOfStackOp);

                    while (formOpPriority <= topOfStackOpPriority) {
                        String n2 = nums.pop();
                        String n1 = nums.pop();
                        operators.pop();

                        String res = calcOperation(n1, n2, topOfStackOp);
                        nums.push(res);

                        if (!operators.empty()) {
                            topOfStackOp = operators.peek();
                            topOfStackOpPriority = getPriority(topOfStackOp);
                        } else break;
                    }
                }
                operators.push(formulaOp);

                numInd = i + 1; // i points to the сurrent operator e.g. "*". next num will have an index of i + 1;
            } else if (ch == '(') {
                // if func before
                if (i > 2 && isFunc(formula.substring(numInd, i))) { // parentheses after function e.g. sin(3+3)
                    int chNumTillClosing = findChNumTillClosing(formula.substring(i));

                    parseFormula(formula.substring(i, (i + chNumTillClosing + 1)));
                    // after parseFormula at the top of the stack will be res of calculations withing brackets
                    String res = nums.pop();
                    res = calcFunc(formula.substring(numInd, i), res);
                    nums.push(res);

                    i += chNumTillClosing;
                    numInd = i + 1;
                } else { // regular parentheses
                    operators.push("(");
                    numInd = i + 1; // i points to the "(". next num will have an index of i + 1;
                    i++; // skip ch after "(" because numInd will point to that number anyway
                }
            } else if (ch == ')') {
                pushToNumsStack(formula, numInd, i);

                String operation = operators.pop();
                String res = "";
                while (!operation.equals("(")) {
                    String n2 = nums.pop();
                    String n1 = nums.pop();

                    res = calcOperation(n1, n2, operation);
                    nums.push(res);

                    operation = operators.pop();
                }
                numInd = i + 1; // i points to the ")". next num will have an index of i + 1;
            }
        }

        // push the last num to the stack
        pushToNumsStack(formula, numInd, formula.length());
    }



    private void pushToNumsStack(String formula, int numInd, int endIndOfNum) throws Exception { // num can be: 6 || -6 || a || -a
        if (numInd < endIndOfNum) { // if numInd == endIndOfNum -> numInd points to something else e.g. "*"
            String num = formula.substring(numInd, endIndOfNum);

            if (isVar(num)) {
                String varValue = findVar(num);

                if (varValue != null) {
                    if (varValue.charAt(0) == '-' && // if args var is negative e.g. a=-2
                            num.charAt(0) == '-' // if var in formula is negative
                    ) // e.g. 5-a || 5^-a || -a+5
                    {
                        num = varValue.substring(1); // var -2 -> 2
                    } else if (num.charAt(0) == '-') { // 10/-a
                        num = "-" + varValue;
                    } else {
                        num = varValue;
                    }
                } else {
                    System.out.println("could not find the variable with the following name: " + num);
                    throw new Exception();
                }
            }

            nums.push(num);
        }
    }

    private String findVar(String varName) { // a || -a
        if (varName.charAt(0) == '-') varName = varName.substring(1);

        return vars.get(varName);
    }

    private boolean isFunc(String str) {
        return str.matches("sin|cos|tan|atan|log10|log2|sqrt");
    }

    private boolean isOp(char ch) {
        return String.valueOf(ch).matches("[\\^*/+-]");
    }

    private boolean isVar(String str) {
        return str.matches(".?[a-zA-Z]+");
    }

    private boolean isDigit(char ch) {
        return Character.isDigit(ch);
    }

    /**
     * The following method
     */
    private int getPriority(String operation) {
        switch (operation) {
            case "(":
//            case ")":
                return 0;
            case "+":
            case "-":
                return 1;
            case "*":
            case "/":
                return 2;
            case "^":
                return 3;
            default:
                System.out.println("unknown operation please check your formula");
                return -1;
        }
    }

    /**
     * The following method performs operation
     */
    private String calcOperation(String firstN, String secondN, String operation) {
        double res = -1;

        double firstNum = Double.parseDouble(firstN);
        double secondNum = Double.parseDouble(secondN);

        switch (operation) {
            case "+":
                res = firstNum + secondNum;
                break;
            case "-":
                res = firstNum - secondNum;
                break;
            case "*":
                res = firstNum * secondNum;
                break;
            case "/":
                res = firstNum / secondNum;
                break;
            case "^":
                res = Math.pow(firstNum, secondNum);
                break;
            default:
                System.out.println("unknown operation check your formula");
        }

        return String.valueOf(res);
    }

    /**
     * The following method calculates functions like "sin", "cos", "tan", "atan", "log10", "log2", "sqrt"
     */
    private String calcFunc(String func, String value) {
        double val = Double.parseDouble(value);

        switch (func) {
            case "sin":
                return String.valueOf(Math.sin(val));
            case "cos":
                return String.valueOf(Math.cos(val));
            case "tan":
                return String.valueOf(Math.tan(val));
            case "atan":
                return String.valueOf(Math.atan(val));
            case "sqrt":
                return String.valueOf(Math.sqrt(val));
            case "log2":
                return String.valueOf(Math.log(val));
            case "log10":
                return String.valueOf(Math.log10(val));
            default:
                System.out.println("unknown function please check your formula");
                return value;
        }
    }

    /**
     * The following method searches for the amount of characters from opening to the closing parentheses
     */
    private int findChNumTillClosing(String parentheses) { // cos(2+3+cos(2+3)*2) -> (2+3+cos(2+3)*2)
        int needToClose = 0;

        for (int i = 0; i < parentheses.length(); i++) {
            char ch = parentheses.charAt(i);
            if (ch == '(') needToClose++;
            else if (ch == ')') needToClose--;

            if (needToClose == 0) return i; // amount of characters from opening to the closing parentheses
        }

        // if no index was found
        System.out.println("There is a mistake in the formula. You forgot to close your parentheses");
        System.exit(0);

        return -1;
    }
}
