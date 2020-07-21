package com.shpp.p2p.cs.ldebryniuk.assignment2;

import acm.graphics.GLabel;
import acm.graphics.GRect;
import com.shpp.cs.a.graphics.WindowProgram;

import java.awt.*;

/**
 * This class depicts a flag and a description to it
 */
public class Assignment2Part4 extends WindowProgram {

    private static final double FLAG_WIDTH = 200.0;
    private static final double FLAG_HEIGHT = 150.0;

    private static final double NUMBER_OF_COLUMNS_IN_FLAG = 3.0;
    private static final double NUMBER_OF_COLUMNS_IN_APPLICATION = 5.0;
    public static final double COLUMN_WIDTH = FLAG_WIDTH / NUMBER_OF_COLUMNS_IN_FLAG;

    public static final int APPLICATION_WIDTH = (int) (COLUMN_WIDTH * NUMBER_OF_COLUMNS_IN_APPLICATION);
    public static final int APPLICATION_HEIGHT = (int) (FLAG_HEIGHT + (FLAG_HEIGHT * 0.5));

    /**
     * This is the starting method of the program
     */
    public void run() {
        double VERTICAL_OFFSET = (getHeight() - FLAG_HEIGHT) / 2.0;

        // draw flag by partitions
        drawFlagPartition(COLUMN_WIDTH, VERTICAL_OFFSET, Color.BLACK);
        drawFlagPartition(COLUMN_WIDTH * 2, VERTICAL_OFFSET, Color.YELLOW);
        drawFlagPartition(COLUMN_WIDTH * 3, VERTICAL_OFFSET, Color.RED);

        // specifies what kind of flag it is
        addFlagInfo();
    }

    /**
     * This method is used for depicting an info about the flag
     */
    private void addFlagInfo() {
        GLabel gLabel = new GLabel("Flag of Belgium");

        // we assume that the appropriate fontSize will be 4% of the application width
        int fontSize = (int) (getWidth() * 0.04);
        Font gLabelFont = new Font(Font.SANS_SERIF, Font.BOLD, fontSize);
        gLabel.setFont(gLabelFont);

        // determining the locqtion for the sign
        double x = getWidth() - gLabel.getWidth();
        double y = getHeight() - (getHeight() * 0.02);
        gLabel.setLocation(x, y);

        add(gLabel);
    }

    /**
     * This method is used for depicting of one patition of the flag
     */
    private void drawFlagPartition(double x, double y, Color color) {
        GRect square = new GRect(x, y, COLUMN_WIDTH, FLAG_HEIGHT);

        square.setFilled(true);
        square.setColor(color);

        add(square);
    }

}
