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

    private HashMap<String, String> db = new HashMap<>();

	/* Constructor: NameSurferDataBase(filename) */
    /**
     * Creates a new NameSurferDataBase and initializes it using the
     * data in the specified file.  The constructor throws an error
     * exception if the requested file does not exist or if an error
     * occurs as the file is being read.
     */
    public NameSurferDataBase(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;

            // each iteration is a new word in the dictionary
            while ((line = br.readLine()) != null) { // while not end of file
                int inexOfFirstSpace = line.indexOf(' ');
                String name = line.substring(0, inexOfFirstSpace);
                String popularityNumbers = line.substring(inexOfFirstSpace);

                db.put(name, popularityNumbers);
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
        name = prepareName(name);

        String popularities = db.get(name);
        if (popularities == null) return null;

        return new NameSurferEntry(name + popularities);
    }

    private String prepareName(String name) {
        return Character.toUpperCase(name.charAt(0)) +
                name.substring(1).toLowerCase();
    }
}

