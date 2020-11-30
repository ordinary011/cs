package com.shpp.p2p.cs.ldebryniuk.assignment15;

import com.shpp.p2p.cs.ldebryniuk.assignment15.binaryTree.BTree;
import com.shpp.p2p.cs.ldebryniuk.assignment15.binaryTree.BTreeNode;
import com.shpp.p2p.cs.ldebryniuk.assignment15.binaryTree.TreeLeaf;
import com.shpp.p2p.cs.ldebryniuk.assignment15.binaryTree.TreeNodeComparator;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.*;

class HuffCompressor {

        private final int MEGABYTE = 1024 * 1024; // bytes
//    private final int MEGABYTE = 5; // bytes
    private final HashMap<Byte, TreeLeaf> byteToItsTreeLeaf = new HashMap<>();
    /**
     * contains chunk of data that is read from the read channel
     */
    private final ByteBuffer readBuff = ByteBuffer.allocate(MEGABYTE);
    ByteBuffer writeBuff;

    void compressFile(FileChannel inputFChan, FileChannel outputFChan, long inputFileSize) throws IOException {
        countUniqueBytes(inputFChan);

        PriorityQueue<TreeLeaf> allTreeLeaves = new BTree().prioritizeBytesAndBuildTree(byteToItsTreeLeaf);

        PriorityQueue<TreeLeaf> prioritizeBytesByENcodingLength = prioritizeBytesByENcodingLength(allTreeLeaves);

        createTable(prioritizeBytesByENcodingLength, outputFChan);

        compressAndWriteData(inputFChan, outputFChan);
    }

    private PriorityQueue<TreeLeaf> prioritizeBytesByENcodingLength(PriorityQueue<TreeLeaf> allTreeLeaves) {
        int leavesCount = allTreeLeaves.size();
        PriorityQueue<TreeLeaf> prioritizedByEncodingLength =
                new PriorityQueue<>(leavesCount, new EncodingLengthComparator());

        for (int i = 0; i < leavesCount; i++) {
            prioritizedByEncodingLength.add(
                    allTreeLeaves.poll()
            );
        }

        return prioritizedByEncodingLength;
    }

    private void countUniqueBytes(FileChannel inputFChan) throws IOException {
        // read the whole input file and find unique bytes
        int bytesInsideReadBuffer;
        while ((bytesInsideReadBuffer = inputFChan.read(readBuff)) != -1) { // read file; -1 means end of file
            readBuff.rewind(); // set the position inside the buff to the beginning

            // count unique bytes in the buffer
            for (int i = 0; i < bytesInsideReadBuffer; i++) {
                byte byteFromAFile = readBuff.get();

                TreeLeaf treeNode = byteToItsTreeLeaf.get(byteFromAFile);
                if (treeNode != null) {
                    treeNode.incrementWeight();
                } else {
                    byteToItsTreeLeaf.put(byteFromAFile, new TreeLeaf(byteFromAFile));
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
        for (int i = 0; i < table.length; i += 2) {
            TreeLeaf byteAsATreeLeaf = prioritizedTreeLeaves.poll();

            // write byte and its encoding to the table
            table[i] = byteAsATreeLeaf.getByteValue();
            table[i + 1] = (byte) byteAsATreeLeaf.getEncodingOfTheByte();

            // count bytes with certain encoding length
            int usedBitesForEncoding = byteAsATreeLeaf.getEncodingLength();
            // increment bytes count with usedBitesForEncoding length
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

        int firstByte = 0; // will contain redundant bits count In Last compressed Byte;
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
        int totalNumberOfBuffers = (int) Math.ceil(inputFChan.size() / (double) MEGABYTE);
        byte[] compressedDataChunk = new byte[MEGABYTE];
        int compressedDataChunkIndAfterLastByte = 0;

        int compressedByte = 0;
        int availableBitsInCompressedByte = 8;

        int rememberedBits = 0;
        int bitsCountInRemembered = 0;

        // read file by chunks till end of file. each iteration is a new read buffer (chunk of data)
        int bytesInsideReadBuffer;
        for (int bufferSequentialNum = 1; bufferSequentialNum <= totalNumberOfBuffers; bufferSequentialNum++) {
            bytesInsideReadBuffer = inputFChan.read(readBuff);
            readBuff.rewind(); // set the position inside the buff to the beginning

            // each iteration is a new byte within current read buffer
            for (int i = 0; i < bytesInsideReadBuffer; i++) {
                byte byteFromTheReadFile = readBuff.get();
                TreeLeaf leafForCurrentByte = byteToItsTreeLeaf.get(byteFromTheReadFile);
                int encodingOfTheByte = leafForCurrentByte.getEncodingOfTheByte();
                int usedBitsForEncodingTheOriginalByte = leafForCurrentByte.getEncodingLength();

                if (bitsCountInRemembered > 0) {
                    compressedByte |= rememberedBits; // move rememberedBits to compressedByte
                    availableBitsInCompressedByte -= bitsCountInRemembered;
                    bitsCountInRemembered = 0;
                    rememberedBits = 0;
                }

                availableBitsInCompressedByte -= usedBitsForEncodingTheOriginalByte;
                if (availableBitsInCompressedByte >= 0) { // true when there is a place to fit the whole encoding
                    // move low order bits to left 00000010 << 5 becomes 01000000
                    encodingOfTheByte <<= availableBitsInCompressedByte;
                    // combine new byte encoded bits with bits that encoded previous byte(s)
                    compressedByte |= encodingOfTheByte; // 01000000 | 00011000 becomes 01011000
                } else { // compressedByte can not fit all the encoding in it
                    bitsCountInRemembered = Math.abs(availableBitsInCompressedByte);
                    rememberedBits = encodingOfTheByte << (8 - bitsCountInRemembered);// remember bits that can not fit to compressed byte
                    encodingOfTheByte >>>= bitsCountInRemembered; // remove bits that can not fit to compressed byte
                    compressedByte |= encodingOfTheByte;
                    availableBitsInCompressedByte = 0;
                }

                if (availableBitsInCompressedByte == 0) { // true when compressed byte is full
                    compressedDataChunk[compressedDataChunkIndAfterLastByte] = (byte) compressedByte;
                    compressedDataChunkIndAfterLastByte++;
                    compressedByte = 0;
                    availableBitsInCompressedByte = 8;
                }
            }
            readBuff.rewind(); // set the position inside the buff to the beginning

            // if this is the last Read Buffer
            if (bufferSequentialNum == totalNumberOfBuffers) {
                if (bitsCountInRemembered > 0) { // true when there are some remembered bits
                    // availableBitsInCompressedByte will be used for determining redundantBitsInLastCompressedByte
                    availableBitsInCompressedByte -= bitsCountInRemembered;
                    compressedDataChunk[compressedDataChunkIndAfterLastByte] = (byte) rememberedBits;
                    compressedDataChunkIndAfterLastByte++;
                } else if (availableBitsInCompressedByte < 8) { // true if compressedByte is not empty yet
                    compressedDataChunk[compressedDataChunkIndAfterLastByte] = (byte) compressedByte;
                    compressedDataChunkIndAfterLastByte++;
                }
            }

            if (writeBuff.capacity() != compressedDataChunkIndAfterLastByte) {
                writeBuff = ByteBuffer.allocate(compressedDataChunkIndAfterLastByte);
            }

            for (int i = 0; i < compressedDataChunkIndAfterLastByte; i++) {
                writeBuff.put(compressedDataChunk[i]);
            }
            writeBuff.rewind();

            outputFChan.write(writeBuff);
            writeBuff.rewind();

            compressedDataChunkIndAfterLastByte = 0;
        }

        int redundantBitsInLastCompressedByte = (availableBitsInCompressedByte == 8) ? 0 : availableBitsInCompressedByte;
        outputFChan.position(0);
        ByteBuffer oneByteBuffer = ByteBuffer.allocate(1);
        oneByteBuffer.put((byte) redundantBitsInLastCompressedByte);
        oneByteBuffer.rewind();
        outputFChan.write(oneByteBuffer);
    }

}

class EncodingLengthComparator implements Comparator<TreeLeaf> {

    @Override
    public int compare(TreeLeaf treeNode1, TreeLeaf treeNode2) {
        if (treeNode1.getEncodingLength() > treeNode2.getEncodingLength())
            return -1;
        if (treeNode1.getEncodingLength() < treeNode2.getEncodingLength())
            return 1;
        return 0;
    }

}