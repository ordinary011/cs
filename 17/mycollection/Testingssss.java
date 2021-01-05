package com.shpp.p2p.cs.ldebryniuk.assignment17.mycollection;

import com.shpp.p2p.cs.ldebryniuk.assignment17.mycollection.lists.MyLinkedList;
import com.shpp.p2p.cs.ldebryniuk.assignment17.mycollection.queues.MyComparator;
import com.shpp.p2p.cs.ldebryniuk.assignment17.mycollection.queues.MyPriorityQueue;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;

//class MyCharComparator implements MyComparator<String> {
//
//    @Override
//    public int compare(String str1, String str2) {
//        char firstStrChar = str1.charAt(0);
//        char secondStrChar = str2.charAt(0);
//
//        if (firstStrChar > secondStrChar) {
//            return 1;
//        }
//        if (firstStrChar < secondStrChar) {
//            return -1;
//        }
//        return 0;
//    }
//
//}
//
//
//class CharComparator implements Comparator<String> {
//
//    @Override
//    public int compare(String str1, String str2) {
//        char firstStrChar = str1.charAt(0);
//        char secondStrChar = str2.charAt(0);
//
//        if (firstStrChar > secondStrChar) {
//            return 1;
//        }
//        if (firstStrChar < secondStrChar) {
//            return -1;
//        }
//        return 0;
//    }
//
//}


//class IntgComparator implements Comparator<Integer> {
//    @Override
//    public int compare(Integer num1, Integer num2) {
//        return num1 - num2;
//    }
//}
//
//class MyIntgComparator implements MyComparator<Integer> {
//    @Override
//    public int compare(Integer num1, Integer num2) {
//        return num1 - num2;
//    }
//}

public class Testingssss {

//    public static void main(String[] args) {
//        MyPriorityQueue<Integer> myPriorityQueue = new MyPriorityQueue<>(10, new MyIntgComparator());
//        PriorityQueue<Integer> nativePriorityQueue = new PriorityQueue<>(10, new IntgComparator());
//
//        nativePriorityQueue.add(4);
//        nativePriorityQueue.add(1);
//        nativePriorityQueue.add(7);
//        nativePriorityQueue.add(5);
//        nativePriorityQueue.add(6);
//        nativePriorityQueue.add(3);
//        nativePriorityQueue.add(2);
//        nativePriorityQueue.add(9);
//        nativePriorityQueue.add(8);
//
//        myPriorityQueue.add(4);
//        myPriorityQueue.add(1);
//        myPriorityQueue.add(7);
//        myPriorityQueue.add(5);
//        myPriorityQueue.add(6);
//        myPriorityQueue.add(3);
//        myPriorityQueue.add(2);
//        myPriorityQueue.add(9);
//        myPriorityQueue.add(8);
//
//
////        for (Integer integer : nativePriorityQueue) {
////            System.out.print(integer + " ");
////        }
////        System.out.println();
////        System.out.println(myPriorityQueue.arr);
//
//
//        System.out.print("n ");
//        System.out.println("m");
//        int size = nativePriorityQueue.size();
//        for (int i = 0; i < size; i++) {
//            System.out.print(nativePriorityQueue.poll() + " ");
//            System.out.print(myPriorityQueue.poll() + " ");
//            System.out.println();
//        }
//
//    }

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
                myHashMap.put(tests[i], i);
                nativeHashMap.put(tests[i], i);
            }

            myHashMap.put("abc", 100);
            nativeHashMap.put("abc", 100);
            myHashMap.put("abc", 200);
            nativeHashMap.put("abc", 200);

            myHashMap.put(null, 200);
            nativeHashMap.put(null, 200);

            myHashMap.put(null, 300);
            nativeHashMap.put(null, 300);

            System.out.print("m ");
            System.out.println("n");
            for (int i = 0; i < tests.length; i++) {
                System.out.print(myHashMap.get(tests[i]) + " ");
                System.out.println(nativeHashMap.get(tests[i]));
            }

            System.out.println(myHashMap.get("hhh"));
            System.out.println(nativeHashMap.get("hhh"));

            System.out.println(myHashMap.get("hhh"));
            System.out.println(nativeHashMap.get("hhh"));

            System.out.println(myHashMap.get("hhh"));
            System.out.println(nativeHashMap.get("hhh"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
