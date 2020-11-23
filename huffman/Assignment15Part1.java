package com.shpp.p2p.cs.ldebryniuk.assignment15;


public class Assignment15Part1 {

    /**
     * @param args possible user inputs:
     *
     * {"test3.txt"},
     * {"test3.txt", "arvhied_poem.par"}
     * {"-a" "test3.txt", "arvhied_poem.par"}
     * {"-u" "test3.txt", "arvhied_poem.par"}
     */
    public static void main(String[] args) {
//        args = new String[]{"test5.txt", "test5.txt.par"};
//        new HuffmanArchiver().determineOperation(args);
        args = new String[]{"test5.txt.par", "res.txt"};
        new HuffmanArchiver().determineOperation(args);
    }
}
