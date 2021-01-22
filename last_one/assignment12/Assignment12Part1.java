package com.shpp.p2p.cs.ldebryniuk.assignment12;

/**
 * The following class is a starting point of our program
 */
public class Assignment12Part1 {

    /**
     * @param args takes in the path to image as the first argument
     */
    public static void main(String[] args) {
        new SilhouetteCounter().countSilhouettes(args.length == 0 ? "test.jpg" : args[0]);
    }

}
