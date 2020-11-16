package com.shpp.p2p.cs.ldebryniuk.assignment15;

import java.util.Comparator;

class TreeNode {

    private int weight = 1;
    private byte byteValue;
    private char tempChar; // todo delete this is for debugging

    private TreeNode leftChild = null;
    private TreeNode rightChild = null;

    private int usedBitesForEncoding = 0;
    private int encodingOfTheByte = 0;

    TreeNode(byte byteValue) {
        this.byteValue = byteValue;
        this.tempChar = (char) byteValue;
    }

    TreeNode(int weight, TreeNode leftChild, TreeNode rightChild) {
        this.weight = weight;
        this.leftChild = leftChild;
        this.rightChild = rightChild;
    }

    void incrementWeight() {
        weight++;
    }

    void addNewBitToEncoding(int bitToAdd) {
        if (bitToAdd == 1) {
            // e.g. (usedBitesForEncoding == 2) 00000001 -> 00000100
            int byteWithSetBitInRightPlace = bitToAdd << usedBitesForEncoding;

            encodingOfTheByte = encodingOfTheByte | byteWithSetBitInRightPlace;
            // 00000011 &
            // 00000100
            // 00000111
        }
        usedBitesForEncoding++;

        if (leftChild != null) {
            leftChild.addNewBitToEncoding(bitToAdd);
        }

        if (rightChild != null) {
            rightChild.addNewBitToEncoding(bitToAdd);
        }
    }

    int getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return "TreeNode{" +
                "weight=" + weight +
                ", byteValue=" + byteValue +
                ", tempChar=" + tempChar +
                ", leftChild=" + leftChild +
                ", rightChild=" + rightChild +
                ", usedBitesForEncoding=" + usedBitesForEncoding +
                ", encodingOfTheByte=" + encodingOfTheByte +
                '}';
    }
}

class TreeNodeComparator implements Comparator<TreeNode> {

    @Override
    public int compare(TreeNode treeNode1, TreeNode treeNode2) {
        if (treeNode1.getWeight() > treeNode2.getWeight())
            return 1;
        if (treeNode1.getWeight() < treeNode2.getWeight())
            return -1;
        return 0;
    }
}