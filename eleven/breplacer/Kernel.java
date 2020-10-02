package com.shpp.p2p.cs.ldebryniuk.assignment11.breplacer;

/**
 * The following class comprises all the logic for calculating functions and other operations
 */
public class Kernel {

    private final String[] functions = {"sin", "cos", "tan", "atan", "log10", "log2", "sqrt"};
    private final OperationManager op = new OperationManager();

    /**
     * The following method performs calculations on formula in the prioritized order
     */
    public String calculate(StringBuilder formula) {
        calcFunctions(formula);

        calcRegParentheses(formula);

        op.powerUp(formula);
        op.calcOneOfFourOperations(formula, "*", "/");
        op.calcOneOfFourOperations(formula, "+", "-");

        return formula.toString();
    }

    /**
     * The following method first extracts value from parentheses
     * of functions like "sin", "cos", "tan", "atan", "log10", "log2", "sqrt" and then calculates them
     * At the end it replaces func with the result e.g. "4+sin(3)" -> 4+0.10000000.......
     */
    public void calcFunctions(StringBuilder formula) {
        for (String func : functions) {
            int indOfFunc;
            while ((indOfFunc = formula.indexOf(func)) > -1) { // while there is at least one func left
                int openParenthesesInd = formula.indexOf("(", indOfFunc);

                StringBuilder resInParentheses = new StringBuilder();
                int closeParenthesesInd = calcInParentheses(formula, openParenthesesInd, resInParentheses);

                String res = calcFunc(func, resInParentheses.toString());
                formula.replace(indOfFunc, closeParenthesesInd + 1, res);
            }
        }
    }

    /**
     * The following method calculates everything that is within parentheses that are not related to functions.
     * e.g. (3+3)+3 -> 6+3.
     */
    private void calcRegParentheses(StringBuilder formula) {
        int openParenthesesInd;
        while ((openParenthesesInd = formula.indexOf("(")) != -1) {
            StringBuilder resInParentheses = new StringBuilder();
            int closeParenthesesInd = calcInParentheses(formula, openParenthesesInd, resInParentheses);

            formula.replace(openParenthesesInd, closeParenthesesInd + 1, resInParentheses.toString());
        }
    }

    /**
     * The following method calculates everything that is within parentheses.
     * It also searches and returns closeParenthesesInd
     */
    private int calcInParentheses(StringBuilder formula, int openParenthesesInd, StringBuilder resInParentheses) {
        int chNumTillClosing = findChNumTillClosing(formula.substring(openParenthesesInd));
        int closeParenthesesInd = openParenthesesInd + chNumTillClosing;
        String withinParentheses = formula.substring(openParenthesesInd + 1, closeParenthesesInd);

        // if withinParentheses == 3, calculate will return 3; else it returns result of calculations
        String res = calculate(new StringBuilder(withinParentheses));
        resInParentheses.append(res);

        return closeParenthesesInd;
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
}
