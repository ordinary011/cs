package com.shpp.p2p.cs.ldebryniuk.assignment17.mycollection.collection_testing;

import com.shpp.p2p.cs.ldebryniuk.assignment17.mycollection.MyStack;
import com.shpp.p2p.cs.ldebryniuk.assignment17.mycollection.lists.MyArrayList;
import com.shpp.p2p.cs.ldebryniuk.assignment17.mycollection.lists.MyLinkedList;
import com.shpp.p2p.cs.ldebryniuk.assignment17.mycollection.queues.MyComparator;
import com.shpp.p2p.cs.ldebryniuk.assignment17.mycollection.queues.MyPriorityQueue;
import com.shpp.p2p.cs.ldebryniuk.assignment17.mycollection.queues.MyQueue;

import java.util.*;

/**
 * The entering class for all the tests
 */
public class CollectionTests {

    private final String LINKED_LIST = "linked list";
    private final String ARRAY_LIST = "array list";

    private final String PRIORITY_QUEUE = "priority queue";
    private final String FIFO_QUEUE = "fifo queue";
    private final String STACK = "stack";

    /**
     * start method for running all the tests
     */
    public void runTests() {
        testLists();

        testStackAndQueues();

        new HashMapTesting().runTests();
    }

    /**
     * tests both types of lists
     */
    private void testLists() {
        // linked list tests
        new ListTesting(new MyLinkedList<>(), new LinkedList<>(), LINKED_LIST).runTestsForList();

        // array list tests
        new ListTesting(new MyArrayList<>(), new ArrayList<>(), ARRAY_LIST).runTestsForList();
    }

    /**
     * starts tests for stack and both queues
     */
    private void testStackAndQueues() {
        // stack tests
        MyStack<Integer> myStack = new MyStack<>();
        Stack<Integer> nativeStack = new Stack<>();
        new QueueAndStackTesting(myStack::push, nativeStack::push, myStack::pop, nativeStack::pop, STACK).start();

        // FIFO queue tests
        MyQueue<Integer> myQueue = new MyQueue<>();
        Queue<Integer> nativeQueue = new LinkedList<>();
        new QueueAndStackTesting(myQueue::add, nativeQueue::add, myQueue::poll, nativeQueue::poll, FIFO_QUEUE).start();

        priorityQueueTests();
    }

    /**
     * starts tests for priority queue
     */
    private void priorityQueueTests() {
        MyComparator<Integer> myComparator = (num1, num2) -> num1 - num2;
        Comparator<Integer> nativeComparator = (num1, num2) -> num1 - num2;

        MyPriorityQueue<Integer> myPriorityQueue = new MyPriorityQueue<>(10, myComparator);
        PriorityQueue<Integer> nativePriorityQueue = new PriorityQueue<>(10, nativeComparator);

        new QueueAndStackTesting(myPriorityQueue::add, nativePriorityQueue::add,
                myPriorityQueue::poll, nativePriorityQueue::poll, PRIORITY_QUEUE).start();
    }

}
