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

    private NameSurferDataBase db;
    private NameSurferGraph graph;
    private JTextField textField;

    /* Method: init() */
    /**
     * This method has the responsibility for reading in the data base
     * and initializing the interactors at the top of the window.
     */
    public void init() {
        db = new NameSurferDataBase(NAMES_DATA_FILE);

        graph = new NameSurferGraph();
        add(graph);

        add(new JLabel("Name:"), NORTH);

        textField = new JTextField(20);
        textField.setActionCommand("EnterWasPressed");
        textField.addActionListener(this);
        add(textField, NORTH);

        JButton graphBtn = new JButton("Graph");
        graphBtn.setActionCommand("GraphBtnPressed");
        add(graphBtn, NORTH);

        JButton clearBtn = new JButton("Clear");
        clearBtn.setActionCommand("ClearBtnPressed");
        add(clearBtn, NORTH);

        addActionListeners();
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
            case "EnterWasPressed" -> addGraph();
            case "GraphBtnPressed" -> addGraph();
            case "ClearBtnPressed" -> graph.clear();
            default -> println("something else happened...");
        }
    }

    private void addGraph() {
        NameSurferEntry entry = db.findEntry(textField.getText());
        if (entry != null) graph.addEntry(entry);
    }
}
