package com.shpp.p2p.cs.ldebryniuk.assignment14;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;

//4 байт - розмір таблиці в байтах
//8 байт - розмір самих даних в бітах (?), ну або розмір незжатих даних в байтах
//Х байт - таблиця
//Y байт - самі дані
public class Compressor extends Archiver {

    private final HashMap<Byte, String> relTable = new HashMap<>();
    private final int kiloByte = 1024; // bytes
    private int bitsForEncoding;
    private long uncompressedFileSize = 0; // bytes
    private ByteBuffer readBuff = ByteBuffer.allocate(kiloByte);
    private byte[] tableByteArr;

    public void compress(String originalFile, String compressedFile) {

        try (FileChannel readFChan = (FileChannel) Files.newByteChannel(Paths.get(originalFile));
             FileChannel writeFChan = (FileChannel) Files.newByteChannel(Paths.get(compressedFile),
                     StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)
        ) {
            ArrayList<Byte> uniqueBytes = new ArrayList<>();

            // find unique bytes
            int bytesInsideBuffer;
            while ((bytesInsideBuffer = readFChan.read(readBuff)) != -1) { // read from a file to a buffer; -1 means end of file
                readBuff.rewind(); // set the position inside the buff to the beginning

                for (int i = 0; i < bytesInsideBuffer; i++) {
                    byte buffByte = readBuff.get();
                    if (!uniqueBytes.contains(buffByte)) uniqueBytes.add(buffByte);
                    uncompressedFileSize++;
                }
            }
            readBuff.rewind(); // set the position inside the buff to the beginning after last read

            createRelationTable(uniqueBytes);

            compressAndWrite(readFChan, writeFChan);
        } catch (Exception e) {
            System.err.println("jo bro mistake down here");
            e.printStackTrace();
        }
    }

    private void compressAndWrite(FileChannel readFChan, FileChannel writeFChan) throws IOException {
//        readBuff = ByteBuffer.allocate(kiloByte - bufferOffset);
        StringBuilder compressedDataStr = new StringBuilder();
        readFChan.position(0); // read the file from the beginning again

        int bytesInsideReadBuffer = readFChan.read(readBuff);
        readBuff.rewind(); // set the position inside the buff to the beginning
        for (int i = 0; i < bytesInsideReadBuffer; i++) {
            byte buffByte = readBuff.get();

            compressedDataStr.append(
                    relTable.get(buffByte)
            );
        }
        readBuff.rewind(); // set the position inside the buff to the beginning after last read

        int bufferOffset = bytesForSavingTableSize + bytesForSavingUncompressedData + tableByteArr.length; // in bytes
        int sizeOfCompressedData = (int) Math.ceil(compressedDataStr.length() / 8.0);
        int writeBufferCapacity = bufferOffset + sizeOfCompressedData;
        ByteBuffer writeBuff = ByteBuffer.allocate(writeBufferCapacity);

        // fill the write buffer
        writeBuff.putInt(relTable.size() * 2); // whole table size in bytes
        writeBuff.putLong(uncompressedFileSize); // uncompressed data size in bytes
        writeBuff.put(tableByteArr); // table of relations
        writeCompressedToAFile(compressedDataStr, writeFChan, writeBuff);

        // if file is more than a kilobyte read and write by pieces the rest of the file
        while ((bytesInsideReadBuffer = readFChan.read(readBuff)) != -1) { // read from a file to a buffer; -1 means end of file
            readBuff.rewind(); // set the position inside of the buff to the beginning
            compressedDataStr.setLength(0);
            if (bytesInsideReadBuffer != writeBuff.capacity()) {
                writeBuff = ByteBuffer.allocate(bytesInsideReadBuffer);
            }

            for (int i = 0; i < bytesInsideReadBuffer; i++) {
                byte buffByte = readBuff.get();

                compressedDataStr.append(
                        relTable.get(buffByte)
                );
            }

            writeCompressedToAFile(compressedDataStr, writeFChan, writeBuff);
        }
    }

    private void writeCompressedToAFile(StringBuilder compressedDataStr, FileChannel writeFChan, ByteBuffer writeBuff)
            throws IOException {
        // add compressed data by slicing compressedDataStr into bytes
        int byteSize = 8;
        for (int i = 0; i < compressedDataStr.length(); i += byteSize) {
            String oneByte;
            if ((i + byteSize) <= compressedDataStr.length()) { // if string has 8 chars (bits) inside
                oneByte = compressedDataStr.substring(i, i + byteSize);
            } else { // add zeroes to the last byte
                String lessThanByte = compressedDataStr.substring(i);
                int zeroBitsToEnd = byteSize - lessThanByte.length();
                oneByte = lessThanByte + "0".repeat(zeroBitsToEnd);
            }

            writeBuff.put(
                    (byte) Integer.parseInt(oneByte, 2)
            );
        }

        // write to a file
        writeBuff.rewind();
        writeFChan.write(writeBuff);
        writeBuff.rewind();
    }

    private void createRelationTable(ArrayList<Byte> unique) {
        byte encodedByte = 0;
        ArrayList<Byte> tableAsArrayList = new ArrayList<>();
        int bitsForEncoding = findEncodingLen(unique.size());

        for (int i = 0; i < unique.size(); i++) {
            Byte uniqueByte = unique.get(i);

            tableAsArrayList.add(uniqueByte);
            tableAsArrayList.add(encodedByte);

            String encodedStr =
                    String.format("%8s", Integer.toBinaryString(encodedByte & 0xFF)).replace(' ', '0');
            encodedStr = encodedStr.substring(encodedStr.length() - bitsForEncoding);
            relTable.put(uniqueByte, encodedStr);

            encodedByte++;
        }

        tableByteArr = new byte[tableAsArrayList.size()];
        for (int i = 0; i < tableAsArrayList.size(); i++) {
            tableByteArr[i] = tableAsArrayList.get(i);
        }
    }

}
