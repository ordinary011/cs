package com.shpp.p2p.cs.ldebryniuk.assignment6.arrays.hg;

public class HistogramEqualizationLogic {
    private static final int MAX_LUMINANCE = 255;

    /**
     * Given the luminances of the pixels in an image, returns a histogram of the frequencies of
     * those luminances.
     * <p/>
     * You can assume that pixel luminances range from 0 to MAX_LUMINANCE, inclusive.
     *
     * @param luminances The luminances in the picture.
     * @return A histogram of those luminances.
     */
    public static int[] histogramFor(int[][] luminances) {
        int[] res = new int[256]; // histogram array

        // each iteration is a new horizontal row of pixels (values specify saturation of a pixel)
        for (int[] rowOfPixels : luminances) {
            for (int pixSaturation : rowOfPixels) {
                res[pixSaturation]++; // increment amount of pixels with current saturation
            }
        }

        return res;
    }

    /**
     * Given a histogram of the luminances in an image, returns an array of the cumulative
     * frequencies of that image.  Each entry of this array should be equal to the sum of all
     * the array entries up to and including its index in the input histogram array.
     * <p/>
     * For example, given the array [1, 2, 3, 4, 5], the result should be [1, 3, 6, 10, 15].
     *
     * @param histogram The input histogram.
     * @return The cumulative frequency array.
     */
    public static int[] cumulativeSumFor(int[] histogram) {
        for (int i = 1; i < histogram.length; i++) { // start from the second element
            histogram[i] += histogram[i - 1]; // add current histogram value to the sum of all previous values
        }

        return histogram;
    }

    /**
     * Returns the total number of pixels in the given image.
     *
     * @param luminances A matrix of the luminances within an image.
     * @return The total number of pixels in that image.
     */
    public static int totalPixelsIn(int[][] luminances) {
        return luminances.length * luminances[0].length;
    }

    /**
     * Applies the histogram equalization algorithm to the given image, represented by a matrix
     * of its luminances.
     * <p/>
     * You are strongly encouraged to use the three methods you have implemented above in order to
     * implement this method.
     *
     * @param luminances The luminances of the input image.
     * @return The luminances of the image formed by applying histogram equalization.
     */
    public static int[][] equalize(int[][] luminances) {
        final int[] cumulativeHistogram = cumulativeSumFor(histogramFor(luminances));
        final int totalPixels = totalPixelsIn(luminances);

        for (int i = 0; i < luminances.length; i++) {
            for (int j = 0; j < luminances[i].length; j++) {
                int L = luminances[i][j];
                luminances[i][j] = MAX_LUMINANCE * cumulativeHistogram[L] / totalPixels;
            }
        }

        return luminances;
    }
}
