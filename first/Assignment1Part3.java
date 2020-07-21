package com.shpp.p2p.cs.ldebryniuk.assignment1;

public class Assignment1Part3 extends AuxKarel {

    public void run() throws Exception {
        // put beepers into every cell and turn around
        populateCellsWithBeepers();
        turnAround();

        // we need this if we are in a world with ONLY one cell
        // when we remove this "if", one celled world will crash
        if (frontIsClear()) {
            isKarelInTheCenter();
        }
    }

    private void isKarelInTheCenter() throws Exception {
        // no beepers present means that Karel is in the center
        if (noBeepersPresent()) {
            putBeeper();
        } else {
            // else means Karel is not in the center yet so we will remove outer beepers
            // until Karel reaches the center
            removeOuterBeepers();
        }
    }

    private void removeOuterBeepers() throws Exception {
        // pick up the first outer beeper and move to the second
        pickBeeper();
        move();
        while (beepersPresent()){
            move();
        }

        // at this point Karel is one cell ahead of the second outer beeper
        // so we need to turn around and move to the cell with the second outer beeper
        // or to the center if there are no more beepers left
        turnAround();
        move();

        // now we check if Karel is in the center or in the second outer beeper
        isKarelInTheCenter();
    }

    private void populateCellsWithBeepers() throws Exception {
        // in case we have ONLY one celled world
        if (frontIsBlocked()) {
            putBeeper();
        }

        // put beeper into every consequent cell
        while (frontIsClear()) {
            move();
            putBeeper();
        }
    }
}
