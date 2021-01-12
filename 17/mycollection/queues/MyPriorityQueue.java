package com.shpp.p2p.cs.ldebryniuk.assignment17.mycollection.queues;

import com.shpp.p2p.cs.ldebryniuk.assignment17.mycollection.MyCollection;
import com.shpp.p2p.cs.ldebryniuk.assignment17.mycollection.lists.MyArrayList;

/**
 * class that is an implementation of priority queue data structure
 */
public class MyPriorityQueue<T> implements MyQueueI<T>, MyCollection {

    private final MyArrayList<T> arr;
    private final MyComparator<T> comparator;

    public MyPriorityQueue(int initialCapacity, MyComparator<T> comparator) {
        arr = new MyArrayList<>(initialCapacity + 1); // +1 because first element is a dummy element
        arr.add(null); // first element is a dummy element because we will calculate array index starting from 1
        this.comparator = comparator;
    }

    /**
     * adds a new element to the queue
     *
     * @param newElement element that needs to be added to the collection
     */
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

    /**
     * retrieves an element from top of the heap
     *
     * @return an element from top of the heap
     */
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

        swapWithChildren(indexOfFirstEl, elFromHeapEnd);

        return elFromHeapTop;
    }

    /**
     * first compares two children and then compares children with higher priority with parent and swaps them if needed
     *
     * @param indexOfElFromHeapEnd initially is set to 1, because we place it from end to the heap top
     * @param elFromHeapEnd        element was at the heap end
     *                             but at the moment this method is called, this element is at top of the heap
     */
    private void swapWithChildren(int indexOfElFromHeapEnd, T elFromHeapEnd) {
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

    /**
     * @return true if priority queue is empty
     */
    @Override
    public boolean isEmpty() {
        // first element (at index 0) is a dummy element
        return arr.size() == 1;
    }

    /**
     * @return amount of elements within the priority queue
     */
    public int size() {
        return arr.size() - 1;
    }
}
