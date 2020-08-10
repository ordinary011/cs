package com.shpp.p2p.cs.ldebryniuk.assignment3;

import acm.graphics.GOval;
import com.shpp.cs.a.graphics.WindowProgram;

import java.awt.*;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Random;

/**
 * The following class depicts animation of falling snow
 */
public class Assignment3Part6 extends WindowProgram {

    // frames per second
    private static final double PAUSE_TIME = 1000.0 / 24;

    // The default width and height of the window. These constants will tell Java to
    // create a window whose size is *approximately* given by these dimensions
    public static final int APPLICATION_WIDTH = 900;
    public static final int APPLICATION_HEIGHT = 700;

    private static final Color BACKGROUND_COLOR = new Color(8, 83, 132, 255);
    private static final Color SNOWMAN_COLOR = new Color(248, 248, 248, 228);

    private final ArrayList<GOval> listOfSnowFlakes = new ArrayList<GOval>();

    private final Random rand = new Random();

    /**
     * this is the starting method of the program
     */
    public void run() {
        setBackground(BACKGROUND_COLOR);

        drawASnowMan();

        createSnowFlakes();
        addSnowFlakesToCanvas();

        stopInFiveSeconds();

        letItSnow();
    }

    /**
     * The following method creates snowflakes for our app
     */
    private void createSnowFlakes() {
        int numOfSnowFlakeRows = 35;
        double y = -460; // at the beginning rows will be above the visible canvas

        // create a row of snow flakes
        for (int i = 0; i < numOfSnowFlakeRows; i++) {
            createRowOfSnowFlakes(y);

            // every subsequent row will be 30 px lower than previous
            y += 30;
        }
    }

    /**
     * The following method creates snowflakes and adds them to ArrauList
     *
     * @param y The y coordinate of the upper-left corner of the circle
     */
    private void createRowOfSnowFlakes(double y) {
        double leftOffsetOfSnowFlake;
        double diameterOfSnowFlake;
        double x = rand.nextInt(30 - 5) + 5;

        int numOfSnowFlakesInRow = 13;

        for (int i = 0; i < numOfSnowFlakesInRow; i++) {
            leftOffsetOfSnowFlake = rand.nextInt(100 - 50) + 50;
            diameterOfSnowFlake = rand.nextInt(7 - 2) + 2;

            listOfSnowFlakes.add(
                    createOval(x, y, diameterOfSnowFlake, Color.WHITE)
            );

            x += leftOffsetOfSnowFlake;
        }
    }

    /**
     * The following method depicts snowflakes on the canvas
     */
    private void addSnowFlakesToCanvas() {
        for (GOval snowFlake : listOfSnowFlakes) {
            add(snowFlake);
        }
    }

    /**
     * The following method depicts a snowman on the canvas
     */
    private void drawASnowMan() {
        drawSnowManBody();

        drawBlackCirclesOnTheSnowMan();
    }

    /**
     * The following method depicts body of the snowman
     */
    private void drawSnowManBody() {
        double diameterOfBodyPart = 180;
        double x = 50;
        double y = getHeight() - diameterOfBodyPart;

        // draw body
        for (int i = 0; i < 3; i++) {
            add(createOval(x, y, diameterOfBodyPart, SNOWMAN_COLOR));

            diameterOfBodyPart *= 0.66;
            y -= diameterOfBodyPart;
            x += (diameterOfBodyPart * 0.25);
        }
    }

    /**
     * The following method depicts buttons smile and eyes on the snowman. They all represented as black circles
     */
    private void drawBlackCirclesOnTheSnowMan() {
        // initial parameters for black circles
        double x = 130;
        double buttonDiameter = 20;
        double y = getHeight() - buttonDiameter - 25;

        // draw buttons on the bottom circle of the the snowman body
        drawButtonsOnTheSnowMan(x, y, buttonDiameter, 50, 3);

        // draw buttons on the center circle of the the snowman body
        drawButtonsOnTheSnowMan(x + 3, y - 180,
                buttonDiameter * 0.66, 40, 2);

        // draw smile on the head of the snowman
        drawSmile(x, y - 275, buttonDiameter * 0.25);

        // draw snowman eyes
        add(createOval(x - 5, y - 310, buttonDiameter * 0.35, Color.BLACK));
        add(createOval(x + 15, y - 310, buttonDiameter * 0.35, Color.BLACK));
    }

    /**
     * The following method depicts a smile snowman's head. Smile consists of black circles
     */
    private void drawSmile(double x, double y, double buttonDiameter) {
        // draw the lowest part of the smile
        add(createOval(x, y, buttonDiameter, Color.BLACK));
        add(createOval(x + 10, y, buttonDiameter, Color.BLACK));

        // draw the central part of the smile
        add(createOval(x - 10, y - 7, buttonDiameter, Color.BLACK));
        add(createOval(x + 20, y - 7, buttonDiameter, Color.BLACK));

        // draw the upper part of the smile
        add(createOval(x - 20, y - 17, buttonDiameter, Color.BLACK));
        add(createOval(x + 30, y - 17, buttonDiameter, Color.BLACK));
    }

    /**
     * The following method depicts black buttons on the snowman
     * Buttons are represented as black circles
     *
     * @param x                       The x coordinate of the upper-left corner of the circle
     * @param y                       The y coordinate of the upper-left corner of the circle
     * @param buttonDiameter          diameter of the circle (circle == black button)
     * @param yDistanceBetweenButtons vertical distance between buttons
     * @param numOfButtons            amount of buttons in this part of the snowman body
     */
    private void drawButtonsOnTheSnowMan(double x, double y, double buttonDiameter,
                                         int yDistanceBetweenButtons, int numOfButtons) {
        for (int i = 0; i < numOfButtons; i++) {
            add(createOval(x, y, buttonDiameter, Color.BLACK));

            y -= yDistanceBetweenButtons;
        }
    }

    /**
     * The following method depicts a circle on the canvas
     *
     * @param x        The x coordinate of the upper-left corner of the circle
     * @param y        The y coordinate of the upper-left corner of the circle
     * @param diameter diameter of the circle
     * @param color    color of the circle
     * @return either a part of the snowman body or a snowflake or a black buttpn
     */
    private GOval createOval(double x, double y, double diameter, Color color) {
        GOval oval = new GOval(x, y, diameter, diameter);
        oval.setFilled(true);
        oval.setColor(color);
        return oval;
    }

    /**
     * The following method starts animation of snow
     */
    private void letItSnow() {
        int random;
        double x = 0;

        while (true) {
            for (GOval snowFlake : listOfSnowFlakes) {
                random = rand.nextInt(5 - 1) + 1; // random between 5 and 1

                // determines how to change position of a snowFlake (move right or left)
                x = (x % 2 == 0) ? -random : random;
                snowFlake.move(x, 3);
            }

            pause(PAUSE_TIME);
        }
    }

    /**
     * The following method stops application in 5 second
     */
    private void stopInFiveSeconds() {
        new Timer().schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        System.exit(0);
                    }
                },
                5000
        );
    }
}
