package com.shpp.p2p.cs.ldebryniuk.assignment2;

import acm.graphics.GOval;
import acm.graphics.GRect;
import com.shpp.cs.a.graphics.WindowProgram;

import java.awt.*;

/**
 * This class depicts four black circles and one white rectangle that overlaps them
 */
public class Assignment2Part2 extends WindowProgram {

    /* CUSTOMIZABLE CONSTANTS */
    // specifies diameter of black circles
    private static final int DIAMETER = 100;
    // The default width and height of the window. These constants will tell Java to
    // create a window whose size is *approximately* given by these dimensions
    public static final int APPLICATION_WIDTH = 500;
    public static final int APPLICATION_HEIGHT = 500;

    /* NOT CUSTOMIZABLE CONSTANTS */
    // specifies radius of black circles
    private static final double RADIUS = DIAMETER / 2.0;

    /* OTHER CLASS MEMBERS */
    // is used as a width of the white rectangle and as a left offset for some circles
    private double horizontal_distance = 0;
    // is used as a height of the white rectangle and as a top offset for some circles
    private double vertical_distance = 0;

    /**
     * This is the starting method of the program
     */
    public void run() {
        calculateDistances();

        drawFourBlackCircles();

        drawWhiteRectangle();
    }

    /**
     * This method calculates distances that are needed for offsets of circles and rectangle
     */
    private void calculateDistances() {
        horizontal_distance = getWidth() - DIAMETER;
        vertical_distance = getHeight() - DIAMETER;
    }

    /**
     * This method depicts four black circles
     */
    private void drawFourBlackCircles() {
        drawCircle(0, 0);
        drawCircle(horizontal_distance, 0);
        drawCircle(0, vertical_distance);
        drawCircle(horizontal_distance, vertical_distance);
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
        add(circle);
    }

    /**
     * This method depicts a white square that overlaps four black circles
     */
    private void drawWhiteRectangle() {
        GRect square = new GRect(RADIUS, RADIUS, horizontal_distance, vertical_distance);
        square.setFilled(true);
        square.setColor(Color.WHITE);
        add(square);
    }
}