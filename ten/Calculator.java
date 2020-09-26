package com.shpp.p2p.cs.ldebryniuk.assignment10;

import java.util.Arrays;

public class Calculator {
    private final Replacer replacer = new Replacer();
    private final Operator op = new Operator();

    /**
     * The following method is the starting point of the calculator. Operations are performed in the prioritized order
     */
    public String runCalc(String[] args) {
        replacer.formatArgs(args);
        StringBuilder formula = new StringBuilder(args[0]);

        try {
            replacer.replaceVars(formula, args);

            op.powerUp(formula);
            op.doOneOfFourOperations(formula, "*", "/");
            op.doOneOfFourOperations(formula, "+", "-");
        } catch (Exception e) {
            System.out.println("Input mistake. Please check your formula and variables");
            return null;
        }

        return formula.toString();
    }

    // cases: 11*-3^2 || 10-3^3 || 10+3^3 || -3^3 || 3^2
    public void runTests() {
        String[][] tests = {
                {"5/0"}, {"∞"},
                {"22"}, {"22"},
                {"1+3^2+2^3"}, {"18"},
                {"5-10+3^2"}, {"4"},
                {"10*-3^3"}, {"-270"},
                {"-5^-5"}, {"-0.00032"},
                {"5^-5"}, {"0.00032"},
                {"5^5"}, {"3125"},
                {"-5^5"}, {"-3125"},
                {"1+3^2"}, {"10"},
                {"11-3^2+3^3"}, {"29"},
                {"3/4^-2"}, {"48"},
                {"3/-4^-2"}, {"48"},
                {"-3/-4^-2"}, {"-48"},
                {"1.0+ 2"}, {"3"},
                {"3+2,0"}, {"5"},
                {"1+3*2"}, {"7"},
                {"-10+3"}, {"-7"},
                {"0-5"}, {"-5"},
                {"10*-3"}, {"-30"},
                {"3*4"}, {"12"},
                {"3*4*10"}, {"120"},
                {"3*4*10/10"}, {"12"},
                {"3/4"}, {"0.75"},
                {"5-3"}, {"2"},
                {"2-3"}, {"-1"},
                {"-2-3"}, {"-5"},
                {"11-10/-2"}, {"16"},
                {"2.33*2"}, {"4.66"},
                {"11-10/-a", "a=5"}, {"13"}, // with args
                {"11-10/-a", "a=-5"}, {"9"},
                {"11+10/-a", "a=-5"}, {"13"},
                {"5^a", "a = 4"}, {"625"},
                {"5^-a", "a=4"}, {"0.0016"},
                {"-3*a^2", "a=2"}, {"-12"},
                {"-3*a^2", "a=-2"}, {"-12"},
                {"2,0*a", "a=-2"}, {"-4"},
                {"-a*2.0", "a=-2"}, {"4"},
                {"-a*2*4", "a=-2"}, {"16"},
                {"2-a", "a=-2"}, {"4"},
                {"a2", "a=5"}, {"10"},
                {"2a", "a=5"}, {"10"},
                {"3*a", "a=-2"}, {"-6"},
                {"3+a", "a=-3"}, {"0"},
                {"3a", "a=-3"}, {"-9"},
                {"3 a", "a=-3"}, {"-9"},
                {"3-a", "a=-3"}, {"6"},
                {" 2-a*5", "a=-2"}, {"12"},
                {"a*a", "a=2"}, {"4"},
                {"2-a*a", "a=2"}, {"-2"},
                {"5^-a", "a=-4"}, {"625"},
                {"a*a", " a=4"}, {"16"},
                {"a*a", "a=-4"}, {"16"},
                {"-a*a", "a=-4"}, {"-16"},
                {"-a*a", "a=4"}, {"-16"},
                {"a+5", "a=-4"}, {"1"},
                {"32+a2", "a=4"}, {"40"},
                {"5-a", "a=-4"}, {"9"},
                {"-a+5", "a=-4"}, {"9"},
                {"-a*a", "a=-4"}, {"-16"},
                {"5-a*a", "a=-2"}, {"1"},
                {"a+55*a", "a=10"}, {"560"},
                {"33+2a", "a=5"}, {"43"},
                {"1+a*2", "a=2"}, {"5"},
                {"1+a*2/2", "a=2"}, {"3"},
                {"1+a*2/2-1", "a=2"}, {"2"},
                {"11*a^3", "a=-2"}, {"-88"},
                {"-a+5.0", "a=-2.000"}, {"7"},
                {"dodo-10*3", "dodo=33"}, {"3"},
                {"a2+b/2-c", "a=5", "b=6", "c=3"}, {"10"},
                {"5-10+a^2-b^c", "a=5", "b=5", "c=5"}, {"-3105"},
                {"d-10+a^2-b^c", "c=-5", "a= 5", "b= -5", "d=33"}, {"48.00032"},
                {"d-10+a^2-b^c+a", "c=-5", "a= 5", "b= -5", "d=33"}, {"53.00032"},
                {"-5-10+a^2-b^codi +10000.44", "codi =5", "a=5", "b=5"}, {"6885.44"},
        };

        for (int i = 0; i < tests.length; i += 2) {
            String res = runCalc(tests[i]);
            if (res != null && res.equals(tests[i + 1][0])) {
                System.out.println("  Pass: " + Arrays.toString(tests[i]) + " Result: " + res);
            } else {
                System.out.println("! FAIL: " + Arrays.toString(tests[i]) +
                        " Expected " + tests[i + 1][0] + " Got: " + res);
                return;
            }
        }

        System.out.println("PASSED ALL TESTS");
    }

}
