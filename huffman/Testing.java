package com.shpp.p2p.cs.ldebryniuk.assignment15;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class Testing {

//    public static void main(String[] args) {
////        int ff = -2;
////        ff >>>= 7;
////        System.out.println(ff);
//
//        try (FileOutputStream writeStream = new FileOutputStream("test3.txt")) {
//            for (int i = 0; i < 250; i++) {
//                writeStream.write(i);
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

// top        2779
//            4202

    public static void main(String[] args) {
//        args = new String[]{"test5.txt", "test5.txt.par"};
//        new HuffmanArchiver().determineOperation(args);
//        args = new String[]{"test5.txt.par", "test5_res.txt"};
//        new HuffmanArchiver().determineOperation(args);

//        emptyTest(args);
        test1(args);
//        test2(args);

    }

    private static void test2(String[] args) {
        args = new String[]{"test9.mp4", "test9.mp4.par"};
        new HuffmanArchiver().determineOperation(args);
        args = new String[]{"test9.mp4.par", "test9_res.mp4"};
        new HuffmanArchiver().determineOperation(args);
    }

    private static void emptyTest(String[] args) {
        args = new String[]{"empty.txt", "empty.txt.par"};
        new HuffmanArchiver().determineOperation(args);
        args = new String[]{"empty.txt.par", "empty_res.txt"};
        new HuffmanArchiver().determineOperation(args);
    }

    private static void test1(String[] args) {
        args = new String[]{"test5.txt", "test5.txt.par"};
        new HuffmanArchiver().determineOperation(args);
        args = new String[]{"test5.txt.par", "test5_res.txt"};
        new HuffmanArchiver().determineOperation(args);

        args = new String[]{"test2.txt", "test2.txt.par"};
        new HuffmanArchiver().determineOperation(args);
        args = new String[]{"test2.txt.par", "test2_res.txt"};
        new HuffmanArchiver().determineOperation(args);

        args = new String[]{"test4.txt", "test4.txt.par"};
        new HuffmanArchiver().determineOperation(args);
        args = new String[]{"test4.txt.par", "test4_res.txt"};
        new HuffmanArchiver().determineOperation(args);

        args = new String[]{"big.txt", "big.txt.par"};
        new HuffmanArchiver().determineOperation(args);
        args = new String[]{"big.txt.par", "big_res.txt"};
        new HuffmanArchiver().determineOperation(args);

        args = new String[]{"test3.txt", "test3.txt.par"};
        new HuffmanArchiver().determineOperation(args);
        args = new String[]{"test3.txt.par", "test3_res.txt"};
        new HuffmanArchiver().determineOperation(args);

        args = new String[]{"test7.png", "test7.png.par"};
        new HuffmanArchiver().determineOperation(args);
        args = new String[]{"test7.png.par", "test7_res.png"};
        new HuffmanArchiver().determineOperation(args);

        args = new String[]{"test8.png", "test8.png.par"};
        new HuffmanArchiver().determineOperation(args);
        args = new String[]{"test8.png.par", "test8_res.png"};
        new HuffmanArchiver().determineOperation(args);

        args = new String[]{"biggest.jpg", "biggest.jpg.par"};
        new HuffmanArchiver().determineOperation(args);
        args = new String[]{"biggest.jpg.par", "biggest_res.jpg"};
        new HuffmanArchiver().determineOperation(args);

        args = new String[]{"test6.pdf", "test6.pdf.par"};
        new HuffmanArchiver().determineOperation(args);
        args = new String[]{"test6.pdf.par", "test6_res.pdf"};
        new HuffmanArchiver().determineOperation(args);
    }
}



