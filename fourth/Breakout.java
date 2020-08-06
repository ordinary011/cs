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
    public static final int APPLICATION_HEIGHT = 1000;

    /**
     * Dimensions of game board (usually the same)
     */
    private static int WIDTH = APPLICATION_WIDTH;
    private static int HEIGHT = APPLICATION_HEIGHT;

    /**
     * Dimensions of the paddle
     */
    private static final int PADDLE_WIDTH = 500;
    private static final int PADDLE_HEIGHT = 10;

    /**
     * Offset of the paddle up from the bottom
     */
    private static final int PADDLE_Y_OFFSET = 50;

    /**
     * Number of bricks per row
     */
    private static final int NBRICKS_PER_ROW = 1;

    /**
     * Number of rows of bricks
     */
    private static final int NBRICK_ROWS = 1;

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
    private static final int BRICK_Y_OFFSET = 60;

    /**
     * Number of turns
     */
    private static final int NTURNS = 3;

    /**
     * paddle follows the mouse and is supposed to redirect the ball
     * paddleY is y coordinate of the paddle
     */
    private GRect paddle = null;
    private double paddleY;

    /**
     * vx, vy coordinates specify the shift of the ball coordinates per frame
     */
    private double vx, vy;

    /**
     * This is the starting method of the program
     */
    public void run() {
        WIDTH = getWidth();
        HEIGHT = getHeight();

        addPaddle();
        addBricks();

        addMouseListeners();
        initGame();
    }

    /**
     * The following method adds a paddle to the screen
     */
    private void addPaddle() {
        paddleY = getHeight() - PADDLE_Y_OFFSET;

        paddle = new GRect(0, paddleY, PADDLE_WIDTH, PADDLE_HEIGHT);
        paddle.setFilled(true);

        add(paddle);
    }

    /**
     * The following method depicts bricks
     */
    private void addBricks() {
        // bricks should appear in the center
        final double BRICK_OFFSET = BRICK_WIDTH + BRICK_SEP;
        final double BRICK_ROW_WIDTH = (NBRICKS_PER_ROW * BRICK_OFFSET) - BRICK_SEP;
        final double X = (getWidth() - BRICK_ROW_WIDTH) / 2;
        double y = BRICK_Y_OFFSET;

        // every second row should have the same color as one before
        Color[] colors = {Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.CYAN};
        byte colorIndex = 0;

        // draw bricks. row by row
        for (byte i = 1; i <= NBRICK_ROWS; i++) {
            drawRowOfBricks(X, y, BRICK_OFFSET, colors[colorIndex]);

            // after each row is depicted we shift y offset
            y += BRICK_HEIGHT;

            if (i % 2 == 0) { // increase color index every second iteration
                colorIndex++;
                // if we have more than 10 rows we will start over picking colors from the beginning
                if (i % 10 == 0) colorIndex = 0;
            }

        }
    }

    /**
     * The following method depicts row of bricks
     *
     * @param x            The x coordinate of the upper-left corner
     * @param y            The y coordinate of the upper-left corner
     * @param BRICK_OFFSET is BRICK_WIDTH + BRICK_SEP
     * @param color        is color of the brick
     */
    private void drawRowOfBricks(double x, double y, double BRICK_OFFSET, Color color) {
        for (int i = 0; i < NBRICKS_PER_ROW; i++) {
            drawBrick(x, y, color);

            x += BRICK_OFFSET;
        }
    }

    /**
     * The following method depicts one brick
     *
     * @param x     The x coordinate of the upper-left corner
     * @param y     The y coordinate of the upper-left corner
     * @param color is color of the brick
     */
    private void drawBrick(double x, double y, Color color) {
        GRect rect = new GRect(x, y, BRICK_WIDTH, BRICK_HEIGHT);

        rect.setFilled(true);
        rect.setColor(color);

        add(rect);
    }

    /**
     * The following method initializes the game
     */
    private void initGame() {
        // initial variables for the game
        int bricksLeft = NBRICK_ROWS * NBRICKS_PER_ROW;

        // start game. Each iteration is a new round
        for (int i = NTURNS; i > 0; i--) {
            if (bricksLeft < 1) break; // if true -> user win

            GOval ball = prepareAndDrawBall();

            // add start label and wait till user makes a mouse click
            GLabel label = addLabel("Click to start");
            waitForClick();

            // round begins below
            remove(label); // removes start label
            bricksLeft = startMovement(ball, bricksLeft);
        }

        // add finish label
        addLabel(
                (bricksLeft > 0) ? "Game over" : "You won=)))"
        );
    }

    /**
     * The following method starts animation. Ball starts to move
     *
     * @param ball       the ball that is moving
     * @param bricksLeft amount of bricks that are still in the game
     * @return amount of bricks that are still in the game
     */
    private int startMovement(GOval ball, int bricksLeft) {
        final double PAUSE_TIME = 1000.0 / 48; // frames per second
        GObject collider; // either a paddle or a brick
        final double DIAMETER = BALL_RADIUS * 2;

        while (!((ball.getY() + DIAMETER) > HEIGHT)) { // while ball not below floor
            ball.move(vx, vy);

            if (ball.getY() < 0) { // if ball above ceiling
                vy = -vy;
            }

            if ((ball.getX() + DIAMETER) > WIDTH) { // if ball beyond right edge
                vx = -vx;
            }

            if (ball.getX() < 0) { // if ball beyond left edge
                vx = -vx;
            }

            // check for collisions
            collider = getCollidingObject(ball);
            if (collider != null) { // if ball hit something
                bricksLeft = determineCollider(collider, ball, bricksLeft);
                if (bricksLeft < 1) break;  // if true -> user won =))
            }

            pause(PAUSE_TIME);
        }

        // either we win or lose we remove ball when round ends
        remove(ball);

        return bricksLeft;
    }

    /**
     * The following method search for ball collisions with other objects
     *
     * @param ball depicted ball
     * @return collided object if it is found
     */
    private GObject getCollidingObject(GOval ball) {
        final double x = ball.getX();
        final double y = ball.getY();
        final double DIAMETER = BALL_RADIUS * 2;

        if (getElementAt(x + DIAMETER, y + DIAMETER) != null)
            return getElementAt(x + DIAMETER, y + DIAMETER);
        if (getElementAt(x, y + DIAMETER) != null) return getElementAt(x, y + DIAMETER);
        if (getElementAt(x + DIAMETER, y) != null) return getElementAt(x + DIAMETER, y);
        if (getElementAt(x, y) != null) return getElementAt(x, y);

        return null;
    }

    /**
     * The following method determines the collider object. can be either a brick or a paddle
     *
     * @param collider   either a paddle or a brick
     * @param ball       the ball that is moving
     * @param bricksLeft amount of bricks that are still in the game
     * @return amount of bricks that are still in the game
     */
    private int determineCollider(GObject collider, GOval ball, int bricksLeft) {
        if (collider == paddle) {
            // the below if is needed in order to omit "ball is stuck to the paddle" bug
            if ((ball.getY() + BALL_RADIUS) < paddleY) { // if ball above paddle
                vy = -vy; // change ball direction
            }
        } else { // means ball hits a brick
            remove(collider); // remove the brick
            vy = -vy; // change ball direction
            bricksLeft--;
        }

        return bricksLeft;
    }

    /**
     * The following method initilizes ball shifts per frame and depicts it
     *
     * @return depicted ball
     */
    private GOval prepareAndDrawBall() {
        // setup ball shift per frame
        generateRandomVXForTheBall();
        vy = 5.0;

        // initial coordinates for the ball to appear in the center
        double x = (WIDTH / 2.0) - BALL_RADIUS;
        double y = (HEIGHT / 2.0) - BALL_RADIUS;

        GOval ball = new GOval(x, y, BALL_RADIUS * 2, BALL_RADIUS * 2);
        ball.setFilled(true);

        add(ball);
        return ball;
    }

    /**
     * The following method generates random horizontal direction for a ball
     */
    private void generateRandomVXForTheBall() {
        RandomGenerator rgen = RandomGenerator.getInstance();
        vx = rgen.nextDouble(1.0, 3.0);
        if (rgen.nextBoolean(0.5)) {
            vx = -vx;
        }
    }


    /**
     * The following method depicts a label
     *
     * @param msg message to be depicted
     * @return label that is currently depicted on the screen
     */
    private GLabel addLabel(String msg) {
        // set location and message of the label
        final double x = WIDTH * 0.34;
        final double y = HEIGHT * 0.40;
        GLabel label = new GLabel(msg, x, y);

        // set font and color of the label
        final int fontSize = (int) (WIDTH * 0.05);
        label.setFont("Serif-" + fontSize);
        label.setColor(Color.RED);

        add(label);
        return label;
    }

    /**
     * The following method fires when a mouse move event appears
     *
     * @param mouseEvent event that just appeared
     */
    public void mouseMoved(MouseEvent mouseEvent) {
        final double halfOfPaddleWidth = (PADDLE_WIDTH / 2.0);
        double x = mouseEvent.getX();

        // check if mouse within the game area
        if (!(x < halfOfPaddleWidth) && !(x > (getWidth() - halfOfPaddleWidth))) {
            // make rect follow mouse. Mouse is to be located in the center of the rect
            x -= halfOfPaddleWidth;
            paddle.setLocation(x, paddleY);
        }
    }

}


