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

    /**
     * Adds element to the end of the list
     * @param element
     */
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
        size++;
    }

    public void add(int pasteIndex, T element) {
        if (pasteIndex > size || pasteIndex < 0) {
            System.err.printf("Index %s is out of boundaries. Last index in the List is %d", pasteIndex, (size - 1));
            System.exit(1);
        } else if (pasteIndex == size) { // true when we add the element to the end of the list OR when list is empty
            add(element);
        } else {
            ListNode newNode = new ListNode(element);
            if (pasteIndex == 0) { // true when list is not empty and we need to add the el at the beginning of the list
                newNode.prevNode = null;
                newNode.nextNode = firstNode;

                firstNode.prevNode = newNode;
                firstNode = newNode;
            } else { // add somewhere whithin the list. not in the beginning nor at the end
                ListNode nodeBeforePasteIndex = firstNode;
                for (int i = 0; i < pasteIndex - 1; i++) { // todo later search optimization
                    nodeBeforePasteIndex = nodeBeforePasteIndex.nextNode;
                }
                newNode.prevNode = nodeBeforePasteIndex;
                newNode.nextNode = nodeBeforePasteIndex.nextNode;

                nodeBeforePasteIndex.nextNode = newNode;
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
        } else { // list has at least 1 element in it
            if (elIndex == 0) { // true when we remove at the beginning
                if (lastNode == firstNode) { // true if we have only 1 element in the list
                    lastNode = null;
                } else { // more than 1 element in the list
                    firstNode.nextNode.prevNode = null;
                }
                firstNode = firstNode.nextNode;
            } else if (elIndex == (size - 1)) { // remove the last element in the list
                lastNode.prevNode.nextNode = null;
                lastNode = lastNode.prevNode;
            } else { // somewhere in the middle
                ListNode nodeToRemove = firstNode;
                for (int i = 0; i < elIndex; i++) { // todo later search optimization
                    nodeToRemove = nodeToRemove.nextNode;
                }

                nodeToRemove.prevNode.nextNode = nodeToRemove.nextNode;
                nodeToRemove.nextNode.prevNode = nodeToRemove.prevNode;

//                if (lastNode == nodeToRemove) {
//                    lastNode = nodeToRemove.prevNode;
//                }
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

// for tests
// MyLinkedList<Integer> m = new MyLinkedList<>();
//        m.add(33);
//        m.remove(0);
//        m.add(34);