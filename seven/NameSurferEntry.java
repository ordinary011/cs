package com.shpp.p2p.cs.ldebryniuk.assignment7;

/*
 * File: NameSurferEntry.java
 * --------------------------
 * This class represents a single entry in the database.  Each
 * NameSurferEntry contains a name and a list giving the popularity
 * of that name for each decade stretching back to 1900.
 */

public class NameSurferEntry implements NameSurferConstants {

	/* Constructor: NameSurferEntry(line) */
    private final String name;
    private final int[] decades = new int[12];

    /**
     * Creates a new NameSurferEntry from a data line as it appears
     * in the data file.  Each line begins with the name, which is
     * followed by integers giving the rank of that name for each
     * decade.
     */
    public NameSurferEntry(String line) {
        String[] strArr = line.split(" ");

        this.name = strArr[0];

        try {
            for (int i = 1; i < strArr.length; i++) {
                decades[i-1] = Integer.parseInt(strArr[i]);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

	/* Method: getName() */

    /**
     * Returns the name associated with this entry.
     */
    public String getName() { return this.name; }

	/* Method: getRank(decade) */

    /**
     * Returns the rank associated with an entry for a particular
     * decade.  The decade value is an integer indicating how many
     * decades have passed since the first year in the database,
     * which is given by the constant START_DECADE.  If a name does
     * not appear in a decade, the rank value is 0.
     */
    public int getRank(int decade) {
        try {
            return decades[decade];
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        return 0;
    }

	/* Method: toString() */

    /**
     * Returns a string that makes it easy to see the value of a
     * NameSurferEntry.
     */
    public String toString() {
        StringBuilder res = new StringBuilder();

        res.append(name).append(" [");
        for (int decade : decades) {
            res.append(decade).append(" ");
        }
        res.insert(res.length() - 1, "]");

        return res.toString();
    }
}

