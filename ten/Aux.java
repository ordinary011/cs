package com.shpp.p2p.cs.ldebryniuk.assignment10;

public class Aux {

    public static Aux instance = null;

    private Aux() {}

    public static Aux getInstance() {
        if (instance == null) {
            instance = new Aux();
        }

        return instance;
    }

    public boolean isDigit(char ch) {
        return String.valueOf(ch).matches("\\d");
    }

}
