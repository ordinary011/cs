package com.shpp.p2p.cs.ldebryniuk.assignment15.binaryTree;

public class TreeLeaf extends BTreeNode {

    private final byte byteValue;

    private int usedBitesForEncoding = 0;
    private int encodingOfTheByte = 0;

    public TreeLeaf(byte byteValue) {
        this.byteValue = byteValue;
    }

    public void incrementWeight() {
        super.incrementWeight();
    }

    public byte getByteValue() {
        return byteValue;
    }

    public int getEncodingLength() {
        return usedBitesForEncoding;
    }

    public int getEncodingOfTheByte() {
        return encodingOfTheByte;
    }

    @Override
    public boolean isTreeLeaf() {
        return true;
    }

    @Override
    void addNewBitToEncoding(int bitToAdd) {
        if (bitToAdd == 1) {
            // we add new bit to the beginning of already existed encoding, hence if e.g. (usedBitesForEncoding == 2)
            int byteWithSetBitInRightPlace = bitToAdd << usedBitesForEncoding; // 00000001 << 2 becomes 00000100

            encodingOfTheByte = encodingOfTheByte | byteWithSetBitInRightPlace; // 00000011 | 00000100 becomes 00000111
        }

        usedBitesForEncoding++;
    }

    @Override
    public String toString() {
        return "TreeNode{" +
                "weight=" + super.getWeight() +
                ", byteValue=" + byteValue +
                ", usedBitesForEncoding=" + usedBitesForEncoding +
                ", encodingOfTheByte=" + encodingOfTheByte +
                "}";
    }
}