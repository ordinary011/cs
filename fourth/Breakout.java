package com.shpp.p2p.cs.ldebryniuk.assignment4;

import acm.graphics.GLabel;
import acm.graphics.GObject;
import acm.graphics.GOval;
import acm.graphics.GRect;
import acm.util.RandomGenerator;
import com.shpp.cs.a.graphics.WindowProgram;

import java.awt.*;
import java.awt.event.MouseEvent;

public class Breakout extends WindowProgram {
    /**
     * Width and height of application window in pixels
     */
    public static final int APPLICATION_WIDTH = 1800;
    public static final int APPLICATION_HEIGHT = 900;

    /**
     * Dimensions of game board (usually the same)
     */
    private static int WIDTH = APPLICATION_WIDTH;
    private static int HEIGHT = APPLICATION_HEIGHT;

    /**
     * Dimensions of the paddle
     */
    private static final int PADDLE_WIDTH = 150;
    private static final int PADDLE_HEIGHT = 10;

    /**
     * Offset of the paddle up from the bottom
     */
    private static final int PADDLE_Y_OFFSET = 50;

    /**
     * Number of bricks per row
     */
    private static final int NBRICKS_PER_ROW = 10;

    /**
     * Number of rows of bricks
     */
    private static final int NBRICK_ROWS = 14;

    /**
     * Separation between bricks
     */
    private static final int BRICK_SEP = 4;

    /**
     * Width of a brick
     */
    private static final int BRICK_WIDTH =
            (WIDTH - (NBRICKS_PER_ROW - 1) * BRICK_SEP) / NBRICKS_PER_ROW;

    /**
     * Height of a brick
     */
    private static final int BRICK_HEIGHT = 15;

    /**
     * Radius of the ball in pixels
     */
    private static final int BALL_RADIUS = 5;

    /**
     * Offset of the top brick row from the top
     */
    private static final int BRICK_Y_OFFSET = 40;

    /**
     * Number of turns
     */
    private static final int NTURNS = 3;

    GRect paddle = null;
    private double paddleY;

    private double vx, vy;

    public void run() {
        WIDTH = getWidth();
        HEIGHT = getHeight();

        drawPaddle();
        addMouseListeners();

        GOval ball = drawBall();

        drawBricks();

        startGame(ball);
    }

    private void drawBricks() {
        double rowLength = (NBRICKS_PER_ROW * (BRICK_WIDTH + BRICK_SEP)) - BRICK_SEP;
        double x = (getWidth() - rowLength) / 2;
        double y = BRICK_Y_OFFSET;

        Color[] colors = {Color.RED, Color.RED,
                Color.ORANGE, Color.ORANGE,
                Color.YELLOW, Color.YELLOW,
                Color.GREEN, Color.GREEN,
                Color.CYAN, Color.CYAN};
        int colorIndex = 0;

        // row by row
        for (int i = 0; i < NBRICK_ROWS; i++) {
            drawRowOfBricks(x, y, colors[colorIndex]);

            y += BRICK_HEIGHT + 10;
            colorIndex++;

            if (colorIndex % 10 == 0) {
                colorIndex = 0;
            }
        }
    }

    private void drawRowOfBricks(double x, double y, Color color) {
        for (int i = 0; i < NBRICKS_PER_ROW; i++) {
            drawBrick(x, y, color);

            x += (BRICK_WIDTH + BRICK_SEP);
        }
    }

    private GOval drawBall() {
        // centerify
        double x = (WIDTH / 2) - BALL_RADIUS;
        double y = (HEIGHT / 2) - BALL_RADIUS;

        GOval ball = new GOval(x, y, BALL_RADIUS * 2, BALL_RADIUS * 2);
        ball.setFilled(true);

        add(ball);
        return ball;
    }

    private void startGame(GOval ball) {
        final double DIAMETER = BALL_RADIUS * 2;
        final double GRAVITY = 0.090;
        final double PAUSE_TIME = 1000.0 / 48;
        GObject collider;

        RandomGenerator rgen = RandomGenerator.getInstance();
        vx = rgen.nextDouble(1.0, 3.0);
        if (rgen.nextBoolean(0.5)) {
            vx = -vx;
        }
        vy = -3.0;


        while (true) {
            ball.move(vx, vy);

            if (vy > 0) {       // means the ball is going down
                vy += GRAVITY;
            } else {
                vy = -3.0;
            }

            // is ballAboveCeeling
            if (ball.getY() < 0) {
                vy = -vy;
            }

            // is ball beyond right edge
            if ((ball.getX() + DIAMETER) > WIDTH) {
                vx = -vx;
            }

            // is ball beyond left edge
            if (ball.getX() < 0) {
                vx = -vx;
            }

            collider = getCollidingObject(ball);
            if (collider != null) {
                if (collider == paddle) {
                    vy = -vy;
                } else {
                    remove(collider);
                    vy = -vy;
                }
            }

            // is ball below floor
            if ((ball.getY() + DIAMETER) > HEIGHT) {
                // game over
                addFinishLabel();
                break;
            }

            pause(PAUSE_TIME);
        }
    }

    private void addFinishLabel() {
        GLabel label = new GLabel("Game over", 300, 300);
        label.setFont("Serif-30");
        label.setColor(Color.RED);
        add(label);
    }

    private GObject getCollidingObject(GOval ball) {
        double x = ball.getX();
        double y = ball.getY();
        double DIAMETER = BALL_RADIUS * 2;

        if (getElementAt(x, y) != null) return getElementAt(x, y);
        if (getElementAt(x + DIAMETER, y) != null) return getElementAt(x + DIAMETER, y);
        if (getElementAt(x, y + DIAMETER) != null) return getElementAt(x, y + DIAMETER);
        if (getElementAt(x + DIAMETER, y + DIAMETER) != null)
            return getElementAt(x + DIAMETER, y + DIAMETER);

        return null;
    }


    public void mouseMoved(MouseEvent mouseEvent) {
        double x = mouseEvent.getX();
        double halfOfPaddleWidth = (PADDLE_WIDTH / 2.0);

        // check if mouse within the game area
        if (!(x < halfOfPaddleWidth) && !(x > (getWidth() - halfOfPaddleWidth))) {
            // make rect follow mouse. Mouse is located in the center of rect
            x -= halfOfPaddleWidth;
            paddle.setLocation(x, paddleY);
        }
    }

    private void drawPaddle() {
        paddleY = getHeight() - PADDLE_Y_OFFSET;

        paddle = new GRect(59, paddleY, PADDLE_WIDTH, PADDLE_HEIGHT);
        paddle.setFilled(true);

        add(paddle);
    }

    private void drawBrick(double x, double y, Color color) {
        GRect rect = new GRect(x, y, BRICK_WIDTH, BRICK_HEIGHT);

        rect.setFilled(true);
        rect.setColor(color);

        add(rect);
    }
}

