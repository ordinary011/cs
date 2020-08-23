package com.shpp.cs.assignments.arrays.tm;

import java.util.Arrays;

public class ToneMatrixLogic {
    /**
     * Given the contents of the tone matrix, returns a string of notes that should be played
     * to represent that matrix.
     *
     * @param toneMatrix The contents of the tone matrix.
     * @param column     The column number that is currently being played.
     * @param samples    The sound samples associated with each row.
     * @return A sound sample corresponding to all notes currently being played.
     */
    public static double[] matrixToMusic(boolean[][] toneMatrix, int column, double[][] samples) {
        double[] result = new double[ToneMatrixConstants.sampleSize()];
        boolean atLeastOneBoxWasChecked = false;
        boolean twoOrMoreBoxesWereChecked = false;

        for (int i = 0; i < toneMatrix.length; i++) {
           if (toneMatrix[i][column]) { // if current box is checked

               if (atLeastOneBoxWasChecked) {
                   twoOrMoreBoxesWereChecked = true;
                   // add each sound wave value of the sound
                   for (int j = 0; j < samples[i].length; j++) {
                       result[j] += samples[i][j];
                   }
               } else { // this is the first box in this column
                   result = samples[i];
                   atLeastOneBoxWasChecked = true;
               }

           }
        }

        if (twoOrMoreBoxesWereChecked) {
            // find max number
            double max = Arrays.stream(result).max().getAsDouble();

            for (int i = 0; i < result.length; i++) {
                result[i] /= max;
            }
        }

        return result;
    }
}
