package com.shpp.p2p.cs.ldebryniuk.calc10_try_to_refactor;

import java.util.Stack;

/**
 * The following class is the main class of the calculator app. It comprises all the necessary logic
 */
public class Calculator {

    private final VariablesHandler variablesHandler = new VariablesHandler();

    Stack<String> operationsStack = new Stack<>();
    Stack<String> numbersStack = new Stack<>();

    /**
     * The following method prepares formula and variables for further actions.
     * It removes redundant spaces and replaces comas with dots
     */
    public void formatArgs(String[] args) {
        for (int i = 0; i < args.length; i++) {
            args[i] = args[i].replaceAll(" ", "");
            args[i] = args[i].replaceAll(",", ".");
        }
    }

    /**
     * The following method is the starting point of the calculator. Operations are performed in the prioritized order
     */
    public String runCalc(String[] args) {
        formatArgs(args);

        try {
            parseFormula(args[0]);
        } catch (Exception e) {
            System.err.println("Input mistake. Please check your formula and variables");
            return null;
        }

//        return formula.toString();
        return null;
    }

    private void parseFormula(String formula) {
        int indexOfNumber = 0;

        // i == 1, because we don't care what first character is
        for (int currentChIndex = 1; currentChIndex < formula.length(); currentChIndex++) {
            char currentChar = formula.charAt(currentChIndex);

            if (isOperation(currentChar)) {
                char charBeforeCurrent = formula.charAt(currentChIndex - 1);
                if (isOperation(charBeforeCurrent)) {
                    // true when e.g. 10*-3. Here "currentChIndex" points to "-" after "*", we will skip iteration
                    // because that "-" is a part of "-3" and we will push "-3" to the numbers stack
                    continue;
                }

                pushNumberToNumbersStack(formula, indexOfNumber, currentChIndex);
            }
        }

        // push the last number to the numbers stack
        pushNumberToNumbersStack(formula, indexOfNumber, formula.length());
    }

    private void pushNumberToNumbersStack(String formula, int numIndexStart, int numIndexEnd) {
//        if (numInd < endIndOfNum) { // if numInd == endIndOfNum -> numInd points to something else e.g. "*"

            String numberOrVar = formula.substring(numIndexStart, numIndexEnd); // number can be: 6 || -6 || a || -a

            if (isVariable(numberOrVar)) {
                String varValue = variablesHandler.getVariable(numberOrVar);

                if (varValue != null) {
                    // cases to consider 5-a || 5^-a || -a+5
                    if (varValue.charAt(0) == '-' && // if args var is negative e.g. a=-2
                            numberOrVar.charAt(0) == '-' // if var in formula is negative
                    ) numberOrVar = varValue.substring(1); // var -2 -> 2

                    else if (numberOrVar.charAt(0) == '-')  // if var in formula is negative 10/-a
                        numberOrVar = "-" + varValue;
                    else  // sign of var is already correct
                        numberOrVar = varValue;

                } else {
                    System.out.println("could not find the variable with the following name: " + numberOrVar);
                    throw new Exception();
                }
            }

            nums.push(numberOrVar);
//        }
    }

    /**
     * The following method determines if ch is an operation
     */
    private boolean isOperation(char ch) {
        return String.valueOf(ch).matches("[\\^*/+-]");
    }

    /**
     * The following method determines if str is a variable
     *
     * @param str can be -a || a || aba
     */
    private boolean isVariable(String str) {
        return str.matches(".?[a-zA-Z]+");
    }


}
