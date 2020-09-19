package com.shpp.p2p.cs.ldebryniuk.assignment10;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;


/**
 * ysed resources:
 * https://stackoverflow.com/questions/4194310/can-java-string-indexof-handle-a-regular-expression-as-a-parameter
 * https://docs.oracle.com/javase/7/docs/api/java/util/regex/Pattern.html
 * https://stackoverflow.com/questions/7162889/android-java-regex-match-plus-sign
 * https://stackoverflow.com/questions/26854591/java-how-do-i-convert-double-to-string-directly
 */
public class Assignment10Part1 {

    private static final String[] prioritizedOperations = {"^", "*", "/", "+", "-"};

    // https://www.java67.com/2014/06/how-to-format-float-or-double-number-java-example.html
    private static final DecimalFormat df = new DecimalFormat("#.#########"); // formats output of operations


    public static void main(String[] args) {
//        runProgram(args);
//        runTests();

        String[] go = {"2-a", "a = -2"};

        System.out.println(
                runProgram(go)
        );
    }


// 3 - -3
// 3 * -3
// "-5-10 + a^2 - b^c + 10000.44" "c = 5" "a = 5" "b = 5"
// "1.0 / 0"
// "a + 1 * a"
// -a+5, a=-2

//String[] go = {"d-10 + a^2 - b^c + a", "c = -5", "a = 5", "b = -5", "d=33"};
//String[] go = {"d-10", "d=33"};
//String[] go = {"3- -2^-3"};

// do some crashes


    private static void removeAllSpaces(String[] args) {
        for (int i = 0; i < args.length; i++) {
            args[i] = args[i].replaceAll(" ", "");
        }
    }

    private static String runProgram(String[] args) {
        if (args.length == 0) {
            System.out.println("sorry no arguments found");
            System.exit(0);
        }

        removeAllSpaces(args);

        StringBuilder formula = new StringBuilder(args[0]);

        substituteVariables(formula, args);

        powerUp(formula);

        multiOrDivide(formula);

//        addOrSubstract(formula);

        return formula.toString();
    }

    private static void substituteVariables(StringBuilder formula, String[] args) {
        if (args.length > 1) { // if there is at least one variable
            for (int i = 1; i < args.length; i++) {
                String var = args[i];
                int equalInd = var.indexOf('=');
                String varName = var.substring(0, equalInd);
                String varValue = var.substring(equalInd + 1);

                int startIndOfVar;
                while ((startIndOfVar = formula.indexOf(varName)) > -1) { // while there is a variable with this name
                    int endIndOfVar = startIndOfVar + varName.length();

                    // if var is negative in formula? e. g. -a+5
                    if (startIndOfVar > 0 && (formula.charAt(startIndOfVar - 1) == '-')) {
                        if (varValue.charAt(0) == '-') { // if value of args var is negive
                            startIndOfVar--;
                            varValue = varValue.substring(1);
                        } // before -a+5, a=-2 => after 2 + 5
                    }

                    formula.replace(startIndOfVar, endIndOfVar, varValue);
                }
            }
        }
    }

    private static void powerUp(StringBuilder formula) {
        int indOfPower;
        while ((indOfPower = formula.indexOf("^")) > -1) { // while there is at least 1 power operation
            int indOperationBefore = findOperationIndex(formula.toString(), indOfPower - 1, false);
            // find start index of digit before power sign
            int startIndOfDigitBeforePower = -1;
            if (indOperationBefore == -1) startIndOfDigitBeforePower = 0;
            else if (formula.charAt(indOperationBefore) == '-') startIndOfDigitBeforePower = indOperationBefore;
            else if (indOperationBefore > -1) startIndOfDigitBeforePower = indOperationBefore + 1;

            int nextOperationInd = findOperationIndex(formula.toString(), indOfPower + 1, true);
            if (nextOperationInd > -1) {
                if (formula.charAt(nextOperationInd) == '-') { // if next operation is minus we will search for next again
                    nextOperationInd = findOperationIndex(formula.toString(), nextOperationInd + 1, true);
                } // if next operation is not minus then we are good and nextOperationInd is already correct
            }
            // if there are no more operations after current
            if (nextOperationInd == -1) nextOperationInd = formula.length();

            String firstN = formula.substring(startIndOfDigitBeforePower, indOfPower);
            String secondN = formula.substring(indOfPower + 1, nextOperationInd);

            String result = doArithmetic(firstN, secondN, "^");
            formula.replace(startIndOfDigitBeforePower, nextOperationInd, result);
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
            // find start index of digit before power sign
            int indDigitBeforeOperation = -1;
            if (indOperationBefore == -1) indDigitBeforeOperation = 0;
            else if (formula.charAt(indOperationBefore) == '-') indDigitBeforeOperation = indOperationBefore;
            else if (indOperationBefore > -1) indDigitBeforeOperation = indOperationBefore + 1;

            int nextOperationInd = findOperationIndex(formula.toString(), operationInd + 1, true);
            if (nextOperationInd > -1) {
                if (formula.charAt(nextOperationInd) == '-') { // if next operation is minus we will search for next again
                    nextOperationInd = findOperationIndex(formula.toString(), nextOperationInd + 1, true);
                } // if next operation is not minus then nextOperationInd is already correct
            }
            // if there are no more operations after current
            if (nextOperationInd == -1) nextOperationInd = formula.length();

            String firstN = formula.substring(indDigitBeforeOperation, operationInd);
            String secondN = formula.substring(operationInd + 1, nextOperationInd);

            String result = doArithmetic(firstN, secondN, operation);
            formula.replace(indDigitBeforeOperation, nextOperationInd, result);
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
                if (indOfSecondOperation == -1 && indOfFirstOperation == -1)
                    break; // if we have "-7" no more actions needed

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
//                double result = doNegArithmetic(expression, operation);

//                formula.replace(indOfDigitBeforeCurrentOperation, nextOperationInd, String.valueOf(result));
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
//                String result = doArithmetic(firstN, expression, operation);
//
//                formula.replace(indOfDigitBeforeCurrentOperation, nextOperationInd, result);
            }
        }
    }

    private static String doArithmetic(String firstN, String secondN, String operation) {
        double res = -1;

        double firstNum = Double.parseDouble(firstN);
        double secondNum = Double.parseDouble(secondN);

        switch (operation) {
            case "+" -> res = firstNum + secondNum;
            case "-" -> res = firstNum - secondNum;
            case "*" -> res = firstNum * secondNum;
            case "/" -> res = firstNum / secondNum;
            case "^" -> res = Math.pow(firstNum, secondNum);
            default -> System.out.println("unknown operation");
        }

        return df.format(res);
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

    private static void runTests() {
        String[][] tests = {
                {"-5^-5"}, {"-0.00032"},
                {"5^-5"}, {"0.00032"},
                {"5^5"}, {"3125"},
                {"-5^5"}, {"-3125"},
                {"5^a", "a = 4"}, {"625"},
                {"5^-a", "a = 4"}, {"0.0016"},
                {"3*4"}, {"12"},
                {"3/4"}, {"0.75"},
                {"3/4^-2"}, {"48"},
                {"-3/-4^-2"}, {"-48"},
                {"-3*a^2", "a = 2"}, {"-12"},
                {"-3*a^2", "a = -2"}, {"-12"},
                {"2*a", "a = -2"}, {"-4"},
                {"5^-a", "a = -4"}, {"625"},
                {"-a*2", "a = -2"}, {"4"},
                {"-a*2*4", "a = -2"}, {"16"},
//                {"1.0 + 2"}, {"3.0"},
//                {"1 + 3 ^ 2"}, {"10.0"},
//                {"1 + 3^2 + 2^3"}, {"18.0"},
//                {"11-3 ^ 2+3^3"}, {"29.0"},
//                {"1 + 3 * 2"}, {"7.0"},
//                {"5-10 + 3^2"}, {"4.0"},
//                {"-10 + 3"}, {"-7.0"},
//                {"1 + a * 2", "a = 2"}, {"5.0"},
//                {"5-10 + a^2 - b^c", "a = 5", "b = 5", "c = 5"}, {"-3105.0"},
//                {"d-10 + a^2 - b^c", "c = -5", "a = 5", "b = -5", "d=33"}
        };

        for (int i = 0; i < tests.length; i += 2) {
            String res = runProgram(tests[i]);
            if (res.equals(tests[i + 1][0])) {
                System.out.println("  Pass: " + tests[i][0] + " Result: " + res);
            } else {
                System.out.println("! FAIL: " + tests[i][0] + " Expected " + tests[i + 1][0] + " Got: " + res);
            }
        }
    }
}

// other test cases

// "2+ (3 + 5) + (1 + 3) ^ 2"
// "24+(1 + 3) ^ 2"
// "(1 + 3) * 2"
// "(1 + 3) ^ 2"

