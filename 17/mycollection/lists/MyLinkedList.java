package com.shpp.p2p.cs.ldebryniuk.assignment17.mycollection.lists;

import com.shpp.p2p.cs.ldebryniuk.assignment17.mycollection.MyCollection;

import java.util.Iterator;

/**
 * class that contains all the elements as nodes. Each node is doubly linked
 */
public class MyLinkedList<T> implements MyList<T>, Iterable<T>, MyCollection {

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
     *
     * @param element element that will be added to the list. can be any reference type
     */
    @Override
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

    /**
     * Adds element at the specified position (pasteIndex) in the list
     *
     * @param pasteIndex position where new element should be inserted
     * @param element    element that will be added to the list. can be any reference type
     * @throws Exception specifies that index is out of boundaries
     */
    @Override
    public void add(int pasteIndex, T element) throws Exception {
        if (pasteIndex > size || pasteIndex < 0) {
            throw new Exception("Index " + pasteIndex + " is out of boundaries. Last index in the List: " + (size - 1));
        } else if (pasteIndex == size) { // true when we add the element to the end of the list OR when list is empty
            add(element);
        } else {
            ListNode newNode = new ListNode(element);
            if (pasteIndex == 0) { // true when list is not empty and we need to add the el at the beginning of the list
                newNode.prevNode = null;
                newNode.nextNode = firstNode;

                firstNode.prevNode = newNode;
                firstNode = newNode;
            } else { // add somewhere within the list. Neither at the beginning nor at the end
                ListNode nodeBeforePasteIndex = firstNode;
                for (int i = 0; i < pasteIndex - 1; i++) { // find nodeBeforePasteIndex
                    nodeBeforePasteIndex = nodeBeforePasteIndex.nextNode;
                }
                ListNode nodeAfterPasteIndex = nodeBeforePasteIndex.nextNode;

                newNode.prevNode = nodeBeforePasteIndex;
                newNode.nextNode = nodeAfterPasteIndex;

                nodeBeforePasteIndex.nextNode = newNode;
                nodeAfterPasteIndex.prevNode = newNode;
            }

            size++;
        }
    }

    /**
     * retrieves the element at the specified index (elIndex)
     *
     * @param elIndex index of element to retrieve
     * @return the element at the specified index (elIndex)
     * @throws Exception specifies that index is out of boundaries
     */
    @Override
    public T get(int elIndex) throws Exception {
        checkForBoundaries(elIndex);

        ListNode currentNode = firstNode;
        for (int i = 0; i < elIndex; i++) {
            currentNode = currentNode.nextNode;
        }
        return currentNode.value;
    }

    /**
     * changes the value of an element at the specified index (elIndex)
     *
     * @param elIndex index of an element that should be changed
     * @param element new value that should be put at the specified index (elIndex)
     * @throws Exception specifies that index is out of boundaries
     */
    @Override
    public void set(int elIndex, T element) throws Exception {
        checkForBoundaries(elIndex);

        ListNode currentNode = firstNode;
        for (int i = 0; i < elIndex; i++) {
            currentNode = currentNode.nextNode;
        }
        currentNode.value = element;
    }

    /**
     * deletes an element at the specified index (elIndex)
     *
     * @param elIndex index of an element to remove
     * @throws Exception specifies that index is out of boundaries
     */
    @Override
    public void remove(int elIndex) throws Exception {
        checkForBoundaries(elIndex);

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
        } else { // remove somewhere in the middle
            ListNode nodeToRemove = firstNode;
            for (int i = 0; i < elIndex; i++) {
                nodeToRemove = nodeToRemove.nextNode;
            }

            nodeToRemove.prevNode.nextNode = nodeToRemove.nextNode;
            nodeToRemove.nextNode.prevNode = nodeToRemove.prevNode;
        }
        size--;
    }

    /**
     * checks if index of an element is within the boundaries of our list
     *
     * @param elIndex index of the element
     * @throws Exception specifies that index is out of boundaries
     */
    private void checkForBoundaries(int elIndex) throws Exception {
        if (elIndex >= size || elIndex < 0 || firstNode == null) {
            throw new Exception("Index " + elIndex + " is out of boundaries. Last index in the List is " + (size - 1));
        }
    }

    /**
     * returns the size of the list
     *
     * @return the size of the list
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * returns false if array is not empty and true if array is empty
     *
     * @return false if array is not empty and true if array is empty
     */
    @Override
    public boolean isEmpty() {
        return firstNode == null && lastNode == null;
    }

    /**
     * merges current list with a new one
     *
     * @param newListToAppend new list that is to be merged with the current
     */
    public void addAll(MyLinkedList<T> newListToAppend) {
        if (newListToAppend.isEmpty()) {
            return;
        }

        ListNode firstNodeInNextList = newListToAppend.firstNode;

        firstNodeInNextList.prevNode = this.lastNode;

        if (this.firstNode == null) {
            this.firstNode = firstNodeInNextList;
        }

        if (this.lastNode != null) {
            this.lastNode.nextNode = firstNodeInNextList;
        }

        lastNode = newListToAppend.lastNode;
        size += newListToAppend.size;
    }

    private class MyLinkedListIterator<T> implements Iterator<T> {
        private ListNode currentNode = firstNode;

        @Override
        public boolean hasNext() {
            return currentNode != null;
        }

        @Override
        public T next() {
            T nodeValue = (T) currentNode.value;
            currentNode = currentNode.nextNode;

            return nodeValue;
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new MyLinkedListIterator<>();
    }


    @Override
    public String toString() {
        StringBuilder res = new StringBuilder("[");

        if (firstNode != null) { // firstNode == null true when list is empty
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