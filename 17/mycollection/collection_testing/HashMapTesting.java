package com.shpp.p2p.cs.ldebryniuk.assignment17.mycollection.collection_testing;

import com.shpp.p2p.cs.ldebryniuk.assignment17.mycollection.map.MyHashMap;

import java.util.HashMap;

/**
 * The following class contains all the logic for myHashMap tests
 */
public class HashMapTesting implements SuccessfulOutput {

    private final int NUMBER_OF_INTEGERS_TO_ADD = 100;
    private final int MAX_INTEGER = 5000;
    private final int MIN_INTEGER = -5000;

    /**
     * the starting method of the tests for myHashMap
     */
    void runTests() {
        try {
            testWithStringsAsKeys();

            testWithIntegersAsKeys();

            System.out.println(ANSI_GREEN + "successfully finished all the tests for hash map");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Tests for myHashMap where key is a String
     * Includes tests with null elements at the beginning, middle, and at the end.
     * Also includes tests for the same keys
     *
     * @throws Exception is thrown when there is a mismatch between myHashMap and nativeHashMap
     */
    private void testWithStringsAsKeys() throws Exception {
        String[] tests = {null, "karl", "john", "ka", "kar", "karl", null,
                "abc", "chris", "Alex", "john", "Tim", "Bill", "Alex", "john", null};

        HashMap<String, Integer> nativeHashMap = new HashMap<>();
        MyHashMap<String, Integer> myHashMap = new MyHashMap<>();

        for (int i = 0; i < tests.length; i++) {
            String key = tests[i];

            addToBothMaps(myHashMap, nativeHashMap, key, i);

            compareInBothMaps(myHashMap, nativeHashMap, key);
        }

        // get by the key that doesn't exist
        if (myHashMap.get("hhh") != null) {
            throw new Exception("get by the key that doesn't exist doesn't work properly");
        }
    }

    /**
     * Tests for myHashMap where key is a random integer
     *
     * @throws Exception is thrown when there is a mismatch between myHashMap and nativeHashMap
     */
    private void testWithIntegersAsKeys() throws Exception {
        HashMap<Integer, Integer> nativeHashMap = new HashMap<>();
        MyHashMap<Integer, Integer> myHashMap = new MyHashMap<>();

        for (int i = 0; i < NUMBER_OF_INTEGERS_TO_ADD; i++) {
            int randomInt = (int) ((Math.random() * (MAX_INTEGER - MIN_INTEGER)) + MIN_INTEGER);

            addToBothMaps(myHashMap, nativeHashMap, randomInt, i);

            compareInBothMaps(myHashMap, nativeHashMap, randomInt);
        }
    }

    /**
     * adds a value to both maps
     *
     * @param myHashMap     my self written hashMap implementation
     * @param nativeHashMap default java implementation
     * @param key           key of the key value pair
     * @param value         value of the key value pair
     * @param <T>           is either a String or an Integer
     */
    private <T> void addToBothMaps(MyHashMap<T, Integer> myHashMap,
                                   HashMap<T, Integer> nativeHashMap, T key, int value) {
        myHashMap.put(key, value);
        nativeHashMap.put(key, value);
    }

    /**
     * compares values for equality in both maps
     *
     * @param myHashMap     my self written hashMap implementation
     * @param nativeHashMap default java implementation
     * @param key           key of the key value pair
     * @param <T>           is either a String or an Integer
     * @throws Exception is thrown when there is a mismatch between myHashMap and nativeHashMap
     */
    private <T> void compareInBothMaps(MyHashMap<T, Integer> myHashMap,
                                       HashMap<T, Integer> nativeHashMap, T key) throws Exception {
        if (!myHashMap.get(key).equals(nativeHashMap.get(key))) {
            throw new Exception("mismatch occurred when key was: " + key);
        }
    }
}
