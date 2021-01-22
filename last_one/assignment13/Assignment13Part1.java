package com.shpp.p2p.cs.ldebryniuk.assignment13;

/**
 * The following class is a starting point of our program
 */
public class Assignment13Part1 {

    /**
     * @param args takes in the path to image as the first argument
     */
    public static void main(String[] args) {
        new SilhouetteCounter().countSilhouettes(args.length == 0 ? "test.jpg" : args[0]);
    }
}
