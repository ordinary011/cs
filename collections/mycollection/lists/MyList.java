package com.shpp.p2p.cs.ldebryniuk.assignment16.mycollection.lists;

public interface MyList<T> {

    void add(T element);

    void add(int pasteIndex, T element) throws Exception;

    T get(int elIndex) throws Exception;

    void set(int elIndex, T element) throws Exception;

    void remove(int elIndex) throws Exception;

    int size();

    boolean isEmpty();

}
