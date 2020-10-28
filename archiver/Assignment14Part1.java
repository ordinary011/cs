package com.shpp.p2p.cs.ldebryniuk.assignment14;


public class Assignment14Part1 {

    public static void main(String[] args) {
//        args = new String[]{"test5.png", "test.par"};
//        args = new String[]{"test.txt", "test.par"};
        args = new String[]{"test.par", "test.txt"};

//        if (args.length < 2) {
//        if (args.length == 0) {
//            new Compressor().compress(args[0], args[1]);
//        }
//        else {
            new Decompressor().decompress(args[0], args[1]);
//        }
    }


}
