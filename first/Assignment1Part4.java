package com.shpp.p2p.cs.ldebryniuk.assignment1;

// this class creates a chess-like field that is filled with beepers
public class Assignment1Part4 extends AuxKarel {

    // This is the starting method
    public void run() throws Exception {
        // in case we have ONLY ONE vertical row
        if (frontIsBlocked()) {
            turnLeft();
        }

        // begin building the field
        populateRowAndCheckIsThereNextRow();
    }

     /*
     * This method puts beepers in cells. Distance between beepers is 1 cell.
     * It also checks if there is one more row to go?
     *
     * Beginning: karel is at the end of the current row. Facing either east or west
     *
     * Result: it will turn in the direction of the next row even if the next row doesn't exist
     */
    private void populateRowAndCheckIsThereNextRow() throws Exception {
        populateRow();

        // check if there is next row, if so move to the next row
        isThereNextRow();
    }

    /*
     * This method populates the current cell. Then starts moving to the end of the row populating others
     *
     * Beginning: karel is either at the beginning or at the end of the current row. Facing either east or west
     *
     * Result: The row is populated with beepers
     */
    private void populateRow() throws Exception {
        putBeeper();
        while (frontIsClear()) {
            move();
            if (frontIsClear()) {
                move();
                putBeeper();
            }
        }
    }

    /*
     * This method checks if there is next row, if so it moves to the next row
     *
     * Beginning: karel is either at the beginning or at the end of the current row.
     *  Facing either east or west. The row is already populated with beepers
     *
     * Result: If next row exists it will change the row and will populate it.
     */
    private void isThereNextRow() throws Exception {
        // turn in the direction of the next row
        if (facingEast()) {
            turnLeft();
        } else {
            turnRight();
        }

        // front is clear only if there is another row
        if (frontIsClear()) {
            changeRow();
            populateRowAndCheckIsThereNextRow();
        }
    }

    /*
     * This method moves Karel to the next row and moves one cell ahead if necessary
     *
     * Beginning: This method is called if we are sure that there is a next row. karel is facing the next row and front is clear
     *
     * Result: the karel is in the next row and it is facing the direction of that row
     * It also moved 1 cell forward if it was needed
     */
    private void changeRow() throws Exception {
        if (noBeepersPresent()) {
            changeRowAndTurn();
        } else {
            changeRowAndTurn();
            move();
        }
    }

    /*
     * This method moves Karel to the next row and turnes
     *
     * Beginning: This method is called if we are sure that there is a next row. karel is facing the next row and front is clear
     *
     * Result: the karel is in the next row and it is facing the direction of that row
     */
    private void changeRowAndTurn() throws Exception {
        // move to the next row
        move();

        // turns Karel In The Direction Of Current Row
        if (rightIsBlocked()) {
            turnLeft();
        } else {
            turnRight();
        }
    }
}
