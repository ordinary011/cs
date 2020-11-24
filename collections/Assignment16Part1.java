package com.shpp.p2p.cs.ldebryniuk.assignment16;

public class Assignment16Part1 {

    public static void main(String[] args) {
//        ArrayList<Integer> ff = new ArrayList<>();
//        ff.add(33);
//        ff.add(33);
//        ff.add(2, 33);
//        ff.remove();
//        ff.set()
//        System.out.println(ff);

        MyArrayList<Integer> m = new MyArrayList<>();
        m.add(33);
        m.add(44);
        m.add(2, 11);

        System.out.println(m);
        m.remove(2);
        System.out.println(m);

//        m.remove(0);
//        m.add(35);
//        m.add(36);
//
//        m.add(37);
//        m.add(38);
//
//        m.add(39);
//
//        System.out.println(m.size());
    }
}
