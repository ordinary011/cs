package com.shpp.p2p.cs.ldebryniuk.assignment17.mycollection.collection_testing;

import com.shpp.p2p.cs.ldebryniuk.assignment17.mycollection.MyHashMap;

import java.util.HashMap;

public class HashMapTesting {

    private final String ANSI_GREEN = "\u001B[32m";

    void runTests() {
        try {
            String[] tests = {null, "karl", "ka", "kar", "karv", null, "abc", "chris", "john", "Tim", "Bill", "Alex", null};

            HashMap<String, Integer> nativeHashMap = new HashMap<>();
            MyHashMap<String, Integer> myHashMap = new MyHashMap<>();

            // populate maps
            for (int i = 0; i < tests.length; i++) {
                String key = tests[i];

                myHashMap.put(key, i);
                nativeHashMap.put(key, i);

//                if (i == 5) {
//                    myHashMap.put(key, 3);
//                }

                if (!myHashMap.get(key).equals(nativeHashMap.get(key))) {
                    throw new Exception("mismatch occured at index " + i);
                }
            }

            // put with the same key
            myHashMap.put("abc", 100);
            nativeHashMap.put("abc", 100);
            myHashMap.put("abc", 200);
            nativeHashMap.put("abc", 200);

            System.out.print("m ");
            System.out.println("n");
            for (int i = 0; i < tests.length; i++) {
                System.out.print(myHashMap.get(tests[i]) + " ");
                System.out.println(nativeHashMap.get(tests[i]));
            }

            // get by key that doesn't exist
            System.out.println(myHashMap.get("hhh"));
            System.out.println(nativeHashMap.get("hhh"));

            System.out.println(myHashMap.get("hhh"));
            System.out.println(nativeHashMap.get("hhh"));

            System.out.println(myHashMap.get("hhh"));
            System.out.println(nativeHashMap.get("hhh"));

            System.out.println(ANSI_GREEN + "successfully finished all the tests for hash map");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
