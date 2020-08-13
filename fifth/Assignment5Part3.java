package com.shpp.p2p.cs.ldebryniuk.assignment5;

import com.shpp.cs.a.console.TextProgram;

import java.io.BufferedReader;
import java.io.FileReader;

public class Assignment5Part3 extends TextProgram {
    public void run() {
        runProgram();

//        runTests();
    }

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
            println(":-( exception occured");
            e.printStackTrace();
        }

        return (word == null) ? "sorry could not find any match" : word;
    }

    private boolean isWordFound(String wordInDictionary, String threeLetters) {
        // each iteration is one of three input letters
        for (char letter : threeLetters.toCharArray()) {
            int indexOftLetter = wordInDictionary.indexOf(letter);

            if (indexOftLetter > -1) {
                wordInDictionary = wordInDictionary.substring(indexOftLetter + 1);
            } else {
                return false;
            }
        }

        return true;
    }

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

    private void check(String testCase, String expectedResult) {
        if (findMatchForThePattern(testCase).equals(expectedResult)) {
            println("  Pass: " + testCase);
        } else {
            println("! FAIL: " + testCase);
        }
    }
}
