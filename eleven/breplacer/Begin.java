package com.shpp.p2p.cs.ldebryniuk.assignment11.breplacer;

public class Begin {

    public static void main(String[] args) {
//        if (args.length == 0) {
//            System.out.println("sorry no arguments found");
//            return;
//        }

//        String[] go = {"-(1+23?/4^5+(-67/(cos(8)^9+sin(tan(atan(log2(10)^11)/12) 13)+14-1516))^17-18+(-19^(-20))*(-21)+22^23+tan(24)-sqrt(25)-26+27^28/29-30)/31^a+sqrt(sqrt(625)) a=32"}; // {"-0.00032"},
        args = new String[]{"sin(34+sqrt(a)+b)+15"};

        Calc calc = new Calc();
        System.out.println(calc.runCalc(args));

//        new Tester().runTests(); // todo uncomment for tests
    }
}


// 5^-3 = 1 ÷ 5 ÷ 5 ÷ 5 = 0.008

// -5^-0.5 = 1 /
