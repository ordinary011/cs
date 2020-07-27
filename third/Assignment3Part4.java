package com.shpp.p2p.cs.ldebryniuk.assignment2;

import acm.graphics.GRect;
import com.shpp.cs.a.graphics.WindowProgram;

import java.awt.*;

/**
 * The following class depicts a pyramid of black squares
 */
public class Assignment3Part4 extends WindowProgram {

    /* CUSTOMIZABLE CONSTANTS */
    private static final double BRICK_HEIGHT = 25;
    private static final double BRICK_WIDTH = 50;
    private static final int BRICKS_IN_BASE = 12;
    // The default width and height of the window. These constants will tell Java to
    // create a window whose size is *approximately* given by these dimensions
    public static final int APPLICATION_WIDTH = 700;
    public static final int APPLICATION_HEIGHT = 500;

    /**
     * This is the starting method of the program
     */
    public void run() {
        drawPyramid();
    }

    /**
     * The following method depicts a pyramid of black squares
     */
    private void drawPyramid() {
        int numOfBricksInRow = BRICKS_IN_BASE;

        // draw Pyramid. Row by row
        for (int i = 1; i <= BRICKS_IN_BASE; i++) { // BRICKS_IN_BASE == number of rows in pyramid
            double rowLength = BRICK_WIDTH * numOfBricksInRow;

            // we want our pyramid to be center horozontally
            // and to be located at the very bottom of the canvas
            double x = (getWidth() - rowLength) / 2;
            double y = getHeight() - (BRICK_HEIGHT * i);

            drawRowOfBricks(numOfBricksInRow, x, y);

            // each subsiquent row must be shorter than previous
            numOfBricksInRow--;
        }
    }

    /**
     * The following method depicts a row of black squares
     *
     * @param numOfBricksInRow specifies amount of bricks in current row
     * @param x                The x coordinate of the upper-left corner
     * @param y                The y coordinate of the upper-left corner
     */
    private void drawRowOfBricks(int numOfBricksInRow, double x, double y) {
        for (int i = 0; i < numOfBricksInRow; i++) {
            drawBrick(x, y);

            // increase offset for the next brick in the current row
            x += BRICK_WIDTH;
        }
    }

    /**
     * The following method depicts one black square (which is considered as a brick in this app)
     *
     * @param x The x coordinate of the upper-left corner
     * @param y The y coordinate of the upper-left corner
     */
    private void drawBrick(double x, double y) {
        GRect square = new GRect(x, y, BRICK_WIDTH, BRICK_HEIGHT);

        square.setFilled(true);
        square.setColor(Color.WHITE);
        square.setFillColor(Color.BLACK);

        add(square);
    }

}
