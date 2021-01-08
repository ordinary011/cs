package com.shpp.p2p.cs.ldebryniuk.assignment17.mycollection.collection_testing;

import java.util.ArrayList;

@FunctionalInterface
interface QueueOrStackAttach<T> {
    void attach(T element);
}

@FunctionalInterface
interface QueueOrStackRetrieve<T> {
    T retrieve() throws Exception;
}

/**
 * The following class contains all the logic for queue and stack tests
 */
class QueueAndStackTesting {

    private final int MAX_NUM_OF_ELEMENTS = 100;
    private final String ANSI_GREEN = "\u001B[32m";
    private final String CURRENT_COLLECTION; // can either be "stack" or "FIFO queue" or "PRIORITY queue"

    private final ArrayList<Integer> arrOfAllAddedElements = new ArrayList<>();

    // reference for the (add or push) method in myQueue or in myStack
    private final QueueOrStackAttach<Integer> myStackOrQueueAttach;
    // reference for the (add or push) method in native Queue or in native Stack
    private final QueueOrStackAttach<Integer> nativeStackOrQueueAttach;
    // reference for the (poll or pop) method in myQueue or in myStack
    private final QueueOrStackRetrieve<Integer> myStackOrQueueRetrieve;
    // reference for the (poll or pop) method in native Queue or in native Stack
    private final QueueOrStackRetrieve<Integer> nativeStackOrQueueRetrieve;

    /**
     * @param myStackOrQueueAttach       reference for the (add or push) method in myQueue or in myStack
     * @param nativeStackOrQueueAttach   reference for the (add or push) method in native Queue or in native Stack
     * @param myStackOrQueueRetrieve     reference for the (poll or pop) method in myQueue or in myStack
     * @param nativeStackOrQueueRetrieve reference for the (poll or pop) method in native Queue or in native Stack
     * @param current_collection         can either be a "priority queue" or "fifo queue" or "stack"
     */
    public QueueAndStackTesting(QueueOrStackAttach<Integer> myStackOrQueueAttach,
                                QueueOrStackAttach<Integer> nativeStackOrQueueAttach,
                                QueueOrStackRetrieve<Integer> myStackOrQueueRetrieve,
                                QueueOrStackRetrieve<Integer> nativeStackOrQueueRetrieve,
                                String current_collection) {
        this.myStackOrQueueAttach = myStackOrQueueAttach;
        this.nativeStackOrQueueAttach = nativeStackOrQueueAttach;
        this.myStackOrQueueRetrieve = myStackOrQueueRetrieve;
        this.nativeStackOrQueueRetrieve = nativeStackOrQueueRetrieve;
        this.CURRENT_COLLECTION = current_collection;
    }

    /**
     * the starting method of the tests for queue and stack
     * contains common logic for tests of the FIFO queue, PRIORITY queue, and the stack
     */
    void start() {
        try {
            testBySampleData();

            addAndGetTheSameQuantity();

            addAndGetDifferentQuantities();

            addAndGetRandomNumbers();

            System.out.println(ANSI_GREEN + "successfully finished all the tests for " + CURRENT_COLLECTION);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @throws Exception specifies that a mismatch occurred between native collection and my own
     */
    private void testBySampleData() throws Exception {
        int[] arr = {4, 1, 7, 5, 6, 3, 2, 9, 8};
        for (int num : arr) {
            attachElements(num);
        }

        retrieveElements(arr.length); // +1 because we want to retrieve null el as well
        arrOfAllAddedElements.clear();
    }

    /**
     * the following method adds 1 elements and then retrieves 1 element.
     * In the next iteration it adds 2 elements and  retrieves 2 elements.
     * And it does so until MAX_NUM_OF_ELEMENTS is added and retrieved from the collection
     *
     * @throws Exception specifies that a mismatch occurred between native collection and my own
     */
    private void addAndGetTheSameQuantity() throws Exception {
        for (int numOfElements = 1; numOfElements < MAX_NUM_OF_ELEMENTS; numOfElements++) {
            // add numOfElementsToAdd to the collectionType
            attachNElements(numOfElements);

            // take out numOfElementsToAdd from the collectionType and check if they are the same
            retrieveElements(numOfElements);
        }
        arrOfAllAddedElements.clear();
    }


    /**
     * adds and retrieves different amounts of elements from the collection
     *
     * @throws Exception specifies that a mismatch occurred between native collection and my own
     */
    private void addAndGetDifferentQuantities() throws Exception {
        // add (50%) more than retrieve (25%)
        int needToAttachElements = (int) (MAX_NUM_OF_ELEMENTS * 0.5); // 50%
        attachNElements(needToAttachElements);
        int needToRetrieveElements = (int) (MAX_NUM_OF_ELEMENTS * 0.25); // 25%
        retrieveElements(needToRetrieveElements);

        // add (100%) less than retrieve (125% including previous attachment)
        needToAttachElements = MAX_NUM_OF_ELEMENTS;
        attachNElements(needToAttachElements);
        needToRetrieveElements = (int) (MAX_NUM_OF_ELEMENTS + (MAX_NUM_OF_ELEMENTS * 0.25)); // 125%
        retrieveElements(needToRetrieveElements);

        arrOfAllAddedElements.clear();
    }

    /**
     * adds elements to the current collection (to the queue or to the stack)
     *
     * @param numOfElementsToAttach amount of elements to add
     */
    private void attachNElements(int numOfElementsToAttach) {
        for (int i = 0; i < numOfElementsToAttach; i++) {
            attachElements(i);
        }
    }

    /**
     * @param elToAttach
     */
    private void attachElements(Integer elToAttach) {
        myStackOrQueueAttach.attach(elToAttach);
        nativeStackOrQueueAttach.attach(elToAttach);

        arrOfAllAddedElements.add(elToAttach);
    }

    /**
     * takes out numOfElementsToRetrieve from current collection (from the queue or from the stack)
     *
     * @param numOfElementsToRetrieve amount of elements to take out
     * @throws Exception specifies that a mismatch occurred between native collection and my own
     */
    private void retrieveElements(int numOfElementsToRetrieve) throws Exception {
        for (int i = numOfElementsToRetrieve; i > 0; i--) {
            if (!myStackOrQueueRetrieve.retrieve().equals(nativeStackOrQueueRetrieve.retrieve())) {
                System.out.println("all added elements: " + arrOfAllAddedElements);
                throw new Exception("mismatch occurred, when there were " + i + " element(s) in " + CURRENT_COLLECTION);
            }
        }
    }

    /**
     *
     */
    private void addAndGetRandomNumbers() throws Exception {
        // add random nums
        for (int i = 0; i < MAX_NUM_OF_ELEMENTS; i++) {
            int randomNum = (int) (Math.random() * MAX_NUM_OF_ELEMENTS);
            attachElements(randomNum);
        }

        retrieveElements(MAX_NUM_OF_ELEMENTS); // +1 because we want to retrieve null el as well
        arrOfAllAddedElements.clear();
    }

}
