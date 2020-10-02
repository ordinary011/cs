package com.shpp.p2p.cs.ldebryniuk.assignment11.breplacer;

/**
 * The following class contains logic for replacing variables in the formula.
 * It also contains one formatting method that removes redundant symbols
 */
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
    public void replaceVars(StringBuilder formula, String[] args) throws Exception {
        if (args.length > 1) { // if there is at least one variable
            for (int i = 1; i < args.length; i++) {
                String var = args[i]; // example of var -> "a=5"
                int equalInd = var.indexOf('=');
                String varName = var.substring(0, equalInd);
                String varValue = var.substring(equalInd + 1);

                if (varValue.contains(varName)) { // if there was a mistake in input var e.g. {"1-2*a+3", "a=a=-5"}
                    System.out.println("your variable can't contain itself: " + var);
                    throw new Exception();
                }

                replaceVar(formula, varName, varValue);
            }
        }
    }

    /**
     * The following method substitutes variable with current name
     */
    private void replaceVar(StringBuilder formula, String varName, String varValue) {
        int varIndInFormula;
        // while there is a variable with such name in the formula e.g. "a+a+a"
        while ((varIndInFormula = formula.indexOf(varName)) > -1) {
            int endIndOfVar = varIndInFormula + varName.length();
            String replacement = varValue;

            // prepare for replacement
            if (varIndInFormula > 0 && // if not at index 0 and if digit before var e.g. 2a
                    isDigit(formula.charAt(varIndInFormula - 1))) replacement = "*" + varValue;

            else if (varIndInFormula != (formula.length() - 1) && // if var is not at the end of string
                    isDigit(formula.charAt(varIndInFormula + 1))) // if digit is after var e.g. a2 || 33+a2
                replacement = varValue + "*";

            else if (varValue.charAt(0) == '-' && // if args var is negative e.g. a=-2
                    varIndInFormula > 0 && // if var in formula is negative
                    formula.charAt(varIndInFormula - 1) == '-') // e.g. 5-a || 5^-a || -a+5
            {
                varIndInFormula--; // now index points to the "-" before the var name in the formula e.g. 5^-a
                replacement = varValue.substring(1); // var: -2 -> 2

                if (varIndInFormula > 0 // after varIndInFormula--; it could become 0  e.g. -a+5
                        && isDigit(formula.charAt(varIndInFormula - 1))) { // 5-a
                    replacement = "+" + replacement; // ["5-a" "a=-2"] -> 5+2
                }
            }

            formula.replace(varIndInFormula, endIndOfVar, replacement);
        }
    }

    /**
     * The following method adds multiplication sign if it's needed. e.g. 2(5+5) -> 2*(5+5) || (3+4)(2+2) -> (3+4)*(2+2)
     */
    public void addMultiplicationSign(StringBuilder formula) {
        for (int i = 1; i < formula.length() - 1; i++) {
            if (formula.charAt(i) == '(' && // if char is "("
                    (isDigit(formula.charAt(i - 1)) || // e.g. 2(5+5)
                            formula.charAt(i - 1) == ')') // e.g. (3+4)(2+2)
            ) {
                formula.insert(i, '*');
                continue;
            }

            if (formula.charAt(i) == ')' && // if char is ")"
                    (isDigit(formula.charAt(i + 1)) || // e.g. (5+5)2
                            formula.charAt(i + 1) == '(') // e.g. (3+4)(2+2)
            ) {
                formula.insert(i + 1, '*');
            }
        }
    }
}

// cases for replacement when a=-2:
// a+5    // -2+5
// a+a    // -2-2
// 5-a    // 5+2
// 5^-a    // 5^2
// -a+5    // 2+5
// -a*a    // 2*-2
// 2a    // 2*-2
