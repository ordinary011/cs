package com.shpp.p2p.cs.ldebryniuk.assignment5;

import com.shpp.cs.a.console.TextProgram;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

/**
 * The following class is an inplementation of a road game.
 * The following program receives three letters from a user
 * and prints the appropriate word from the dictionary
 *
 * used sources: "https://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html"
 */
public class Assignment5Part3 extends TextProgram {

    /* This is the starting method of the program */
    public void run() {
        runProgram();
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
            System.out.print("Matches for \"" + word + "\" are: ");
            System.out.println(findMatchForThePattern(word));
            System.out.println("Please press \"q\" if you would like to stop the program");
        }
    }

    /**
     * The following method searches for a word that can be composed from three input letters
     *
     * @param threeLetters input letters from a user
     * @return word from the dictionary
     */
    private ArrayList<String> findMatchForThePattern(String threeLetters) {
        final String fileLocation = "src/com/shpp/p2p/cs/ldebryniuk/assignment5/en-dictionary.txt";
        final ArrayList<String> res = new ArrayList<>();

        // open file for reading
        try (BufferedReader br = new BufferedReader(new FileReader(fileLocation))) {
            threeLetters = threeLetters.toLowerCase();
            String word;

            // each iteration is a new word in the dictionary
            while ((word = br.readLine()) != null) { // while not end of file
                if (isWordFound(word, threeLetters)) {
                    res.add(word);
                }
            }
        } catch (Exception e) {
            println(":-( exception occurred");
            e.printStackTrace();
        }

        return res;
    }

    /**
     * The following method determines if the word from dictionary can be suitable for our input letters
     *
     * @param wordInDictionary string that is retreived from a file
     * @param threeLetters input letters from user
     * @return does word contain all the letters in it?
     */
    private boolean isWordFound(String wordInDictionary, String threeLetters) {
        int indexOfLetter = -1;

        // each iteration is one of three input letters
        for (char letter : threeLetters.toCharArray()) {
            indexOfLetter = wordInDictionary.indexOf(letter, ++indexOfLetter);

            if (indexOfLetter == -1) return false;
        }

        return true;
    }
}
