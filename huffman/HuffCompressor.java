package com.shpp.p2p.cs.ldebryniuk.assignment15;

import com.shpp.p2p.cs.ldebryniuk.assignment15.binaryTree.BTree;
import com.shpp.p2p.cs.ldebryniuk.assignment15.binaryTree.TreeLeaf;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.PriorityQueue;

class HuffCompressor {

    private final int MEGABYTE = 1024 * 1024; // bytes
    private final HashMap<Byte, TreeLeaf> byteToByteTreeLeaf = new HashMap<>();
    private PriorityQueue<TreeLeaf> prioritizedTreeLeaves;
    /**
     * contains chunk of data that is read from the read channel
     */
    private final ByteBuffer readBuff = ByteBuffer.allocate(MEGABYTE);

    void compressFile(FileChannel inputFChan, FileChannel outputFChan, long inputFileSize) throws IOException {
        countUniqueBytes(inputFChan);

        prioritizedTreeLeaves = new BTree().prioritizeBytesAndBuildTree(byteToByteTreeLeaf);

//        for (int i = prioritizedTreeLeaves.size(); i > 0; i--) {
//            System.out.println(prioritizedTreeLeaves.poll());
//        }
//        System.out.println("oppaaaa");

        ByteBuffer writeBuff = ByteBuffer.allocate(prioritizedTreeLeaves.size());

        for (int i = 0; i < 10; i++) {
            writeBuff.put(
//                    prioritizedTreeLeaves.peek()
                    prioritizedTreeLeaves.poll().getByteValue()
            );
        }
        writeBuff.rewind();

        outputFChan.write(writeBuff);
    }

    private void countUniqueBytes(FileChannel inputFChan) throws IOException {
        // read the whole input file and find unique bytes
        int bytesInsideReadBuffer;
        while ((bytesInsideReadBuffer = inputFChan.read(readBuff)) != -1) { // read file; -1 means end of file
            readBuff.rewind(); // set the position inside the buff to the beginning

            // count unique bytes in the buffer
            for (int i = 0; i < bytesInsideReadBuffer; i++) {
                byte buffByte = readBuff.get();

                TreeLeaf treeNode = byteToByteTreeLeaf.get(buffByte);
                if (treeNode != null) {
                    treeNode.incrementWeight();
                } else {
                    byteToByteTreeLeaf.put(buffByte, new TreeLeaf(buffByte));
                }
            }
            readBuff.rewind(); // set the position inside the buff to the beginning
        }
    }
}

