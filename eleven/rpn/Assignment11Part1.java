package com.shpp.p2p.cs.ldebryniuk.assignment11.rpn;

public class Assignment11Part1 {

    public static void main(String[] args) {
//        if (args.length == 0) {
//            System.out.println("sorry no arguments found");
//            return;
//        }

        args = new String[]{"11-10/-a", "a=5"}; // {"13.00000"},
        System.out.println(new Calculator().runCalc(args));


//        new Tester().runTests(); // todo uncomment for tests
    }
}


// 5^-3 = 1 ÷ 5 ÷ 5 ÷ 5 = 0.008

// -5^-0.5 = 1 /



