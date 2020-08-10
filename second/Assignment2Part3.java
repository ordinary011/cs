package com.shpp.p2p.cs.ldebryniuk.assignment2;

import acm.graphics.GOval;
import com.shpp.cs.a.graphics.WindowProgram;

import java.awt.*;

/*
 * The following class creates two black pawprints of a cartoon animal. ^_^)
 */
public class Assignment2Part3 extends WindowProgram {
    /* Constants controlling the relative positions of the
     * three toes to the upper-left corner of the pawprint.
     *
     * (Yes, I know that actual pawprints have four toes.
     * Just pretend it's a cartoon animal. ^_^)
     */
    private static final double FIRST_TOE_OFFSET_X = 0;
    private static final double FIRST_TOE_OFFSET_Y = 20;
    private static final double SECOND_TOE_OFFSET_X = 30;
    private static final double SECOND_TOE_OFFSET_Y = 0;
    private static final double THIRD_TOE_OFFSET_X = 60;
    private static final double THIRD_TOE_OFFSET_Y = 20;

    /* The position of the heel relative to the upper-left
     * corner of the pawprint.
     */
    private static final double HEEL_OFFSET_X = 20;
    private static final double HEEL_OFFSET_Y = 40;

    /* Each toe is an oval with this width and height. */
    private static final double TOE_WIDTH = 20;
    private static final double TOE_HEIGHT = 30;

    /* The heel is an oval with this width and height. */
    private static final double HEEL_WIDTH = 40;
    private static final double HEEL_HEIGHT = 60;

    /* The default width and height of the window. These constants will tell Java to
     * create a window whose size is *approximately* given by these dimensions. You should
     * not directly use these constants in your program; instead, use getWidth() and
     * getHeight(), which return the *exact* width and height of the window.
     */
    public static final int APPLICATION_WIDTH = 270;
    public static final int APPLICATION_HEIGHT = 220;

    /**
     * This is the starting method of the program
     */
    public void run() {
        drawPawprint(20, 20);
        drawPawprint(180, 70);
    }

    /**
     * Draws a pawprint. The parameters should specify the upper-left corner of the
     * bounding box containing that pawprint.
     *
     * @param x The x coordinate of the upper-left corner of the bounding box for the pawprint.
     * @param y The y coordinate of the upper-left corner of the bounding box for the pawprint.
     */
    private void drawPawprint(double x, double y) {
        // draw toes
        drawOval(x + FIRST_TOE_OFFSET_X, y + FIRST_TOE_OFFSET_Y, TOE_WIDTH, TOE_HEIGHT);
        drawOval(x + SECOND_TOE_OFFSET_X, y + SECOND_TOE_OFFSET_Y, TOE_WIDTH, TOE_HEIGHT);
        drawOval(x + THIRD_TOE_OFFSET_X, y + THIRD_TOE_OFFSET_Y, TOE_WIDTH, TOE_HEIGHT);

        // draw the heel for toes =))
        drawOval(x + HEEL_OFFSET_X, y + HEEL_OFFSET_Y, HEEL_WIDTH, HEEL_HEIGHT);
    }

    /**
     * Draws either a heel or a toe. The parameters should specify the upper-left corner of the
     * bounding box containing that oval.
     *
     * @param x The x coordinate of the upper-left corner of the bounding box for the oval.
     * @param y The y coordinate of the upper-left corner of the bounding box for the oval.
     */
    private void drawOval(double x, double y, double width, double height) {
        GOval oval = new GOval(x, y, width, height);
        oval.setFilled(true);
        add(oval);
    }

}