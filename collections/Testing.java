package com.shpp.p2p.cs.ldebryniuk.assignment16;

import com.shpp.p2p.cs.ldebryniuk.assignment16.mycollection.MyQueue;
import com.shpp.p2p.cs.ldebryniuk.assignment16.mycollection.lists.MyArrayList;
import com.shpp.p2p.cs.ldebryniuk.assignment16.mycollection.MyStack;
import com.shpp.p2p.cs.ldebryniuk.assignment16.mycollection.lists.MyLinkedList;
import com.shpp.p2p.cs.ldebryniuk.assignment16.mycollection.lists.MyList;

import java.util.*;

public class Testing { }

class StackTesting {

    public void stackTests() throws Exception {
        MyStack<Integer> myStack = new MyStack<>();
        Stack<Integer> stack = new Stack<>();

        int maxNumOfElements = 10;
        for (int elementsInIteration = 1; elementsInIteration < maxNumOfElements; elementsInIteration++) {
            // add elementsInIteration to the queue
            for (int j = 0; j < elementsInIteration; j++) {
                myStack.push(j);
                stack.push(j);
            }

            // poll elementsInIteration from the queue and check if they are the same
            for (int j = 0; j < elementsInIteration; j++) {
                if (!myStack.pop().equals(stack.pop())) {
                    System.err.println("mismatch happened");
                }
            }
        }

        mixedMethodCalls();

        System.out.println("successfully finished all the stack tests");
    }

    private void mixedMethodCalls() throws Exception {
        MyStack<Integer> myStack = new MyStack<>();
        Stack<Integer> stack = new Stack<>();

        // add more than pop
        for (int i = 0; i < 50; i++) {
            myStack.push(i);
            stack.push(i);
        }
        for (int i = 0; i < 25; i++) {
            if (!myStack.pop().equals(stack.pop())) {
                System.err.println("mismatch happened");
            }
        }

        // pop more than add
        for (int i = 0; i < 100; i++) {
            myStack.push(i);
            stack.push(i);
        }
        for (int i = 0; i < 125; i++) {
            if (!myStack.pop().equals(stack.pop())) {
                System.err.println("mismatch happened");
            }
        }
    }


}

class QueueTesting {

    public void queueTests() throws Exception {
        MyQueue<Integer> myQueue = new MyQueue<>();
        Queue<Integer> queue = new LinkedList<>();

        int maxNumOfElements = 10;
        for (int elementsInIterationNum = 1; elementsInIterationNum < maxNumOfElements; elementsInIterationNum++) {
            // add elementsInIterationNum to the queue
            for (int j = 0; j < elementsInIterationNum; j++) {
                myQueue.add(j);
                queue.add(j);
            }

            // poll elementsInIterationNum from the queue and check if they are the same
            for (int j = 0; j < elementsInIterationNum; j++) {
                if (!myQueue.poll().equals(queue.poll())) {
                    System.err.println("mismatch happened");
                }
            }
        }

        mixedMethodCalls();

        System.out.println("successfully finished all the queue tests");
    }

    private void mixedMethodCalls() throws Exception {
        MyQueue<Integer> myQueue = new MyQueue<>();
        Queue<Integer> queue = new LinkedList<>();

        // add more than poll
        for (int i = 0; i < 50; i++) {
            myQueue.add(i);
            queue.add(i);
        }
        for (int i = 0; i < 25; i++) {
            if (!myQueue.poll().equals(queue.poll())) {
                System.err.println("mismatch happened");
            }
        }

        // poll more than add
        for (int i = 0; i < 100; i++) {
            myQueue.add(i);
            queue.add(i);
        }
        for (int i = 0; i < 125; i++) {
            if (!myQueue.poll().equals(queue.poll())) {
                System.err.println("mismatch happened");
            }
        }
    }
}


class ListTesting {

//    private MyList<Integer> myArrList = new MyLinkedList<>();
    private MyList<Integer> myArrList = new MyArrayList<>();

    void mainTestsForList() {
        try {
            int maxNumOfNodes = 10; // should be an even number
            // add elements with one param add
            for (int i = 0; i < maxNumOfNodes; i++) {
                myArrList.add(i);

                if (myArrList.get(i) != i) {
                    throw new Exception("linkedListTests failed to get an element at index: " + i);
                }
            }

            // remove from the last node to the first
            for (int i = (maxNumOfNodes - 1); i > 0; i--) {
                myArrList.remove(i);

                if (myArrList.get(i - 1) != (i - 1)) {
                    throw new Exception("linkedListTests failed to remove an element at index: " + i);
                }
            }
            myArrList.remove(0); // remove last element from the list
            if (!myArrList.isEmpty()) {
                throw new Exception("linkedListTests failed to remove an element at index: " + 0);
            }


            // add elements with two params add
            for (int i = 0; i < maxNumOfNodes; i++) {
                myArrList.add(i, i);

                if (myArrList.get(i) != i) {
                    throw new Exception("linkedListTests failed to get an element at index: " + i);
                }
            }

            // remove from the first node to the last
            for (int i = 0; i < (maxNumOfNodes - 1); i++) {
                myArrList.remove(0);

                if (myArrList.get(0) != (i + 1)) {
                    throw new Exception("linkedListTests failed to remove an element at index: " + i);
                }
            }
            myArrList.remove(0); // remove last element from the list
            if (!myArrList.isEmpty()) {
                throw new Exception("linkedListTests failed to remove an element at index: " + 0);
            }


            // add odd elements with one param add
            int indexOfOddEl = 0;
            for (int i = 1; i < maxNumOfNodes; i += 2) {
                myArrList.add(i);

                if (myArrList.get(indexOfOddEl) != i) {
                    throw new Exception("linkedListTests failed to get an element at index: " + i);
                }
                indexOfOddEl++;
            }

            // add even elements with two params add
            for (int i = 0; i < maxNumOfNodes; i += 2) {
                myArrList.add(i, i);

                if (myArrList.get(i) != i) {
                    throw new Exception("linkedListTests failed two params add test at index: " + i);
                }
            }

            // check the whole list (it should contain numbers from 0 to 9)
            for (int i = 0; i < maxNumOfNodes; i++) {
                if (myArrList.get(i) != i) {
                    throw new Exception("the order of elements is wrong. There was a mistake while adding elements");
                }
            }

            // remove odd elements
            for (int i = 1; i <= (maxNumOfNodes / 2); i++) {
                myArrList.remove(i);
            }

            // check the whole list (it should contain only even numbers (including zero))
            int evenNumIndex = 0;
            for (int i = 0; i < maxNumOfNodes; i += 2) {
                if (myArrList.get(evenNumIndex) != i) {
                    throw new Exception("the order of elements is wrong. There was a mistake while removing elements");
                }

                evenNumIndex++;
            }

            mixedMethodCalls();

            System.out.println("successfully finished all the list tests");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void mixedMethodCalls() throws Exception {
        List<Integer> arrList = new ArrayList<>();
        myArrList = new MyArrayList<>();

        myArrList.add(33);
        arrList.add(33);
        myArrList.add(44);
        arrList.add(44);
        myArrList.add(0, 55);
        arrList.add(0, 55);
        myArrList.add(2, 77);
        arrList.add(2, 77);

        myArrList.set(1, 100);
        arrList.set(1, 100);
        myArrList.set(0, 1000);
        arrList.set(0, 1000);
        myArrList.set((myArrList.size() - 1), 20);
        arrList.set((myArrList.size() - 1), 20);

        // check if the same
        for (int i = 0; i < arrList.size(); i++) {
            if (!arrList.get(i).equals(myArrList.get(i))) {
                System.err.println("mismatch happened");
            }
        }

        myArrList.add(0, 44);
        arrList.add(0, 44);
        myArrList.add(222);
        arrList.add(222);
        myArrList.add(333);
        arrList.add(333);
        myArrList.add(444);
        arrList.add(444);

        myArrList.set(5, 5432);
        arrList.set(5, 5432);

        for (int i = 0; i < 4; i++) {
            myArrList.remove(0);
            arrList.remove(0);
        }

        for (int i = 0; i < 100; i += 10) {
            myArrList.add(i);
            arrList.add(i);
        }

        for (int i = 0; i < 3; i++) {
            myArrList.set(i, 8888);
            arrList.set(i, 8888);
        }

        for (int i = 0; i < 5; i++) {
            myArrList.remove(0);
            arrList.remove(0);
        }

        // check if the same
        for (int i = 0; i < arrList.size(); i++) {
            if (!arrList.get(i).equals(myArrList.get(i))) {
                System.err.println("mismatch happened");
            }
        }

        for (int i = 0; i < 6; i++) {
            myArrList.add(3);
            arrList.add(3);
        }

        // paste at different indeces
        myArrList.add(6, 1);
        arrList.add(6, 1);
        myArrList.add(1, 1);
        arrList.add(1, 1);
        myArrList.add(2, 1);
        arrList.add(2, 1);
        myArrList.add(4, 1);
        arrList.add(4, 1);
        myArrList.add(0, 1);
        arrList.add(0, 1);
        myArrList.add(0, 1);
        arrList.add(0, 1);
        myArrList.add(2, 1);
        arrList.add(2, 1);

        for (int i = 0; i < 4; i++) {
            myArrList.remove(0);
            arrList.remove(0);
        }

        for (int i = 0; i < 10; i++) {
            myArrList.add(5888);
            arrList.add(5888);
        }

        // check if the same
        for (int i = 0; i < arrList.size(); i++) {
            if (!arrList.get(i).equals(myArrList.get(i))) {
                System.err.println("mismatch happened");
            }
        }
    }
}