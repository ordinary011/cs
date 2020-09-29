package com.shpp.p2p.cs.ldebryniuk.assignment11;

public class Assignment11Part1 {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("sorry no arguments found");
            return;
        }

        Calculator calc = new Calculator();
        System.out.println(calc.runCalc(args));

//        new Tester().runTests(); // todo uncomment for tests
    }
}
