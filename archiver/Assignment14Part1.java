package com.shpp.p2p.cs.ldebryniuk.assignment14;

public class Assignment14Part1 {

    public static void main(String[] args) {
//        args = new String[]{"test3.txt"};
//        args = new String[]{"test3.txt.par"};
        args = new String[]{"test3.txt", "arvhied_poem.par"};
//        args = new String[]{"biggest.jpg.par", "res.jpg"};
        new Archiver().determineOperation(args);
    }
}
