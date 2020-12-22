package com.shpp.p2p.cs.ldebryniuk.assignment17.mycollection.queues;

import com.shpp.p2p.cs.ldebryniuk.assignment17.mycollection.MyComparator;

import java.util.ArrayList;

public class MyPriorityQueue<T> implements MyQueueI<T> {

    private final ArrayList<T> arr;
    private final MyComparator<T> comparator;

    public MyPriorityQueue(int initialCapacity, MyComparator<T> comparator) {
        arr = new ArrayList<>(initialCapacity + 1);
        arr.add(null); // first element is a dummy element because we will calculate array index starting from 1
        this.comparator = comparator;
    }


    @Override
    public void add(T newElement) {
        arr.add(newElement);
        if (arr.size() == 2) { // true if we have only one element in the arr (the first element is dummy element)
            return;
        }

        int indexOfNewElement = arr.size() - 1;
        int indexOfParent = (int) Math.floor(indexOfNewElement / 2.0);
        T parent = arr.get(indexOfParent);

        while (comparator.compare(newElement, parent) < 0) {
            // swap newElement and parent (bubble up)
            arr.set(indexOfParent, newElement);
            arr.set(indexOfNewElement, parent);

            indexOfNewElement = indexOfParent;
            indexOfParent = (int) Math.floor(indexOfNewElement / 2.0);

            parent = arr.get(indexOfParent);
        }
    }

    @Override
    public T poll() throws Exception {
        if (arr.size() == 1) { // first element in the arr is a dummy element
            throw new Exception("The priority queue is empty");
        }

        T returnVal = arr.get(1); // from the top of the heap
        T parent = arr.get(arr.size() - 1);
        arr.remove(arr.size() - 1);
        arr.set(1, parent); // put the last element to the top of the heap

        // bubble down
        int indexOfParent = 1;
        int rightChildIndex = (int) Math.ceil(indexOfParent * 2 + 1);
        T rightChild = arr.get(rightChildIndex);

        while (comparator.compare(rightChild, parent) < 0) {
            // swap parent and child (bubble down)
            arr.set(indexOfParent, rightChild);
            arr.set(rightChildIndex, parent);

            indexOfParent = rightChildIndex;
            rightChildIndex = (int) Math.ceil(indexOfParent * 2 + 1);

            if (rightChildIndex < arr.size()) {
                rightChild = arr.get(rightChildIndex);
            } else {
                break;
            }
        }

        return returnVal;
    }


    /**
     * returns element from the top of the heap but does not remove it
     *
     * @return element from the top of the heap
     */
    @Override
    public T peek() {
        // at index 0 we have a dummy element
        return arr.get(1);
    }
}
