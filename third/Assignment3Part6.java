package com.shpp.p2p.cs.ldebryniuk.assignment2;

import acm.graphics.GOval;
import com.shpp.cs.a.graphics.WindowProgram;

import java.awt.*;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Random;

/**
 * This class
 */
public class Assignment3Part6 extends WindowProgram {

    private static final double PAUSE_TIME = 1000.0 / 20;

    // The default width and height of the window. These constants will tell Java to
    // create a window whose size is *approximately* given by these dimensions
    public static final int APPLICATION_WIDTH = 900;
    public static final int APPLICATION_HEIGHT = 700;

    private static final Color BACKGROUND_COLOR = new Color(8, 83, 132, 255);

    private ArrayList<GOval> listOfSnowFlakes = new ArrayList<GOval>();

    /**
     * this is the starting method of the program
     */
    public void run() {
        stopInFiveSeconds();

        setBackground(BACKGROUND_COLOR);

        createSnowFlakes();
        addSnowFlakesToCanvas();

        drawASnowMan();

        letItSnow();
    }

    private void addSnowFlakesToCanvas() {
        for (GOval snowFlake : listOfSnowFlakes) {
            add(snowFlake);
        }
    }

    private void createSnowFlakes() {
        int num_of_rows = 20;
        double y = 30;

        // create a row of snow flakes
        for (int i = 0; i < num_of_rows; i++) {
            createRowOfSnowFlakes(y);

            y += 30;
        }
    }

    private void createRowOfSnowFlakes(double y) {
        Random rand = new Random();
        double distanceBetweenSnowFlakes;
        double diameterOfSnowFlake;
        double x = rand.nextInt(30 - 5) + 5;

        int numOfSnowFlakesInRow = 13;

        for (int i = 0; i < numOfSnowFlakesInRow; i++) {
            distanceBetweenSnowFlakes = rand.nextInt(100 - 50) + 50;
            diameterOfSnowFlake = rand.nextInt(7 - 2) + 2;

            listOfSnowFlakes.add(
                    drawOval(x, y, diameterOfSnowFlake)
            );

            x += distanceBetweenSnowFlakes;
        }
    }

    private void drawASnowMan() {
        double diameter = 180;
        double x = 50;
        double y = getHeight() - diameter;

        for (int i = 0; i < 3; i++) {
            add(drawOval(x, y, diameter));

            diameter *= 0.67;
            y -= diameter;
            x += (diameter * 0.25);
        }

    }

    private GOval drawOval(double x, double y, double diameter) {
        GOval oval = new GOval(x, y, diameter, diameter);
        oval.setFilled(true);
        oval.setColor(Color.WHITE);
        return oval;
    }

    private void letItSnow() {
        Random rand = new Random();
        int random = 0;
        double x = 0;

        while (true){
            for (GOval listOfSnowFlake : listOfSnowFlakes) {
                random = rand.nextInt(5 - 1) + 1;
                x = (x % 2 == 0) ? -random : random;
                listOfSnowFlake.move(x, 1);
            }

            pause(PAUSE_TIME*2);
        }
    }

    private void stopInFiveSeconds() {
        new Timer().schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        System.out.println("here");
                        pause(1000000);
                    }
                },
                5000
        );
    }


}
