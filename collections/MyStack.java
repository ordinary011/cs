package com.shpp.p2p.cs.ldebryniuk.assignment16;

public class MyStack<T> {

    private StackNode lastNode = null;

    private class StackNode {
        private StackNode prevNode = null;
        private T value;

        private StackNode(T element) {
            this.value = element;
        }
    }

    /**
     * Adds element to the end of the stack
     * @param element
     */
    public void push(T element) {
        StackNode newNode = new StackNode(element);
        newNode.prevNode = lastNode;

        lastNode = newNode;
    }

    public T pop() {
        if (lastNode == null) {
            System.err.print("sorry, stack is empty");
            System.exit(1);
        }

        StackNode tmpNode = lastNode;

        if (lastNode.prevNode == null) { // only one element
            lastNode = null;
        } else { // if we have more than 1 el
            lastNode = lastNode.prevNode;
        }

        return tmpNode.value;
    }

    public T peek() {
        return lastNode.value;
    }

    public boolean isEmpty() {
        return lastNode == null;
    }
}
