package com.shpp.p2p.cs.ldebryniuk.assignment1;

public class Assignment1Part1 extends AuxKarel {

    public void run() throws Exception {
        // move one row down
        changeRow();

        // move in the direction of beeper and pick it up
        moveFourCells();
        pickBeeper();

        // turn around and start moving back to the start
        turnAround();
        moveFourCells();

        // return to the very first cell
        changeRow();
    }

    private void moveFourCells() throws Exception {
        for (int i = 0; i < 4; i++) {
            move();
        }
    }

    private void changeRow() throws Exception {
        turnRight();
        move();
        turnLeft();
    }
}
