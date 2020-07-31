package com.shpp.p2p.cs.ldebryniuk.assignment3;

import com.shpp.cs.a.console.TextProgram;

/*
 * The following class calculates power of the number
 */
public class Assignment3Part3 extends TextProgram {

    /**
     * This is the starting method of the program
     */
    public void run() {
        getInputFromUserAndRaiseToPower();
    }

    /**
     * The following method receives input from user and calls raiseToPower()
     */
    private void getInputFromUserAndRaiseToPower() {
        double base = 0;
        int exponent = 0;

        try {
            base = readDouble("Please enter base: ");

            exponent = readInt("Please enter exponent: ");
        } catch (Exception e) {
            System.out.println("sorry wrong input");
            e.printStackTrace();
        }

        raiseToPower(base, exponent);
    }

    /**
     * The following method calculates power of the number
     *
     * @param base     number to find power of
     * @param exponent of the first parameter
     */
    private void raiseToPower(double base, int exponent) {
        double res = 1;

        // making exponent positive for our loop iterations
        int positiveExponent = exponent < 0 ? -exponent : exponent;

        for (int i = 0; i < positiveExponent; i++) {
            res *= base;
        }

        System.out.println(
                exponent < 0 ? (1 / res) : res
        );
    }

}