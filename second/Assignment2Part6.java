package com.shpp.p2p.cs.ldebryniuk.assignment2;

import acm.graphics.GOval;
import com.shpp.cs.a.graphics.WindowProgram;

import java.awt.*;

/**
 * This class depicts a caterpillar
 */
public class Assignment2Part6 extends WindowProgram {

    // specifies the maount of caterpilar's sergments
    private static final double AMOUNT_OF_SEGMENTS = 3.0;

    // caterpilar's sergments consists of 2 circles. Below we specify the DIAMETER of one circle
    private static final double DIAMETER = 100;
    private static final double RADIUS = DIAMETER / 2;

    // we assume that the distance between caterpillar circles is 60% of the DIAMETER
    private static final double DISTANCE_BETWEEN_OVALS = DIAMETER * 0.6;

    public static final int APPLICATION_WIDTH = (int) (AMOUNT_OF_SEGMENTS * (DIAMETER + RADIUS));
    public static final int APPLICATION_HEIGHT = (int) (DIAMETER * 2.5);

    // x and y coordinates are used as an offset for a caterpilar circles
    private static double x = 0;
    private static double y = RADIUS;

    /**
     * this is the starting method of the program
     */
    public void run() {
        // draw the caterpilar by segments
        for (int i = 0; i < AMOUNT_OF_SEGMENTS; i++) {
            // depicts one segment of the caterpillar
            drawCaterpillarSegment();
        }
    }

    /**
     * this method depicts one caterpilar segment which consists of two circles
     */
    private void drawCaterpillarSegment() {
        // draw first circle of the segment and change coordinates
        drawCircle();
        x += DISTANCE_BETWEEN_OVALS;
        y = 0;

        // draw second circle of the segment and change coordinates
        drawCircle();
        x += DISTANCE_BETWEEN_OVALS;
        y = RADIUS;
    }

    /**
     * this method depicts a circle that is the part of the caterpillar segment
     */
    private void drawCircle() {
        GOval oval = new GOval(x, y, DIAMETER, DIAMETER);

        oval.setFilled(true);
        oval.setColor(Color.RED);
        oval.setFillColor(Color.GREEN);

        add(oval);
    }

}
