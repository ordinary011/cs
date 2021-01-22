package com.shpp.p2p.cs.ldebryniuk.assignment15.binaryTree;

import java.util.HashMap;
import java.util.PriorityQueue;

/**
 * The following class contains all the logic for building the tree and prioritizing tree leaves
 */
public class BTree {

    /**
     * Creates priority queue of treeLeaves and its copy
     *
     * @param byteToItsTreeLeaf key = unique byte; value = TreeLeaf that is created for corresponding byte
     * @return ordered by encoding length tree leaves (in descending order)
     */
    public PriorityQueue<TreeLeaf> prioritizeAndBuildTree(HashMap<Byte, TreeLeaf> byteToItsTreeLeaf) {
        // create a prioritizedByWeight queue of tree leaves
        PriorityQueue<BTreeNode> prioritizedByWeight =
                new PriorityQueue<>(byteToItsTreeLeaf.size(), new WeightComparator());
        prioritizedByWeight.addAll(byteToItsTreeLeaf.values());

        // create a copy of prioritizedByWeight queue
        PriorityQueue<BTreeNode> copyOfPrioritizedByWeight = new PriorityQueue<>(prioritizedByWeight);

        buildTree(prioritizedByWeight);

        return prioritizeBytesByEncodingLength(copyOfPrioritizedByWeight);
    }

    /**
     * builds the tree based in the frequencies of bytes
     *
     * @param orderedByWeightAscending ordered (in ascending order) by frequencies tree leaves
     */
    private void buildTree(PriorityQueue<BTreeNode> orderedByWeightAscending) {
        BTreeNode firstSmallestNode;
        BTreeNode secondSmallestNode;

        if (orderedByWeightAscending.size() == 1) { // true if we have only 1 unique byte in the file
            orderedByWeightAscending.poll().addNewBitToEncoding(0);
        }

        while (orderedByWeightAscending.size() > 1) { // till root node is created
            firstSmallestNode = orderedByWeightAscending.poll();
            secondSmallestNode = orderedByWeightAscending.poll();

            connectTwoNodes(firstSmallestNode, secondSmallestNode, orderedByWeightAscending);
        }
    }

    /**
     * connect two nodes by creating a parent node
     *
     * @param firstSmallestNode  tree node with the smallest byte frequency in the file
     * @param secondSmallestNode tree node with the second smallest byte frequency in the file
     * @param treeNodes          prioritised by byte frequency queue of tree leaves
     */
    private void connectTwoNodes(BTreeNode firstSmallestNode, BTreeNode secondSmallestNode,
                                 PriorityQueue<BTreeNode> treeNodes) {
        long sumOfChildrenWeights = firstSmallestNode.getWeight() + secondSmallestNode.getWeight();

        treeNodes.add(new InternalTreeNode(sumOfChildrenWeights, firstSmallestNode, secondSmallestNode));

        firstSmallestNode.addNewBitToEncoding(0);
        secondSmallestNode.addNewBitToEncoding(1);
    }

    /**
     * prioritises tree leaves by encoding length of the byte
     *
     * @param orderedByWeightAscending ordered (in ascending order) by frequencies tree leaves
     * @return ordered by encoding length tree leaves (in descending order)
     */
    private PriorityQueue<TreeLeaf> prioritizeBytesByEncodingLength(PriorityQueue<BTreeNode> orderedByWeightAscending) {
        int leavesCount = orderedByWeightAscending.size();
        PriorityQueue<TreeLeaf> prioritizedByEncodingLength =
                new PriorityQueue<>(leavesCount, new EncodingLengthComparator());

        for (int i = 0; i < leavesCount; i++) {
            prioritizedByEncodingLength.add(
                    (TreeLeaf) orderedByWeightAscending.poll()
            );
        }

        return prioritizedByEncodingLength;
    }

}
