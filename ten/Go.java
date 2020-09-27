package com.shpp.p2p.cs.ldebryniuk.assignment10;

public class Go implements Aux {
    private final OperationManager op = new OperationManager();
//    private final FunctionManager func = new FunctionManager();

    public String goo(StringBuilder formula) {
        goFunctions(formula);

        calcInParentheses(formula);

        op.powerUp(formula);
        op.doOneOfFourOperations(formula, "*", "/");
        op.doOneOfFourOperations(formula, "+", "-");

        return formula.toString();
    }

    private void calcInParentheses(StringBuilder formula) {
        int openParenthesesInd;
        while ((openParenthesesInd = formula.indexOf("(")) != -1) {
            int chNumTillClosing = findChNumTillClosing(formula.substring(openParenthesesInd));
            int closeParenthesesInd = openParenthesesInd + chNumTillClosing;
            String withinParentheses = formula.substring(openParenthesesInd + 1, closeParenthesesInd);

            String res = goo(new StringBuilder(withinParentheses));
            formula.replace(openParenthesesInd, closeParenthesesInd + 1, res);
        }
    }

    private final String[] functions = {"sin", "cos", "tan", "atan", "log10", "log2", "sqrt"};

    /**
     * The following method
     */
    public void goFunctions(StringBuilder formula) {
        for (String func : functions) {
            int indOfFunc;
            while ((indOfFunc = formula.indexOf(func)) > -1) { // while there is at least one func left
                int openParenthesesInd = formula.indexOf("(", indOfFunc);
                int chNumTillClosing = findChNumTillClosing(formula.substring(openParenthesesInd));
                int closeParenthesesInd = openParenthesesInd + chNumTillClosing;
                String withinParentheses = formula.substring(openParenthesesInd + 1, closeParenthesesInd);

                String res = goo(new StringBuilder(withinParentheses));
                res = calcFunc(func, res);
                formula.replace(indOfFunc, closeParenthesesInd + 1, res);
            }
        }
    }

    /**
     * The following method
     */
    private String calcFunc(String func, String value) {
        double res = -1;

        double val = Double.parseDouble(value);

        switch (func) {
            case "sin":
                res = Math.sin(val);
                break;
            case "cos":
                res = Math.cos(val);
                break;
            case "tan":
                res = Math.tan(val);
                break;
            case "atan":
                res = Math.atan(val);
                break;
            case "sqrt":
                res = Math.sqrt(val);
                break;
            case "log2":
                res = Math.log(val);
                break;
            case "log10":
                res = Math.log10(val);
                break;
            default:
                System.out.println("unknown function please check your formula");
        }

        return String.valueOf(res);
    }
}
