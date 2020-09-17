package com.shpp.p2p.cs.ldebryniuk.assignment10;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ysed resources:
 * https://stackoverflow.com/questions/4194310/can-java-string-indexof-handle-a-regular-expression-as-a-parameter
 * https://docs.oracle.com/javase/7/docs/api/java/util/regex/Pattern.html
 * https://stackoverflow.com/questions/7162889/android-java-regex-match-plus-sign
 */
public class Assignment10Part1 {
    private static final String[] prioritizedOperations = {"^", "^", "*", "/", "+", "-"};

    public static void main(String[] args) {
        if (args.length > 0) calculate(args);
        else System.out.println("sorry no arguments found");
    }

    private static void removeAllSpaces(String[] args) {
        for (int i = 0; i < args.length; i++) {
            args[i] = args[i].replaceAll(" ", "");
        }
    }

    private static void calculate(String[] args) {
        removeAllSpaces(args);

        StringBuilder formula = new StringBuilder(args[0]);
        System.out.println(formula);

        for (int i = 0; i < prioritizedOperations.length; i += 2) {
            String firstOperation = prioritizedOperations[i];
            String secondOperation = prioritizedOperations[i + 1];

            go(formula, firstOperation, secondOperation);
        }
    }

    private static void go(StringBuilder formula, String firstOperation, String secondOperation) {
        int indOfFirstOperation;
        int indOfSecondOperation = -1;

        String operation;
        int operationInd;

        // while first or second operation still exists
        while ((indOfFirstOperation = formula.indexOf(firstOperation)) > -1 |
                (indOfSecondOperation = formula.indexOf(secondOperation)) > -1) {

            // determine which operation goes first
            if (indOfFirstOperation > -1 && indOfSecondOperation > -1) { // if both operations are present
                operation = indOfFirstOperation < indOfSecondOperation ? firstOperation : secondOperation;
                operationInd = Math.min(indOfFirstOperation, indOfSecondOperation);
            } else { // if only one of 2 operations present
                if (indOfFirstOperation > -1) {
                    operation = firstOperation;
                    operationInd = indOfFirstOperation;
                } else { // only second operation is present
                    operation = secondOperation;
                    operationInd = indOfSecondOperation;
                }
            }

            int indOperationBefore = findOperationIndex(formula.toString(), operationInd - 1, false);
            int nextOperationInd = findOperationIndex(formula.toString(), operationInd + 1, true);
            int indOfDigitBeforeCurrentOperation = indOperationBefore + 1;

            // if this is the last operation in the formula
            if (nextOperationInd == -1) nextOperationInd = formula.length();

            String expression = formula.substring(indOfDigitBeforeCurrentOperation, nextOperationInd);
            double result = doArithmetic(expression, operation);

            formula.replace(indOfDigitBeforeCurrentOperation, nextOperationInd, String.valueOf(result));
            System.out.println(formula);
        }
    }

    private static double doArithmetic(String formula, String operation) {
        int operationIndex = formula.indexOf(operation);
        double res = -1;

        double firstNum = Double.parseDouble(formula.substring(0, operationIndex));
        double secondNum = Double.parseDouble(formula.substring(operationIndex + 1));

        switch (operation) {
            case "+" -> res = firstNum + secondNum;
            case "-" -> res = firstNum - secondNum;
            case "*" -> res = firstNum * secondNum;
            case "/" -> res = firstNum / secondNum;
            case "^" -> res = Math.pow(firstNum, secondNum);
            default -> System.out.println("unknown operation");
        }

        return res;
    }

    private static int findOperationIndex(String formula, int startInd, boolean searchToTheRight) {
        if (searchToTheRight) {
            for (int i = startInd; i < formula.length(); i++) {
                for (String operaion : prioritizedOperations) {
                    if (formula.charAt(i) == operaion.charAt(0)) return i; // index of operation
                }
            }
            return -1;
        } else {
            for (int i = startInd; i > 0; i--) {
                for (String operaion : prioritizedOperations) {
                    if (formula.charAt(i) == operaion.charAt(0)) return i; // index of operation
                }
            }
            return -1;
        }
    }
}

// "1.0 + 2"
// "1 + 3 ^ 2"
// "1 + 3^2 + 2^3"
// "11-3 ^ 2+3^3"
// "(1 + 3) ^ 2"
// "1 + 3 * 2"
// "1 + a * 2"