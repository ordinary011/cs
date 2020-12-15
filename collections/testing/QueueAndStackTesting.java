package com.shpp.p2p.cs.ldebryniuk.assignment16.testing;

import com.shpp.p2p.cs.ldebryniuk.assignment16.mycollection.MyQueue;
import com.shpp.p2p.cs.ldebryniuk.assignment16.mycollection.MyStack;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

@FunctionalInterface
interface QueueOrStackAttach<T> {
    void attach(T element);
}

@FunctionalInterface
interface QueueOrStackRetrieve<T> {
    T retrieve() throws Exception;
}

class QueueAndStackTesting {

    private final int MAX_NUM_OF_ELEMENTS = 100;
    private final String QUEUE = "queue";
    private final String STACK = "stack";
    private final String ANSI_GREEN = "\u001B[32m";

    /**
     * the starting method of the tests for queue and stack
     */
    void startQueueAndStackTests() {
        // queue tests
        MyQueue<Integer> myQueue = new MyQueue<>();
        Queue<Integer> nativeQueue = new LinkedList<>();
        beginTests(myQueue::add, nativeQueue::add, myQueue::poll, nativeQueue::poll, QUEUE);

        // stack tests
        MyStack<Integer> myStack = new MyStack<>();
        Stack<Integer> nativeStack = new Stack<>();
        beginTests(myStack::push, nativeStack::push, myStack::pop, nativeStack::pop, STACK);
    }

    /**
     * contains common logic for tests of the queue and the stack
     *
     * @param myStackOrQueueAttach       reference for the (add or push) method in myQueue or in myStack
     * @param nativeStackOrQueueAttach   reference for the (add or push) method in native Queue or in native Stack
     * @param myStackOrQueueRetrieve     reference for the (poll or pop) method in myQueue or in myStack
     * @param nativeStackOrQueueRetrieve reference for the (poll or pop) method in native Queue or in native Stack
     * @param collectionType             can either be a "queue" or "stack"
     */
    private void beginTests(QueueOrStackAttach<Integer> myStackOrQueueAttach,
                            QueueOrStackAttach<Integer> nativeStackOrQueueAttach,
                            QueueOrStackRetrieve<Integer> myStackOrQueueRetrieve,
                            QueueOrStackRetrieve<Integer> nativeStackOrQueueRetrieve, String collectionType) {
        try {
            addAndGetTheSameQuantity(myStackOrQueueAttach, nativeStackOrQueueAttach,
                    myStackOrQueueRetrieve, nativeStackOrQueueRetrieve, collectionType);

            addAndGetDifferentQuantities(myStackOrQueueAttach, nativeStackOrQueueAttach,
                    myStackOrQueueRetrieve, nativeStackOrQueueRetrieve, collectionType);

            System.out.println(ANSI_GREEN + "successfully finished all the tests for " + collectionType);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * the following class adds 1 elements and then retrieves 1 element.
     * In the next iteration it adds 2 elements and  retrieves 2 elements.
     * And it does so until MAX_NUM_OF_ELEMENTS is added and retrieved from the collection
     *
     * @param myStackOrQueueAttach       reference for the (add or push) method in myQueue or in myStack
     * @param nativeStackOrQueueAttach   reference for the (add or push) method in native Queue or in native Stack
     * @param myStackOrQueueRetrieve     reference for the (poll or pop) method in myQueue or in myStack
     * @param nativeStackOrQueueRetrieve reference for the (poll or pop) method in native Queue or in native Stack
     * @param collectionType             can either be a "queue" or "stack"
     * @throws Exception specifies that a mismatch occurred between native collection and my own
     */
    private void addAndGetTheSameQuantity(QueueOrStackAttach<Integer> myStackOrQueueAttach,
                                          QueueOrStackAttach<Integer> nativeStackOrQueueAttach,
                                          QueueOrStackRetrieve<Integer> myStackOrQueueRetrieve,
                                          QueueOrStackRetrieve<Integer> nativeStackOrQueueRetrieve,
                                          String collectionType) throws Exception {
        for (int numOfElements = 1; numOfElements < MAX_NUM_OF_ELEMENTS; numOfElements++) {
            // add numOfElementsToAdd to the collectionType
            for (int j = 0; j < numOfElements; j++) {
                myStackOrQueueAttach.attach(j);
                nativeStackOrQueueAttach.attach(j);
            }

            // take out numOfElementsToAdd from the collectionType and check if they are the same
            for (int j = 0; j < numOfElements; j++) {
                if (!myStackOrQueueRetrieve.retrieve().equals(nativeStackOrQueueRetrieve.retrieve())) {
                    throw new Exception(
                            "mismatch happened, when there were " + collectionType + " elements in " + collectionType
                    );
                }
            }
        }
    }

    /**
     * adds and retrieves different amounts of elements from the collection
     *
     * @param myStackOrQueueAttach       reference for the (add or push) method in myQueue or in myStack
     * @param nativeStackOrQueueAttach   reference for the (add or push) method in native Queue or in native Stack
     * @param myStackOrQueueRetrieve     reference for the (poll or pop) method in myQueue or in myStack
     * @param nativeStackOrQueueRetrieve reference for the (poll or pop) method in native Queue or in native Stack
     * @param collectionType             can either be a "queue" or "stack"
     * @throws Exception specifies that a mismatch occurred between native collection and my own
     */
    private void addAndGetDifferentQuantities(QueueOrStackAttach<Integer> myStackOrQueueAttach,
                                              QueueOrStackAttach<Integer> nativeStackOrQueueAttach,
                                              QueueOrStackRetrieve<Integer> myStackOrQueueRetrieve,
                                              QueueOrStackRetrieve<Integer> nativeStackOrQueueRetrieve,
                                              String collectionType) throws Exception {
        // add (50%) more than retrieve (25%)
        int needToAttachElements = (int) (MAX_NUM_OF_ELEMENTS * 0.5); // 50%
        for (int i = 0; i < needToAttachElements; i++) {
            myStackOrQueueAttach.attach(i);
            nativeStackOrQueueAttach.attach(i);
        }
        int needToRetrieveElements = (int) (MAX_NUM_OF_ELEMENTS * 0.25); // 25%
        for (int i = 0; i < needToRetrieveElements; i++) {
            if (!myStackOrQueueRetrieve.retrieve().equals(nativeStackOrQueueRetrieve.retrieve())) {
                throw new Exception("mismatch happened, when there were " + i + " elements in " + collectionType);
            }
        }

        // add (100%) less than retrieve (125% including previous attachment)
        for (int i = 0; i < MAX_NUM_OF_ELEMENTS; i++) {
            myStackOrQueueAttach.attach(i);
            nativeStackOrQueueAttach.attach(i);
        }
        needToRetrieveElements = (int) (MAX_NUM_OF_ELEMENTS + (MAX_NUM_OF_ELEMENTS * 0.25)); // 125%
        for (int i = 0; i < needToRetrieveElements; i++) {
            if (!myStackOrQueueRetrieve.retrieve().equals(nativeStackOrQueueRetrieve.retrieve())) {
                throw new Exception("mismatch happened, when there were " + i + " elements in " + collectionType);
            }
        }
    }

}
