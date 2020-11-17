package com.shpp.p2p.cs.ldebryniuk.assignment15.binaryTree;

public class InternalTreeNode extends BTreeNode {

    private BTreeNode leftChild = null;
    private BTreeNode rightChild = null;

    public InternalTreeNode(int weight, BTreeNode leftChild, BTreeNode rightChild) {
        super.weight = weight;
        this.leftChild = leftChild;
        this.rightChild = rightChild;
    }

    @Override
    public void addNewBitToEncoding(int bitToAdd) {
        if (leftChild != null) {
            leftChild.addNewBitToEncoding(bitToAdd);
        }

        if (rightChild != null) {
            rightChild.addNewBitToEncoding(bitToAdd);
        }
    }

    @Override
    public String toString() {
        return "InternalTreeNode{" +
                "leftChild=" + leftChild +
                ", rightChild=" + rightChild +
                ", weight=" + weight +
                '}';
    }
}
