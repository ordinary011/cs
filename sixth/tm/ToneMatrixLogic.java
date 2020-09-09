package com.shpp.p2p.cs.ldebryniuk.assignment6.arrays.tm;

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
        int activeBoxN = 0;

        for (int row = 0; row < toneMatrix.length; row++) {
            if (toneMatrix[row][column]) { // true if current box is checked
                if (activeBoxN > 0) {
                    // add each sound wave value of the sound
                    for (int el = 0; el < samples[row].length; el++) {
                        result[el] += samples[row][el];
                    }
                } else { // logic for the first box
                    result = samples[row];
                }

                activeBoxN++;
            }
        }

        if (activeBoxN > 1) {
            final double MAX = getPositiveOrNegativeMax(result);

            for (int i = 0; i < result.length; i++) {
                result[i] /= MAX;
            }
        }

        return result;
    }

    /**
     * The following method searches for the biggest negative or the biggest positive number
     * It makes all numbers positive only for comparison.
     *
     * @param result array that contains sound wave values
     * @return the bigger positive or negative number
     */
    private static double getPositiveOrNegativeMax(double[] result) {
        double positiveMax = 0;
        int indexOfMaxNum = 0;

        for (int i = 0; i < result.length; i++) {
            double positiveVal = Math.abs(result[i]);

            if (positiveVal > positiveMax) {
                positiveMax = positiveVal;
                indexOfMaxNum = i;
            }
        }

        return result[indexOfMaxNum];
    }
}
