package com.shpp.p2p.cs.ldebryniuk.assignment16;

public class MyQueue<T> {

    private QueueNode firstNode = null;
    private QueueNode lastNode = null;

    private class QueueNode {
        private QueueNode nextNode = null;
        private T value;

        private QueueNode(T element) {
            this.value = element;
        }
    }

    /**
     * Adds element to the end of the queue
     * @param element
     */
    public void add(T element) {
        QueueNode newNode = new QueueNode(element);

        if (firstNode == null) { // true if queue is empty
            firstNode = newNode;
        } else { // there is at least 1 element in the queue
            lastNode.nextNode = newNode;
        }

        lastNode = newNode;
    }

    public T poll() {
        if (firstNode == null) {
            System.err.print("sorry, queue is empty");
            System.exit(1);
        }

        QueueNode nodeToPoll = firstNode;
        firstNode = firstNode.nextNode;

        if (firstNode == lastNode) { // only 1 el is in the queue
            lastNode = null;
        }

        return nodeToPoll.value;
    }

    public T peek() {
        return firstNode.value;
    }

    public boolean isEmpty() {
        return firstNode == null;
    }

}


// tests add and pull to zero adn than add again