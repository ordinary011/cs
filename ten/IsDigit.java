package com.shpp.p2p.cs.ldebryniuk.assignment10;

/**
 *
 */
public interface IsDigit {
    public default boolean isDigit(char ch) {
        return Character.isDigit(ch);
    }
}
