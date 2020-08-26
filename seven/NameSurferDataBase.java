package com.shpp.p2p.cs.ldebryniuk.assignment7;

/*
 * File: NameSurferDataBase.java
 * -----------------------------
 * This class keeps track of the complete database of names.
 * The constructor reads in the database from a file, and
 * the only public method makes it possible to look up a
 * name and get back the corresponding NameSurferEntry.
 * Names are matched independent of case, so that "Eric"
 * and "ERIC" are the same names.
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;

public class NameSurferDataBase implements NameSurferConstants {

    // contains <input name, name popularity in specific decade>
    private final HashMap<String, String> db = new HashMap<>();

    /* Constructor: NameSurferDataBase(filename) */

    /**
     * Creates a new NameSurferDataBase and initializes it using the
     * data in the specified file.  The constructor throws an error
     * exception if the requested file does not exist or if an error
     * occurs as the file is being read.
     */
    public NameSurferDataBase(String filename) {
        // copy all entries from file to db
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;

            // each iteration is a new word in the dictionary
            while ((line = br.readLine()) != null) { // while not end of file
                int indexOfFirstSpaceSymbol = line.indexOf(' ');
                String name = line.substring(0, indexOfFirstSpaceSymbol);
                String namePopularityWithinDecades = line.substring(indexOfFirstSpaceSymbol);

                db.put(name, namePopularityWithinDecades);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* Method: findEntry(name) */

    /**
     * Returns the NameSurferEntry associated with this name, if one
     * exists.  If the name does not appear in the database, this
     * method returns null.
     */
    public NameSurferEntry findEntry(String name) {
        name = prepareForSearch(name);

        String namePopularityWithinDecades = db.get(name);
        if (namePopularityWithinDecades == null) return null;

        return new NameSurferEntry(name + namePopularityWithinDecades);
    }

    /**
     * The following method makes sure that the first letter is an upper case letter
     * and the rest are in lower case
     *
     * @param name input name that can not correspond to the search pattern
     * @return formatted name that is ready for search
     */
    private String prepareForSearch(String name) {
        name = name.trim(); // remove possible spaces
        return Character.toUpperCase(name.charAt(0)) +
                name.substring(1).toLowerCase();
    }
}

