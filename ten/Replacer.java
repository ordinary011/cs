package com.shpp.p2p.cs.ldebryniuk.assignment10;

public class Replacer {
    private final Aux aux = Aux.getInstance();

    /**
     * The following method prepares args for further actions.
     * Removes redundant spaces and replaces comas with dots
     */
    public void formatArgs(String[] args) {
        for (int i = 0; i < args.length; i++) {
            args[i] = args[i].replaceAll(" ", "");
            args[i] = args[i].replaceAll(",", ".");
        }
    }

    /**
     * The following method substitutes variables with their corresponding values e.g. a+5, a=4 -> 4+5
     */
    public void substituteVariables(StringBuilder formula, String[] args) {
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
    private void substituteVarsWithCurrentName(StringBuilder formula, String varName, String varValue) {
        // cases for a -2:
        // 5-a    // 5+2
        // a+5    // -2+5
        // 5^-a    // 5^--2
        // -a+5    // --4+5
        // -a*a    // --4*-4
        // a+a    // -4
        // 2a    //
        int startIndOfVarInFormula;
        // while there is a variable with this name in the formula
        while ((startIndOfVarInFormula = formula.indexOf(varName)) > -1) {
            int endIndOfVar = startIndOfVarInFormula + varName.length();

//            // if var in formula is negative
//            if (startIndOfVarInFormula > 0 && formula.charAt(startIndOfVarInFormula - 1) == '-') {
//                startIndOfVarInFormula--;
//                if (varValue.charAt(0) == '-') { // if args var is negative   // 5-a || 5^-a
//
//                }
//            }
            varValue = addMultiplicationSignIfNeeded(formula, startIndOfVarInFormula, varValue);

            formula.replace(startIndOfVarInFormula, endIndOfVar, varValue);
        }
    }

    /**
     * The following method adds multiplication sings if they are needed. e.g. [a2, a=2] => 2*2
     */
    private String addMultiplicationSignIfNeeded(StringBuilder formula, int startIndOfVarInFormula, String varValue) {
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
     * The following method replaces "--" with "+"
     */
    private void replaceDoubleMinuses(StringBuilder formula) {
        for (int i = 0; i < formula.length(); i++) {
            if (formula.charAt(i) == '-' && i > 0 && formula.charAt(i - 1) == '-') {
                // e.g. 5--2 -> 5+2 || --2+5 -> +2+5 || 5^--2 -> 5^+2 || --4*-4 -> +4*-4
                formula.replace(i - 1, i + 1, "+");
            }
        }
    }

    /**
     * The following method deletes redundant "+" signs
     */
    private void deleteRedundantPluses(StringBuilder formula) {
        if (formula.charAt(0) == '+') formula.deleteCharAt(0); // e.g. +2+5 -> 2+5
        for (int i = 1; i < formula.length(); i++) {
            if (formula.charAt(i) == '+' && String.valueOf(formula.charAt(i - 1)).matches("[\\^*/]")) {
                formula.deleteCharAt(i); // e. g. 5^+2 -> 5^2
            }
        }
    }
}
