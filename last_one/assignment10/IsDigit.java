package com.shpp.p2p.cs.ldebryniuk.assignment10;

/**
 * The following interface was created for a single method that determines if char is a digit
 */
public interface IsDigit {
    default boolean isDigit(char ch) {
        return Character.isDigit(ch);
    }
}
