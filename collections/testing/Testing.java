package com.shpp.p2p.cs.ldebryniuk.assignment16.testing;

/**
 * The entering class for all the tests
 */
public class Testing {

    /**
     * start method for running all the tests
     */
    public void runTests() {
        new QueueAndStackTesting().startQueueAndStackTests();
        new ListTesting().startListTesting();
    }

}
