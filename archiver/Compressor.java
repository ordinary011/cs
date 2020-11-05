package com.shpp.p2p.cs.ldebryniuk.assignment14;

import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

//4 байт - розмір таблиці в байтах
//8 байт - розмір самих даних в бітах (?), ну або розмір незжатих даних в байтах
//Х байт - таблиця
//Y байт - самі дані
public class Compressor extends Archiver {

    private final HashMap<Byte, String> relTable = new HashMap<>();

    public void compress(String file, String compressedFile) {
        try {
            ArrayList<Byte> unique = new ArrayList<>();

            Path path = Paths.get(file);
            byte[] originalFileBytes = Files.readAllBytes(path);

            for (byte b : originalFileBytes) {
                if (!unique.contains(b)) unique.add(b);
            }

            ArrayList<Byte> tableInBytes = ralationTable(originalFileBytes, unique);

            ArrayList<Byte> res = new ArrayList<>();
            addTableSize(res);
            compress(tableInBytes, originalFileBytes, res);

            byte[] readyForCompressing = prepareForCompressing(res);

            path = Paths.get(compressedFile);
            Files.write(path, readyForCompressing);
        } catch (Exception e) {
            System.err.println("jo bro");
            e.printStackTrace();
        }
    }

    private byte[] prepareForCompressing(ArrayList<Byte> res) {
        byte[] readyForFile = new byte[res.size()];

        for (int i = 0; i < res.size(); i++) {
            readyForFile[i] = res.get(i);
        }

        return readyForFile;
    }

    private void compress(ArrayList<Byte> relTableInBytes, byte[] originalFileBytes, ArrayList<Byte> res) {
        // compress data
        StringBuilder compressedDataStr = new StringBuilder();
        for (int i = 0; i < originalFileBytes.length; i++) {
            compressedDataStr.append(
                    relTable.get(originalFileBytes[i])
            );
        }

        // add uncompressed data size
//        byte[] bytesOfInt = ByteBuffer.allocate(8).putLong(compressed.length()).array();
        byte[] bytesOfInt = ByteBuffer.allocate(8).putLong(originalFileBytes.length).array();
        for (byte b : bytesOfInt) {
            res.add(b);
        }

        // add table of relations
        res.addAll(relTableInBytes);

        // add compressed data by slicing compressedDataStr into bytes
        int byteSize = 8;
        for (int i = 0; i < compressedDataStr.length(); i += byteSize) {
            String oneByte;
            if ((i + byteSize) <= compressedDataStr.length()) { // if string has 8 bits (characters) inside
                oneByte = compressedDataStr.substring(i, i + byteSize);
            } else { // add zeroes to the last byte
                String lessThanByte = compressedDataStr.substring(i);
                int zeroBitsToEnd = byteSize - lessThanByte.length();
                oneByte = lessThanByte + "0".repeat(zeroBitsToEnd);
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

    private void addTableSize(ArrayList<Byte> res) {
        int tablesize = relTable.size() * 2;
        byte[] bytesOfInt = ByteBuffer.allocate(4).putInt(tablesize).array();
        for (byte b : bytesOfInt) {
            res.add(b);
        }
    }

    private ArrayList<Byte> ralationTable(byte[] data, ArrayList<Byte> unique) throws Exception {
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
            relTable.put(uniqueByte, encodedStr);

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

}
