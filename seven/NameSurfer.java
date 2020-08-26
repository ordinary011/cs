package com.shpp.p2p.cs.ldebryniuk.assignment7;

/*
 * File: NameSurfer.java
 * ---------------------
 * When it is finished, this program will implements the viewer for
 * the baby-name database described in the assignment handout.
 */

import com.shpp.cs.a.simple.SimpleProgram;

import javax.swing.*;
import java.awt.event.*;

public class NameSurfer extends SimpleProgram implements NameSurferConstants {

    // contains info about all of the names
    private final NameSurferDataBase db = new NameSurferDataBase(NAMES_DATA_FILE);;
    // manages adding and removing of the graphs to the pop up window
    private final NameSurferGraph graphManager = new NameSurferGraph();
    // contains name entered by the user
    private final JTextField textField = new JTextField(20);

    /* Method: init() */

    /**
     * This method has the responsibility for reading in the data base
     * and initializing the interactors at the top of the window.
     */
    public void init() {
        add(graphManager);

        addInteractors();
    }

    /**
     * The following method adds a label, text field and two buttons to the pop up screen
     */
    private void addInteractors() {
        add(new JLabel("Name:"), NORTH);

        textField.setActionCommand("EnterWasPressed");
        textField.addActionListener(this);
        add(textField, NORTH);

        createButton("Graph", "AddGraphBtnPressed");
        createButton("Clear", "ClearBtnPressed");
        addActionListeners(); // listeners for buttons
    }

    /**
     * The following method adds a button to the pop up screen
     *
     * @param label     name that will be put on the button
     * @param actionCmd string that is needed for distinguishing button press events
     */
    private void createButton(String label, String actionCmd) {
        JButton btn = new JButton(label);
        btn.setActionCommand(actionCmd);
        add(btn, NORTH);
    }

    /* Method: actionPerformed(e) */

    /**
     * This class is responsible for detecting when the buttons are
     * clicked, so you will have to define a method to respond to
     * button actions.
     */
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();

        switch (cmd) {
            case "EnterWasPressed", "AddGraphBtnPressed" -> addGraphEntry();
            case "ClearBtnPressed" -> graphManager.clear();
            default -> println("something else happened...");
        }
    }

    /**
     * The following method searches and adds a graph that contains information about the name
     */
    private void addGraphEntry() {
        NameSurferEntry entry = db.findEntry(textField.getText());
        if (entry != null) graphManager.addEntry(entry);
    }
}
