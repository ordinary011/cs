package com.shpp.p2p.cs.ldebryniuk.assignment2;

import acm.graphics.GOval;
import com.shpp.cs.a.graphics.WindowProgram;

import java.awt.*;

/**
 * This class depicts a caterpillar
 */
public class Assignment2Part6 extends WindowProgram {

    /* CUSTOMIZABLE CONSTANTS */
    // specifies the amount of circles in caterpilar
    private static final int AMOUNT_OF_SEGMENTS = 8;
    // specifies DIAMETER of one circle
    private static final double DIAMETER = 100.0;
    // The default width and height of the window. These constants will tell Java to
    // create a window whose size is *approximately* given by these dimensions
    public static final int APPLICATION_WIDTH = 600;
    public static final int APPLICATION_HEIGHT = 600;

    /* NOT CUSTOMIZABLE CONSTANTS */
    // we assume that the distance between circles in caterpillar is 60% of the DIAMETER
    private static final double DISTANCE_BETWEEN_OVALS = DIAMETER * 0.6;
    // specifies radius of one circle
    private static final double RADIUS = DIAMETER / 2;
    // x and y coordinates are used as an offset for a caterpilar circles
    private static double x = 0;
    private static double y = RADIUS;

    /**
     * this is the starting method of the program
     */
    public void run() {
        // Drawing of caterpilar by segments
        for (int i = 1; i <= AMOUNT_OF_SEGMENTS; i++) {
            drawSegment();

            // determines whether upper or lower segment goes in the next iteration
            y = i % 2 == 0 ? RADIUS : 0; // upper segments have vertical offset of zero
            x += DISTANCE_BETWEEN_OVALS; // increasing x for the subsequent circle
        }
    }

    /**
     * this method depicts a circle that is the segment of caterpillar
     */
    private void drawSegment() {
        GOval oval = new GOval(x, y, DIAMETER, DIAMETER);

        oval.setFilled(true);
        oval.setColor(Color.RED);
        oval.setFillColor(Color.GREEN);

        add(oval);
    }

}
