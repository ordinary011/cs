package com.shpp.p2p.cs.ldebryniuk.assignment5;

import com.shpp.cs.a.console.TextProgram;

public class Assignment5Part1 extends TextProgram {
    public void run() {
        runProgram();

//        runTests();
    }

    private void runProgram() {
        /* Repeatedly prompt the user for a word and print out the estimated
         * number of syllables in that word.
         */
        while (true) {
            String word = readLine("Enter a single word: ");
            if (word.equals("q")) break;
            println("  Syllable count: " + syllablesIn(word));
            System.out.println("Please press \"q\" if you would like to stop the program");
        }
    }

    /**
     * Given a word, estimates the number of syllables in that word according to the
     * heuristic specified in the handout.
     *
     * @param word A string containing a single word.
     * @return An estimate of the number of syllables in that word.
     */
    private int syllablesIn(String word) {
        if (word.length() == 0) return 0; // validation for empty strings

        word = word.toLowerCase();
        int vowelCount = 0;
        int indexOfLastChar = word.length() - 1;

        // start iterating through every string character EXCEPT the last one
        for (int i = 0; i < indexOfLastChar; i++) {
            if (isVowel(word.charAt(i))) { // if current character is vowel
                if (!isPreviousCharVowel(word, i - 1)) { // if previous character is not vowel
                    vowelCount++;
                }
            }
        }

        // for the last character we have a bit different logic
        char lastCharInString = word.charAt(indexOfLastChar);
        if (
                isVowel(lastCharInString) &&
                        lastCharInString != 'e' &&
                        !isPreviousCharVowel(word, indexOfLastChar - 1)
        ) {
            vowelCount++;
        }

        return vowelCount == 0 ? 1 : vowelCount; // vowels determine amount of syllables here=))
    }

    private boolean isPreviousCharVowel(String word, int charIndex) {
        if (charIndex < 0) return false; // ensures that Index out of bounds will not occur

        return isVowel(word.charAt(charIndex));
    }

    private boolean isVowel(char strChar) {
        char[] vowels = {'a', 'e', 'i', 'o', 'u', 'y'};

        for (char vowel : vowels) {
            if (strChar == vowel) return true;
        }

        return false;
    }

    private void runTests() {
        check("", 0);
        check("me", 1);
        check("the", 1);
        check("be", 1);
        check("she", 1);
        check("quokka", 2);
        check("springbok", 2);
        check("syllable", 2);
        check("Unity", 3);
        check("Unite", 2);
        check("Growth", 1);
        check("Possibilities", 5);
        check("Nimble", 1);
        check("Me", 1);
        check("Beautiful", 3);
        check("Manatee", 3);
        check("Manatae", 3);
        check("racecar", 3);
        check("kayak", 1);
        check("deified", 2);
        check("shakshuka", 3);
        check("tteokbokki", 3);
        check("Racecar", 3);
        check("KaYAK", 1);
        check("Noon", 1);
        check("aa", 1);
        check("I", 1);
    }

    private void check(String testCase, int expectedResult) {
        if (syllablesIn(testCase) == expectedResult) {
            println("  Pass: " + testCase);
        } else {
            println("! FAIL: " + testCase);
        }
    }
}