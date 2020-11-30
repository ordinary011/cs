package com.shpp.p2p.cs.ldebryniuk.assignment15.binaryTree;

public class TreeLeaf extends BTreeNode {

    private byte byteValue;
    private char tempChar; // todo delete this is for debugging

    private int usedBitesForEncoding = 0;
    private int encodingOfTheByte = 0;

    public TreeLeaf(byte byteValue) {
        this.byteValue = byteValue;
        this.tempChar = (char) byteValue;
    }

    public void incrementWeight() {
        weight++;
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

    public void setWeight(int weight) {
        super.weight = weight;
    }

    // todo delete later this is for tests only
    public void setEncodingLength(int length) {
        usedBitesForEncoding = length;
    }

    @Override
    public boolean isTreeLeaf() {
        return true;
    }

    @Override
    public void addNewBitToEncoding(int bitToAdd) {
        if (bitToAdd == 1) {
            // e.g. (usedBitesForEncoding == 2) 00000001 -> 00000100
            int byteWithSetBitInRightPlace = bitToAdd << usedBitesForEncoding;

            encodingOfTheByte = encodingOfTheByte | byteWithSetBitInRightPlace;
            // 00000011 |
            // 00000100
            // 00000111
        }

        usedBitesForEncoding++;
    }

//    @Override
//    public BTreeNode findEncodedByte(int encodedByte, int shift) {
//        return null;
//    }

    @Override
    public String toString() {
        return "TreeNode{" +
                "weight=" + weight +
                ", tempChar=" + tempChar +
                ", byteValue=" + byteValue +
                ", usedBitesForEncoding=" + usedBitesForEncoding +
                ", encodingOfTheByte=" + encodingOfTheByte +
                "}";
    }
}