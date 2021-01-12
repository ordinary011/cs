package com.shpp.p2p.cs.ldebryniuk.assignment17.mycollection.map;

public interface MyMap<T, E> {

    void put(T key, E value);

    E get(T key);

    boolean containsKey(T key);

    interface Entry<K, V> {
        K getKey();

        V getValue();
    }
}
