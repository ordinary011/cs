package com.shpp.p2p.cs.ldebryniuk.assignment8;

import acm.graphics.GObject;

import java.awt.*;

public class CircleThread implements Runnable {

    // reference to the circle object
    private final GObject circle;
    // name of the thread
    private final String name;
    // circle diameter
    private final double DIAMETER;
    // application params
    private final int WIDTH;
    private final int HEIGHT;
    // change of delta pixels per frame
    private final double CIRCLE_d_SHIFT = 2.0; // pixels;
    // circle initial delta params
    private double dx = CIRCLE_d_SHIFT;
    private double dy = 0;

    /**
     * Creates a thread for a circle and provides its movement
     *
     * @param threadName name of the current thread
     * @param circle reference to the circle object
     * @param DIAMETER of the circle
     * @param WIDTH application width
     * @param HEIGHT application height
     */
    CircleThread(String threadName, GObject circle, double DIAMETER, int WIDTH, int HEIGHT) {
        this.name = threadName;
        this.circle = circle;
        this.DIAMETER = DIAMETER;
        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;
        new Thread(this, name).start();
    }

    // This is the starting method of current thread
    @Override
    public void run() {
        try {
            startMovingCircle();
        } catch (Exception e) {
            System.out.println(name + " interrupted");
        }
        System.out.println(name + " exiting");
    }

    /**
     * The following method moves circles and reverses its direction when it hits screen edges
     *
     * @throws InterruptedException if the thread is interrupted
     */
    private void startMovingCircle() throws InterruptedException {
        while (true) {
            circle.move(dx, dy);

            if ((circle.getX() + DIAMETER) > WIDTH) { // if circle beyond right edge
                dx = -dx;
                circle.move(dx, dy); // prevents circle from being stuck in the wall
            }

            if (circle.getX() < 0) { // if circle beyond left edge
                dx = -dx;
                circle.move(dx, dy); // prevents circle from being stuck in the wall
            }

            if (circle.getY() < 0) { // if circle above ceiling
                dy = -dy;
                circle.move(dx, dy); // prevents circle from being stuck in the wall
            }

            if ((circle.getY() + DIAMETER) > HEIGHT) { // if circle below floor
                dy = -dy;
                circle.move(dx, dy); // prevents circle from being stuck in the wall
            }

            Thread.sleep(10);
        }
    }

    /**
     * Changes circle direction from horizontal to vertical and vice versa
     */
    public void changeDirection() {
        if (dx != 0) { // if circle is moving horizontally
            this.dx = 0;
            this.dy = CIRCLE_d_SHIFT;
        } else { // circle is moving vertically
            this.dx = CIRCLE_d_SHIFT;
            this.dy = 0;
        }
    }

    public GObject getCircle() {
        return circle;
    }

    public void setColor(Color color) {
        circle.setColor(color);
    }
}
