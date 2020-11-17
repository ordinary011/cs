package com.shpp.p2p.cs.ldebryniuk.assignment15.binaryTree;

abstract class BTreeNode {

    protected int weight = 1;

    protected int getWeight() {
        return weight;
    }

    abstract public void addNewBitToEncoding(int bitToAdd);
}
