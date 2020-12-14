package com.shpp.p2p.cs.ldebryniuk.assignment16;

import com.shpp.p2p.cs.ldebryniuk.assignment16.mycollection.lists.MyArrayList;
import com.shpp.p2p.cs.ldebryniuk.assignment16.mycollection.lists.MyList;

public class FFF {
    public static void main(String[] args) {
        try {
            MyList<Integer> myArrList = new MyArrayList<>();

            myArrList.add(1);
            myArrList.add(2);
            myArrList.add(4);
            myArrList.add(5);
            myArrList.add(6);
            myArrList.add(7);

            myArrList.add(2, 3);
            System.out.println(myArrList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
