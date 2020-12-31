package com.shpp.p2p.cs.ldebryniuk.assignment17.mycollection;

import com.shpp.p2p.cs.ldebryniuk.assignment17.mycollection.lists.MyLinkedList;

public class MyHashMap<T, E> {

    private int size = 0;
    //    private int arrCapacity = 16;
    private int arrCapacity = 3;
    private Object[] arrOfBuckets = new Object[arrCapacity];

    private class ChainSegment {
        private final T key;
        private E value;

        public ChainSegment(T key, E value) {
            this.key = key;
            this.value = value;
        }
    }

    public void put(T key, E value) throws Exception {
        if (size == arrCapacity) {
            extendArrSize();
        }

        findArrIndexAndAdd(key, value, arrOfBuckets);
    }

    private void findArrIndexAndAdd(T key, E value, Object[] arrOfBuckets) {
        int hashCode = (key == null) ? 0 : key.hashCode();
        int arrIndexForTheKey = hashCode % arrCapacity;

        MyLinkedList<ChainSegment> bucket = (MyLinkedList<ChainSegment>) arrOfBuckets[arrIndexForTheKey];
        if (bucket == null) {
            bucket = new MyLinkedList<>();
            arrOfBuckets[arrIndexForTheKey] = bucket;
        }

        // search if key already exists, then we just replace it's value
        boolean keyIsNew = true;
        for (ChainSegment chainSegment : bucket) {
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
            bucket.add(new ChainSegment(key, value));
            size++;
        }
    }

    private MyLinkedList<ChainSegment> findBucketByKey(T key, Object[] arrOfBuckets) {
        int hashCode = (key == null) ? 0 : key.hashCode();
        int arrIndexForTheKey = hashCode % arrCapacity;

        return (MyLinkedList<ChainSegment>) arrOfBuckets[arrIndexForTheKey];
    }

    private void extendArrSize() throws Exception {
        arrCapacity *= 2;
        Object[] extendArr = new Object[arrCapacity];

        for (Object bucket : arrOfBuckets) {
            if (bucket != null) {
                ChainSegment segment = ((MyLinkedList<ChainSegment>) bucket).get(0);

                int hashCode = (segment.key == null) ? 0 : segment.key.hashCode();
                int arrIndexForTheKey = hashCode % arrCapacity;

                extendArr[arrIndexForTheKey] = bucket;
            }
        }

        arrOfBuckets = extendArr;
    }

    public E get(T key) {
        int hashCode = (key == null) ? 0 : key.hashCode();
        int arrIndexForTheKey = hashCode % arrCapacity;

        MyLinkedList<ChainSegment> wholeChain = findBucketByKey(key, arrOfBuckets);
        if (wholeChain == null) {
            return null;
        }

        for (ChainSegment chainSegment : wholeChain) {
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

