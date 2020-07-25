package com.shpp.p2p.cs.ldebryniuk.assignment2;

import acm.graphics.GLabel;
import acm.graphics.GRect;
import com.shpp.cs.a.graphics.WindowProgram;

import java.awt.*;

/**
 * This class depicts a flag and a description to it
 */
public class Assignment2Part4 extends WindowProgram {

    /* CUSTOMIZABLE CONSTANTS */
    // specifies diameter of black circles
    private static final double FLAG_WIDTH = 200.0;
    private static final double FLAG_HEIGHT = 150.0;
    // The default width and height of the window. These constants will tell Java to
    // create a window whose size is *approximately* given by these dimensions
    public static final int APPLICATION_WIDTH = 300;
    public static final int APPLICATION_HEIGHT = 250;

    /* NOT CUSTOMIZABLE CONSTANTS */
    // specifies amount of stripes in the flag
    private static final int NUMBER_OF_STRIPES_IN_FLAG = 3;
    // calculation of the stripe length
    private static final double STRIPE_WIDTH = FLAG_WIDTH / NUMBER_OF_STRIPES_IN_FLAG;

    /**
     * This is the starting method of the program
     */
    public void run() {
        drawFlag();

        addFlagInfo();
    }

    /**
     * This method is used for depicting a flag
     */
    private void drawFlag() {
        // calculating offsets for flag
        final double VERTICAL_OFFSET = (getHeight() - FLAG_HEIGHT) / 2.0;
        final double HORIZONTAL_OFFSET = (getWidth() - FLAG_WIDTH) / 2.0;

        // Depicting of flag -> stripe by spripe
        drawFlagStripe(HORIZONTAL_OFFSET, VERTICAL_OFFSET, Color.BLACK);
        drawFlagStripe(HORIZONTAL_OFFSET + STRIPE_WIDTH, VERTICAL_OFFSET, Color.YELLOW);
        drawFlagStripe(HORIZONTAL_OFFSET + STRIPE_WIDTH * 2, VERTICAL_OFFSET, Color.RED);
    }

    /**
     * This method is used for depicting of one patition of the flag
     */
    private void drawFlagStripe(double x, double y, Color color) {
        GRect square = new GRect(x, y, STRIPE_WIDTH, FLAG_HEIGHT);
        square.setFilled(true);
        square.setColor(color);
        add(square);
    }

    /**
     * This method is used for depicting an info about the flag
     */
    private void addFlagInfo() {
        GLabel gLabel = new GLabel("Flag of Belgium");

        // we assume that the appropriate fontSize is 4% of the application width
        int fontSize = (int) (getWidth() * 0.04); // 0.04 is 4% of total width
        Font gLabelFont = new Font(Font.SANS_SERIF, Font.BOLD, fontSize);
        gLabel.setFont(gLabelFont);

        // determining the locqtion for the sign
        double x = getWidth() - gLabel.getWidth();
        double y = getHeight() - (getHeight() * 0.015); // 0.015 is 1.5% of total height
        gLabel.setLocation(x, y);

        add(gLabel);
    }
}
