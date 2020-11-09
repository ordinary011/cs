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
    private final int megaByte = 1024 * 1024; // bytes
    private int bitsForEncoding;
    private int bufferCount;
    private long uncompressedFileSize; // bytes
    private ByteBuffer readBuff = ByteBuffer.allocate(50);
    private byte[] tableByteArr;
    StringBuilder compressedDataStr = new StringBuilder();

    public void compress(String originalFile, String compressedFile) {
        try (FileChannel readFChan = (FileChannel) Files.newByteChannel(Paths.get(originalFile));
             FileChannel writeFChan = (FileChannel) Files.newByteChannel(Paths.get(compressedFile),
                     StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)
        ) {
            uncompressedFileSize = readFChan.size();
            bufferCount = (int) Math.ceil(uncompressedFileSize / 50.0);

            ArrayList<Byte> uniqueBytes = new ArrayList<>();

            // find unique bytes
            int bytesInsideReadBuffer;
            while ((bytesInsideReadBuffer = readFChan.read(readBuff)) != -1) { // read from a file to a buffer; -1 means end of file
                readBuff.rewind(); // set the position inside the buff to the beginning

                for (int i = 0; i < bytesInsideReadBuffer; i++) {
                    byte buffByte = readBuff.get();
                    if (!uniqueBytes.contains(buffByte)) uniqueBytes.add(buffByte);
                }
                readBuff.rewind(); // set the position inside the buff to the beginning
            }

            createRelationTable(uniqueBytes);

            compressAndWrite(readFChan, writeFChan);
        } catch (Exception e) {
            System.err.println("jo bro mistake down here");
            e.printStackTrace();
        }
    }

    private void compressAndWrite(FileChannel readFChan, FileChannel writeFChan) throws IOException {
        readFChan.position(0); // read file from the beginning again

        int bytesInsideReadBuffer = readFChan.read(readBuff);
        readBuff.rewind(); // set the position inside the buff to the beginning
        for (int i = 0; i < bytesInsideReadBuffer; i++) {
            byte buffByte = readBuff.get();

            compressedDataStr.append(
                    relTable.get(buffByte)
            );
        }
        readBuff.rewind(); // set the position inside the buff to the beginning after last read

        int bufferSeuqentionNum = 1; // first buffer
        int bufferOffset = bytesForSavingTableSize + bytesForSavingUncompressedData + tableByteArr.length; // in bytes
        int sizeOfCompressedDataChunck =
                (bufferCount == bufferSeuqentionNum) ? // if this is the last buffer there will be no remainders from prev buffer
                        (int) Math.ceil(compressedDataStr.length() / 8.0) : compressedDataStr.length() / 8;
        int writeBufferCapacity = bufferOffset + sizeOfCompressedDataChunck;
        ByteBuffer writeBuff = ByteBuffer.allocate(writeBufferCapacity);


        // fill the write buffer
        writeBuff.putInt(relTable.size() * 2); // whole table size in bytes
        writeBuff.putLong(uncompressedFileSize); // uncompressed data size in bytes
        writeBuff.put(tableByteArr); // table of relations
        writeCompressedToAFile(writeFChan, writeBuff, bufferSeuqentionNum);
        bufferSeuqentionNum++;

        // if file is more than a kilobyte read and write by pieces the rest of the file
        while ((bytesInsideReadBuffer = readFChan.read(readBuff)) != -1) { // read from a file to a buffer; -1 means end of file
            readBuff.rewind(); // set the position inside of the buff to the beginning

            for (int i = 0; i < bytesInsideReadBuffer; i++) {
                byte buffByte = readBuff.get();

                compressedDataStr.append(
                        relTable.get(buffByte)
                );
            }
            readBuff.rewind(); // set the position inside of the buff to the beginning

            sizeOfCompressedDataChunck =
                    (bufferCount == bufferSeuqentionNum) ? // if this is the last buffer there will be no remainders from prev buffer
                            (int) Math.ceil(compressedDataStr.length() / 8.0) : compressedDataStr.length() / 8;
            if (sizeOfCompressedDataChunck != writeBuff.capacity()) {
                writeBuff = ByteBuffer.allocate(sizeOfCompressedDataChunck);
            }

            writeCompressedToAFile(writeFChan, writeBuff, bufferSeuqentionNum);
            bufferSeuqentionNum++;
        }
    }

    private void writeCompressedToAFile(FileChannel writeFChan, ByteBuffer writeBuff, int bufferSeuqentionNum)
            throws IOException {
        // add compressed data to the write buff by slicing compressedDataStr into bytes
        int byteSize = 8;
        String remainder = "";
        String oneByte = "";
        for (int i = 0; i < compressedDataStr.length(); i += byteSize) {
            if ((i + byteSize) <= compressedDataStr.length()) { // if string has 8 chars (bits) inside
                oneByte = compressedDataStr.substring(i, i + byteSize);
                writeBuff.put((byte) Integer.parseInt(oneByte, 2));
            } else { // if last encodedByteString is less than a byte
                String lessThanByte = compressedDataStr.substring(i);
                if (bufferSeuqentionNum == bufferCount) { // if this is the last write buffer
                    int zeroBitsToEnd = byteSize - lessThanByte.length();
                    oneByte = lessThanByte + "0".repeat(zeroBitsToEnd);
                    writeBuff.put((byte) Integer.parseInt(oneByte, 2));
                } else { // leave remainder for the next buffer
                    remainder = lessThanByte;
                }
            }
        }
        writeBuff.rewind();
        compressedDataStr.setLength(0);
        if (remainder.length() > 0) compressedDataStr.append(remainder);

        // write to a file
        writeFChan.write(writeBuff);
        writeBuff.rewind();
    }

    private void createRelationTable(ArrayList<Byte> unique) {
        byte encodingForTheByte = 0;
        ArrayList<Byte> tableAsArrayList = new ArrayList<>();
        int bitsForEncoding = findEncodingLen(unique.size());

        for (int i = 0; i < unique.size(); i++) {
            Byte uniqueByte = unique.get(i);

            tableAsArrayList.add(uniqueByte);
            tableAsArrayList.add(encodingForTheByte);

            String encodedByteStr = String.format("%8s", Integer.toBinaryString(encodingForTheByte & 0xFF))
                    .replace(' ', '0');
            encodedByteStr = encodedByteStr.substring(encodedByteStr.length() - bitsForEncoding); // "00000011" -> "11"
            relTable.put(uniqueByte, encodedByteStr);

            encodingForTheByte++;
        }

        tableByteArr = new byte[tableAsArrayList.size()];
        for (int i = 0; i < tableAsArrayList.size(); i++) {
            tableByteArr[i] = tableAsArrayList.get(i);
        }
    }

}
