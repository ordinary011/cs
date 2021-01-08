package com.shpp.p2p.cs.ldebryniuk.assignment17.calculator;

public class Assignment11Part1 {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("sorry no arguments found");
            return;
        }

        System.out.println(new Calculator().runCalc(args));

//        new CalculatorTests().runTests(); // uncomment for tests
    }

}



