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
//        return null;
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

    private void parseFormula(String formula) {
        int numInd = 0;
        // iterate through string
        for (int i = 1; i < formula.length(); i++) { // "11-10/-a"
            if (isOp(formula.charAt(i))) {
                if (isOp(formula.charAt(i - 1))) continue; // e.g. 10*-3. Here "i" points to "-" after "*"

                pushNumToStack(formula, numInd, i);

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
            }
        }

        // push the last num to the stack
        pushNumToStack(formula, numInd, formula.length());
    }

    private void pushNumToStack(String formula, int numInd, int endIndOfNum) { // num can be: 6 || -6 || a || -a
        String num = formula.substring(numInd, endIndOfNum);

        if (isVar(num)) {
            String varName = num;
            if (num.charAt(0) == '-') varName = num.substring(1);

            String varValue = vars.get(varName);

            // cases for replacement when a=-2:
            // a+a    // -2-2
            // 5-a    // 5+2
            // 5^-a    // 5^2
            // -a+5    // 2+5
            // -a*a    // 2*-2
            // 2a    // 2*-2
            if (varValue != null) {
                if (varValue.charAt(0) == '-' && // if args var is negative e.g. a=-2
                        num.charAt(0) == '-' // if var in formula is negative
                ) // e.g. 5-a || 5^-a || -a+5
                {
                    num = varValue.substring(1); // var -2 -> 2
                } else num = varValue;
            } else {
                System.out.println("could not find the variable with the following name: " + varName);
                System.exit(0);
            }
        }

        nums.push(num);
    }

    private boolean isOp(char ch) {
        return String.valueOf(ch).matches("[\\^*/+-]");
    }

    private boolean isVar(String str) {
        return str.matches("[a-zA-Z]");
    }

    /**
     * The following method
     */
    private int getPriority(String operation) {
        switch (operation) {
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
}
