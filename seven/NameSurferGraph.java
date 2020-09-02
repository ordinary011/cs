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

    // list of all graphs in app
    private final ArrayList<NameSurferEntry> graphs = new ArrayList<>();
    // colors are used to distinguish graphs
    private final Color[] colors = {Color.BLUE, Color.RED, Color.MAGENTA, Color.BLACK};

    // constants for adjusting application scalability
    private double WIDTH;
    private double HEIGHT;
    private double DISTANCE_BETWEEN_VERTICAL_LINES;
    private double GRAPH_UPPER_EDGE;
    private double GRAPH_LOWER_EDGE;

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
        String name = entry.getName();

        for (NameSurferEntry graph : graphs) {
            if (graph.getName().equals(name)) return; // if graph with current name is already depicted
        }

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
        WIDTH = getWidth();
        HEIGHT = getHeight();

        DISTANCE_BETWEEN_VERTICAL_LINES = WIDTH / NDECADES;
        GRAPH_UPPER_EDGE = HEIGHT * 0.05;
        GRAPH_LOWER_EDGE = HEIGHT - GRAPH_UPPER_EDGE;

        final double INITIAL_X = 2.0; // pixels

        drawBackgroundMesh(INITIAL_X);

        drawGraphs(INITIAL_X);
    }

    /**
     * The following method depicts a background Mesh
     *
     * @param x initial x offset
     */
    private void drawBackgroundMesh(double x) {
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

    /**
     * The following method depicts a graphs
     *
     * @param INITIAL_X initial x offset
     */
    private void drawGraphs(double INITIAL_X) {
        final int FONT_SIZE = (int) (DISTANCE_BETWEEN_VERTICAL_LINES * 0.15);
        final Font FONT = new Font("Serif", Font.PLAIN, FONT_SIZE);
        final double DISTANCE_BETWEEN_GRAPH_EDGES = GRAPH_LOWER_EDGE - GRAPH_UPPER_EDGE;
        final double X_OF_LAST_DECADE = INITIAL_X + (DISTANCE_BETWEEN_VERTICAL_LINES * (NDECADES - 1));
        int colorIndex = 0;

        // each iteration draws a new graph
        for (NameSurferEntry entry : graphs) {
            String name = entry.getName() + " ";

            drawGraph(INITIAL_X, entry, name, colorIndex, DISTANCE_BETWEEN_GRAPH_EDGES, FONT);

            // add rank label for the last decade of current graph
            addRankLabel(X_OF_LAST_DECADE, entry.getRank(NDECADES - 1), name,
                    DISTANCE_BETWEEN_GRAPH_EDGES, FONT, colors[colorIndex]);

            // prepare for the next iteration (graph)
            colorIndex++;
            if (colorIndex == 4) colorIndex = 0;
        }
    }

    /**
     * The following method depicts a graph
     *
     * @param x x offset in pixels
     * @param entry contains info about the name
     * @param name input name
     * @param colorIndex index of color in colors array
     * @param DISTANCE_BETWEEN_GRAPH_EDGES distance between upper and lower graph edges
     * @param FONT font for rank labels
     */
    private void drawGraph(double x, NameSurferEntry entry, String name, int colorIndex,
                           double DISTANCE_BETWEEN_GRAPH_EDGES, Font FONT) {
        for (int j = 0; j < NDECADES - 1; j++) {
            double y = addRankLabel(x, entry.getRank(j), name, DISTANCE_BETWEEN_GRAPH_EDGES, FONT, colors[colorIndex]);

            // draw rank line
            double nextRankOffset = getRankOffset(entry.getRank(j + 1), DISTANCE_BETWEEN_GRAPH_EDGES);
            GLine line = new GLine(x, y,
                    x + DISTANCE_BETWEEN_VERTICAL_LINES, GRAPH_UPPER_EDGE + nextRankOffset);
            line.setColor(colors[colorIndex]);
            add(line);

            x += DISTANCE_BETWEEN_VERTICAL_LINES; // increase for the next iteration (rank)
        }
    }

    /**
     * The following method adds a graph label
     *
     * @param x x offset in pixels
     * @param rank name rank in current decade
     * @param name input name
     * @param DISTANCE_BETWEEN_GRAPH_EDGES distance between upper and lower graph edges
     * @param font font for rank label
     * @param color of the label
     * @return y offset of the label
     */
    private double addRankLabel(double x, int rank, String name,
                                double DISTANCE_BETWEEN_GRAPH_EDGES, Font font, Color color) {
        double y = GRAPH_UPPER_EDGE + getRankOffset(rank, DISTANCE_BETWEEN_GRAPH_EDGES);
        String rankName = name + (rank == 0 ? "*" : rank);
        addLabel(rankName, x, y, font, color);

        return y;
    }

    /**
     * The following method determines offset for the rank
     *
     * @param rank rank of input name in current decade
     * @param DISTANCE_BETWEEN_GRAPH_EDGES distance between upper and lower graph edges
     * @return offset as a percent in relation to graph size
     */
    private double getRankOffset(int rank, double DISTANCE_BETWEEN_GRAPH_EDGES) {
        if (rank == 0) return DISTANCE_BETWEEN_GRAPH_EDGES;

        return rank * DISTANCE_BETWEEN_GRAPH_EDGES / MAX_RANK; // percent in relation to graph size
    }

    /**
     * The following method adds a label
     *
     * @param name label name
     * @param x offset
     * @param y offset
     * @param font label font
     * @param color label color
     */
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
