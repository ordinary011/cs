package com.shpp.p2p.cs.ldebryniuk.assignment17.mycollection.map;

import com.shpp.p2p.cs.ldebryniuk.assignment17.mycollection.MyCollection;
import com.shpp.p2p.cs.ldebryniuk.assignment17.mycollection.lists.MyLinkedList;

/**
 * class that is an implementation of HashMap data structure
 */
public class MyHashMap<K, V> implements MyMap<K, V>, MyCollection {

    private int insertedElements = 0;
    private MyLinkedList<MyMapEntry>[] arrOfChains = new MyLinkedList[16];

    private class MyMapEntry implements Entry<K, V> {
        private final K key;
        private V value;

        public MyMapEntry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
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
    public void put(K key, V value) {
        if (insertedElements == arrOfChains.length) {
            extendArrCapacity();
        }

        addElement(key, value);
    }

    /**
     * searches for the element by index and replaces it
     *
     * @param key   key that is used to determine the location of the value
     * @param value value that is to be stored "behind" key
     */
    private void addElement(K key, V value) {
        int arrIndexForTheKey = findArrIndexForTheKey(key);

        MyLinkedList<MyMapEntry> chainOfEntries = arrOfChains[arrIndexForTheKey];
        if (chainOfEntries == null) {
            chainOfEntries = new MyLinkedList<>();
            arrOfChains[arrIndexForTheKey] = chainOfEntries;
        } else {
            // search if key already exists, then we just replace it's value
            MyMapEntry myMapEntry = findEntryInChain(key, chainOfEntries);
            if (myMapEntry != null) {
                myMapEntry.value = value;
                return;
            }
        }

        // code below is executed only if the key is new
        chainOfEntries.add(new MyMapEntry(key, value));
        insertedElements++;
    }

    /**
     * searches for an entry in the chain and returns it
     *
     * @param key            key that is used to determine the location of the value
     * @param chainOfEntries chain that contains entries (entry is a key-value pair)
     * @return entry that is a key-value pair
     */
    private MyMapEntry findEntryInChain(K key, MyLinkedList<MyMapEntry> chainOfEntries) {
        for (MyMapEntry myMapEntry : chainOfEntries) {
            if (myMapEntry.key != null && myMapEntry.key.equals(key) ||
                    myMapEntry.key == null && key == null) {
                return myMapEntry;
            }
        }

        return null;
    }

    /**
     * increases size of the array (x2)
     * and copies all the elements to the new arr that is two times bigger than before
     */
    private void extendArrCapacity() {
        MyLinkedList<MyMapEntry>[] extendedArr = new MyLinkedList[arrOfChains.length * 2];

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
     * @param key key that is used to determine the location of the value
     * @return index for the key within our hashMap
     */
    private int findArrIndexForTheKey(K key) {
        int hashCode = (key == null) ? 0 : key.hashCode();
        return Math.abs(hashCode % arrOfChains.length);
    }

    /**
     * first searches for a chain and then for an entry in the chain and returns it
     *
     * @param key key that is used to determine the location of the value
     * @return entry (a key-value pair)
     */
    private MyMapEntry findEntryByKey(K key) {
        int arrIndexForTheKey = findArrIndexForTheKey(key);

        MyLinkedList<MyMapEntry> chainOfEntries = arrOfChains[arrIndexForTheKey];
        if (chainOfEntries == null) {
            return null;
        }

        return findEntryInChain(key, chainOfEntries);
    }

    /**
     * based on the key returns value
     *
     * @param key key that is used to determine the location of the value
     * @return value that is stored "behind" the key
     */
    @Override
    public V get(K key) {
        MyMapEntry myMapEntry = findEntryByKey(key);
        if (myMapEntry != null) {
            return myMapEntry.value;
        }

        return null;
    }

    /**
     * @param key key that is used to determine the location of the value
     * @return true if current map already contains this key
     */
    @Override
    public boolean containsKey(K key) {
        MyMapEntry myMapEntry = findEntryByKey(key);
        return myMapEntry != null;
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
     * removes entry from the chain
     *
     * @param key key that is used to determine the location of the value
     */
    @Override
    public void remove(K key) {
        int arrIndexForTheKey = findArrIndexForTheKey(key);

        MyLinkedList<MyMapEntry> chainOfEntries = arrOfChains[arrIndexForTheKey];
        if (chainOfEntries == null) {
            return;
        }

        int entryIndex = 0;
        for (MyMapEntry myMapEntry : chainOfEntries) {
            if (myMapEntry.key != null && myMapEntry.key.equals(key) ||
                    myMapEntry.key == null && key == null) {
                chainOfEntries.remove(entryIndex);
                return;
            }
            entryIndex++;
        }
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
