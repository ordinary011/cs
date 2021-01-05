package com.shpp.p2p.cs.ldebryniuk.assignment17.mycollection.queues;

import java.util.ArrayList;

public class MyPriorityQueue<T> implements MyQueueI<T> {

    // todo make private
    public final ArrayList<T> arr;
    private final MyComparator<T> comparator;

    public MyPriorityQueue(int initialCapacity, MyComparator<T> comparator) {
        arr = new ArrayList<>(initialCapacity + 1); // +1 because first element is a dummy element
        arr.add(null); // first element is a dummy element because we will calculate array index starting from 1
        this.comparator = comparator;
    }

    @Override
    public void add(T newElement) {
        arr.add(newElement);
        if (arr.size() == 2) { // true if we have only one element in the arr (the first element is a dummy element)
            return;
        }

        int indexOfNewElement = arr.size() - 1;
        int indexOfParent = (int) Math.floor(indexOfNewElement / 2.0);
        T parent = arr.get(indexOfParent);

        while (indexOfParent != 0 && comparator.compare(newElement, parent) < 0) {
            // swap newElement and parent (bubble up)
            arr.set(indexOfParent, newElement);
            arr.set(indexOfNewElement, parent);

            indexOfNewElement = indexOfParent;
            indexOfParent = (int) Math.floor(indexOfNewElement / 2.0);

            parent = arr.get(indexOfParent);
        }
    }

    @Override
    public T poll() {
        if (arr.size() == 1) { // first element in the arr is a dummy element
            return null;
        }

        int indexOfFirstEl = 1;
        int indexOfLastEl = arr.size() - 1;

        T elFromHeapTop = arr.get(indexOfFirstEl);
        T elFromHeapEnd = arr.get(indexOfLastEl);

        arr.remove(indexOfLastEl);

        if (arr.size() == 1) { // true if there was only one element inside
            return elFromHeapTop;
        }

        arr.set(indexOfFirstEl, elFromHeapEnd);

        int indexOfElFromHeapEnd = indexOfFirstEl;
        while (true) { // while has at least one child
            int indexOfLeftChild = indexOfElFromHeapEnd * 2;
            int indexOfRightChild = indexOfElFromHeapEnd * 2 + 1;

            if (indexOfLeftChild >= arr.size()) { // true if there are no more children
                break;
            }

            T leftChild = arr.get(indexOfLeftChild);
            T rightChild;

            T childWithHigherPriority = leftChild;
            int indexOfChildWithPriority = indexOfLeftChild;

            if (indexOfRightChild < arr.size()) { // true if there is a right child
                rightChild = arr.get(indexOfRightChild);

                if (comparator.compare(rightChild, leftChild) < 0) {
                    childWithHigherPriority = rightChild;
                    indexOfChildWithPriority = indexOfRightChild;
                }
            }

            if (comparator.compare(childWithHigherPriority, elFromHeapEnd) < 0) {
                arr.set(indexOfElFromHeapEnd, childWithHigherPriority);
                arr.set(indexOfChildWithPriority, elFromHeapEnd);
                indexOfElFromHeapEnd = indexOfChildWithPriority;
            } else {
                break;
            }
        }

        return elFromHeapTop;
    }

    /**
     * returns element from the top of the heap but does not remove it
     *
     * @return element from the top of the heap
     */
    @Override
    public T peek() {
        // at index 0 we have a dummy element
        // todo check peeek when there are no elements
        return arr.get(1);
    }
}


//        if (arr.size() == 1) { // first element in the arr is a dummy element
//            return null;
//        }
//
//        int indexOfElementToRemove = arr.size() - 1;
//
//        int indexAtTopOfTheHeap = 1;
//        T fromTopOfTheHeap = arr.get(indexAtTopOfTheHeap);
//        T elFromTheEnd = arr.get(indexOfElementToRemove);
//        arr.set(indexAtTopOfTheHeap, elFromTheEnd);
//        int indexOfElFromTheEnd = indexAtTopOfTheHeap;
//
//        if (arr.size() == 4) { // 3 elements
//            int indexOfLeftChild = indexOfElFromTheEnd * 2;
//            T leftChild = arr.get(indexOfLeftChild); // second element
//            if (comparator.compare(leftChild, elFromTheEnd) < 0) {
//                arr.set(indexOfElFromTheEnd, leftChild);
//                arr.set(indexOfLeftChild, elFromTheEnd);
//            }
//        }
//
//        if (arr.size() == 5) { // 4 elements
//            int indexOfLeftChild = indexOfElFromTheEnd * 2;
//            int indexOfRightChild = indexOfElFromTheEnd * 2 + 1;
//            T leftChild = arr.get(indexOfLeftChild);
//            T rightChild = arr.get(indexOfRightChild);
//
//            T childToCompareWith;
//            if (comparator.compare(rightChild, leftChild) < 0) {
//                childToCompareWith = rightChild;
//            } else {
//                childToCompareWith = leftChild;
//            }
//
//            if (comparator.compare(childToCompareWith, elFromTheEnd) < 0) {
//                arr.set(indexOfElFromTheEnd, leftChild);
//                arr.set(indexOfLeftChild, elFromTheEnd);
//            }
//        }
//
//        if (arr.size() == 6) { // 5 elements
//            int indexOfLeftChild = indexOfElFromTheEnd * 2;
//            int indexOfRightChild = indexOfElFromTheEnd * 2 + 1;
//            T leftChild = arr.get(indexOfLeftChild);
//            T rightChild = arr.get(indexOfRightChild);
//
//            T childToCompareWith;
//            int indexOfChildToCompareWith;
//            if (comparator.compare(rightChild, leftChild) < 0) {
//                childToCompareWith = rightChild;
//                indexOfChildToCompareWith = indexOfRightChild;
//            } else {
//                childToCompareWith = leftChild;
//                indexOfChildToCompareWith = indexOfLeftChild;
//            }
//
//            if (comparator.compare(childToCompareWith, elFromTheEnd) < 0) {
//                arr.set(indexOfElFromTheEnd, leftChild);
//                arr.set(indexOfChildToCompareWith, elFromTheEnd);
//                indexOfElFromTheEnd = indexOfChildToCompareWith;
//            }
//
//            indexOfLeftChild = indexOfElFromTheEnd * 2;
//            leftChild = arr.get(indexOfLeftChild);
//            if (comparator.compare(leftChild, elFromTheEnd) < 0) {
//                arr.set(indexOfElFromTheEnd, leftChild);
//                arr.set(indexOfLeftChild, elFromTheEnd);
//            }
//        }
//
//        arr.remove(indexOfElementToRemove);
//
//        return fromTopOfTheHeap;
//    }