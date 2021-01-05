package com.shpp.p2p.cs.ldebryniuk.assignment8;

import acm.graphics.GObject;
import acm.graphics.GOval;
import acm.util.RandomGenerator;
import com.shpp.cs.a.graphics.WindowProgram;

import java.awt.event.MouseEvent;

public class Assignment8Part1 extends WindowProgram {

    // amount of circles on the screen
    private final int CIRCLE_N = 6;
    // pool of threads (each thread moves its own circles)
    private final CircleThread[] threadPool = new CircleThread[CIRCLE_N];
    // diameter of each circle
    private final double DIAMETER = 70.0; // pixels
    // total amount of threads in the thread pool
    private int threadCount = 0;
    // application params
    private int WIDTH;
    private int HEIGHT;

    /**
     * This is the starting method of the program
     */
    public void run() {
        WIDTH = getWidth();
        HEIGHT = getHeight();

        addMouseListeners();
        addCircles();
    }

    /**
     * The following method draws CIRCLE_N circles on the screen
     */
    private void addCircles() {
        double x = 0;
        double y = 0;

        // each iteration adds a new circle to the canvas
        for (int i = 0; i < CIRCLE_N; i++) {
            createOval(x, y);

            // prepare for the next iteration
            x += DIAMETER;
            y += DIAMETER;
        }

    }

    /**
     * The following method depicts a circle on the canvas
     *
     * @param x The x coordinate of the upper-left corner of the circle
     * @param y The y coordinate of the upper-left corner of the circle
     */
    private void createOval(double x, double y) {
        GOval oval = new GOval(x, y, DIAMETER, DIAMETER);
        oval.setColor(RandomGenerator.getInstance().nextColor());
        oval.setFilled(true);
        add(oval);
    }

    /**
     * The following method is executed every time a mouse is clicked
     * It creates a new thread for a circle and starts its movement or
     * if circle already has a thread it will change its color and direction
     *
     * @param e mouse event object
     */
    public void mousePressed(MouseEvent e) {
        GObject circle = getElementAt(e.getX(), e.getY());

        if (circle != null) {
            int threadNum = getThreadNum(circle);
            if (threadNum > -1) { // if thread for the circle already exists
                threadPool[threadNum].changeDirection();
                threadPool[threadNum].setColor(RandomGenerator.getInstance().nextColor());
            } else { // create thread for the circle
                String threadName = "Thread" + threadCount;
                threadPool[threadCount] = new CircleThread(threadName, circle, DIAMETER, WIDTH, HEIGHT);
                threadCount++;
            }
        }
    }

    /**
     * The following method searches for a thread in the thread pool by comparing the references of circle objects
     *
     * @param circle reference to the current circle object
     * @return index of the thread in the thread pool
     */
    private int getThreadNum(GObject circle) {
        for (int i = 0; i < threadCount; i++) {
            if (threadPool[i].getCircle() == circle) { // if thread is already in pool
                return i;
            }
        }

        return -1;
    }
}
