package com.shpp.p2p.cs.ldebryniuk.assignment3;

import com.shpp.cs.a.console.TextProgram;

/**
 * This class resolves The Hailstone Sequence of Douglas Hofstadter
 */
public class Assignment3Part2 extends TextProgram {

    /**
     * This is the starting method of the program
     */
    public void run() {
        int n = getInputFromUser();

        calculateHailstoneSequence(n);
    }

    /**
     * The following method is used for receiving the input number from a user
     *
     * @return the input number
     */
    private int getInputFromUser() {
        int n = 1;

        try {
            n = readInt("Enter a number: "); // getting input from a user
            while (n < 1) { // there is no point in calculations if n < 1
                n = readInt("input number can not be less than 1, Enter a number: ");
            }
        } catch (Exception e) {
            System.out.println("sorry wrong input");
            e.printStackTrace();
        }

        return n;
    }

    /**
     * The following method calculates The Hailstone Sequence of Douglas Hofstadter
     *
     * @param n represents input number received from the user
     */
    private void calculateHailstoneSequence(int n) {
        // if n = 1 all the calculations should be finished
        while (n > 1) {
            if (n % 2 == 0) {
                // logic for even numbers
                System.out.print(n + " is even so I take half: ");
                n /= 2;
            } else {
                // logic for odd numbers
                System.out.print(n + " is odd so I make 3n + 1: ");
                n = n * 3 + 1;
            }
            System.out.println(n);
        }
        System.out.println("The end");
    }

}