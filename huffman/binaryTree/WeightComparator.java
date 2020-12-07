package com.shpp.p2p.cs.ldebryniuk.assignment15.binaryTree;

import java.util.Comparator;

class WeightComparator implements Comparator<BTreeNode> {

    @Override
    public int compare(BTreeNode treeNode1, BTreeNode treeNode2) {
        if (treeNode1.getWeight() > treeNode2.getWeight())
            return 1;
        if (treeNode1.getWeight() < treeNode2.getWeight())
            return -1;
        return 0;
    }

}
