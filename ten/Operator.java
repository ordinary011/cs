package com.shpp.p2p.cs.ldebryniuk.assignment10;

import java.text.DecimalFormat;

public class Operator {
    private final Aux aux = Aux.getInstance();

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

            splitAndDoOperation2(formula, operationInd, operation.toString());
        }
    }

    /**
     * The following method indicates first and second number in the formula. Then performs operation.
     * And replaces result in the formula
     */
    private void splitAndDoOperation(StringBuilder formula, int operationInd, String operation) {
        int indOfNumBeforeOperation = findIndOfDigitBefore(formula, operationInd);
        int nextOperationInd = findNextOperationIndex(formula, operationInd);

        String firstN = formula.substring(indOfNumBeforeOperation, operationInd);
        String secondN = formula.substring(operationInd + 1, nextOperationInd);

        String res = doOperation(firstN, secondN, operation);
        res = addPlusForPositiveNumsIfNeeded(res, indOfNumBeforeOperation, formula);
        formula.replace(indOfNumBeforeOperation, nextOperationInd, res);
    }

    private void splitAndDoOperation2(StringBuilder formula, int operationInd, String operation) {
        int indOfNumBeforeOperation = findIndOfDigitBefore2(formula, operationInd);
        int nextOperationInd = findNextOperationIndex(formula, operationInd);

        String firstN = formula.substring(indOfNumBeforeOperation, operationInd);
        String secondN = formula.substring(operationInd + 1, nextOperationInd);

        String res = doOperation(firstN, secondN, operation);
        res = addPlusForPositiveNumsIfNeeded(res, indOfNumBeforeOperation, formula);
        formula.replace(indOfNumBeforeOperation, nextOperationInd, res);
    }

    private int findIndOfDigitBefore2(StringBuilder formula, int indOfOperation) {
        int indOperationBefore = findOperationIndex(formula.toString(), indOfOperation - 1, false);

        // find start index of digit before operation sign
        int indOfDigitBeforeOperation = -1;
        if (indOperationBefore == -1) indOfDigitBeforeOperation = 0; // if there is no operation before
        else if (formula.charAt(indOperationBefore) == '-') indOfDigitBeforeOperation = indOperationBefore;
        else if (indOperationBefore > -1) indOfDigitBeforeOperation = indOperationBefore + 1;

        return indOfDigitBeforeOperation;
    }

    /**
     * Add plus sign if needed. cases for consideration: 11-3^2 || 11-10/-2 || 11+3^2 || 5^-5
     */
    private String addPlusForPositiveNumsIfNeeded(String res, int indOfNumBeforeOperation, StringBuilder formula) {
        if (res.charAt(0) != '-' && // if res negative no more actions needed
                indOfNumBeforeOperation > 0 && // if indOfDigitBeforeOperation == 0 -> "+" is not needed
                formula.charAt(indOfNumBeforeOperation - 1) != '+') { // if there was no plus before e.g. 11-3^2->11+9
            return "+" + res; // e.g. 11-3^2 -> 11+9
        }

        return res;
    }

    /**
     * The following method determines what operation is first in the formula. e.g. "+" OR "-" ; "*" OR "/"
     */
    private int findFirstOperation(StringBuilder determinedOperation, int indOfFirstOperation,
                                   int indOfSecondOperation, String firstOperation, String secondOperation) {
        int operationInd;
        String operation;

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

        determinedOperation.append(operation);
        return operationInd;
    }

    /**
     * The following method searches the start index of the digit before current operation
     */
    private int findIndOfDigitBefore(StringBuilder formula, int indOfOperation) {
        int indOfPreviousOperation = findOperationIndex(formula.toString(), indOfOperation - 1, false);

        // cases: 11*-3^2 || 10-3^3 || 10+3^3 || -3^3 || 3^2
        // find start index of digit before current operation sign
        int indOfDigitBeforeCurrentOperation;
        if (indOfPreviousOperation == -1) { // if no operation before e.g. 3^2
            indOfDigitBeforeCurrentOperation = 0;
        } else if (indOfPreviousOperation == 0) { // if num is negative e.g. -3^2 only "-" can be at index of 0.
            indOfDigitBeforeCurrentOperation = indOfPreviousOperation;
        } else { // all other cases like 10-3^3 || 10+3^3 || 11*-3^2
            if (!aux.isDigit(formula.charAt(indOfPreviousOperation - 1))) { // e.g. 11*-3^2
                indOfDigitBeforeCurrentOperation = indOfPreviousOperation;
            } else { // e.g. 10-3^3 || 10+3^3
                indOfDigitBeforeCurrentOperation = indOfPreviousOperation + 1;
            }
        }

        return indOfDigitBeforeCurrentOperation;
    }

    /**
     * The following method searches the index of the subsequent operation
     */
    private int findNextOperationIndex(StringBuilder formula, int operationInd) {
        int nextOperationInd = findOperationIndex(formula.toString(), operationInd + 1, true);
        // cases: 4/2-1 || 4/-2-1 || 3+-3 || 3+-3+5 || 1+2-1
        if (nextOperationInd > -1 &&
                (nextOperationInd == (operationInd + 1))) { // e.g. ^-2 and nextOperationInd points to "-" of the "-2"
            { // e.g. 4/-2-1 || 3+-3+5
                nextOperationInd = findOperationIndex(formula.toString(), nextOperationInd + 1, true);
            }
        }
        // if there are no more operations after current
        if (nextOperationInd == -1) nextOperationInd = formula.length();

        return nextOperationInd;
    }

    /**
     * The following method performs operation
     */
    private String doOperation(String firstN, String secondN, String operation) {
        double res = -1;

        double firstNum = Double.parseDouble(firstN);
        double secondNum = Double.parseDouble(secondN);

        switch (operation) {
            case "+" -> res = firstNum + secondNum;
            case "-" -> res = firstNum - secondNum;
            case "*" -> res = firstNum * secondNum;
            case "/" -> res = firstNum / secondNum;
            case "^" -> res = Math.pow(firstNum, secondNum);
            default -> System.out.println("unknown operation check your formula");
        }

        return df.format(res);
    }

    /**
     * The following method searches the index of the operation either before or after current
     */
    private int findOperationIndex(String formula, int startInd, boolean searchToRight) {
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

}
