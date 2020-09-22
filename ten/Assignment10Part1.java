package com.shpp.p2p.cs.ldebryniuk.assignment10;

import java.text.DecimalFormat;
import java.util.Arrays;

/**
 * The following class is a simulation of the simplified calculator.
 * It perform the following operations: "^", "*", "/", "+", "-"
 * The whole algorithm is built around the StringBuilder's replace method.
 * Every time certain operation is performed the program replaces expression by the result
 * For example: 5+3^2 -> 5+9 -> 14
 *
 * used resources:
 * https://docs.oracle.com/javase/7/docs/api/java/util/regex/Pattern.html
 * https://stackoverflow.com/questions/40246231/java-util-regex-patternsyntaxexception-dangling-meta-character-near-index-0
 */
public class Assignment10Part1 {

    // https://www.java67.com/2014/06/how-to-format-float-or-double-number-java-example.html
    private static final DecimalFormat df = new DecimalFormat("#.#########"); // formats output of operations

    /**
     * Starting method
     */
    public static void main(String[] args) {
        System.out.println(runProgram(args));

//        runTests();
    }

    /**
     * The following method is the starting point of the calculator. Operations are performed in the prioritized order
     */
    private static String runProgram(String[] args) {
        if (args.length == 0) {
            System.out.println("sorry no arguments found");
            return null;
        }

        formatArgs(args);
        StringBuilder formula = new StringBuilder(args[0]);

        try {
            substituteVariables(formula, args);
            powerUp(formula);
            doOneOfFourOperations(formula, "*", "/");
            doOneOfFourOperations(formula, "+", "-");
        } catch (Exception e) {
            System.out.println("Input mistake. Please check your formula and variables");
            return null;
        }

        return formula.toString();
    }

    /**
     * The following method prepares args for further actions
     */
    private static void formatArgs(String[] args) {
        for (int i = 0; i < args.length; i++) {
            args[i] = args[i].replaceAll(" ", "");
            args[i] = args[i].replaceAll(",", ".");
        }
    }

    /**
     * The following method substitutes variables with their corresponding values e.g. a+5, a=4 -> 4+5
     */
    private static void substituteVariables(StringBuilder formula, String[] args) {
        if (args.length > 1) { // if there is at least one variable
            for (int i = 1; i < args.length; i++) {
                String var = args[i];
                int equalInd = var.indexOf('=');
                String varName = var.substring(0, equalInd);
                String varValue = var.substring(equalInd + 1);

                substituteVarsWithCurrentName(formula, varName, varValue);
            }

            replaceDoubleMinuses(formula);
            deleteRedundantPluses(formula);
        }
    }

    /**
     * The following method substitutes variable with current name
     */
    private static void substituteVarsWithCurrentName(StringBuilder formula, String varName, String varValue) {
        int startIndOfVarInFormula;
        // while there is a variable with this name in the formula
        while ((startIndOfVarInFormula = formula.indexOf(varName)) > -1) {
            int endIndOfVar = startIndOfVarInFormula + varName.length();

            varValue = addMultiplicationSign(formula, startIndOfVarInFormula, varValue);

            formula.replace(startIndOfVarInFormula, endIndOfVar, varValue);
        }
    }

    /**
     * The following method deletes redundant "+" signs
     */
    private static void deleteRedundantPluses(StringBuilder formula) {
        if (formula.charAt(0) == '+') formula.deleteCharAt(0); // e.g. +2+5 -> 2+5
        for (int i = 1; i < formula.length(); i++) {
            if (formula.charAt(i) == '+' && String.valueOf(formula.charAt(i - 1)).matches("[\\^*/]")) {
                formula.deleteCharAt(i); // e. g. 5^+2 -> 5^2
            }
        }
    }

    /**
     * The following method replaces "--" with "+"
     */
    private static void replaceDoubleMinuses(StringBuilder formula) {
        for (int i = 0; i < formula.length(); i++) {
            if (formula.charAt(i) == '-' && i > 0 && formula.charAt(i - 1) == '-') {
                // e.g. 5--2 -> 5+2 || --2+5 -> +2+5 || 5^--2 -> 5^+2 || --4*-4 -> +4*-4
                formula.replace(i - 1, i + 1, "+");
            }
        }
    }

    /**
     * The following method replaces "--" with "+" e.g. [a2, a=4] -> 4*2
     */
    private static String addMultiplicationSign(StringBuilder formula, int startIndOfVarInFormula, String varValue) {
        if (startIndOfVarInFormula == 0 &&
                String.valueOf(formula.charAt(startIndOfVarInFormula + 1)).matches("\\d"))  //e.g. a2
        {
            return varValue + "*";
        }

        if (startIndOfVarInFormula > 0 &&
                String.valueOf(formula.charAt(startIndOfVarInFormula - 1)).matches("\\d"))  //e.g. 2a
        {
            return "*" + varValue;
        }

        return varValue;
    }

    /**
     * The following method powers up a number
     */
    private static void powerUp(StringBuilder formula) {
        int indOfPower;
        while ((indOfPower = formula.indexOf("^")) > -1) { // while there is at least one power operation
            int indOfDigitBeforeOperation = findIndOfDigitBefore(formula, indOfPower);
            int nextOperationInd = findNextOperationIndex(formula, indOfPower);

            String firstN = formula.substring(indOfDigitBeforeOperation, indOfPower);
            String secondN = formula.substring(indOfPower + 1, nextOperationInd);

            String res = doOperation(firstN, secondN, "^");

            // change sign to plus if needed
            if (res.charAt(0) != '-' && // cases: 11-3^2 || 11+3^2 || 5^-5
                    indOfDigitBeforeOperation > 0 &&
                    formula.charAt(indOfDigitBeforeOperation - 1) != '+') {
                res = "+" + res; // e.g. 11-3^2 -> 11+9
            }
            formula.replace(indOfDigitBeforeOperation, nextOperationInd, res);
        }

        deleteRedundantPluses(formula);
    }

    /**
     * The following method performs one of the following operations: "+", "-", "*", "/",
     */
    private static void doOneOfFourOperations(StringBuilder formula, String firstOperation, String secondOperation) {
        int indOfFirstOperation;
        int indOfSecondOperation;

        // while first or second operation still exists
        while ((indOfFirstOperation = formula.indexOf(firstOperation, 1)) != -1 |
                (indOfSecondOperation = formula.indexOf(secondOperation, 1)) != -1) {

            StringBuilder operation = new StringBuilder();
            int operationInd = findFirstOperation(operation, indOfFirstOperation, indOfSecondOperation,
                    firstOperation, secondOperation);

            int indOfDigitBeforeOperation = findIndOfDigitBefore(formula, operationInd);
            int nextOperationInd = findNextOperationIndex(formula, operationInd);

            String firstN = formula.substring(indOfDigitBeforeOperation, operationInd);
            String secondN = formula.substring(operationInd + 1, nextOperationInd);

            String result = doOperation(firstN, secondN, operation.toString());
            formula.replace(indOfDigitBeforeOperation, nextOperationInd, result);
        }
    }

    /**
     * The following method determines what operation goes first. e.g. "+" OR "-" ; "*" OR "/"
     */
    private static int findFirstOperation(StringBuilder determinedOperation, int indOfFirstOperation,
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
    private static int findIndOfDigitBefore(StringBuilder formula, int indOfOperation) {
        int indOperationBefore = findOperationIndex(formula.toString(), indOfOperation - 1, false);

        // find start index of digit before operation sign
        int indOfDigitBeforeOperation = -1;
        if (indOperationBefore == -1) indOfDigitBeforeOperation = 0;
        else if (formula.charAt(indOperationBefore) == '-') indOfDigitBeforeOperation = indOperationBefore;
        else if (indOperationBefore > -1) indOfDigitBeforeOperation = indOperationBefore + 1;

        return indOfDigitBeforeOperation;
    }

    /**
     * The following method searches the index of the subsequent operation
     */
    private static int findNextOperationIndex(StringBuilder formula, int operationInd) {
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
    private static String doOperation(String firstN, String secondN, String operation) {
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
    private static int findOperationIndex(String formula, int startInd, boolean searchToRight) {
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

    private static void runTests() {
        String[][] tests = {
                {"1.0 + 2"}, {"3"},
                {"1 + 3 ^ 2"}, {"10"},
                {"1 + 3^2 + 2^3"}, {"18"},
                {"1 + 3 * 2"}, {"7"},
                {"5-10 + 3^2"}, {"4"},
                {"-10 + 3"}, {"-7"},
                {"3+2,0"}, {"5"},
                {"11-3 ^ 2+3^3"}, {"47"},
                {"10*-3"}, {"-30"},
                {"10*-3^3"}, {"-270"},
                {"-5^-5"}, {"-0.00032"},
                {"5^-5"}, {"0.00032"},
                {"5^5"}, {"3125"},
                {"-5^5"}, {"-3125"},
                {"5^a", "a = 4"}, {"625"},
                {"5^-a", "a = 4"}, {"0.0016"},
                {"3*4"}, {"12"},
                {"3*4*10"}, {"120"},
                {"3*4*10/10"}, {"12"},
                {"3/4"}, {"0.75"},
                {"3/4^-2"}, {"48"},
                {"-3/-4^-2"}, {"-48"},
                {"-3*a^2", "a = 2"}, {"-12"},
                {"-3*a^2", "a = -2"}, {"-12"},
                {"2,0*a", "a = -2"}, {"-4"},
                {"-a*2.0", "a = -2"}, {"4"},
                {"-a*2*4", "a = -2"}, {"16"},
                {"5-3"}, {"2"},
                {"2-3"}, {"-1"},
                {"-2-3"}, {"-5"},
                {"22"}, {"22"},
                {"2-a", "a = -2"}, {"4"},
                {"3 * a", "a = -2"}, {"-6"},
                {"3 + a", "a = -3"}, {"0"},
                {"3 - a", "a = -3"}, {"6"},
                {" 2-a* 5", "a = -2"}, {"12"},
                {"a*a", "a = 2"}, {"4"},
                {"2-a*a", "a = 2"}, {"-2"},
                {"5^-a", "a = -4"}, {"625"},
                {"a*a", "a = 4"}, {"16"},
                {"a*a", "a = -4"}, {"16"},
                {"-a*a", "a = -4"}, {"-16"},
                {"-a*a", "a = 4"}, {"-16"},
                {"a+5", "a = -4"}, {"1"},
                {"5-a", "a = -4"}, {"9"},
                {"-a+5", "a =-4"}, {"9"},
                {"-a*a", "a = -4"}, {"-16"},
                {"5-a*a", "a = -2"}, {"1"},
                {"a + 55 * a", "a = 10"}, {"560"},
                {"1 + a * 2", "a = 2"}, {"5"},
                {"1 + a * 2 / 2", "a = 2"}, {"3"},
                {"1 + a * 2 / 2 - 1", "a = 2"}, {"2"},
                {"-a+5.0", "a=-2.000"}, {"7"},
                {"dodo-10*3", "dodo=33"}, {"3"},
                {"d-10 + a^2 - b^c + a", "c = -5", "a = 5", "b = -5", "d=33"}, {"53.00032"},
                {"d-10 + a^2 - b^c", "c = -5", "a = 5", "b = -5", "d=33"}, {"48.00032"},
                {"-5-10 + a^2 - b^codi + 10000.44", "codi = 5", "a = 5", "b = 5"}, {"6885.44"},
                {"5-10 + a^2 - b^c", "a = 5", "b = 5", "c = 5"}, {"-3105"},
                {"a2", "a = 5"}, {"10"},
                {"a2+b/2-c", "a = 5", "b = 6", "c = 3"}, {"10"},
                {"1.0 / 0"}, {"∞"},
                {"5 / 0"}, {"∞"},
        };

        for (int i = 0; i < tests.length; i += 2) {
            String res = runProgram(tests[i]);
            if (res != null && res.equals(tests[i + 1][0])) {
                System.out.println("  Pass: " + Arrays.toString(tests[i]) + " Result: " + res);
            } else {
                System.out.println("! FAIL: " + Arrays.toString(tests[i]) +
                        " Expected " + tests[i + 1][0] + " Got: " + res);
                return;
            }
        }

        System.out.println("PASSED ALL TESTS");
    }
}


// edge cases for substitution
// cases: -a+2 || 2-a || 2+a || a+a || 2a
// a+5    // -2+5
// 5-a    // 5--2
// 5^-a    // 5^--2
// -a+5    // --4+5
// -a*a    // --4*-4


// other test cases for future
// String[] go = {"3- -2^-3"};
// "(1 + 3) * 2"
// "(1 + 3) ^ 2"
// "24+(1 + 3) ^ 2"
// "2+ (3 + 5) + (1 + 3) ^ 2"

