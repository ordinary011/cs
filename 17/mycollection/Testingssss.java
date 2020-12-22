package com.shpp.p2p.cs.ldebryniuk.assignment17.mycollection;

import com.shpp.p2p.cs.ldebryniuk.assignment17.mycollection.lists.MyLinkedList;
import com.shpp.p2p.cs.ldebryniuk.assignment17.mycollection.queues.MyPriorityQueue;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;

class MyCharComparator implements MyComparator<String> {

    @Override
    public int compare(String str1, String str2) {
        char firstStrChar = str1.charAt(0);
        char secondStrChar = str2.charAt(0);

        if (firstStrChar > secondStrChar) {
            return 1;
        }
        if (firstStrChar < secondStrChar) {
            return -1;
        }
        return 0;
    }

}


class CharComparator implements Comparator<String> {

    @Override
    public int compare(String str1, String str2) {
        char firstStrChar = str1.charAt(0);
        char secondStrChar = str2.charAt(0);

        if (firstStrChar > secondStrChar) {
            return 1;
        }
        if (firstStrChar < secondStrChar) {
            return -1;
        }
        return 0;
    }

}

public class Testingssss {
//    public static void main(String[] args) throws Exception {
//        MyPriorityQueue<String> myPriorityQueue = new MyPriorityQueue<>(10, new MyCharComparator());
//        PriorityQueue<String> nativePriorityQueue = new PriorityQueue<>(10, new CharComparator());
//
//        nativePriorityQueue.add("Agatha");
//        nativePriorityQueue.add("Tarvek");
//        nativePriorityQueue.add("Lucrezia");
//        nativePriorityQueue.add("Zeetha");
//        nativePriorityQueue.add("Zola");
//        nativePriorityQueue.add("Gil");
//        nativePriorityQueue.add("Dupree");
//        nativePriorityQueue.add("Othar");
//
//        myPriorityQueue.add("Agatha");
//        myPriorityQueue.add("Tarvek");
//        myPriorityQueue.add("Lucrezia");
//        myPriorityQueue.add("Zeetha");
//        myPriorityQueue.add("Zola");
//        myPriorityQueue.add("Gil");
//        myPriorityQueue.add("Dupree");
//        myPriorityQueue.add("Othar");
//
//        int size = nativePriorityQueue.size();
//        for (int i = 0; i < size; i++) {
////            System.out.println(nativePriorityQueue.poll());
//            System.out.println(myPriorityQueue.poll());
//        }
//}

    public static void main(String[] args) {
        try {
            HashMap<String, Integer> nativeHashMap = new HashMap<>();
            MyHashMap<String, Integer> myHashMap = new MyHashMap<>();

            String[] tests = {null, "karl", "ka", "kar", "karv", "abc"};

            for (int i = 0; i < tests.length; i++) {
//                myHashMap.put(tests[i], i);
                nativeHashMap.put(tests[i], i);
            }
            myHashMap.put("abc", 100);
            myHashMap.put("abc", 200);
            nativeHashMap.put("abc", 100);
            nativeHashMap.put("abc", 200);
            nativeHashMap.put(null, 200);
            nativeHashMap.put(null, 300);
            nativeHashMap.put(null, 400);

            for (int i = 0; i < tests.length; i++) {
                System.out.println(myHashMap.get(tests[i]));
                System.out.println(nativeHashMap.get(tests[i]));
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
