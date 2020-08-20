package com.shpp.p2p.cs.ldebryniuk.assignment5;

import com.shpp.cs.a.console.TextProgram;

/*
 * The following class is able to add really large numbers together
 *
 * used resource: https://docs.oracle.com/javase/tutorial/java/data/converting.html
 */
public class Assignment5Part2 extends TextProgram {

    /* This is the starting method of the program */
    public void run() {
//        runProgram();

        runTests();

//        addNumericStrings("44", "7");
    }

    /**
     * The following method receives input numbers from user and prints out their sum
     */
    private void runProgram() {
        /* Sit in a loop, reading numbers and adding them. */
        while (true) {
            String n1 = readLine("Enter first number: ");
            if (n1.equals("q")) break;

            String n2 = readLine("Enter second number: ");
            if (n2.equals("q")) break;

            println(n1 + " + " + n2 + " = " + addNumericStrings(n1, n2));
            println("Please press \"q\" if you would like to stop ");
        }
    }

    /**
     * Given two string representations of non negative integers, adds the
     * numbers represented by those strings and returns the result.
     *
     * @param n1 The first number.
     * @param n2 The second number.
     * @return A String representation of n1 + n2
     */
    private String addNumericStrings(String n1, String n2) {
        StringBuilder res = new StringBuilder();
        int remainderOfPreviousNum = 0;

        if (n1.length() > n2.length()) { // determining smaller number
            n2 = addZerosToSmallerNum(n2, (n1.length() - n2.length()));
        } else {
            n1 = addZerosToSmallerNum(n1, (n2.length() - n1.length()));
        }

        // start iterating the string from the end to start
        for (int i = n1.length() - 1; i > -1; i--) {
            // converting from chars to integers
            int firstNum = n1.charAt(i) - '0';
            int secondNum = n2.charAt(i) - '0';
            int sum = firstNum + secondNum + remainderOfPreviousNum;

            remainderOfPreviousNum = sum / 10; // if sum > 10 we'll get 1

            res.insert(0, sum % 10);
        }

        if (remainderOfPreviousNum == 1) res.insert(0, 1);

        return res.toString();
    }

    /**
     * The following method adds zeroes to a smaller number
     *
     * @param smallerNum one of two input numbers
     * @param numOfZerosToAdd difference in length between number strings
     * @return number with zeroes in front of it
     */
    private String addZerosToSmallerNum(String smallerNum, int numOfZerosToAdd) {
        return "0".repeat(numOfZerosToAdd) + smallerNum;
    }

    /**
     * The following method tests different cases for our app
     */
    private void runTests() {
        check("7", "7", "14");
        check("77", "77", "154");
        check("777", "777", "1554");
        check("7777", "7777", "15554");
        check("999", "999", "1998");
        check("123", "123", "246");
        check("80", "80", "160");
        check("800", "800", "1600");
        check("800", "80", "880");
        check("1234567890", "1234567890", "2469135780");
        check("6993309969", "43252003274489856000", "43252003281483165969");
    }

    /**
     * The following prints positive or negative results of test
     *
     * @param n1 first input number from a user
     * @param n2 second input number from a user
     * @param expectedResult sum of numbers
     */
    private void check(String n1, String n2, String expectedResult) {
        String result = addNumericStrings(n1, n2);
        if (result.equals(expectedResult)) {
            System.out.printf("  Pass for n1: %s and n2: %s \n", n1, n2);
        } else {
            System.out.printf("! FAIL for n1: %s and n2: %s; Result: %s \n", n1, n2, result);
        }
    }

}
