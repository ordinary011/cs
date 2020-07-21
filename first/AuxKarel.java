package com.shpp.p2p.cs.ldebryniuk.assignment1;

import com.shpp.karel.KarelTheRobot;

//This class is contains the most common Karel methods
public class AuxKarel extends KarelTheRobot {

    public void turnAround() throws Exception {
        turnLeft();
        turnLeft();
    }

    public void turnRight() throws Exception {
        turnLeft();
        turnLeft();
        turnLeft();
    }

}
