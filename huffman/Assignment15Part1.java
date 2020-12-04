package com.shpp.p2p.cs.ldebryniuk.assignment15;

public class Assignment15Part1 {

    /**
     * @param args possible user inputs below:
     *             {"test3.txt"},
     *             {"test3.txt", "archived_poem.par"}
     *             {"-a" "test3.txt", "archived_poem_poem.par"}
     *             {"-u" "test3.txt", "archived_poem_poem.par"}
     */
    public static void main(String[] args) {
        new HuffmanArchiver().determineOperation(args);
    }
}
