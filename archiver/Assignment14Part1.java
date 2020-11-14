package com.shpp.p2p.cs.ldebryniuk.assignment14;

public class Assignment14Part1 {

    /**
     * @param args possible user inputs:
     *
     * {"test3.txt"},
     * {"test3.txt", "arvhied_poem.par"}
     * {"-a" "test3.txt", "arvhied_poem.par"}
     * {"-u" "test3.txt", "arvhied_poem.par"}
     */
    public static void main(String[] args) {
//        args = new String[]{"biggest.jpg", "biggest.jpg.par"};
        args = new String[]{"biggest.jpg.par", "res.jpg"};
        new Archiver().determineOperation(args);
    }
}
