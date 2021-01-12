package com.shpp.p2p.cs.ldebryniuk.assignment17.archiver;

import com.shpp.p2p.cs.ldebryniuk.assignment17.archiver.binaryTree.BTree;
import com.shpp.p2p.cs.ldebryniuk.assignment17.archiver.binaryTree.TreeLeaf;

import com.shpp.p2p.cs.ldebryniuk.assignment17.mycollection.lists.MyArrayList;
import com.shpp.p2p.cs.ldebryniuk.assignment17.mycollection.map.MyHashMap;
import com.shpp.p2p.cs.ldebryniuk.assignment17.mycollection.map.MyMap;
import com.shpp.p2p.cs.ldebryniuk.assignment17.mycollection.queues.MyComparator;
import com.shpp.p2p.cs.ldebryniuk.assignment17.mycollection.queues.MyPriorityQueue;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * The following class contains all the logic that is needed for compression
 */
class HuffCompressor extends CommonUtils {

    /**
     * key = unique byte; value = TreeLeaf that is created for corresponding byte
     */
    private final MyHashMap<Byte, TreeLeaf> byteToItsTreeLeaf = new MyHashMap<>();

    /**
     * key = encoding length (n) of the byte; value = bytes count with the encoding length of n
     */
    private final MyHashMap<Integer, Integer> encodingLengthToByteCount = new MyHashMap<>();

    /**
     * This is the starting method of the compressing process
     *
     * @param inputFChan    reference to the input channel between our process and OS
     * @param outputFChan   reference to the output channel between our process and OS
     * @param inputFileSize size of input file
     * @throws IOException can be caused by reading or writing the file
     */
    void compressFile(FileChannel inputFChan, FileChannel outputFChan, long inputFileSize) throws IOException {
        countUniqueBytes(inputFChan);

        MyPriorityQueue<TreeLeaf> orderedByEncodingLengthDesc = new BTree().prioritizeAndBuildTree(byteToItsTreeLeaf);

        MyArrayList<Byte> table = createTable(orderedByEncodingLengthDesc);

        byte[] tableInfo = createTableInfo();

        writeInfoForDecompression(outputFChan, tableInfo, table);

        new CompressByChunks().compressByChunks(inputFChan, outputFChan, inputFileSize, byteToItsTreeLeaf);
    }

    /**
     * The following method counts frequency of bytes
     *
     * @param inputFChan reference to the input channel between our process and OS
     * @throws IOException can be caused by reading or writing the file
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
     * The following class creates a table (unique byte : encoding for the byte)
     *
     * @param prioritizedLeaves ordered by encoding length tree leaves (in descending order)
     * @return table that contains (unique byte : encoding for the byte)
     */
    private MyArrayList<Byte> createTable(MyPriorityQueue<TreeLeaf> prioritizedLeaves) {
        int uniqueBytesCount = prioritizedLeaves.size();
        MyArrayList<Byte> table = new MyArrayList<>();

        // create table and count bytes with certain encoding length
        for (int i = 0; i < uniqueBytesCount; i++) {
            TreeLeaf byteAsATreeLeaf = prioritizedLeaves.poll();
            int encodingLengthOfCurrentByte = byteAsATreeLeaf.getEncodingLength();

            // add byte to the table
            table.add(byteAsATreeLeaf.getByteValue());
            // write encoding for the byte to the table
            if (encodingLengthOfCurrentByte > Byte.SIZE) {
                int secondLowerByteInInt = byteAsATreeLeaf.getEncodingOfTheByte() & 0xFF00;
                secondLowerByteInInt >>>= Byte.SIZE;
                table.add((byte) secondLowerByteInInt); // get second lower byte from int
            }
            table.add((byte) byteAsATreeLeaf.getEncodingOfTheByte());

            // count bytes of certain encoding length
            Integer byteCount = encodingLengthToByteCount.get(encodingLengthOfCurrentByte);
            if (byteCount == null) {
                byteCount = 0;
            }
            encodingLengthToByteCount.put(encodingLengthOfCurrentByte, byteCount + 1);
        }

        return table;
    }

    /**
     * Creates information about the table (please read algorithmExplanation.txt)
     */
    private byte[] createTableInfo() {
        int uniqueBytesN = encodingLengthToByteCount.size();
        byte[] tableInfo = new byte[uniqueBytesN * 2];

        MyPriorityQueue<MyMap.Entry<Integer, Integer>> sortedByEncodingLengthAsc =
                new MyPriorityQueue<>(uniqueBytesN, (entry1, entry2) -> entry1.getKey() - entry2.getKey());

        for (MyMap.Entry<Integer, Integer> entry : encodingLengthToByteCount.entryList()) {
            sortedByEncodingLengthAsc.add(entry);
        }

        // add sorted bytes to the tableInfo
        int tableInfoIndex = 0;
        for (int i = 0; i < uniqueBytesN; i++) {
            MyMap.Entry<Integer, Integer> entry = sortedByEncodingLengthAsc.poll();

            tableInfo[tableInfoIndex] = entry.getValue().byteValue(); // bytesCount with the same encoding length
            tableInfo[tableInfoIndex + 1] = entry.getKey().byteValue(); // encoding length of bytesCount
            tableInfoIndex += 2;
        }

        return tableInfo;
    }

    /**
     * writes all the information that is needed for decompression
     *
     * @param outputFChan reference to the output channel between our process and OS
     * @param tableInfo   information for the table (n bytes with the same encoding length : encoding length of n bytes)
     * @param table       table that contains (unique byte : encoding for the byte)
     * @throws IOException can be caused by reading or writing the file
     */
    private void writeInfoForDecompression(FileChannel outputFChan,
                                           byte[] tableInfo, MyArrayList<Byte> table) throws IOException {
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

    private int fourBytesContainer = 0;
    private int freeBitsInContainer = Integer.SIZE;

    private int rememberedBits = 0; // bits that did not fit to the container are saved for the next
    private int bitsCountInRemembered = 0;

    private int lastContainer = 0;
    private int bitsCountInLastContainer = 0;

    /**
     * The starting method of the class
     *
     * @param inputFChan        reference to the input channel between our process and OS
     * @param outputFChan       reference to the output channel between our process and OS
     * @param inputFileSize     size of input file
     * @param byteToItsTreeLeaf key = unique byte; value = TreeLeaf that is created for corresponding byte
     * @throws IOException can be caused by reading or writing the file
     */
    void compressByChunks(FileChannel inputFChan, FileChannel outputFChan,
                          long inputFileSize, MyHashMap<Byte, TreeLeaf> byteToItsTreeLeaf) throws IOException {
        inputFChan.position(0); // read file from the beginning again
        int totalNumberOfRBuffers = (int) Math.ceil(inputFileSize / (double) MEGABYTE);

        // read file by chunks till end of file. each iteration is a new read buffer (chunk of data)
        for (int bufferSequentialNum = 1; bufferSequentialNum <= totalNumberOfRBuffers; bufferSequentialNum++) {
            int bytesInsideReadBuffer = inputFChan.read(readBuff);
            readBuff.rewind(); // set the position inside the buff to the beginning

            compressBytesFromRBUff(bytesInsideReadBuffer, byteToItsTreeLeaf);

            if (bufferSequentialNum == totalNumberOfRBuffers) { // true if this is the last Read Buffer
                logicForTheLastRBuff();
            }

            writeBytesFromContainerToFile(outputFChan);

            // new compressed bytes will overwrite previous compressed bytes in compressedDataChunk array
            compressedChunkIndex = 0;
        }

        int bytesInsideLastContainer = writeLastContainerToTheFile(outputFChan);

        addRedundantZeroesInLastCompressedByte(outputFChan, bytesInsideLastContainer);
    }

    /**
     * contains logic for the last read buffer
     */
    private void logicForTheLastRBuff() {
        if (bitsCountInRemembered > 0) { // true if there are some remembered bits
            lastContainer = rememberedBits;
            bitsCountInLastContainer = bitsCountInRemembered;
        } else if (freeBitsInContainer < Integer.SIZE) { // true if fourBytesContainer is not empty
            lastContainer = fourBytesContainer;
            bitsCountInLastContainer = Integer.SIZE - freeBitsInContainer;
        }
    }

    /**
     * writes redundantZeroesInLastCompressedByte to the beginning of the file
     *
     * @param outputFChan              reference to the output channel between our process and OS
     * @param bytesInsideLastContainer number of bytes in the last four bytes container
     * @throws IOException can be caused by reading or writing the file
     */
    private void addRedundantZeroesInLastCompressedByte(FileChannel outputFChan,
                                                        int bytesInsideLastContainer) throws IOException {
        outputFChan.position(0); // set the position of outputFChan to the beginning (see algorithmExplanation.txt)
        int redundantZeroesInLastCompressedByte = (bytesInsideLastContainer * Byte.SIZE) - bitsCountInLastContainer;

        ByteBuffer oneByteBuffer = ByteBuffer.allocate(1);
        oneByteBuffer.put((byte) redundantZeroesInLastCompressedByte);
        oneByteBuffer.rewind(); // set the position inside the buff to the beginning
        outputFChan.write(oneByteBuffer);
    }

    /**
     * writes bytes from container to the file
     *
     * @param outputFChan reference to the output channel between our process and OS
     * @throws IOException can be caused by reading or writing the file
     */
    private void writeBytesFromContainerToFile(FileChannel outputFChan) throws IOException {
        // if writeBuff has less or more bytes than in the compressedDataChunk array
        if (writeBuff.capacity() != (compressedChunkIndex * Integer.BYTES)) {
            writeBuff = ByteBuffer.allocate(compressedChunkIndex * Integer.BYTES);
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
     *
     * @param outputFChan reference to the output channel between our process and OS
     * @return amount of bytes inside last four bytes container
     * @throws IOException can be caused by reading or writing the file
     */
    private int writeLastContainerToTheFile(FileChannel outputFChan) throws IOException {
        int bytesInsideLastContainer = (int) Math.ceil(bitsCountInLastContainer / (double) Byte.SIZE);
        ByteBuffer lastWBuffer = ByteBuffer.allocate(bytesInsideLastContainer);

        int shift = Integer.SIZE - Byte.SIZE; // 24
        for (int i = 0; i < bytesInsideLastContainer; i++) {
            byte byteFromContainer = (byte) (lastContainer >>> shift);
            lastWBuffer.put(byteFromContainer);
            shift -= Byte.SIZE;
        }
        lastWBuffer.rewind(); // set the position inside the buff to the beginning

        outputFChan.write(lastWBuffer);

        return bytesInsideLastContainer;
    }

    /**
     * compresses the bytes, and puts compressed chunk to the array that will be copied to the buffer
     *
     * @param bytesInsideReadBuffer amount of bytes Inside Read Buffer
     * @param byteToItsTreeLeaf     key = unique byte; value = TreeLeaf that is created for corresponding byte
     */
    private void compressBytesFromRBUff(int bytesInsideReadBuffer, MyHashMap<Byte, TreeLeaf> byteToItsTreeLeaf) {
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
                rememberedBits = encodingOfTheByte << (Integer.SIZE - bitsCountInRemembered);
                encodingOfTheByte >>>= bitsCountInRemembered; // remove bits that can not fit to the container
                fourBytesContainer |= encodingOfTheByte;
                freeBitsInContainer = 0;
            }

            if (freeBitsInContainer == 0) { // true when fourBytesContainer is full
                compressedDataChunk[compressedChunkIndex] = fourBytesContainer;
                compressedChunkIndex++;
                fourBytesContainer = 0;
                freeBitsInContainer = Integer.SIZE;
            }
        }
        readBuff.rewind(); // set the position inside the buff to the beginning
    }

}