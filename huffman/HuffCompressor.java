package com.shpp.p2p.cs.ldebryniuk.assignment15;

import com.shpp.p2p.cs.ldebryniuk.assignment15.binaryTree.BTree;
import com.shpp.p2p.cs.ldebryniuk.assignment15.binaryTree.TreeLeaf;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.*;

class HuffCompressor {

    private final int MEGABYTE = 1024 * 1024; // bytes
    private final HashMap<Byte, TreeLeaf> byteToItsTreeLeaf = new HashMap<>();
    /**
     * contains chunk of data that is read from the read channel
     */
//    private final ByteBuffer readBuff = ByteBuffer.allocate(MEGABYTE);
    private final ByteBuffer readBuff = ByteBuffer.allocate(5);
    ByteBuffer writeBuff;

    void compressFile(FileChannel inputFChan, FileChannel outputFChan, long inputFileSize) throws IOException {
        countUniqueBytes(inputFChan);

        PriorityQueue<TreeLeaf> prioritizedTreeLeaves = new BTree().prioritizeBytesAndBuildTree(byteToItsTreeLeaf);

        createTable(prioritizedTreeLeaves, outputFChan);

        compressAndWriteData(inputFChan, outputFChan);
    }

    private void countUniqueBytes(FileChannel inputFChan) throws IOException {
        // read the whole input file and find unique bytes
        int bytesInsideReadBuffer;
        while ((bytesInsideReadBuffer = inputFChan.read(readBuff)) != -1) { // read file; -1 means end of file
            readBuff.rewind(); // set the position inside the buff to the beginning

            // count unique bytes in the buffer
            for (int i = 0; i < bytesInsideReadBuffer; i++) {
                byte buffByte = readBuff.get();

                TreeLeaf treeNode = byteToItsTreeLeaf.get(buffByte);
                if (treeNode != null) {
                    treeNode.incrementWeight();
                } else {
                    byteToItsTreeLeaf.put(buffByte, new TreeLeaf(buffByte));
                }
            }
            readBuff.rewind(); // set the position inside the buff to the beginning
        }
    }

    private void createTable(PriorityQueue<TreeLeaf> prioritizedTreeLeaves, FileChannel outputFChan) throws IOException {
        HashMap<Integer, Integer> encodingLengthToByteCount = new HashMap<>();
        byte[] table = new byte[prioritizedTreeLeaves.size() * 2];

        int longestEncodingLength = prioritizedTreeLeaves.peek().getEncodingLength();

        // create table and count bytes of their corresponding encoding length
        for (int i = 0; i < table.length; i+=2) {
            TreeLeaf byteAsATreeLeaf = prioritizedTreeLeaves.poll();

            // write byte and its encoding to the table
            table[i] = byteAsATreeLeaf.getByteValue();
            table[i + 1] = (byte) byteAsATreeLeaf.getEncodingOfTheByte();

            // count bytes with their corresponding encoding length
            int usedBitesForEncoding = byteAsATreeLeaf.getEncodingLength();
            Integer byteCount = encodingLengthToByteCount.getOrDefault(usedBitesForEncoding, 0);
            encodingLengthToByteCount.put(usedBitesForEncoding, byteCount + 1);
        }

        byte[] tableInfo = new byte[encodingLengthToByteCount.size()];
        TreeMap<Integer, Integer> sortedMap = new TreeMap<>(encodingLengthToByteCount);
        NavigableMap<Integer, Integer> descendingMap = sortedMap.descendingMap();

        int index = 0;
        for (Map.Entry<Integer, Integer> entry : descendingMap.entrySet()) {
            tableInfo[index] = entry.getValue().byteValue();
            index++;
        }

        // Please watch algorithmExlanation.txt in order to ubderstand code below
        int buffCapacity = 2 + tableInfo.length + table.length;
        writeBuff = ByteBuffer.allocate(buffCapacity);

        int firstByte = 0;
        writeBuff.put((byte) firstByte); // first byte will be added at the very end. Now this is a placeholder

        int secondByte = 0;
        secondByte |= (tableInfo.length << 4); // fill the first 4 bits with amount of bytes in tableInfo
        secondByte |= longestEncodingLength; // fill the last 4 bits with longest encoding length (in bits)
        writeBuff.put((byte) secondByte);

        writeBuff.put(tableInfo);
        writeBuff.put(table);
        writeBuff.rewind();

        outputFChan.write(writeBuff);
        writeBuff.rewind();
    }

    private void compressAndWriteData(FileChannel inputFChan, FileChannel outputFChan) throws IOException {
        inputFChan.position(0); // read file from the beginning again
        byte[] compressedDataChunk = new byte[MEGABYTE];
        int lastByteIndex = 0;

        int compressedByte = 0;
        int availableBitsInCompressedByte = 8;

        int rememberedBits = 0;
        int bitsCountInRemembered = 0;

        int bytesInsideReadBuffer;
        while ((bytesInsideReadBuffer = inputFChan.read(readBuff)) != -1) { // read file by chunks till end
            readBuff.rewind(); // set the position inside the buff to the beginning

            for (int i = 0; i < bytesInsideReadBuffer; i++) {
                byte byteFromTheReadFile = readBuff.get();
                TreeLeaf leafForCurrentByte = byteToItsTreeLeaf.get(byteFromTheReadFile);
                int encodingOfTheByte = leafForCurrentByte.getEncodingOfTheByte();
                int usedBitsForEncodingTheOriginalByte = leafForCurrentByte.getEncodingLength();

                if (bitsCountInRemembered > 0) {
                    compressedByte |= rememberedBits;
                    availableBitsInCompressedByte -= bitsCountInRemembered;
                    bitsCountInRemembered = 0;
                    rememberedBits = 0;
                }

                availableBitsInCompressedByte -= usedBitsForEncodingTheOriginalByte;
                if (availableBitsInCompressedByte >= 0) { // true when there is a place to fit the whole encoding
                    encodingOfTheByte <<= availableBitsInCompressedByte;
                    compressedByte |= encodingOfTheByte;
                } else { // compressedByte can not fit all the encoding in it
                    bitsCountInRemembered = Math.abs(availableBitsInCompressedByte);
                    rememberedBits = encodingOfTheByte << (8 - bitsCountInRemembered);
                    encodingOfTheByte >>>= bitsCountInRemembered; // remove remembered
                    compressedByte |= encodingOfTheByte;
                    availableBitsInCompressedByte = 0;
                }

                if (availableBitsInCompressedByte == 0) { // true when compressed byte is full
                    compressedDataChunk[lastByteIndex] = (byte) compressedByte;
                    lastByteIndex++;
                    compressedByte = 0;
                    availableBitsInCompressedByte = 8;
                }
            }
            readBuff.rewind(); // set the position inside the buff to the beginning

            // last byte in this chunk
            if (bitsCountInRemembered > 0) {
                compressedByte |= rememberedBits;
                availableBitsInCompressedByte -= bitsCountInRemembered;
                bitsCountInRemembered = 0;
                rememberedBits = 0;

                compressedDataChunk[lastByteIndex] = (byte) compressedByte;
                lastByteIndex++;
                compressedByte = 0;
            }

            if (writeBuff.capacity() != lastByteIndex) {
                writeBuff = ByteBuffer.allocate(lastByteIndex);
            }

            for (int i = 0; i < lastByteIndex; i++) {
                writeBuff.put(compressedDataChunk[i]);
            }
            writeBuff.rewind();

            outputFChan.write(writeBuff);
            writeBuff.rewind();

            lastByteIndex = 0;
        }

        int redundantBitsInLastCompressedByte = availableBitsInCompressedByte;
        outputFChan.position(0);
        ByteBuffer oneByteBuffer = ByteBuffer.allocate(1);
        oneByteBuffer.put((byte) redundantBitsInLastCompressedByte);
        oneByteBuffer.rewind();
        outputFChan.write(oneByteBuffer);
    }

}

