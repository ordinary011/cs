package com.shpp.p2p.cs.ldebryniuk.assignment14;

import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

public class Assignment14Part1 {

    static private final HashMap<Byte, String> tableRel = new HashMap<>();

    public static void main(String[] args) {
//        if (args.length == 0) jo("test5.png", "test.par");
        if (args.length < 2) jo("test.txt", "test.par");
        else jo(args[0], args[1]);
    }

    public static void jo(String str, String str2) {
        try {
            ArrayList<Byte> unique = new ArrayList<>();

            Path path = Paths.get(str);
            byte[] fileBytes = Files.readAllBytes(path);

            for (byte b : fileBytes) {
                if (!unique.contains(b)) unique.add(b);
            }

            ArrayList<Byte> tableInBytes = ralationTable(fileBytes, unique);

            ArrayList<Byte> res = new ArrayList<>();
            addTableSize(res);
            compress(tableInBytes, fileBytes, res);

            byte[] readyForFile = prepareForFile(res);

            path = Paths.get(str2);
            Files.write(path, readyForFile);
        } catch (Exception e) {
            System.err.println("jo bro");
            e.printStackTrace();
        }
    }

    private static byte[] prepareForFile(ArrayList<Byte> res) {
        byte[] readyForFile = new byte[res.size()];

        for (int i = 0; i < res.size(); i++) {
            readyForFile[i] = res.get(i);
        }

        return readyForFile;
    }

    private static void compress(ArrayList<Byte> tableInBytes, byte[] bytesInFile, ArrayList<Byte> res) {
        StringBuilder compressed = new StringBuilder();
        for (int i = 0; i < bytesInFile.length; i++) {
            compressed.append(
                    tableRel.get(bytesInFile[i])
            );
        }

        // add compressed data size
        byte[] bytesOfInt = ByteBuffer.allocate(8).putLong(compressed.length()).array();
        for (byte b : bytesOfInt) {
            res.add(b);
        }

        // add table of relations
        res.addAll(tableInBytes);

        int byteSize = 8;
        for (int i = 0; i < compressed.length(); i += byteSize) {
            String oneByte;
            if ((i + byteSize) < compressed.length()) {
                oneByte = compressed.substring(i, i + byteSize);
            } else { // add zeroes to last byte
                int zeroesToEnd = compressed.length() - i;
                oneByte = compressed.substring(i) + "0".repeat(zeroesToEnd);
            }

            res.add(
                    (byte) Integer.parseInt(oneByte, 2)
            );
        }


//        // print bytes in a file
//        for (Byte b : res) {
////        for (Byte b : fileBytes) {
//            String s1 = String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
//            System.out.println(s1);
//        }
    }

    private static void addTableSize(ArrayList<Byte> res) {
        int tsize = tableRel.size() * 2;
        byte[] bytesOfInt = ByteBuffer.allocate(4).putInt(tsize).array();
        for (byte b : bytesOfInt) {
            res.add(b);
        }
    }

    private static ArrayList<Byte> ralationTable(byte[] data, ArrayList<Byte> unique) throws Exception {
        byte encodedByte = 0;
        ArrayList<Byte> tableInBytes = new ArrayList<>();
        int bitsForEncoding = findEncodingLen(unique.size());

        for (int i = 0; i < unique.size(); i++) {
            Byte uniqueByte = unique.get(i);

            tableInBytes.add(uniqueByte);
            tableInBytes.add(encodedByte);

            String encodedStr =
                    String.format("%8s", Integer.toBinaryString(encodedByte & 0xFF)).replace(' ', '0');
            encodedStr = encodedStr.substring(encodedStr.length() - bitsForEncoding);
            tableRel.put(uniqueByte, encodedStr);

            encodedByte++;
        }

        return tableInBytes;


//        for (int i = 0; i < unique.size(); i++) {
//            Byte uniqueByte = unique.get(i);
//            tableInBytes.add(unique.get(i));
//            encodedSequence++;
//            if (encodedSequence > - 1) { // check if encodedSequence more than 1 byte. todo maybe redundant???
//                tableInBytes.add(encodedSequence);
//            } else {
//                System.out.println("can't hold 128 or more in one signed byte");
//            }
//        }
    }

    private static int findEncodingLen(int size) throws Exception {
//        int[] powers = {1, 2, 4, 8, 16, 32, 64, 127};
        int exponent = 0;
        int byteSize = 8;
        for (int i = 0; i <= byteSize; i++) {
            if (size <= Math.pow(2, exponent)) {
                return exponent;
            }

            exponent++;
        }

        return -1;
    }
}
