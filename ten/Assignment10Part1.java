package com.shpp.p2p.cs.ldebryniuk.assignment10;

public class Assignment10Part1 {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("sorry no arguments found");
            return;
        }

        Calculator calc = new Calculator();
        System.out.println(calc.runCalc(args));

//        calc.runTests(); todo uncomment for tests
    }
}


//        String[] go = {"1-2*a+3", "a=a=-5"}; // {"14"}