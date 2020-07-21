package com.shpp.p2p.cs.ldebryniuk.assignment2;

import acm.graphics.GRect;
import com.shpp.cs.a.graphics.WindowProgram;

import java.awt.*;

/**
 * This class depicts certain amount of black squares in the center of the screen
 */
public class Assignment2Part5 extends WindowProgram {

    /* The number of rows and columns in the grid, respectively. */
    private static final int NUM_ROWS = 5;
    private static final int NUM_COLS = 6;

    /* The width and height of each box. */
    private static final double BOX_SIZE = 40;

    /* The horizontal and vertical spacing between the boxes. */
    private static final double BOX_SPACING = 10;

    // OFFSET is used for calculating approximate initial pop up model size
    private static final double OFFSET = BOX_SIZE + BOX_SPACING;

    // HORIZONTAL_LENGTH and VERTICAL_LENGTH represent total length of all the boxes
    private static final double HORIZONTAL_LENGTH = OFFSET * NUM_COLS - BOX_SPACING;
    private static final double VERTICAL_LENGTH = OFFSET * NUM_ROWS - BOX_SPACING;

    public static final int APPLICATION_WIDTH = (int) (HORIZONTAL_LENGTH + OFFSET);
    public static final int APPLICATION_HEIGHT = (int) (VERTICAL_LENGTH + OFFSET + BOX_SPACING);

    /**
     * This is the starting method of the program
     */
    public void run() {
        // initial coordinates for the first row of squares
        double x = (getWidth() - HORIZONTAL_LENGTH) / 2.0;
        double y = (getHeight() - VERTICAL_LENGTH) / 2.0;

        // Drawing black squares. Row by row
        for (int i = 0; i < NUM_ROWS; i++) {
            drawRow(x, y);

            // after a row was drawn we will increase y by offset
            // this is needed for the next row
            y += OFFSET;
        }
    }

    /**
     * this method depicts a row of black squares
     *
     * @param x The x coordinate of the upper-left corner of the bounding box for the rectangle
     * @param y The y coordinate of the upper-left corner of the bounding box for the row of rectangles
     */
    private void drawRow(double x, double y) {
        for (int i = 0; i < NUM_COLS; i++) {
            drawSquare(x, y);

            // after a square was drawn we will increase x by offset
            // this is needed for the next square
            x += OFFSET;
        }
    }

    /**
     * this method depicts one black square
     *
     * @param x The x coordinate of the upper-left corner of the bounding box for the rectangle
     * @param y The y coordinate of the upper-left corner of the bounding box for the rectangle
     */
    private void drawSquare(double x, double y) {
        GRect square = new GRect(x, y, BOX_SIZE, BOX_SIZE);

        square.setFilled(true);
        square.setColor(Color.BLACK);

        add(square);
    }
}
