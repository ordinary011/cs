package com.shpp.p2p.cs.ldebryniuk.assignment3;

import com.shpp.cs.a.console.TextProgram;

/**
 * This class allows us to play saint petersburg game
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
     * The following method starts the game
     */
    private void startGame() {
        int luckyHasMoney = 0;
        int countOfGames = 0;

        // start iteration of the game
        while (luckyHasMoney < 20) {
            int moneyReward = tossCoinUntilTailsAndGetReward();

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
     * @return amount of money that were gained in the current game iteration
     */
    private int tossCoinUntilTailsAndGetReward() {
        int moneyReward = 1;

        int coinValue = (int) (Math.random() * 2); // generates 0 or 1
        while (coinValue == 1) { // 1 means heads. 0 means tails
            moneyReward *= 2;

            coinValue = (int) (Math.random() * 2); // generates 0 or 1
        }

        // if tails we return reward
        return moneyReward;
    }

}
