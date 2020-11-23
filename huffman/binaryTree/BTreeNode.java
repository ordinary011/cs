package com.shpp.p2p.cs.ldebryniuk.assignment15.binaryTree;

public abstract class BTreeNode {

    protected long weight = 1;

    public long getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    abstract public boolean isTreeLeaf();

    abstract public void addNewBitToEncoding(int bitToAdd);

//    abstract public BTreeNode findEncodedByte(int encodedByte, int shift);
}
