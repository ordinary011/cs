package com.shpp.p2p.cs.ldebryniuk.assignment17.mycollection.queues;

public interface MyQueueI<T> {

    void add(T element);

    T poll() throws Exception;

    T peek();

}
