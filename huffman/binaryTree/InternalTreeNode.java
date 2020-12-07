package com.shpp.p2p.cs.ldebryniuk.assignment15.binaryTree;

public class InternalTreeNode extends BTreeNode {

    private BTreeNode leftChild = null;
    private BTreeNode rightChild = null;
    private final int BITS_IN_INTEGER = 32;

    public InternalTreeNode() {}

    public InternalTreeNode(long weight, BTreeNode leftChild, BTreeNode rightChild) {
        super(weight);
        this.leftChild = leftChild;
        this.rightChild = rightChild;
    }

    public void recreateTreeLeaf(int uniqueByte, int byteEncoding, int offsetFromRight, int currentBit) {
        if (offsetFromRight == 1) { // true if this is the last bit in encoding sequence
            if (currentBit == 1) {
                rightChild = new TreeLeaf((byte) uniqueByte);
            } else { // currentBit == 0
                leftChild = new TreeLeaf((byte) uniqueByte);
            }
        } else { // this is not the last bit in encoding sequence
            InternalTreeNode foundOrNewNode;
            if (currentBit == 1) {
                if (rightChild == null) {
                    rightChild = new InternalTreeNode();
                }
                foundOrNewNode = (InternalTreeNode) rightChild;
            } else { // currentBit == 0
                if (leftChild == null) {
                    leftChild = new InternalTreeNode();
                }
                foundOrNewNode = (InternalTreeNode) leftChild;
            }

            offsetFromRight--; // set the position to the next bit in encoding
            // remove all bits that are on the left from current bit
            currentBit = byteEncoding << (BITS_IN_INTEGER - offsetFromRight); // 00001111 after "<<" becomes 11100000
            // remove all bits that are on the right from current bit
            currentBit = currentBit >>> (BITS_IN_INTEGER - 1); // 11100000 >>> 00000001

            foundOrNewNode.recreateTreeLeaf(uniqueByte, byteEncoding, offsetFromRight, currentBit);
        }
    }

    public BTreeNode findEncodedByte(int encodedByte, int shiftBitsToTheRight) {
        int byteWithNeededBitAtTheEnd = encodedByte >>> shiftBitsToTheRight; // 11010000 >>> 4 becomes 00001101
        int onlyNeededBit = byteWithNeededBitAtTheEnd & 1; // only the lowest bit: 00001101 & 00000001 becomes 00000001

        if (onlyNeededBit == 1) {
            return this.rightChild;
        } else { // neededBit == 0
            return this.leftChild;
        }
    }

    @Override
    public boolean isTreeLeaf() {
        return false;
    }

    @Override
    void addNewBitToEncoding(int bitToAdd) {
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
                ", weight=" + getWeight() +
                '}';
    }
}
