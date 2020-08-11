package com.shpp.p2p.cs.ldebryniuk.assignment5;

import com.shpp.cs.a.console.TextProgram;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Assignment5Part3 extends TextProgram {
    public void run() {
//        runProgram();

//        runTests();


        try {
            go("K");
//            go("KDD");
        } catch (IOException e) {
            println(":-(");
            e.printStackTrace();
        }
    }

    private void runProgram() {
        /* Repeatedly prompt the user for a word and print out the estimated
         * number of syllables in that word.
         */
        while (true) {
            String word = readLine("Enter three letters: ");
            if (word.equals("q")) break;
//            println("  Syllable count: " + syllablesIn(word));
            System.out.println("Please press \"q\" if you would like to stop the program");
        }
    }

    private void go(String threeLetters) throws IOException {
        threeLetters = threeLetters.toLowerCase();
        String fileLocation = "src/com/shpp/p2p/cs/ldebryniuk/assignment5/en-dictionary.txt";
        /* Open the file for reading. */
        BufferedReader br = new BufferedReader(new FileReader(fileLocation));

        boolean wordFound = false;
        while (!wordFound) {
            wordFound = isWordFound(br.readLine(), threeLetters);
        }

        System.out.println(
                br.readLine()
        );
    }

    private boolean isWordFound(String readLine, String threeLetters) {
        

        return true;
    }

}
