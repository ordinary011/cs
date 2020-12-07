package com.shpp.p2p.cs.ldebryniuk.assignment15.binaryTree;

import java.util.HashMap;
import java.util.PriorityQueue;

public class BTree {

    /**
     * Creates priority queue of treeLeaves and its copy
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

    private void connectTwoNodes(BTreeNode firstSmallestNode, BTreeNode secondSmallestNode,
                                 PriorityQueue<BTreeNode> treeNodes) {
        long sumOfChildrenWeights = firstSmallestNode.getWeight() + secondSmallestNode.getWeight();

        treeNodes.add(new InternalTreeNode(sumOfChildrenWeights, firstSmallestNode, secondSmallestNode));

        firstSmallestNode.addNewBitToEncoding(0);
        secondSmallestNode.addNewBitToEncoding(1);
    }

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
