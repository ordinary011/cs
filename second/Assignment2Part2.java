package com.shpp.p2p.cs.ldebryniuk.assignment2;

import acm.graphics.GOval;
import acm.graphics.GRect;
import com.shpp.cs.a.graphics.WindowProgram;

import java.awt.*;

/**
 * This class depicts four black circles and one white rectangle that overlaps them
 */
public class Assignment2Part2 extends WindowProgram {

    // specifies diameter for black circles
    private static final int DIAMETER = 100;
    private static final double RADIUS = DIAMETER / 2.0;

    // 3 is just a multiplier which makes our pop up window 3 times longer that the diameter
    public static final int APPLICATION_LENGTH = DIAMETER * 3;

    public static final int APPLICATION_WIDTH = APPLICATION_LENGTH;
    public static final int APPLICATION_HEIGHT = APPLICATION_LENGTH;

    /**
     * This is the starting method of the program
     */
    public void run() {
        // HORIZONTAL_DISTANCE is used as a width of the white rectangle
        // and as a left offset for some circles. We make it twice longer than DIAMETER
        double HORIZONTAL_DISTANCE = DIAMETER * 2;

        // VERTICAL_DISTANCE is used as a height of the white rectangle
        // and as a right offset for some circles. it is 60% of the total height
        double VERTICAL_DISTANCE = getHeight() * 0.60;

        // drawing 4 black circles
        drawCircle(0, 0);
        drawCircle(HORIZONTAL_DISTANCE, 0);
        drawCircle(0, VERTICAL_DISTANCE);
        drawCircle(HORIZONTAL_DISTANCE, VERTICAL_DISTANCE);

        // drawing a white square that overlaps black circles
        GRect square = new GRect(RADIUS, RADIUS, HORIZONTAL_DISTANCE, VERTICAL_DISTANCE);
        square.setFilled(true);
        square.setColor(Color.WHITE);
        add(square);
    }

    /**
     * This method draws a black circle on a pop up screen
     *
     * @param x The x coordinate of the upper-left corner of the bounding box for the circle.
     * @param y The y coordinate of the upper-left corner of the bounding box for the circle.
     */
    private void drawCircle(double x, double y) {
        GOval circle = new GOval(x, y, DIAMETER, DIAMETER);

        circle.setFilled(true);
        circle.setColor(Color.BLACK);

        add(circle);
    }
}