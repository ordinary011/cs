package com.shpp.p2p.cs.ldebryniuk.assignment3;

import com.shpp.cs.a.console.TextProgram;

import java.util.Random;

/**
 * This class allows us to play saint petersburg's game
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

    /**
     * The following method statrts the game
     */
    private void startGame() {
        Random rand = new Random();

        int luckyHasMoney = 0;
        int countOfGames = 0;

        // start iteration of the game
        while (luckyHasMoney < 20) {
            int moneyReward = tossCoinUntillTails(1, rand);

            luckyHasMoney += moneyReward;

            System.out.println("This game, you earned " + moneyReward);
            System.out.println("Your total is $" + luckyHasMoney);

            countOfGames++;
        }

        System.out.println("It took " + countOfGames + " games to earn $20");
    }

    /**
     * The following method simulates tossing of coin
     *
     * @param moneyReward amount of money at the beginning of gambling
     * @param rand        a reference to a random generator
     * @return amount of money that were gained in the current game iteration
     */
    private int tossCoinUntillTails(int moneyReward, Random rand) {
        // coin > 15000 means heads
        // coin < 15000 means tails
        int coinValue = rand.nextInt(30000); // generates int in range between 1 to 30000
        while (coinValue > 15000) {
            moneyReward *= 2;

            coinValue = rand.nextInt(30000); // generates int in range between 1 to 30000
        }

        // if tails we return reward
        return moneyReward;
    }

}
