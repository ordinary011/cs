package com.shpp.p2p.cs.ldebryniuk.assignment15.binaryTree;

import java.util.Collection;
import java.util.HashMap;
import java.util.PriorityQueue;

public class BTree {

    /**
     * Creates priority queue of treeLeaves and its copy
     *
     * @param byteToByteTreeLeaf
     * @return
     */
    public PriorityQueue<TreeLeaf> prioritizeBytesAndBuildTree(HashMap<Byte, TreeLeaf> byteToByteTreeLeaf) {
        PriorityQueue<BTreeNode> prioritizedTreeLeaves =
                new PriorityQueue<>(byteToByteTreeLeaf.size(), new TreeNodeComparator());
        PriorityQueue<TreeLeaf> copyOfPrioritizedTreeLeaves =
                new PriorityQueue<>(byteToByteTreeLeaf.size(), new TreeNodeComparator());

        // add values to created queue
        prioritizedTreeLeaves.addAll(byteToByteTreeLeaf.values());
        // create a copy of prioritized queue
        copyOfPrioritizedTreeLeaves.addAll(byteToByteTreeLeaf.values());
//        PriorityQueue<BTreeNode> copyOfPrioritizedTreeLeaves = new PriorityQueue<>(prioritizedTreeLeaves);

        startBuildingTree(prioritizedTreeLeaves);

//        PriorityQueue<TreeLeaf> prioritizedByEncodingLength =
//                new PriorityQueue<>(byteToByteTreeLeaf.size(), new TreeNodeComparator());

        return copyOfPrioritizedTreeLeaves;
    }

    // todo private
    public void startBuildingTree(PriorityQueue<BTreeNode> orderedTreeLeaves) {
//        int size = orderedTreeLeaves.size();
//        for (int i = 0; i < size; i++) {
//            System.out.println(orderedTreeLeaves.poll());
//        }
        BTreeNode firstSmallestNode;
        BTreeNode secondSmallestNode;
        while (orderedTreeLeaves.size() > 1) { // till root node is created
            firstSmallestNode = orderedTreeLeaves.poll();
            secondSmallestNode = orderedTreeLeaves.poll();

            connectTwoNodes(firstSmallestNode, secondSmallestNode, orderedTreeLeaves);
        }
    }

    private void connectTwoNodes(BTreeNode firstSmallestNode, BTreeNode secondSmallestNode,
                                 PriorityQueue<BTreeNode> treeNodes) {
        long sumOfChildrenWeights = firstSmallestNode.getWeight() + secondSmallestNode.getWeight();

        treeNodes.add(new InternalTreeNode(sumOfChildrenWeights, firstSmallestNode, secondSmallestNode));

        firstSmallestNode.addNewBitToEncoding(0);
        secondSmallestNode.addNewBitToEncoding(1);
    }

//    private BTreeNode findSecondSmallestNode(PriorityQueue<InternalTreeNode> internalTreeNodes,
//                                             PriorityQueue<TreeLeaf> orderedTreeLeaves) {
//        if (!orderedTreeLeaves.isEmpty()) {
//
//            if (!internalTreeNodes.isEmpty()) { // true if both queues have nodes
////                return orderedTreeLeaves.peek().getWeight() <= internalTreeNodes.peek().getWeight() ?
////                        orderedTreeLeaves.poll() : internalTreeNodes.poll();
//
//                return internalTreeNodes.peek().getWeight() <= orderedTreeLeaves.peek().getWeight() ?
//                        internalTreeNodes.poll() : orderedTreeLeaves.poll();
//            } else { // nodes are present only in orderedTreeNodes queue
//                return orderedTreeLeaves.poll();
//            }
//        }
//
//        // nodes are present only in internalTreeNodes queue
//        return internalTreeNodes.poll();
//    }

}
