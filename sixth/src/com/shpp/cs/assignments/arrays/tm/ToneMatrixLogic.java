package com.shpp.cs.assignments.arrays.tm;

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

        for (int row = 0; row < toneMatrix.length; row++) {
            if (toneMatrix[row][column]) { // if current box is checked

                if (atLeastOneBoxWasChecked) { // means the second box is also checked
                    twoOrMoreBoxesWereChecked = true;

                    // add each sound wave value of the sound
                    for (int el = 0; el < samples[row].length; el++) {
                        result[el] += samples[row][el];
                    }
                } else { // this is the first box in this column
                    result = samples[row];
                    atLeastOneBoxWasChecked = true;
                }
           }
        }

        if (twoOrMoreBoxesWereChecked) {
            double max = getPositiveOrNegativeMax(result);

            for (int i = 0; i < result.length; i++) {
                result[i] /= max;
            }
        }

        return result;
    }

    /**
     * The following method searches for the biggest negative or the biggest negative number
     *
     * @param result array that contains sound wave values
     * @return the bigger positive or negative number
     */
    private static double getPositiveOrNegativeMax(double[] result) {
        double positiveMax = 0;
        int indexOfMaxNum = 0;

        for (int i = 0; i < result.length; i++) {
            double positiveVal = result[i] < 0 ? -result[i] : result[i];

            if (positiveVal > positiveMax) {
                positiveMax = positiveVal;
                indexOfMaxNum = i;
            }
        }

        return result[indexOfMaxNum];
    }
}
