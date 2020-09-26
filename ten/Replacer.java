package com.shpp.p2p.cs.ldebryniuk.assignment10;

public class Replacer implements IsDigit {

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
     * The following method substitutes variables with their corresponding values e.g. a+5, a=4 -> 4+5
     */
    public void replaceVars(StringBuilder formula, String[] args) {
        if (args.length > 1) { // if there is at least one variable
            for (int i = 1; i < args.length; i++) {
                String var = args[i]; // example of var -> "a = 5"
                int equalInd = var.indexOf('=');
                String varName = var.substring(0, equalInd);
                String varValue = var.substring(equalInd + 1);

                replaceVar(formula, varName, varValue);
            }
        }
    }

    /**
     * The following method substitutes variable with current name
     */
    private void replaceVar(StringBuilder formula, String varName, String varValue) {
        // cases for a -2:
        // a+5    // -2+5
        // a+a    // -2-2
        // 5-a    // 5+2
        // 5^-a    // 5^2
        // -a+5    // 2+5
        // -a*a    // 2*-2
        // 2a    // 2*-2
        int varIndInFormula;
        // while there is a variable with such name in the formula e.g. "a+a+a"
        while ((varIndInFormula = formula.indexOf(varName)) > -1) {
            int endIndOfVar = varIndInFormula + varName.length();
            String replacement = varValue;

            // prepare for replacement
            if (varIndInFormula > 0 && // if not at index 0 and if digit before var e.g. 2a
                    isDigit(formula.charAt(varIndInFormula - 1))) replacement = "*" + varValue;
            else if (varIndInFormula != (formula.length() - 1) && // if var is not at the end of string
                    isDigit(formula.charAt(varIndInFormula + 1)))
                replacement = varValue + "*"; // if digit is after var e.g. a2 || 33+a2
            else if (varValue.charAt(0) == '-') { // if args var is negative e.g. a=-2
                // a+a || a+5 || 5+a
                // if var in formula is negative 5-a || 5^-a || -a+5 || -a*a
                if (varIndInFormula > 0 && formula.charAt(varIndInFormula - 1) == '-') {
                    varIndInFormula--;
                    replacement = varValue.substring(1); // var: -2 -> 2

                    if (varIndInFormula > 0 && isDigit(formula.charAt(varIndInFormula - 1))) { // 5-a
                        replacement = "+" + replacement;
                    }
                }
            }

            formula.replace(varIndInFormula, endIndOfVar, replacement);
        }
    }

}
