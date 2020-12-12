package com.shpp.p2p.cs.ldebryniuk.assignment15.binaryTree;

/**
 * the following class contains common logic for every node (it can be TreeLeaf or InternalTreeNode)
 */
public abstract class BTreeNode {

    private long weight = 1;

    BTreeNode() {}

    BTreeNode(long weight) {
        this.weight = weight;
    }

    long getWeight() {
        return weight;
    }

    void incrementWeight() {
        weight++;
    }

    abstract public boolean isTreeLeaf();

    abstract void addNewBitToEncoding(int bitToAdd);

}
