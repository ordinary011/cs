package com.shpp.p2p.cs.ldebryniuk.assignment17.archiver.binaryTree;

/**
 * The following class contains logic for Internal Node of the tree that is used for encoding the file
 */
public class InternalTreeNode extends BTreeNode {

    private BTreeNode leftChild = null;
    private BTreeNode rightChild = null;

    public InternalTreeNode() {
    }

    public InternalTreeNode(long weight, BTreeNode leftChild, BTreeNode rightChild) {
        super(weight);
        this.leftChild = leftChild;
        this.rightChild = rightChild;
    }

    /**
     * the following method is used only for decompression of the file. it recreates tree from byte encodings
     *
     * @param uniqueByte      unique byte from the file
     * @param byteEncoding    encoding for the unique byte
     * @param offsetFromRight offsetFromRight inside of the byte
     * @param currentBit      one of the bits from the encoding
     */
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
            currentBit = byteEncoding << (Integer.SIZE - offsetFromRight); // 00001111 after "<<" becomes 11100000
            // remove all bits that are on the right from current bit
            currentBit = currentBit >>> (Integer.SIZE - 1); // 11100000 >>> 00000001

            foundOrNewNode.recreateTreeLeaf(uniqueByte, byteEncoding, offsetFromRight, currentBit);
        }
    }

    /**
     * the following method is used for finding the tree leaf
     *
     * @param compressedByte      compressed byte
     * @param shiftBitsToTheRight amount of bits that need to be shifted to the right
     * @return tree node that is one level below current (can be treeLeaf or another internalNode)
     */
    public BTreeNode findEncodedByte(int compressedByte, int shiftBitsToTheRight) {
        int byteWithNeededBitAtTheEnd = compressedByte >>> shiftBitsToTheRight; // 11010000 >>> 4 becomes 00001101
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

    /**
     * adds bit to the encoding of the byte. this method is used when we build the tree
     *
     * @param bitToAdd either 0 or 1
     */
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
