package com.shpp.p2p.cs.ldebryniuk.assignment14;

public class Assignment14Part1 {

    public static void main(String[] args) {
//        if (args.length == 0) {
//
//        }

//        args = new String[]{"biggest.jpg", "test.par"};
//        new Compressor().compressFile(args[0], args[1]);

        args = new String[]{"test.par", "res.jpg"};
        new Decompressor().decompressFile(args[0], args[1]);
    }


}
