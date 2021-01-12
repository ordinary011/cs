package com.shpp.p2p.cs.ldebryniuk.assignment17.mycollection.map;

import com.shpp.p2p.cs.ldebryniuk.assignment17.mycollection.MyCollection;
import com.shpp.p2p.cs.ldebryniuk.assignment17.mycollection.lists.MyLinkedList;

public class MyHashMap<T, E> implements MyMap<T, E>, MyCollection {

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

    /**
     * adds new element to the array
     *
     * @param key   key that is used to determine the location of the value
     * @param value value that is to be stored "behind" key
     */
    @Override
    public void put(T key, E value) {
        if (insertedElements == arrCapacity) {
            extendArrSize();
        }

        findArrIndexAndAdd(key, value);
    }

    /**
     * searchs for the element by index and replaces it
     *
     * @param key   key that is used to determine the location of the value
     * @param value value that is to be stored "behind" key
     */
    private void findArrIndexAndAdd(T key, E value) {
        int arrIndexForTheKey = findArrIndexForTheKey(key);

        MyLinkedList<MyMapEntry> chainOfEntries = arrOfChains[arrIndexForTheKey];
        if (chainOfEntries == null) {
            chainOfEntries = new MyLinkedList<>();
            arrOfChains[arrIndexForTheKey] = chainOfEntries;
        }

        // search if key already exists, then we just replace it's value
        for (MyMapEntry myMapEntry : chainOfEntries) {
            if (myMapEntry.key != null && myMapEntry.key.equals(key) ||
                    myMapEntry.key == null && key == null) {
                myMapEntry.value = value;
                return;
            }
        }

        // code below is executed only if the key is new
        chainOfEntries.add(new MyMapEntry(key, value));
        insertedElements++;
    }

    /**
     * increases size of the array (x2)
     * and copies all the elements to the new arr that is two times bigger than before
     */
    private void extendArrSize() {
        arrCapacity *= 2;
        MyLinkedList<MyMapEntry>[] extendedArr = new MyLinkedList[arrCapacity];

        for (MyLinkedList<MyMapEntry> chainOfEntries : arrOfChains) {
            if (chainOfEntries != null) {
                for (MyMapEntry myMapEntry : chainOfEntries) {
                    int arrIndexForTheKey = findArrIndexForTheKey(myMapEntry.key);

                    MyLinkedList<MyMapEntry> chainInExtendedArr = extendedArr[arrIndexForTheKey];
                    if (chainInExtendedArr == null) {
                        chainInExtendedArr = new MyLinkedList<>();
                        extendedArr[arrIndexForTheKey] = chainInExtendedArr;
                    }
                    chainInExtendedArr.add(myMapEntry);
                }
            }
        }

        arrOfChains = extendedArr;
    }

    /**
     * based on the key returns value
     *
     * @param key key that is used to determine the location of the value
     * @return value that is stored "behind" the key
     */
    @Override
    public E get(T key) {
        int arrIndexForTheKey = findArrIndexForTheKey(key);

        if (arrIndexForTheKey >= arrOfChains.length) {
            return null;
        }

        MyLinkedList<MyMapEntry> chainOfEntries = arrOfChains[arrIndexForTheKey];
        if (chainOfEntries == null) {
            return null;
        }

        for (MyMapEntry myMapEntry : chainOfEntries) {
            if (myMapEntry.key != null && myMapEntry.key.equals(key) ||
                    myMapEntry.key == null && key == null) {
                return myMapEntry.value;
            }
        }

        return null;
    }

    /**
     * @param key key that is used to determine the location of the value
     * @return index for the key within our hashMap
     */
    private int findArrIndexForTheKey(T key) {
        int hashCode = (key == null) ? 0 : key.hashCode();
        return Math.abs(hashCode % arrCapacity);
    }

    /**
     * @param key key that is used to determine the location of the value
     * @return true if current map already contains this key
     */
    @Override
    public boolean containsKey(T key) {
        return get(key) != null;
    }

    /**
     * merges all the chains into one list and returns it
     *
     * @return list of all the chains in the hashMap
     */
    public MyLinkedList<MyMapEntry> entryList() {
        MyLinkedList<MyMapEntry> allEntriesList = new MyLinkedList<>();

        for (MyLinkedList<MyMapEntry> chainOfEntries : arrOfChains) {
            if (chainOfEntries != null) {
                allEntriesList.addAll(chainOfEntries);
            }
        }

        return allEntriesList;
    }

    /**
     * @return true if hashMap is empty
     */
    @Override
    public boolean isEmpty() {
        return insertedElements == 0;
    }

    /**
     * @return amount of elements within the hashMap
     */
    @Override
    public int size() {
        return insertedElements;
    }

}
