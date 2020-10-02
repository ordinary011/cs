package com.shpp.p2p.cs.ldebryniuk.assignment11.breplacer;

/**
 * The following class is the main class of the calculator app. It comprises all the necessary logic
 */
public class Calc {

    private final Replacer replacer = new Replacer();
    private final Kernel kernel = new Kernel();

    /**
     * The following method is the starting point of the calculator. Operations are performed in the prioritized order
     */
    public String runCalc(String[] args) {
        replacer.formatArgs(args);
        StringBuilder formula = new StringBuilder(args[0]);

        try {
            replacer.replaceVars(formula, args);
            replacer.addMultiplicationSign(formula);

            kernel.calculate(formula);
        } catch (Exception e) {
            System.out.println("Input mistake. Please check your formula and variables");
            return formula.toString();
        }

        return formula.toString();
    }
}
