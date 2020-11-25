package com.shpp.p2p.cs.ldebryniuk.assignment16;

public class MyLinkedList<T> {

    private ListNode firstNode = null;
    private ListNode lastNode = null;
    private int size = 0;

    private class ListNode {
        private ListNode nextNode = null;
        private ListNode prevNode = null;
        private T value;

        private ListNode(T element) {
            this.value = element;
        }
    }

    public void add(T element) {
        ListNode newNode = new ListNode(element);
        newNode.prevNode = lastNode;
        newNode.nextNode = null;

        if (firstNode == null) {
            firstNode = newNode;
        }

        if (lastNode != null) {
            lastNode.nextNode = newNode;
        }

        lastNode = newNode;
        size++; // todo check for last node and first where do they point?
    }

    public void add(int pasteIndex, T element) {
        if (pasteIndex > size || pasteIndex < 0) {
            System.err.printf("Index %s is out of boundaries. Last index in the List is %d", pasteIndex, (size - 1));
            System.exit(1);
        } else if (pasteIndex == size) { // true when we add the element to the end OR when list is empty
            add(element);
        } else {
            ListNode newNode = new ListNode(element);
            if (pasteIndex == 0) {
                newNode.prevNode = null;
                newNode.nextNode = firstNode;

                firstNode.prevNode = newNode;
                firstNode = newNode;
            } else {
                ListNode nodeBeforePasteIndex = firstNode;
                for (int i = 0; i < pasteIndex - 1; i++) { // todo later search optimization
                    nodeBeforePasteIndex = nodeBeforePasteIndex.nextNode;
                }
                newNode.prevNode = nodeBeforePasteIndex;
                newNode.nextNode = nodeBeforePasteIndex.nextNode;

                nodeBeforePasteIndex.nextNode = newNode; // todo check for last node and first where do they point?
            }

            size++;
        }
    }

    public T get (int elIndex) {
        if (elIndex >= size || elIndex < 0 || firstNode == null) {
            System.err.printf("Index %s is out of boundaries. Last index in the List is %d", elIndex, (size - 1));
            System.exit(1);
        } else {
            ListNode currentNode = firstNode;
            for (int i = 0; i < elIndex; i++) { // todo later search optimization
                currentNode = currentNode.nextNode;
            }
            return currentNode.value;
        }

        return null;
    }

    public void set (int elIndex, T element) {
        if (elIndex >= size || elIndex < 0 || firstNode == null) {
            System.err.printf("Index %s is out of boundaries. Last index in the List is %d", elIndex, (size - 1));
            System.exit(1);
        } else {
            ListNode currentNode = firstNode;
            for (int i = 0; i < elIndex; i++) { // todo later search optimization
                currentNode = currentNode.nextNode;
            }
            currentNode.value = element;
        }
    }

    public void remove(int elIndex) { // todo first and last nodes references need to be changed
        if (elIndex >= size || elIndex < 0 || firstNode == null) {
            System.err.printf("Index %s is out of boundaries. Last index in the List is %d", elIndex, (size - 1));
            System.exit(1);
        } else {
            if (elIndex == 0) {
                firstNode = firstNode.nextNode;
            } else {
                ListNode currentNode = firstNode;
                for (int i = 0; i < elIndex; i++) { // todo later search optimization
                    currentNode = currentNode.nextNode;
                }

                currentNode.prevNode.nextNode = currentNode.nextNode;
                currentNode.nextNode.prevNode = currentNode.prevNode;
            }
        }
    }


    public int size() {
        return size;
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder("[");

        if (firstNode != null) { // firstNode == null if list is empty
            ListNode currentNode = firstNode;
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

// todo tests for empty lists
