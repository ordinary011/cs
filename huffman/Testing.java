package com.shpp.p2p.cs.ldebryniuk.assignment15;

import com.shpp.p2p.cs.ldebryniuk.assignment15.binaryTree.TreeLeaf;
import com.shpp.p2p.cs.ldebryniuk.assignment15.binaryTree.TreeNodeComparator;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.PriorityQueue;

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

//    public static void main(String[] args) {
//        PriorityQueue<TreeLeaf> prioritizedTreeLeaves =
//                new PriorityQueue<>(5, new TreeNodeComparator());
//
//        TreeLeaf byte1 = new TreeLeaf((byte) 'a');
//        byte1.setWeight(3);
//        TreeLeaf byte2 = new TreeLeaf((byte) 'b');
//        byte2.setWeight(3);
//        TreeLeaf byte3 = new TreeLeaf((byte) 'c');
//        byte3.setWeight(3);
//        TreeLeaf byte4 = new TreeLeaf((byte) 'd');
//        byte4.setWeight(3);
//        TreeLeaf byte5 = new TreeLeaf((byte) 'e');
//        byte5.setWeight(2);
//        TreeLeaf byte6 = new TreeLeaf((byte) 'f');
//        byte6.setWeight(1);
//
//        prioritizedTreeLeaves.add(byte1);
//        prioritizedTreeLeaves.add(byte2);
//        prioritizedTreeLeaves.add(byte3);
//        prioritizedTreeLeaves.add(byte4);
//        prioritizedTreeLeaves.add(byte5);
//        prioritizedTreeLeaves.add(byte6);
//
//        for (int i = 0; i < 6; i++) {
//            System.out.println(prioritizedTreeLeaves.poll());
//        }
//
//    }

    public static void main(String[] args) {
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

//        args = new String[]{"test9.mp4", "test9.mp4.par"};
//        new HuffmanArchiver().determineOperation(args);
//        args = new String[]{"test9.mp4.par", "test9_res.mp4"};
//        new HuffmanArchiver().determineOperation(args);
    }
}



