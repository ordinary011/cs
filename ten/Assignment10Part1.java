package com.shpp.p2p.cs.ldebryniuk.assignment10;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * ysed resources:
 * https://stackoverflow.com/questions/4194310/can-java-string-indexof-handle-a-regular-expression-as-a-parameter
 * https://docs.oracle.com/javase/7/docs/api/java/util/regex/Pattern.html
 */
public class Assignment10Part1 {

    public static void main(String[] args) {
        if (args.length > 0) calculate(args);
        else System.out.println("sorry no arguments");
    }

    private static void calculate(String[] args) {
        removeAllSpaces(args);

        String formula = args[0];
        StringBuilder formulaBld = new StringBuilder(args[0]);
//        String[] operations = {"\\^", "\\*", "\\/", "\\"};
        // "1+3^2"
        // "3^2"

//        while (findOperationIndex(formula, "\\p{Punct}") > -1) { // till there is
//
//        }

        String operation = "\\^";
        int operationInd = findOperationIndex(formula, operation);
        if (operationInd > -1) {
            int indOfOperationBefore = findOperationIndex(formula.substring(0, operationInd), "\\p{Punct}");
            int indOfOperationAfter = findOperationIndex(formula.substring(operationInd + 1), "\\p{Punct}");
            int indOfDigitBeforeOperation = indOfOperationBefore + 1;

            // if this is the last operation in the formula
            if (indOfOperationAfter == -1) indOfOperationAfter = formula.length();

            String expression = formula.substring(indOfDigitBeforeOperation, indOfOperationAfter);
            double result = doArithmetic(expression, operation);

            formulaBld.replace(indOfDigitBeforeOperation, indOfOperationAfter, String.valueOf(result));
        }
        System.out.println(formulaBld);
    }

    private static void removeAllSpaces(String[] args) {
        for (int i = 0; i < args.length; i++) {
            args[i] = args[i].replaceAll(" ", "");
        }
    }

    private static double doArithmetic(String formula, String operation) {
        int operationIndex = findOperationIndex(formula, operation);
        double res = -1;

        double firstNum = Double.parseDouble(formula.substring(0, operationIndex));
        double secondNum = Double.parseDouble(formula.substring(operationIndex + 1));

        switch (operation) {
            case "\\+" -> res = firstNum + secondNum;
            case "\\-" -> res = firstNum - secondNum;
            case "\\*" -> res = firstNum * secondNum;
            case "\\/" -> res = firstNum / secondNum;
            case "\\^" -> res = Math.pow(firstNum, secondNum);
            default -> System.out.println("unknown operation");
        }

        return res;
    }

    private static int findOperationIndex(String expression, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(expression);

        if (matcher.find()) return matcher.start(); // index of operation
        else return -1;
    }
}

// "1.0 + 2"
// "1 + 3 ^ 2"
// "1 + 3^2 + 2^3"
// "(1 + 3) ^ 2"
// "1 + 3 * 2"
// "1 + a * 2"