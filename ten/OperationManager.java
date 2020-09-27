package com.shpp.p2p.cs.ldebryniuk.assignment10;

import java.text.DecimalFormat;

/**
 * The following class comprises all the logic about operations implementation
 */
public class OperationManager implements Aux {
    // https://www.java67.com/2014/06/how-to-format-float-or-double-number-java-example.html
    private final DecimalFormat df = new DecimalFormat("#.#########"); // formats output of operations

    /**
     * The following method powers up a number
     */
    public void powerUp(StringBuilder formula) {
        int indOfPower;
        while ((indOfPower = formula.indexOf("^")) > -1) { // while there is at least one power operation
            splitAndDoOperation(formula, indOfPower, "^");
        }
    }

    /**
     * The following method performs one of the following operations: "*", "/", "+", "-",
     */
    public void doOneOfFourOperations(StringBuilder formula, String firstOperation, String secondOperation) {
        int indOfFirstOperation;
        int indOfSecondOperation;

        // while first or second operation still exists
        while ((indOfFirstOperation = formula.indexOf(firstOperation, 1)) != -1 |
                (indOfSecondOperation = formula.indexOf(secondOperation, 1)) != -1) {

            StringBuilder operation = new StringBuilder();
            int operationInd = findFirstOperation(operation, indOfFirstOperation, indOfSecondOperation,
                    firstOperation, secondOperation);

            splitAndDoOperation(formula, operationInd, operation.toString());
        }
    }

    /**
     * The following method determines which operation is first in the formula. e.g. "+" OR "-" ; "*" OR "/"
     */
    private int findFirstOperation(StringBuilder determinedOperation, int indOfFirstOperation,
                                   int indOfSecondOperation, String firstOperation, String secondOperation) {
        int operationInd;
        String operation;

        if (indOfFirstOperation > -1) { // if first operation is present
            if (indOfSecondOperation > -1) { // true if both operation are present
                operation = indOfFirstOperation < indOfSecondOperation ? firstOperation : secondOperation;
                operationInd = Math.min(indOfFirstOperation, indOfSecondOperation);
            } else { // only first operation is present
                operation = firstOperation;
                operationInd = indOfFirstOperation;
            }
        } else { // only second operation is present
            operation = secondOperation;
            operationInd = indOfSecondOperation;
        }

        determinedOperation.append(operation);
        return operationInd;
    }

    /**
     * The following method indicates first and second number in the formula. Then performs operation on them.
     * And replaces result in the formula e.g. 3+5^2 -> 3+25
     */
    private void splitAndDoOperation(StringBuilder formula, int operationInd, String operation) {
        int indOfNumBeforeOperation = findIndOfDigitBefore(formula, operationInd);
        int nextOperationInd = findNextOperationIndex(formula, operationInd);

        String firstN = formula.substring(indOfNumBeforeOperation, operationInd);
        String secondN = formula.substring(operationInd + 1, nextOperationInd);

        String res = doOperation(firstN, secondN, operation);
        res = addPlusSign(res, indOfNumBeforeOperation, formula);
        formula.replace(indOfNumBeforeOperation, nextOperationInd, res);
    }

    /**
     * The following method searches for the index of the digit before current operation
     * e.g. operation == '^' in -33^2 || 33^2 -> digitIndBeforeOperation == 0
     */
    private int findIndOfDigitBefore(StringBuilder formula, int indOfOperation) {
        int indOfPrevOperation = findOperationInd(formula.toString(), indOfOperation - 1, false);

        // find index of digit before current operation sign
        int digitIndBeforeOperation;
        if (indOfPrevOperation < 1) { // e.g. 33^2 || -33^2
            digitIndBeforeOperation = 0;
        } else if (!isDigit(formula.charAt(indOfPrevOperation - 1))) { // e.g. 11*-3^2; indOfPrevOperation points to "-"
            digitIndBeforeOperation = indOfPrevOperation;
        } else { // all other cases e.g. 10-3^3 || 10+3^3 || 10*-3 || 10-3*2 etc...
            digitIndBeforeOperation = indOfPrevOperation + 1;
        }

        return digitIndBeforeOperation;
    }

    /**
     * The following method searches the index of the subsequent operation
     */
    private int findNextOperationIndex(StringBuilder formula, int operationInd) {
        int nextOperationInd = findOperationInd(formula.toString(), operationInd + 1, true);
        // cases for consideration: 4/2-1 || 4/-2-1
        if (nextOperationInd > -1 &&
                (nextOperationInd == (operationInd + 1))) { // e.g. 3^-2 and nextOperationInd points to "-" of the "-2"
            nextOperationInd = findOperationInd(formula.toString(), nextOperationInd + 1, true);
        }
        // if there are no more operations after current e.g. 3^-2 || 3^2
        if (nextOperationInd == -1) nextOperationInd = formula.length();

        return nextOperationInd;
    }

    /**
     * The following method searches the index of the operation either before or after current
     */
    private int findOperationInd(String formula, int startInd, boolean searchToRight) {
        if (searchToRight) {
            for (int i = startInd; i < formula.length(); i++) {
                if (String.valueOf(formula.charAt(i)).matches("[\\^*/+-]")) return i; // index of operation
            }
        } else {
            for (int i = startInd; i >= 0; i--) {
                if (String.valueOf(formula.charAt(i)).matches("[\\^*/+-]")) return i; // index of operation
            }
        }

        return -1;
    }

    /**
     * The following method performs operation
     */
    private String doOperation(String firstN, String secondN, String operation) {
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

        return df.format(res);
    }

    /**
     * Adds plus sign if needed
     */
    private String addPlusSign(String res, int indOfNumBeforeOperation, StringBuilder formula) {
        if (res.charAt(0) != '-' && // if res negative no more actions needed
                indOfNumBeforeOperation > 0 && // if indOfDigitBeforeOperation == 0 -> "+" is not needed
                formula.charAt(indOfNumBeforeOperation - 1) != '+') {// if there was no plus before: 11-10/-2 -> 11+5
            return "+" + res; // e.g. 11-10/-2 -> 11+5
        }

        return res;
    }
}
