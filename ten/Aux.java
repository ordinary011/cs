package com.shpp.p2p.cs.ldebryniuk.assignment10;

/**
 * The following interface was created for a single method that determines if char is a digit
 */
public interface Aux {
    default boolean isDigit(char ch) {
        return Character.isDigit(ch);
    }

    default int findChNumTillClosing(String parentheses) { // e.g. 3-(8-(2+2))+33 -> (8-(2+2))+33
        int needToClose = 0;

        for (int i = 0; i < parentheses.length(); i++) {
            char ch = parentheses.charAt(i);
            if (ch == '(') needToClose++;
            else if (ch == ')') needToClose--;

            if (needToClose == 0) return i; // amount of characters from opening to the closing parantheses
        }

        // if no index was found
        System.out.println("There is a mistake in the formula. You forgot to close your parantheses");
        System.exit(2);

        return -1;
    }
}
