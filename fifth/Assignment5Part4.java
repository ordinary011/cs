package com.shpp.p2p.cs.ldebryniuk.assignment5;

import com.shpp.cs.a.console.TextProgram;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * The following class is a csv parser
 *
 * used sources: "https://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html"
 */
public class Assignment5Part4 extends TextProgram {

    /* This is the starting method of the program */
    public void run() {
        extractColumn("food-origins.csv", 1);
    }

    /**
     * The following method opens a csv file and extracts values from a certain column
     *
     * @param filename name of the file to open
     * @param columnIndex index of the column in the csv file
     * @return list with values from extracted column
     */
    private ArrayList<String> extractColumn(String filename, int columnIndex) {
        String fileLocation = "src/com/shpp/p2p/cs/ldebryniuk/assignment5/" + filename;
        ArrayList<String> res = new ArrayList<>();
        String line;

        // open file for reading
        try (BufferedReader br = new BufferedReader(new FileReader(fileLocation))) {
            while ((line = br.readLine()) != null) { // while not end of file
                res.add(
                        fieldsIn(line).get(columnIndex)
                );
            }
        } catch (Exception e) {
            println("file was not found or could not be opened");
            e.printStackTrace();
            return null;
        }

        printResults(res);

        return res;
    }

    /**
     * The following method seperates line by fields
     *
     * @param line string that is a row from the csv file
     * @return list of fields
     */
    private ArrayList<String> fieldsIn(String line) {
        ArrayList<String> res = new ArrayList<>();
        StringBuilder field = new StringBuilder();
        boolean quotesOpened = false;

        for (char ch : line.toCharArray()) {
            if (quotesOpened) {
                if (ch == 34) { // Double quotes
                    quotesOpened = false;
                    continue;
                }
                field.append(ch);
            } else {
                if (ch == 34) { // Double quotes
                    quotesOpened = true;
                } else if (ch == 44 || ch == 59) { // comma or semicolon
                    res.add(field.toString());
                    field.setLength(0);
                } else {
                    field.append(ch);
                }
            }
        }

        res.add(field.toString()); // add last field to the result

        return res;
    }

    /**
     * The following method adds quotes for each value of the array list and prints the result
     *
     * @param list is a list of values that were extracted from a csv file
     */
    private void printResults(ArrayList<String> list) {
        System.out.println(
                '[' +
                        list.stream().map(s -> "\"" + s + "\"").collect(Collectors.joining(", ")) +
                        ']'
        );
    }
}
