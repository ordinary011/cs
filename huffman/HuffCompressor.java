package com.shpp.p2p.cs.ldebryniuk.assignment15;

import com.shpp.p2p.cs.ldebryniuk.assignment15.binaryTree.BTree;
import com.shpp.p2p.cs.ldebryniuk.assignment15.binaryTree.TreeLeaf;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.*;

/**
 * The following class contains all the logic that is needed for compression
 */
class HuffCompressor extends CommonUtils {

    /**
     * key = unique byte; value = TreeLeaf that is created for corresponding byte
     */
    private final HashMap<Byte, TreeLeaf> byteToItsTreeLeaf = new HashMap<>();

    /**
     * key = encoding length (n) of the byte; value = bytes count with the encoding length of n
     */
    private final HashMap<Integer, Integer> encodingLengthToByteCount = new HashMap<>();

    /**
     * This is the starting method of the compressing process
     */
    void compressFile(FileChannel inputFChan, FileChannel outputFChan, long inputFileSize) throws IOException {
        countUniqueBytes(inputFChan);

        PriorityQueue<TreeLeaf> orderedByEncodingLengthDesc = new BTree().prioritizeAndBuildTree(byteToItsTreeLeaf);

        ArrayList<Byte> table = createTable(orderedByEncodingLengthDesc);

        byte[] tableInfo = createTableInfo();

        writeInfoForDecompression(outputFChan, tableInfo, table);

        new CompressByChunks().compressByChunks(inputFChan, outputFChan, inputFileSize, byteToItsTreeLeaf);
    }

    /*
     * the following class counts frequency of bytes
     */
    private void countUniqueBytes(FileChannel inputFChan) throws IOException {
        // read the whole input file by chunks and search for unique bytes
        int bytesInsideReadBuffer;
        while ((bytesInsideReadBuffer = inputFChan.read(readBuff)) != -1) { // read file till end; -1 means end of file
            readBuff.rewind(); // set the position inside the buff to the beginning

            // count unique bytes in the buffer
            for (int i = 0; i < bytesInsideReadBuffer; i++) {
                byte byteFromTheInputFile = readBuff.get();

                TreeLeaf treeNode = byteToItsTreeLeaf.get(byteFromTheInputFile);
                if (treeNode != null) {
                    treeNode.incrementWeight();
                } else {
                    byteToItsTreeLeaf.put(byteFromTheInputFile, new TreeLeaf(byteFromTheInputFile));
                }
            }
            readBuff.rewind(); // set the position inside the buff to the beginning
        }
    }

    /**
     * The following class creates a table (byte : encoding for the byte)
     */
    private ArrayList<Byte> createTable(PriorityQueue<TreeLeaf> prioritizedLeaves) {
        int uniqueBytesCount = prioritizedLeaves.size();
        ArrayList<Byte> table = new ArrayList<>();

        // create table and count bytes with certain encoding length
        for (int i = 0; i < uniqueBytesCount; i++) {
            TreeLeaf byteAsATreeLeaf = prioritizedLeaves.poll();
            int encodingLengthOfCurrentByte = byteAsATreeLeaf.getEncodingLength();

            // add byte to the table
            table.add(byteAsATreeLeaf.getByteValue());
            // write encoding for the byte to the table
            if (encodingLengthOfCurrentByte > BYTE_SIZE) {
                int secondLowerByteInInt = byteAsATreeLeaf.getEncodingOfTheByte() & 0xFF00;
                secondLowerByteInInt >>>= BYTE_SIZE;
                table.add((byte) secondLowerByteInInt); // get second lower byte from int
            }
            table.add((byte) byteAsATreeLeaf.getEncodingOfTheByte());

            // count bytes of certain encoding length
            Integer byteCount = encodingLengthToByteCount.getOrDefault(encodingLengthOfCurrentByte, 0);
            encodingLengthToByteCount.put(encodingLengthOfCurrentByte, byteCount + 1);
        }

        return table;
    }

    /**
     * creates information about the table (please read algorithmExplanation.txt)
     */
    private byte[] createTableInfo() {
        byte[] tableInfo = new byte[encodingLengthToByteCount.size() * 2];
        TreeMap<Integer, Integer> sortedByEncodingLengthAsc = new TreeMap<>(encodingLengthToByteCount);

        int tableInfoIndex = 0;
        for (Map.Entry<Integer, Integer> entry : sortedByEncodingLengthAsc.entrySet()) {
            tableInfo[tableInfoIndex] = entry.getValue().byteValue(); // bytesCount with the same encoding length
            tableInfo[tableInfoIndex + 1] = entry.getKey().byteValue(); // encoding length of bytesCount
            tableInfoIndex += 2;
        }

        return tableInfo;
    }

    /**
     * writes all the information that is needed for decompression
     */
    private void writeInfoForDecompression(FileChannel outputFChan,
                                           byte[] tableInfo, ArrayList<Byte> table) throws IOException {
        // Please read algorithmExplanation.txt in order to understand code below
        int bytesBeforeTableInfo = 2;
        int buffCapacity = bytesBeforeTableInfo + tableInfo.length + table.size();
        writeBuff = ByteBuffer.allocate(buffCapacity);

        int firstByte = 0; // later will contain redundant bits count In Last compressed Byte;
        writeBuff.put((byte) firstByte); // first byte will be added at the very end. Now this is a placeholder
        writeBuff.put((byte) tableInfo.length); // second byte contains amount of bytes in tableInfo
        writeBuff.put(tableInfo);
        // put table to the buffer
        for (Byte byteFromTable : table) {
            writeBuff.put(byteFromTable);
        }
        writeBuff.rewind(); // set the position inside the buff to the beginning

        // write to the file
        outputFChan.write(writeBuff);
        writeBuff.rewind(); // set the position inside the buff to the beginning
    }

}

/**
 * The following class contains all the logic that is needed
 * for compressing and writing the data by chunks
 */
class CompressByChunks extends CommonUtils {

    private final int[] compressedDataChunk = new int[MEGABYTE];
    private int compressedChunkIndex = 0;

    private final int BYTES_IN_INTEGER = 4;
    private final int BITS_IN_INTEGER = 32;

    private int fourBytesContainer = 0;
    private int freeBitsInContainer = BITS_IN_INTEGER;

    private int rememberedBits = 0; // bits that did not fit to the container are saved for the next
    private int bitsCountInRemembered = 0;

    private int lastContainer = 0;
    private int bitsCountInLastContainer = 0;

    /**
     * The starting method of the class
     */
    void compressByChunks(FileChannel inputFChan, FileChannel outputFChan,
                          long inputFileSize, HashMap<Byte, TreeLeaf> byteToItsTreeLeaf) throws IOException {
        inputFChan.position(0); // read file from the beginning again
        int totalNumberOfRBuffers = (int) Math.ceil(inputFileSize / (double) MEGABYTE);

        // read file by chunks till end of file. each iteration is a new read buffer (chunk of data)
        for (int bufferSequentialNum = 1; bufferSequentialNum <= totalNumberOfRBuffers; bufferSequentialNum++) {
            int bytesInsideReadBuffer = inputFChan.read(readBuff);
            readBuff.rewind(); // set the position inside the buff to the beginning

            compressBytesFromRBUff(bytesInsideReadBuffer, byteToItsTreeLeaf);

            if (bufferSequentialNum == totalNumberOfRBuffers) { // true if this is the last Read Buffer
                if (bitsCountInRemembered > 0) { // true if there are some remembered bits
                    lastContainer = rememberedBits;
                    bitsCountInLastContainer = bitsCountInRemembered;
                } else if (freeBitsInContainer < BITS_IN_INTEGER) { // true if fourBytesContainer is not empty
                    lastContainer = fourBytesContainer;
                    bitsCountInLastContainer = BITS_IN_INTEGER - freeBitsInContainer;
                }
            }

            writeBytesFromContainerToFile(outputFChan);

            // new compressed bytes will overwrite previous compressed bytes in compressedDataChunk array
            compressedChunkIndex = 0;
        }

        int bytesInsideLastContainer = writeLastContainerToTheFile(outputFChan);

        addRedundantZeroesInLastCompressedByte(outputFChan, bytesInsideLastContainer);
    }

    /**
     * writes redundantZeroesInLastCompressedByte to the beginning of the file
     */
    private void addRedundantZeroesInLastCompressedByte(FileChannel outputFChan,
                                                        int bytesInsideLastContainer) throws IOException {
        outputFChan.position(0); // set the position of outputFChan to the beginning (see algorithmExplanation.txt)
        int redundantZeroesInLastCompressedByte = (bytesInsideLastContainer * BYTE_SIZE) - bitsCountInLastContainer;

        ByteBuffer oneByteBuffer = ByteBuffer.allocate(1);
        oneByteBuffer.put((byte) redundantZeroesInLastCompressedByte);
        oneByteBuffer.rewind(); // set the position inside the buff to the beginning
        outputFChan.write(oneByteBuffer);
    }

    /**
     * writes bytes from container to the file
     */
    private void writeBytesFromContainerToFile(FileChannel outputFChan) throws IOException {
        // if writeBuff has less or more bytes than in the compressedDataChunk array
        if (writeBuff.capacity() != (compressedChunkIndex * BYTES_IN_INTEGER)) {
            writeBuff = ByteBuffer.allocate(compressedChunkIndex * BYTES_IN_INTEGER);
        }

        // put bytes from compressedDataChunk to the write buff
        for (int i = 0; i < compressedChunkIndex; i++) {
            writeBuff.putInt(compressedDataChunk[i]);
        }
        writeBuff.rewind(); // set the position inside the buff to the beginning

        outputFChan.write(writeBuff);
        writeBuff.rewind(); // set the position inside the buff to the beginning
    }

    /**
     * writes bytes from the last container to the file
     */
    private int writeLastContainerToTheFile(FileChannel outputFChan) throws IOException {
        int bytesInsideLastContainer = (int) Math.ceil(bitsCountInLastContainer / 8.0);
        ByteBuffer lastWBuffer = ByteBuffer.allocate(bytesInsideLastContainer);

        int shift = BITS_IN_INTEGER - BYTE_SIZE; // 24
        for (int i = 0; i < bytesInsideLastContainer; i++) {
            byte byteFromContainer = (byte) (lastContainer >>> shift);
            lastWBuffer.put(byteFromContainer);
            shift -= BYTE_SIZE;
        }
        lastWBuffer.rewind(); // set the position inside the buff to the beginning

        outputFChan.write(lastWBuffer);

        return bytesInsideLastContainer;
    }

    /**
     * compresses the bytes, and puts compressed chunk to the array that will be copied to the buffer
     */
    private void compressBytesFromRBUff(int bytesInsideReadBuffer, HashMap<Byte, TreeLeaf> byteToItsTreeLeaf) {
        for (int i = 0; i < bytesInsideReadBuffer; i++) {
            byte byteFromTheReadFile = readBuff.get();
            TreeLeaf leafForCurrentByte = byteToItsTreeLeaf.get(byteFromTheReadFile);
            int encodingOfTheByte = leafForCurrentByte.getEncodingOfTheByte();
            int usedBitsForEncodingTheByte = leafForCurrentByte.getEncodingLength();

            if (bitsCountInRemembered > 0) { // true when previous byte encoding did not fit to the container
                fourBytesContainer |= rememberedBits; // move rememberedBits to fourBytesContainer
                freeBitsInContainer -= bitsCountInRemembered;
                bitsCountInRemembered = 0;
                rememberedBits = 0;
            }

            freeBitsInContainer -= usedBitsForEncodingTheByte;
            if (freeBitsInContainer >= 0) { // true when there is a place to fit the whole encoding
                encodingOfTheByte <<= freeBitsInContainer; // move low order bits to left 00000010 << 5 becomes 01000000
                // combine new byte encoded bits with bits that encoded previous byte(s)
                fourBytesContainer |= encodingOfTheByte; // 01000000 | 00011000 becomes 01011000
            } else { // compressedByte can not fit all the encoding in it
                bitsCountInRemembered = Math.abs(freeBitsInContainer);
                //save bits that can't fit to container
                rememberedBits = encodingOfTheByte << (BITS_IN_INTEGER - bitsCountInRemembered);
                encodingOfTheByte >>>= bitsCountInRemembered; // remove bits that can not fit to the container
                fourBytesContainer |= encodingOfTheByte;
                freeBitsInContainer = 0;
            }

            if (freeBitsInContainer == 0) { // true when fourBytesContainer is full
                compressedDataChunk[compressedChunkIndex] = fourBytesContainer;
                compressedChunkIndex++;
                fourBytesContainer = 0;
                freeBitsInContainer = BITS_IN_INTEGER;
            }
        }
        readBuff.rewind(); // set the position inside the buff to the beginning
    }

}