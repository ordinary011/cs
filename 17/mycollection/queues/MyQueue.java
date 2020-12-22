package com.shpp.p2p.cs.ldebryniuk.assignment17.mycollection.queues;

import com.shpp.p2p.cs.ldebryniuk.assignment17.mycollection.MyCollection;

/**
 * Class that implements FIFO logic
 */
public class MyQueue<T> implements MyQueueI<T>, MyCollection {

    private QueueNode firstNode = null;
    private QueueNode lastNode = null;

    private class QueueNode {
        private QueueNode nextNode = null;
        private final T value;

        private QueueNode(T element) {
            this.value = element;
        }
    }

    /**
     * Adds element to the end of the queue
     *
     * @param element element that will be added to the queue. can be any reference type
     */
    @Override
    public void add(T element) {
        QueueNode newNode = new QueueNode(element);

        if (firstNode == null) { // true if queue is empty
            firstNode = newNode;
        } else { // there is at least 1 element in the queue
            lastNode.nextNode = newNode;
        }

        lastNode = newNode;
    }

    /**
     * retrieves the element from the beginning of the queue
     *
     * @return the element from the beginning of the queue
     * @throws Exception specifies that the queue is empty
     */
    @Override
    public T poll() throws Exception {
        if (firstNode == null) {
            throw new Exception("sorry, queue is empty");
        }

        QueueNode nodeToPoll = firstNode;
        firstNode = firstNode.nextNode;

        if (firstNode == lastNode) { // true if only 1 el is in the queue
            lastNode = null;
        }

        return nodeToPoll.value;
    }

    /**
     * returns the first element in the queue, but does not poll it from the queue. Element remains in the queue
     *
     * @return the element from the beginning of the queue
     */
    @Override
    public T peek() {
        return firstNode.value;
    }

    /**
     * returns false if queue is not empty and true if queue is empty
     *
     * @return false if queue is not empty and true if queue is empty
     */
    @Override
    public boolean isEmpty() {
        return firstNode == null;
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder("[");

        if (firstNode != null) { // firstNode == null true when list is empty
            QueueNode currentNode = firstNode;
            while (currentNode != null) {
                res.append(currentNode.value).append(", ");
                currentNode = currentNode.nextNode;
            }
            res.delete(res.length() - 2, res.length()); // remove coma and space after the last element
        }

        res.append("]");
        return res.toString();
    }
}
