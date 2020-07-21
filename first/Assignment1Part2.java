package com.shpp.p2p.cs.ldebryniuk.assignment1;

public class Assignment1Part2 extends AuxKarel {

    public void run() throws Exception {
        buildColumnAndMoveToTheNextColumn();
    }

    private void buildColumnAndMoveToTheNextColumn() throws Exception {
        buildColumn();

        moveToTheNextColumn();
    }

    private void buildColumn() throws Exception {
        // Karel is facing east our columns are vertical so we turn
        turnLeft();

        // start building the column
        if (noBeepersPresent()) {
            putBeeper();
        }
        while (frontIsClear()) {
            move();
            if (noBeepersPresent()) {
                putBeeper();
            }
        }

        // after column is built move to the bottom of the column
        turnAround();
        while (frontIsClear()) {
            move();
        }
    }

    private void moveToTheNextColumn() throws Exception {
        // turn In Direction Of the Next Column
        turnLeft();

        // move to the next column
        // if there is no next column just turn right
        for (int i = 0; i < 4; i++) {
            if (frontIsClear()) {
                move();
            } else {
                // if there is no next column we turn Karel to the right
                turnRight();
            }
        }

        // if Karel is faced to east, then we arrived to the next column
        // if this column would not exist the Karel would face another direction
        if (facingEast()) {
            buildColumnAndMoveToTheNextColumn();
        }
    }
}
