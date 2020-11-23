package com.shpp.p2p.cs.ldebryniuk.assignment15.binaryTree;

public class InternalTreeNode extends BTreeNode {

    private BTreeNode leftChild = null;
    private BTreeNode rightChild = null;

    public InternalTreeNode() { }

    public InternalTreeNode(long weight, BTreeNode leftChild, BTreeNode rightChild) {
        this.weight = weight;
        this.leftChild = leftChild;
        this.rightChild = rightChild;
    }

    public void recreateTreeLeaf(int uniqueByte, int byteEncoding,
                                 int bitPositionInEncoding, int currentBit, InternalTreeNode parentNode) {
        if (bitPositionInEncoding == 1) { // true if this is the last bit in encoding sequence
            if (currentBit == 1) {
                parentNode.rightChild = new TreeLeaf((byte) uniqueByte);
            } else { // currentBit == 0
                parentNode.leftChild = new TreeLeaf((byte) uniqueByte); // todo parent node may be current node
            }
        } else { // this is not the last bit in encoding sequence
            InternalTreeNode currentNode;
            if (currentBit == 1) {
                if (parentNode.rightChild == null) {
                    parentNode.rightChild = new InternalTreeNode();
                }
                currentNode = (InternalTreeNode) parentNode.rightChild;
            } else { // currentBit == 0
                if (parentNode.leftChild == null) {
                    parentNode.leftChild = new InternalTreeNode();
                }
                currentNode = (InternalTreeNode) parentNode.leftChild;
            }

            bitPositionInEncoding--; // set the position to the next bit in encoding
            currentBit = byteEncoding << (8 - bitPositionInEncoding); // 00001111 after "<<" becomes 11100000
            currentBit = currentBit & 0x000000FF; // 00000001 11100000 becomes 00000000 11100000
            currentBit = currentBit >>> (8 - 1); // 11100000 >>> 00000001
            currentNode.recreateTreeLeaf(uniqueByte, byteEncoding, bitPositionInEncoding, currentBit, currentNode);
        }
    }

    public BTreeNode findEncodedByte(int encodedByte, int shift) {
        encodedByte >>>= shift; // 11010000 becomes 00001101
        encodedByte &= 1; // 00001101 becomes 00000001
        if (encodedByte == 1) {
            return this.rightChild;
        } else { // encodedByte == 0
            return this.leftChild;
        }
    }

    @Override
    public boolean isTreeLeaf() {
        return false;
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
