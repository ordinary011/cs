package com.shpp.p2p.cs.ldebryniuk.assignment15;

import com.shpp.p2p.cs.ldebryniuk.assignment15.binaryTree.BTree;
import com.shpp.p2p.cs.ldebryniuk.assignment15.binaryTree.TreeLeaf;
import com.shpp.p2p.cs.ldebryniuk.assignment15.binaryTree.TreeNodeComparator;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

public class HuffDecompressor {

    /**
     * Starting method for decompressing the data
     *
     * @throws IOException
     */
    void decompressFile(FileChannel inputFChan, FileChannel outputFChan) throws IOException {
        final ByteBuffer readBuff = ByteBuffer.allocate(1024 * 1024);
        PriorityQueue<TreeLeaf> prioritizedTreeLeaves =
                new PriorityQueue<>(10, new TreeNodeComparator());

        // read first chunk of data to the buff
        inputFChan.read(readBuff);
        readBuff.rewind(); // set the position inside the buff to the beginning

        for (int i = 0; i < 10; i++) {
            TreeLeaf tl = new TreeLeaf(readBuff.get());
            tl.setWeight(i);
            prioritizedTreeLeaves.add(tl);
        }

        PriorityQueue<TreeLeaf> copyOfPrioritizedTreeLeaves = new PriorityQueue<>(prioritizedTreeLeaves);

        new BTree().startBuildingTree(prioritizedTreeLeaves);

        for (int i = copyOfPrioritizedTreeLeaves.size(); i > 0; i--) {
            System.out.println(copyOfPrioritizedTreeLeaves.poll());
        }
    }


}
