package com.shpp.p2p.cs.ldebryniuk.assignment2;

import com.shpp.cs.a.console.TextProgram;

/**
 * This class calculates roots of a quadratic equation
 * <p>
 * used resource https://www.berdov.com/docs/equation/quadratic_equations
 */
public class Assignment2Part1 extends TextProgram {

    /**
     * This is the starting method of the program
     */
    public void run() {
        // getting the first coefficient from a user
        double a = readDouble("Please enter a: ");
        while (a == 0) {
            a = readDouble("a can not be zero. Please enter a: ");
        }

        // getting the rest of the coefficients
        double b = readDouble("Please enter b: ");
        double c = readDouble("Please enter c: ");

        // calculating discriminant
        double DISCRIMINANT = b * b - 4 * a * c;

        // DISCRIMINANT < 0 indicates that there are no real roots
        System.out.println(
                DISCRIMINANT < 0 ? "There are no real roots" : findRoots(a, b, c, DISCRIMINANT)
        );
    }

    /**
     * This method calculates roots of the equation
     *
     * @param a            Is one of the coefficients that are needed for the formula
     * @param b            Is one of the coefficients that are needed for the formula
     * @param c            Is one of the coefficients that are needed for the formula
     * @param DISCRIMINANT Is needed for determining the amount of roots in equation
     * @return String that contains the result
     */
    private String findRoots(double a, double b, double c, double DISCRIMINANT) {
        double firstRoot = (-b + Math.sqrt(DISCRIMINANT)) / (2 * a);

        // DISCRIMINANT == 0 indicates that there is only one root
        if (DISCRIMINANT == 0) {
            return "There is one root: " + firstRoot;
        } else {
            // DISCRIMINANT > 0 indicates that there are two roots
            double secondRoot = (-b - Math.sqrt(DISCRIMINANT)) / (2 * a);

            return "There are two roots: " + firstRoot + " and " + secondRoot;
        }
    }

}
