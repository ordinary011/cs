package com.shpp.p2p.cs.ldebryniuk.assignment17.mycollection;

import com.shpp.p2p.cs.ldebryniuk.assignment17.mycollection.lists.MyLinkedList;

public class MyHashMap<T, E> {

    private int insertedElements = 0;
    //    private int arrCapacity = 16;
    private int arrCapacity = 3;
    private MyLinkedList<ChainSegment>[] arrOfChains = new MyLinkedList[arrCapacity];

    private class ChainSegment {
        private final T key;
        private E value;

        public ChainSegment(T key, E value) {
            this.key = key;
            this.value = value;
        }
    }

    public void put(T key, E value) throws Exception {
        if (insertedElements == arrCapacity) {
            extendArrSize();
        }

        findArrIndexAndAdd(key, value);
    }

    private void findArrIndexAndAdd(T key, E value) {
        int hashCode = (key == null) ? 0 : key.hashCode();
        int arrIndexOfTheKey = hashCode % arrCapacity;

        MyLinkedList<ChainSegment> chain = arrOfChains[arrIndexOfTheKey];
        if (chain == null) {
            chain = new MyLinkedList<>();
            arrOfChains[arrIndexOfTheKey] = chain;
        }

        // search if key already exists, then we just replace it's value
        boolean keyIsNew = true;
        for (ChainSegment chainSegment : chain) {
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
            chain.add(new ChainSegment(key, value));
            insertedElements++;
        }
    }

    private void extendArrSize() {
        arrCapacity *= 2;
        MyLinkedList<ChainSegment>[] extendedArr = new MyLinkedList[arrCapacity];

        for (MyLinkedList<ChainSegment> chain : arrOfChains) {
            if (chain != null) {
                for (ChainSegment chainSegment : chain) {
                    int hashCode = (chainSegment.key == null) ? 0 : chainSegment.key.hashCode();
                    int arrIndexForTheKey = hashCode % arrCapacity;

                    MyLinkedList<ChainSegment> chainInExtendedArr = extendedArr[arrIndexForTheKey];
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

    public E get(T key) {
        int hashCode = (key == null) ? 0 : key.hashCode();
        int arrIndexForTheKey = hashCode % arrCapacity;

        MyLinkedList<ChainSegment> chain = arrOfChains[arrIndexForTheKey];
        if (chain == null) {
            return null;
        }

        for (ChainSegment chainSegment : chain) {
            if (chainSegment.key == null && key == null) {
                return chainSegment.value;
            }

            if (chainSegment.key != null && chainSegment.key.equals(key)) {
                return chainSegment.value;
            }
        }

        return null;
    }
}

// todo add 1 null value
// todo add to the same chain
// todo put with the same key
// get by key that doesn't exist
