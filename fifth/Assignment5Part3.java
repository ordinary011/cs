package com.shpp.p2p.cs.ldebryniuk.assignment5;

import com.shpp.cs.a.console.TextProgram;

import java.io.BufferedReader;
import java.io.FileReader;

/**
 * The following class is an inplementation of a road game.
 * The following program receives three letters from a user
 * and prints the appropriate word from the dictionary
 */
public class Assignment5Part3 extends TextProgram {

    /* This is the starting method of the program */
    public void run() {
        runProgram();

//        runTests();
    }

    /**
     * The following method receives three letters from a user
     * and prints the appropriate word from the dictionary
     */
    private void runProgram() {
        /* Repeatedly prompt the user for a word and print out the estimated
         * number of syllables in that word.
         */
        while (true) {
            String word = readLine("Enter three letters please: ");
            if (word.equals("q")) break;
            System.out.printf("match for \"%s'\" is: %s. ", word, findMatchForThePattern(word));
            System.out.println("Please press \"q\" if you would like to stop the program");
        }
    }

    /**
     * The following method searches for a word that can be composed from three input letters
     *
     * @param threeLetters input letters from a user
     * @return word from the dictionary
     */
    private String findMatchForThePattern(String threeLetters) {
        String fileLocation = "src/com/shpp/p2p/cs/ldebryniuk/assignment5/en-dictionary.txt";
        threeLetters = threeLetters.toLowerCase();
        String word = null;

        // open file for reading
        try (BufferedReader br = new BufferedReader(new FileReader(fileLocation))) {
            boolean wordFound = false;

            // each iteration is a new word in the dictionary
            while (!wordFound) {
                word = br.readLine();
                if (word == null) break; // if end of file

                wordFound = isWordFound(word, threeLetters);
            }
        } catch (Exception e) {
            println(":-( exception occurred");
            e.printStackTrace();
        }

        return (word == null) ? "sorry could not find any match" : word;
    }

    /**
     * The following method determines if the word from dictionary can be suitable for our input letters
     *
     * @param wordInDictionary string that is retreived from a file
     * @param threeLetters input letters from user
     * @return does word contain all the letters in it?
     */
    private boolean isWordFound(String wordInDictionary, String threeLetters) {
        // each iteration is one of three input letters
        for (char letter : threeLetters.toCharArray()) {
            int indexOfLetter = wordInDictionary.indexOf(letter);

            if (indexOfLetter > -1) {
                wordInDictionary = wordInDictionary.substring(indexOfLetter + 1);
            } else {
                return false;
            }
        }

        return true;
    }

    /**
     * The following method tests different cases for our app
     */
    private void runTests() {
        check("KDD", "acknowledged");
        check("NPT", "anapaest");
        check("FTW", "afterglow");
        check("NNN", "abandoning");
        check("QQQ", "sorry could not find any match");
        check("XCX", "executrix");
        check("I", "aahing");
        check("ACNW", "acknowledge");
    }

    /**
     * The following method prints positive or negative results of test
     *
     * @param testCase three letters that we get from the user
     * @param expectedResult word from the dictionary
     */
    private void check(String testCase, String expectedResult) {
        if (findMatchForThePattern(testCase).equals(expectedResult)) {
            println("  Pass: " + testCase);
        } else {
            println("! FAIL: " + testCase);
        }
    }
}
