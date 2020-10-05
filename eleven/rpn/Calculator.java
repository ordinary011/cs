package com.shpp.p2p.cs.ldebryniuk.assignment11.rpn;

/**
 * The following class is the main class of the calculator app. It comprises all the necessary logic
 */
public class Calculator {

    private final VarHandler vh = VarHandler.getInstance();
    private final Kernel kernel = new Kernel();

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
     * The following method is the starting point of our application
     */
    public String runCalc(String[] args) {
        formatArgs(args);
        String formula = args[0];

        boolean setSignOfRes = formula.startsWith("-("); // e.g. -(3-6)

        try {
            vh.parseVars(args);
            kernel.parseFormula(formula);

            String res = kernel.calcRestOfStack();

            if (setSignOfRes) {
                if (res.charAt(0) == '-') res = res.substring(1); // e.g. -(3-7), res=-4 -> -(-4) -> 4
                else res = "-" + res; // e. g. -(33+53), res=86 -> -(86) -> -86
            }

            return String.format("%.5f", Double.parseDouble(res));
        } catch (Exception e) {
            System.out.println("Input mistake. Please check your formula and variables");
        }

        return null;
    }


}
