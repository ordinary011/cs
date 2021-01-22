package com.shpp.p2p.cs.ldebryniuk.assignment17.mycollection.map;

public interface MyMap<K, V> {

    void put(K key, V value);

    V get(K key);

    void remove(K key);

    boolean containsKey(K key);

    interface Entry<K, V> {
        K getKey();

        V getValue();
    }
}
