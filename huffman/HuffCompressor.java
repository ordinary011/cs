package com.shpp.p2p.cs.ldebryniuk.assignment15;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.PriorityQueue;

class HuffCompressor {

    private final int MAGABYTE = 1024 * 1024; // bytes
    private final HashMap<Byte, TreeNode> byteToByteFrequency = new HashMap<>();
    private PriorityQueue<TreeNode> copyOfPrioritizedTreeNodes;
    /**
     * contains chunk of data that is read from the read channel
     */
    protected final ByteBuffer readBuff = ByteBuffer.allocate(MAGABYTE);

    void compressFile(FileChannel inputFChan, FileChannel outputFChan, long inputFileSize) throws IOException {
        countUniqueBytes(inputFChan);

        createBinaryHeap();


    }

    private void countUniqueBytes(FileChannel inputFChan) throws IOException {
        // read the whole input file and find unique bytes
        int bytesInsideReadBuffer;
        while ((bytesInsideReadBuffer = inputFChan.read(readBuff)) != -1) { // read file; -1 means end of file
            readBuff.rewind(); // set the position inside the buff to the beginning

            for (int i = 0; i < bytesInsideReadBuffer; i++) {
                byte buffByte = readBuff.get();

                TreeNode treeNode = byteToByteFrequency.get(buffByte);
                if (treeNode != null) {
                    treeNode.incrementWeight();
                } else {
                    byteToByteFrequency.put(buffByte, new TreeNode(buffByte));
                }
            }
            readBuff.rewind(); // set the position inside the buff to the beginning
        }
    }

    private void createBinaryHeap() {
        PriorityQueue<TreeNode> orderedTreeNodes =
                new PriorityQueue<>(byteToByteFrequency.size(), new TreeNodeComparator());
        orderedTreeNodes.addAll(byteToByteFrequency.values());
        copyOfPrioritizedTreeNodes = new PriorityQueue<>(orderedTreeNodes);

//        for (int i = orderedTreeNodes.size(); i > 0; i--) {
//            System.out.println(orderedTreeNodes.poll());
//        }

        startBuildingHeap(orderedTreeNodes);
    }

    private void startBuildingHeap(PriorityQueue<TreeNode> orderedTreeNodes) {
        PriorityQueue<TreeNode> internalTreeNodes = new PriorityQueue<>(new TreeNodeComparator());
        TreeNode firstSmallestNode;
        TreeNode secondSmallestNode;

        while (!orderedTreeNodes.isEmpty()) {
            firstSmallestNode = orderedTreeNodes.poll();
            secondSmallestNode = findSecondSmallestNode(internalTreeNodes, orderedTreeNodes);

            connectTwoNodes(firstSmallestNode, secondSmallestNode, internalTreeNodes);
        }

        while (internalTreeNodes.size() > 1) { // till root node is created
            firstSmallestNode = internalTreeNodes.poll();
            secondSmallestNode = internalTreeNodes.poll();

            connectTwoNodes(firstSmallestNode, secondSmallestNode, internalTreeNodes);
        }
    }

    private void connectTwoNodes(TreeNode firstSmallestNode, TreeNode secondSmallestNode,
                                 PriorityQueue<TreeNode> internalTreeNodes) {
        int sumOfChildrenWeights = firstSmallestNode.getWeight() + secondSmallestNode.getWeight();

        internalTreeNodes.add(new TreeNode(sumOfChildrenWeights, firstSmallestNode, secondSmallestNode));

        firstSmallestNode.addNewBitToEncoding(0);
        secondSmallestNode.addNewBitToEncoding(1);
    }

    private TreeNode findSecondSmallestNode(PriorityQueue<TreeNode> internalTreeNodes,
                                        PriorityQueue<TreeNode> orderedTreeNodes) {
        if (!orderedTreeNodes.isEmpty()) {

            if (!internalTreeNodes.isEmpty()) { // true if both queues have nodes
                return internalTreeNodes.peek().getWeight() <= orderedTreeNodes.peek().getWeight() ?
                        internalTreeNodes.poll() : orderedTreeNodes.poll();
            } else { // nodes are present only in orderedTreeNodes queue
                return orderedTreeNodes.poll();
            }
        }

        // nodes are present only in internalTreeNodes queue
        return internalTreeNodes.poll();
    }


}

