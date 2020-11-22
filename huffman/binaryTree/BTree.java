package com.shpp.p2p.cs.ldebryniuk.assignment15.binaryTree;

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
        PriorityQueue<TreeLeaf> prioritizedTreeLeaves =
                new PriorityQueue<>(byteToByteTreeLeaf.size(), new TreeNodeComparator());

        // add values to created queue
        prioritizedTreeLeaves.addAll(byteToByteTreeLeaf.values());
        // create a copy of prioritized queue
        PriorityQueue<TreeLeaf> copyOfPrioritizedTreeLeaves = new PriorityQueue<>(prioritizedTreeLeaves);

        startBuildingTree(prioritizedTreeLeaves);

        return copyOfPrioritizedTreeLeaves;
    }

    // todo private
    public void startBuildingTree(PriorityQueue<TreeLeaf> orderedTreeLeaves) {
        PriorityQueue<InternalTreeNode> internalTreeNodes = new PriorityQueue<>(new TreeNodeComparator());
        BTreeNode firstSmallestNode;
        BTreeNode secondSmallestNode;

        while (!orderedTreeLeaves.isEmpty()) {
            firstSmallestNode = orderedTreeLeaves.poll();
            secondSmallestNode = findSecondSmallestNode(internalTreeNodes, orderedTreeLeaves);

            connectTwoNodes(firstSmallestNode, secondSmallestNode, internalTreeNodes);
        }

        while (internalTreeNodes.size() > 1) { // till root node is created
            firstSmallestNode = internalTreeNodes.poll();
            secondSmallestNode = internalTreeNodes.poll();

            connectTwoNodes(firstSmallestNode, secondSmallestNode, internalTreeNodes);
        }
    }

    private void connectTwoNodes(BTreeNode firstSmallestNode, BTreeNode secondSmallestNode,
                                 PriorityQueue<InternalTreeNode> internalTreeNodes) {
        long sumOfChildrenWeights = firstSmallestNode.getWeight() + secondSmallestNode.getWeight();

        internalTreeNodes.add(new InternalTreeNode(sumOfChildrenWeights, firstSmallestNode, secondSmallestNode));

        firstSmallestNode.addNewBitToEncoding(0);
        secondSmallestNode.addNewBitToEncoding(1);
    }

    private BTreeNode findSecondSmallestNode(PriorityQueue<InternalTreeNode> internalTreeNodes,
                                             PriorityQueue<TreeLeaf> orderedTreeNodes) {
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
