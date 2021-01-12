package com.shpp.p2p.cs.ldebryniuk.assignment17.mycollection.collection_testing;

import com.shpp.p2p.cs.ldebryniuk.assignment17.mycollection.map.MyHashMap;
import com.shpp.p2p.cs.ldebryniuk.assignment17.mycollection.map.MyMap;

import java.util.HashMap;

public class HashMapTesting {

    private final String ANSI_GREEN = "\u001B[32m";

    void runTests() {
        try {
            String[] tests = {null, "karl", "ka", "kar", "karv", null, "abc", "chris", "john", "Tim", "Bill", "Alex", null};

            HashMap<String, Integer> nativeHashMap = new HashMap<>();
            MyHashMap<String, Integer> myHashMap = new MyHashMap<>();

            myHashMap.put("karl", 3);
            myHashMap.put("karlvvvv", 5);
            myHashMap.put("karlvvvvbbbb", 7);

            for (MyMap.Entry<String, Integer> s : myHashMap.entryList()) {
                System.out.println("key " + s.getKey());
                System.out.println("val " + s.getValue());
            }

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
