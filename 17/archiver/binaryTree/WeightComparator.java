package com.shpp.p2p.cs.ldebryniuk.assignment17.archiver.binaryTree;

import com.shpp.p2p.cs.ldebryniuk.assignment17.mycollection.queues.MyComparator;

/**
 * The following class is used for comparing tree leaves by byte frequency in the file
 */
class WeightComparator implements MyComparator<BTreeNode> {

    @Override
    public int compare(BTreeNode treeNode1, BTreeNode treeNode2) {
        if (treeNode1.getWeight() > treeNode2.getWeight())
            return 1;
        if (treeNode1.getWeight() < treeNode2.getWeight())
            return -1;
        return 0;
    }

}
