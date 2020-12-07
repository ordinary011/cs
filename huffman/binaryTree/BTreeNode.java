package com.shpp.p2p.cs.ldebryniuk.assignment15.binaryTree;

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
