package com.shpp.p2p.cs.ldebryniuk.assignment2;

import com.shpp.cs.a.console.TextProgram;

import java.util.Random;

/**
 * This class
 * <p>
 * used resources: https://www.educative.io/edpresso/how-to-generate-random-numbers-in-java
 */
public class Assignment3Part5 extends TextProgram {

    /**
     * This is the starting method of the program
     */
    public void run() {
        startGame();
    }

    private void startGame() {
        Random rand = new Random();

        int luckyHasMoney = 0;
        int countOfGames = 0;

        while (luckyHasMoney < 20) {
            int moneyReward = 1;

            // toss a coin
            // coin > 15000 means heads
            int coin = rand.nextInt(30000);
            while (coin > 15000) {
                moneyReward *= 2;

                coin = rand.nextInt(30000);
            }

            luckyHasMoney += moneyReward;
            countOfGames++;

            System.out.println("This game, you earned " + moneyReward);
            System.out.println("Your total is $" + luckyHasMoney);
        }

        System.out.println("It took " + countOfGames + " games to earn $20");
    }

}
