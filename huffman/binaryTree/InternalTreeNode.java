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
                                 int bitPositionInEncoding, int currentBit, InternalTreeNode currentNode) {
        if (bitPositionInEncoding == 1) { // true if this is the last bit in encoding sequence
            if (currentBit == 1) {
                currentNode.rightChild = new TreeLeaf((byte) uniqueByte);
            } else { // currentBit == 0
                currentNode.leftChild = new TreeLeaf((byte) uniqueByte);
            }
        } else { // this is not the last bit in encoding sequence
            InternalTreeNode foundOrNewNode;
            if (currentBit == 1) {
                if (currentNode.rightChild == null) {
                    currentNode.rightChild = new InternalTreeNode();
                }
                foundOrNewNode = (InternalTreeNode) currentNode.rightChild;
            } else { // currentBit == 0
                if (currentNode.leftChild == null) {
                    currentNode.leftChild = new InternalTreeNode();
                }
                foundOrNewNode = (InternalTreeNode) currentNode.leftChild;
            }

            bitPositionInEncoding--; // set the position to the next bit in encoding
            // remove all all bits that are on the left from current bit
            currentBit = byteEncoding << (16 - bitPositionInEncoding); // 00001111 after "<<" becomes 11100000
            currentBit &= 0xFFFF; // 00000001 11100000 & 00000000 11111111 becomes 00000000 11100000

            currentBit = currentBit >>> (16 - 1); // 11100000 >>> 00000001 // todo int stetchik????
            currentBit &= 1;
            foundOrNewNode.recreateTreeLeaf(uniqueByte, byteEncoding, bitPositionInEncoding, currentBit, foundOrNewNode);
        }
    }

    public BTreeNode findEncodedByte(int encodedByte, int shiftBitsToTheRight) {
        int byteWithNeededBitAtTheEnd = encodedByte >>> shiftBitsToTheRight; // 11010000 >>> 4 becomes 00001101
        int neededBit = byteWithNeededBitAtTheEnd & 1;//retain only the lowest bit: 00001101 & 00000001 becomes 00000001

        if (neededBit == 1) {
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
