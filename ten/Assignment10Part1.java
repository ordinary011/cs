package com.shpp.p2p.cs.ldebryniuk.assignment10;

public class Assignment10Part1 {

    public static void main(String[] args) {
//        if (args.length == 0) {
//            System.out.println("sorry no arguments found");
//            return;
//        }

        Calculator calc = new Calculator();


        // all other cases like 10-3^3 || 11*-3^2

//        String[] go = {"11-3^2+3^3"}; // 29
//        String[] go = {"-3/-4^-2"}; // -48
//        System.out.println(calc.runCalc(go));


        calc.runTests();
    }

}
