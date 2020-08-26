package com.shpp.p2p.cs.ldebryniuk.assignment7;

/*
 * File: NameSurferGraph.java
 * ---------------------------
 * This class represents the canvas on which the graph of
 * names is drawn. This class is responsible for updating
 * (redrawing) the graphs whenever the list of entries changes
 * or the window is resized.
 */

import acm.graphics.*;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class NameSurferGraph extends GCanvas
        implements NameSurferConstants, ComponentListener {

    private final ArrayList<NameSurferEntry> graphs = new ArrayList<>();
    private final Color[] colors = {Color.BLUE, Color.RED, Color.MAGENTA, Color.BLACK};

    /**
     * Creates a new NameSurferGraph object that displays the data.
     */
    public NameSurferGraph() {
        addComponentListener(this);
        // You fill in the rest //
    }


    /**
     * Clears the list of name surfer entries stored inside this class.
     */
    public void clear() {
        graphs.clear();
        update();
    }


    /* Method: addEntry(entry) */

    /**
     * Adds a new NameSurferEntry to the list of entries on the display.
     * Note that this method does not actually draw the graph, but
     * simply stores the entry; the graph is drawn by calling update.
     */
    public void addEntry(NameSurferEntry entry) {
        graphs.add(entry);
        update();
    }


    /**
     * Updates the display image by deleting all the graphical objects
     * from the canvas and then reassembling the display according to
     * the list of entries. Your application must call update after
     * calling either clear or addEntry; update is also called whenever
     * the size of the canvas changes.
     */
    public void update() {
        removeAll();
        final double WIDTH = getWidth();
        final double HEIGHT = getHeight();

        final double DISTANCE_BETWEEN_VERTICAL_LINES = WIDTH / NDECADES;
        final double GRAPH_UPPER_EDGE = HEIGHT * 0.05;
        final double GRAPH_LOWER_EDGE = HEIGHT - GRAPH_UPPER_EDGE;

        double initialX = 2.0; // pixels

        drawBackground(initialX, DISTANCE_BETWEEN_VERTICAL_LINES, GRAPH_UPPER_EDGE, GRAPH_LOWER_EDGE, WIDTH, HEIGHT);

        drawGraphs(initialX, DISTANCE_BETWEEN_VERTICAL_LINES, GRAPH_UPPER_EDGE, GRAPH_LOWER_EDGE);
    }

    private void drawBackground(double x, double DISTANCE_BETWEEN_VERTICAL_LINES, double GRAPH_UPPER_EDGE,
                                double GRAPH_LOWER_EDGE, double WIDTH, double HEIGHT) {
        // determine font for decade labels
        final int fontSIze = (int) (((WIDTH + HEIGHT) / 2) * 0.03);
        final Font FONT = new Font("Serif", Font.BOLD, fontSIze);
        int decade = START_DECADE;

        // add two horizontal lines (edges) for the graphs
        add(new GLine(0, GRAPH_UPPER_EDGE, WIDTH, GRAPH_UPPER_EDGE));
        add(new GLine(0, GRAPH_LOWER_EDGE, WIDTH, GRAPH_LOWER_EDGE));

        // draw vertical lines (background mesh) and decade labels
        for (int i = 0; i < NDECADES; i++) {
            add(new GLine(x, 0, x, HEIGHT));

            // add decade label
            addLabel(String.valueOf(decade), x, HEIGHT, FONT, Color.BLACK);

            // prepare variables for the next iteration
            x += DISTANCE_BETWEEN_VERTICAL_LINES;
            decade += 10;
        }
    }

    private void drawGraphs(double x, double DISTANCE_BETWEEN_VERTICAL_LINES,
                            double GRAPH_UPPER_EDGE, double GRAPH_LOWER_EDGE) {
        int colorIndex = 0;
        final int fontSIze = (int) (DISTANCE_BETWEEN_VERTICAL_LINES * 0.15);
        final Font FONT = new Font("Serif", Font.PLAIN, fontSIze);
        final double DISTANCE_BETWEEN_GRAPH_EDGES = GRAPH_LOWER_EDGE - GRAPH_UPPER_EDGE;

        for (NameSurferEntry entry : graphs) {
            String name = entry.getName() + " ";

            for (int j = 0; j < NDECADES - 1; j++) { // draw a graph
                int currentRank = entry.getRank(j);
                double currentRankPercent = currentRank * DISTANCE_BETWEEN_GRAPH_EDGES / MAX_RANK;
                double nextRankPercent = entry.getRank(j + 1) * DISTANCE_BETWEEN_GRAPH_EDGES / MAX_RANK;

                if (currentRankPercent == 0) currentRankPercent = DISTANCE_BETWEEN_GRAPH_EDGES;
                if (nextRankPercent == 0) nextRankPercent = DISTANCE_BETWEEN_GRAPH_EDGES;

                double y = GRAPH_UPPER_EDGE + currentRankPercent;

                // add rank label
                String rankName = name + (currentRank == 0 ? "*" : currentRank);
                addLabel(rankName, x, y, FONT, colors[colorIndex]);

                GLine line = new GLine(x, y,
                        x + DISTANCE_BETWEEN_VERTICAL_LINES, GRAPH_UPPER_EDGE + nextRankPercent);
                line.setColor(colors[colorIndex]);
                add(line);

                x += DISTANCE_BETWEEN_VERTICAL_LINES; // increase for the next iteration
            }

            colorIndex++;
            if (colorIndex == 4) colorIndex = 0;
            x = 2.0;
        }
    }

    private void addLabel(String name, double x, double y, Font font, Color color) {
        GLabel label = new GLabel(name, x, y);
        label.setFont(font);
        label.setColor(color);
        add(label);
    }

    /* Implementation of the ComponentListener interface */
    public void componentHidden(ComponentEvent e) {
    }

    public void componentMoved(ComponentEvent e) {
    }

    public void componentResized(ComponentEvent e) {
        update();
    }

    public void componentShown(ComponentEvent e) {
    }
}
