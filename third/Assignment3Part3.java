package com.shpp.p2p.cs.ldebryniuk.assignment2;

import com.shpp.cs.a.console.TextProgram;

/*
 * The following class calculates power of the number
 */
public class Assignment3Part3 extends TextProgram {

    /**
     * This is the starting method of the program
     */
    public void run() {
        raiseToPower(2.0, 3);
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