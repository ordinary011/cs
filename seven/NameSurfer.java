package com.shpp.p2p.cs.ldebryniuk.assignment7;

/*
 * File: NameSurfer.java
 * ---------------------
 * When it is finished, this program will implements the viewer for
 * the baby-name database described in the assignment handout.
 */

import acm.graphics.GCanvas;
import com.shpp.cs.a.simple.SimpleProgram;

import javax.swing.*;
import java.awt.event.*;

public class NameSurfer extends SimpleProgram implements NameSurferConstants {

	/* Method: init() */
    private JTextField textField;

    /**
     * This method has the responsibility for reading in the data base
     * and initializing the interactors at the top of the window.
     */
    public void init() {
        add(new NameSurferGraph());
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

        if (cmd.equals("GraphBtnPressed")) {
            System.out.println("GraphBtnPressed");
        } else if (cmd.equals("ClearBtnPressed")) {
            System.out.println("ClearBtnPressed");
        } else if (cmd.equals("EnterWasPressed")) {
            System.out.println("EnterWasPressed");
            System.out.println(textField.getText());
        } else {
            println("something else happened...");
        }
    }
}
