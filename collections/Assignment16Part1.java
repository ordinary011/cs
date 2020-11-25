package com.shpp.p2p.cs.ldebryniuk.assignment16;

import java.util.LinkedList;

public class Assignment16Part1 {

    public static void main(String[] args) {
//        LinkedList<Integer> ff = new LinkedList<>();
//        ff.add(33);
//        ff.add(32);
//        ff.add(31);
//        ff.add(30);
//        ff.add(1, 11);
//        System.out.println(ff.get(0));

        MyLinkedList<Integer> m = new MyLinkedList<>();
        m.add(33);
        m.add(34);

        m.add(35);
        m.add(36);

        m.add(37);
        m.add(38);

        m.add(39);
//        m.add(4, 11);

//        m.set(7, 100);

        m.remove(6);

        System.out.println(m);
//        System.out.println(m.size());
    }
}

// todo test this later ff.add(0, 34);