package com.shpp.p2p.cs.ldebryniuk.assignment10;

import java.util.Arrays;

/**
 *
 * ysed resources:
 * https://stackoverflow.com/questions/4194310/can-java-string-indexof-handle-a-regular-expression-as-a-parameter
 * https://docs.oracle.com/javase/7/docs/api/java/util/regex/Pattern.html
 * https://stackoverflow.com/questions/7162889/android-java-regex-match-plus-sign
 */
public class Assignment10Part1 {
    private static final String[] prioritizedOperations = {"^", "*", "/", "+", "-"};

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
        System.out.println("start " + args[0]);

        StringBuilder formula = new StringBuilder(args[0]);

        substituteVariables(formula, args);

        System.out.println("replaced vars " + formula);

        powerUp(formula);

        multiOrDivide(formula);

        addOrSubstract(formula);

        System.out.println("finish: " + formula);
    }

    private static void substituteVariables(StringBuilder formula, String[] args) {
        if (args.length > 1) { // if there is at least one variable
            for (int i = 1; i < args.length; i++) {
                String var = args[i];
                int equalInd = var.indexOf('=');
                String varName = var.substring(0, equalInd);
                String varValue = var.substring(equalInd + 1);

                int varIndInFormula;
                while ((varIndInFormula = formula.indexOf(varName)) > -1) { // while there is a variable with this name
                    int endOfVar = varIndInFormula + varName.length();
                    formula.replace(varIndInFormula, endOfVar, varValue);
                }
            }
        }
    }

    private static void powerUp(StringBuilder formula) {
        int indOfPower;
        while ((indOfPower = formula.indexOf("^")) > -1) {
            int indOperationBefore = findOperationIndex(formula.toString(), indOfPower - 1, false);
            int nextOperationInd = findOperationIndex(formula.toString(), indOfPower + 1, true);
            int indOfDigitBeforePower = indOperationBefore + 1;

            // if this is the last operation in the formula
            if (nextOperationInd == -1) nextOperationInd = formula.length();

            String expression = formula.substring(indOfDigitBeforePower, nextOperationInd);
            double result = doArithmetic(expression, "^");

            formula.replace(indOfDigitBeforePower, nextOperationInd, String.valueOf(result));
            System.out.println(formula);
        }
    }

    private static void multiOrDivide(StringBuilder formula) {
        String firstOperation = "*";
        String secondOperation = "/";
        int indOfFirstOperation;
        int indOfSecondOperation;

        // while first or second operation still exists
        while ((indOfFirstOperation = formula.indexOf(firstOperation)) > -1 |
                (indOfSecondOperation = formula.indexOf(secondOperation)) > -1) {
            String operation;
            int operationInd;

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
        }
    }

    private static void addOrSubstract(StringBuilder formula) {
        String firstOperation = "+";
        String secondOperation = "-";
        int indOfFirstOperation;
        int indOfSecondOperation;

        // while first or second operation still exists
        while ((indOfFirstOperation = formula.indexOf(firstOperation)) > -1 |
                (indOfSecondOperation = formula.indexOf(secondOperation)) > -1) {
            if (indOfSecondOperation == 0) { // first number is negative
                indOfSecondOperation = formula.indexOf(secondOperation, 1);
                if (indOfSecondOperation == -1 && indOfFirstOperation == -1) break; // if we have "-7" no more actions needed

                String operation;
                int operationInd;

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

//                int indOperationBefore = findOperationIndex(formula.toString(), operationInd - 1, false);
                int nextOperationInd = findOperationIndex(formula.toString(), operationInd + 1, true);
                int indOfDigitBeforeCurrentOperation = 0;

                // if this is the last operation in the formula
                if (nextOperationInd == -1) nextOperationInd = formula.length();

                String expression = formula.substring(indOfDigitBeforeCurrentOperation, nextOperationInd);
                double result = doNegArithmetic(expression, operation);

                formula.replace(indOfDigitBeforeCurrentOperation, nextOperationInd, String.valueOf(result));
                System.out.println(formula);
            } else { // first number is positive
                String operation;
                int operationInd;

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
            }
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

    private static double doNegArithmetic(String formula, String operation) {
        int operationIndex = formula.indexOf(operation, 1);
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
            for (int i = startInd; i >= 0; i--) {
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
// "2+ (3 + 5) + (1 + 3) ^ 2"
// "24+(1 + 3) ^ 2"
// "(1 + 3) * 2"
// "(1 + 3) ^ 2"
// "1 + 3 * 2"
// "1 + a * 2"
// "5-10 + 3^2"
// "-10 + 3"
// "5-10 + a^2 - b^c" "a = 5" "b = 5" "c = 5"
// "5-10 + a^2 - b^c" "c = 5" "a = 5" "b = 5"
// "-5-10 + a^2 - b^c + 10000.44" "c = 5" "a = 5" "b = 5"





//        // get rid of pARANTHESIS
//        int indexOfParanthesis;
//        while ((indexOfParanthesis = formula.indexOf("(")) > -1) {
//            int indexOfParanthesis2 = formula.indexOf(")");
//            String expression = formula.substring(indexOfParanthesis + 1, indexOfParanthesis2);
//            StringBuilder res = perform(new StringBuilder(expression));
//
//            formula.replace(indexOfParanthesis, indexOfParanthesis2 + 1, res.toString());
//            System.out.println(formula);
//        }
