package com.shpp.p2p.cs.ldebryniuk.assignment17.archiver.binaryTree;

import com.shpp.p2p.cs.ldebryniuk.assignment17.mycollection.queues.MyComparator;

/**
 * The following class is used for comparing encoding lengths of tree leaves
 */
class EncodingLengthComparator implements MyComparator<TreeLeaf> {

    @Override
    public int compare(TreeLeaf treeNode1, TreeLeaf treeNode2) {
        if (treeNode1.getEncodingLength() > treeNode2.getEncodingLength())
            return 1;
        if (treeNode1.getEncodingLength() < treeNode2.getEncodingLength())
            return -1;
        return 0;
    }

}
