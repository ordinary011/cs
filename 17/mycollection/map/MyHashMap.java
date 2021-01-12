package com.shpp.p2p.cs.ldebryniuk.assignment17.mycollection.map;

import com.shpp.p2p.cs.ldebryniuk.assignment17.mycollection.lists.MyLinkedList;

public class MyHashMap<T, E> implements MyMap<T, E> {

    private int insertedElements = 0;
    private int arrCapacity = 16;
    private MyLinkedList<MyMapEntry>[] arrOfChains = new MyLinkedList[arrCapacity];

    private class MyMapEntry implements MyMap.Entry<T, E> {
        private final T key;
        private E value;

        public MyMapEntry(T key, E value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public T getKey() {
            return key;
        }

        @Override
        public E getValue() {
            return value;
        }
    }

    @Override
    public void put(T key, E value) {
        if (insertedElements == arrCapacity) {
            extendArrSize();
        }

        findArrIndexAndAdd(key, value);
    }

    private void findArrIndexAndAdd(T key, E value) {
        int hashCode = (key == null) ? 0 : key.hashCode();
        int arrIndexOfTheKey = Math.abs(hashCode % arrCapacity);

        MyLinkedList<MyMapEntry> chain = arrOfChains[arrIndexOfTheKey];
        if (chain == null) {
            chain = new MyLinkedList<>();
            arrOfChains[arrIndexOfTheKey] = chain;
        }

        // search if key already exists, then we just replace it's value
        boolean keyIsNew = true;
        for (MyMapEntry chainSegment : chain) {
            if (chainSegment.key == null && key == null) {
                chainSegment.value = value;
                keyIsNew = false;
            }

            if (chainSegment.key != null && chainSegment.key.equals(key)) {
                chainSegment.value = value;
                keyIsNew = false;
            }
        }

        if (keyIsNew) {
            chain.add(new MyMapEntry(key, value));
            insertedElements++;
        }
    }

    private void extendArrSize() {
        arrCapacity *= 2;
        MyLinkedList<MyMapEntry>[] extendedArr = new MyLinkedList[arrCapacity];

        for (MyLinkedList<MyMapEntry> chain : arrOfChains) {
            if (chain != null) {
                for (MyMapEntry chainSegment : chain) {
                    int hashCode = (chainSegment.key == null) ? 0 : chainSegment.key.hashCode();
                    int arrIndexForTheKey = Math.abs(hashCode % arrCapacity);

                    MyLinkedList<MyMapEntry> chainInExtendedArr = extendedArr[arrIndexForTheKey];
                    if (chainInExtendedArr == null) {
                        chainInExtendedArr = new MyLinkedList<>();
                        extendedArr[arrIndexForTheKey] = chainInExtendedArr;
                    }
                    chainInExtendedArr.add(chainSegment);
                }
            }
        }

        arrOfChains = extendedArr;
    }

    @Override
    public E get(T key) {
        int hashCode = (key == null) ? 0 : key.hashCode();
        int arrIndexForTheKey = Math.abs(hashCode % arrCapacity);

        if (arrIndexForTheKey >= arrOfChains.length) {
            return null;
        }

        MyLinkedList<MyMapEntry> chain = arrOfChains[arrIndexForTheKey];
        if (chain == null) {
            return null;
        }

        for (MyMapEntry chainSegment : chain) {
            if (chainSegment.key == null && key == null) {
                return chainSegment.value;
            }

            if (chainSegment.key != null && chainSegment.key.equals(key)) {
                return chainSegment.value;
            }
        }

        return null;
    }

    @Override
    public boolean containsKey(T key) {
        int hashCode = (key == null) ? 0 : key.hashCode();
        int arrIndexOfTheKey = Math.abs(hashCode % arrCapacity);

        if (arrIndexOfTheKey >= arrOfChains.length) {
            return false;
        }

        MyLinkedList<MyMapEntry> chain = arrOfChains[arrIndexOfTheKey];
        if (chain == null) {
            return false;
        }

        for (MyMapEntry chainSegment : chain) {
            if (chainSegment.key == null && key == null) {
                return true;
            }

            if (chainSegment.key != null && chainSegment.key.equals(key)) {
                return true;
            }
        }

        return false;
    }

    public MyLinkedList<MyMapEntry> entryList() { // todo entryList for 0 values + entryList tests
        MyLinkedList<MyMapEntry> allEntriesList = new MyLinkedList<>();

        for (MyLinkedList<MyMapEntry> chain : arrOfChains) {
            if (chain != null) { // todo maybe redundant
                allEntriesList.addAll(chain);
            }
        }

        return allEntriesList;
    }

    public int size() {
        return insertedElements;
    }

}
