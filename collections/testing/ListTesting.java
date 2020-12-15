package com.shpp.p2p.cs.ldebryniuk.assignment16.testing;

import com.shpp.p2p.cs.ldebryniuk.assignment16.mycollection.lists.MyArrayList;
import com.shpp.p2p.cs.ldebryniuk.assignment16.mycollection.lists.MyLinkedList;
import com.shpp.p2p.cs.ldebryniuk.assignment16.mycollection.lists.MyList;

import java.util.ArrayList;
import java.util.List;

/**
 * The following class contains all the logic for arrayList and LinkedList tests
 */
class ListTesting {

    private final int MAX_NUM_OF_ELEMENTS = 10; // should be an even number

    private final String LINKED_LIST = "linked list";
    private final String ARRAY_LIST = "array list";
    private String CURRENT_LIST;

    private MyList<Integer> myList;
    private List<Integer> nativeList;

    private final String ANSI_GREEN = "\u001B[32m";

    /**
     * the starting method of the tests for arrayList and LinkedList
     */
    void startListTesting() {
        // linked list tests
        myList = new MyLinkedList<>();
        nativeList = new ArrayList<>();
        runTestsForList(LINKED_LIST);

        // array list tests
        myList = new MyArrayList<>();
        nativeList = new ArrayList<>();
        runTestsForList(ARRAY_LIST);
    }

    /**
     * contains common logic for tests of the arrayList and LinkedList
     *
     * @param listType either "linked list" or "array list"
     */
    private void runTestsForList(String listType) {
        try {
            CURRENT_LIST = listType;

            addAndRemoveFromEndToStart();
            addAndRemoveFromStartToEnd();

            addOddNumbersToTheLists();
            addEvenNumbersToTheLists();
            removeOnlyOddNumbers();

            setMethodCheck();

            System.out.println(ANSI_GREEN + "successfully finished all the tests for " + listType);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * adds numbers to the end of the list and then removes elements from the end to start of the list
     *
     * @throws Exception specifies that native list and myList are different
     */
    private void addAndRemoveFromEndToStart() throws Exception {
        for (int i = 0; i < MAX_NUM_OF_ELEMENTS; i++) {
            addToTheEnd(i);
            checkInTwoLists(i);
        }

        // remove from the last element to the first
        for (int i = (MAX_NUM_OF_ELEMENTS - 1); i > 0; i--) {
            removeFromLists(i);
            checkInTwoLists(i - 1);
        }
        removeFromLists(0); // remove last elements from the lists
    }

    /**
     * add numbers to the end of the list (by using the index) and then removes elements from start to the end
     *
     * @throws Exception specifies that native list and myList are different
     */
    private void addAndRemoveFromStartToEnd() throws Exception {
        for (int i = 0; i < MAX_NUM_OF_ELEMENTS; i++) {
            addAtIndex(i, i);
            checkInTwoLists(i);
        }

        // remove from the first element to the last
        for (int i = 0; i < (MAX_NUM_OF_ELEMENTS - 1); i++) {
            removeFromLists(0);
            checkInTwoLists(0);
        }
        removeFromLists(0); // remove last elements from the lists
    }

    /**
     * adds odd numbers to the end of the list
     *
     * @throws Exception specifies that native list and myList are different
     */
    private void addOddNumbersToTheLists() throws Exception {
        int indexOfOddEl = 0;
        for (int i = 1; i < MAX_NUM_OF_ELEMENTS; i += 2) {
            addToTheEnd(i);
            checkInTwoLists(indexOfOddEl);
            indexOfOddEl++;
        }
    }

    /**
     * add even numbers to the lists
     *
     * @throws Exception specifies that native list and myList are different
     */
    private void addEvenNumbersToTheLists() throws Exception {
        for (int i = 0; i < MAX_NUM_OF_ELEMENTS; i += 2) {
            addAtIndex(i, i);
            checkInTwoLists(i);
        }
    }

    /**
     * the following method removes only odd numbers from the list
     *
     * @throws Exception specifies that native list and myList are different
     */
    private void removeOnlyOddNumbers() throws Exception {
        for (int i = 1; i <= (MAX_NUM_OF_ELEMENTS / 2); i++) {
            removeFromLists(i);
            checkInTwoLists(i - 1);
        }
    }

    /**
     * checks if set method works appropriately
     *
     * @throws Exception specifies that native list and myList are different
     */
    private void setMethodCheck() throws Exception {
        int numberToSet = 999;

        // set the first and the last element
        setInLists(0, numberToSet);
        setInLists((myList.size() - 1), numberToSet);

        // set all other elements
        for (int i = 1; i < myList.size(); i++) {
            setInLists(i, numberToSet);
        }

        // check if the nativeList and myList are the same
        for (int i = 0; i < nativeList.size(); i++) {
            checkInTwoLists(i);
        }
    }

    /**
     * adds an element to both lists
     *
     * @param valueToAdd value that will be added to both lists
     */
    private void addToTheEnd(int valueToAdd) {
        myList.add(valueToAdd);
        nativeList.add(valueToAdd);
    }

    /**
     * adds an element to both lists at the insertIndex
     *
     * @param insertIndex index at which the value will be added
     * @param valueToAdd  value that will be added to both lists
     * @throws Exception specifies that index is out of list boundaries
     */
    private void addAtIndex(int insertIndex, int valueToAdd) throws Exception {
        myList.add(insertIndex, valueToAdd);
        nativeList.add(insertIndex, valueToAdd);
    }

    /**
     * removes an element from both lists
     *
     * @param indexOfElToRemove index of the element to remove
     * @throws Exception specifies that index is out of list boundaries
     */
    private void removeFromLists(int indexOfElToRemove) throws Exception {
        myList.remove(indexOfElToRemove);
        nativeList.remove(indexOfElToRemove);
    }

    /**
     * changes the value of the element in indexInTheList
     *
     * @param indexInTheList index at which the value will be changed
     * @param valueToBeSet   new value that will be placed instead of the previous
     * @throws Exception specifies that index is out of list boundaries
     */
    private void setInLists(int indexInTheList, int valueToBeSet) throws Exception {
        myList.set(indexInTheList, valueToBeSet);
        nativeList.set(indexInTheList, valueToBeSet);
    }

    /**
     * takes values from both lists and checks if they are equal
     *
     * @param elIndexInTheList index at which values are taken from the lists
     * @throws Exception specifies that values are different in two lists
     */
    private void checkInTwoLists(int elIndexInTheList) throws Exception {
        if (!myList.get(elIndexInTheList).equals(nativeList.get(elIndexInTheList))) {
            throw new Exception("mismatch occurred, at index: " + elIndexInTheList + " in " + CURRENT_LIST);
        }
    }
}
