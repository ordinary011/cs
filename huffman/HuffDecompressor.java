package com.shpp.p2p.cs.ldebryniuk.assignment15;

import com.shpp.p2p.cs.ldebryniuk.assignment15.binaryTree.BTree;
import com.shpp.p2p.cs.ldebryniuk.assignment15.binaryTree.TreeLeaf;
import com.shpp.p2p.cs.ldebryniuk.assignment15.binaryTree.TreeNodeComparator;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.PriorityQueue;

public class HuffDecompressor {

    private final ByteBuffer readBuff = ByteBuffer.allocate(1024 * 1024);

    /**
     * Starting method for decompressing the data
     *
     * @throws IOException
     */
    void decompressFile(FileChannel inputFChan, FileChannel outputFChan) throws IOException {
        // read first chunk of data to the buff
        inputFChan.read(readBuff);
        readBuff.rewind(); // set the position inside the buff to the beginning

        int redundantBitsInLastCompressedByte = readBuff.get();

        int secondByte = readBuff.get();
        int tableInfoSize = secondByte >>> 4;
        int theLongestEncodingLength = secondByte & 0x0F;

        int keysCountInTable = 0;
        byte[] tableInfo = new byte[tableInfoSize];
        for (int i = 0; i < tableInfoSize; i++) {
            byte byteCountOfSomeEncodingLength = readBuff.get();
            tableInfo[i] = byteCountOfSomeEncodingLength;
            keysCountInTable += byteCountOfSomeEncodingLength;
        }

        int tableSize = keysCountInTable * 2;
        byte[] table = new byte[tableSize];
        for (int i = 0; i < tableSize; i++) {

        }
    }

}



//    PriorityQueue<TreeLeaf> prioritizedTreeLeaves =
//            new PriorityQueue<>(10, new TreeNodeComparator());
//

//
//                for (int i = 0; i < 10; i++) {
//        TreeLeaf tl = new TreeLeaf(readBuff.get());
//        tl.setWeight(i);
//        prioritizedTreeLeaves.add(tl);
//        }
//
//        PriorityQueue<TreeLeaf> copyOfPrioritizedTreeLeaves = new PriorityQueue<>(prioritizedTreeLeaves);
//
//        new BTree().startBuildingTree(prioritizedTreeLeaves);
//
//        for (int i = copyOfPrioritizedTreeLeaves.size(); i > 0; i--) {
//        System.out.println(copyOfPrioritizedTreeLeaves.poll());
//        }