package com.shpp.cs.assignments.arrays.sg;

import acm.graphics.*;

public class SteganographyLogic {
    /**
     * Given a GImage containing a hidden message, finds the hidden message
     * contained within it and returns a boolean array containing that message.
     * <p/>
     * A message has been hidden in the input image as follows.  For each pixel
     * in the image, if that pixel has a red component that is an even number,
     * the message value at that pixel is false.  If the red component is an odd
     * number, the message value at that pixel is true.
     *
     * @param source The image containing the hidden message.
     * @return The hidden message, expressed as a boolean array.
     */
    public static boolean[][] findMessage(GImage source) {
        // black pixels are odd nums in red component and should be marked as true in res arr
        int[][] arr = source.getPixelArray();
        boolean[][] res = new boolean[arr.length][];

        // each iteration in a new array that contains row of pixels
        for (int i = 0; i < arr.length; i++) {
            boolean[] rowOfPixels = new boolean[arr[i].length];

            // each iteration is a new pixel within current row
            for (int j = 0; j < rowOfPixels.length; j++) {
                rowOfPixels[j] = (GImage.getRed(arr[i][j]) % 2) != 0; // true if num is odd
            }

            res[i] = rowOfPixels;
        }

        return res;
    }

    /**
     * Hides the given message inside the specified image.
     * <p/>
     * The image will be given to you as a GImage of some size, and the message will
     * be specified as a boolean array of pixels, where each white pixel is denoted
     * false and each black pixel is denoted true.
     * <p/>
     * The message should be hidden in the image by adjusting the red channel of all
     * the pixels in the original image.  For each pixel in the original image, you
     * should make the red channel an even number if the message color is white at
     * that position, and odd otherwise.
     * <p/>
     * You can assume that the dimensions of the message and the image are the same.
     * <p/>
     *
     * @param message The message to hide.
     * @param source  The source image.
     * @return A GImage whose pixels have the message hidden within it.
     */
    public static GImage hideMessage(boolean[][] message, GImage source) {
        // black pixels are odd nums in red component and should be defined as true
        int[][] image = source.getPixelArray();

        // each iteration is a horizontal row of pixels
        for (int i = 0; i < image.length; i++) {

            // iterate through array of pixels
            for (int j = 0; j < image[i].length; j++) {
//                int r = createRedComponent(image[i][j], message[i][j]);
                int r = GImage.getRed(image[i][j]);
                boolean isRedOdd = r % 2 != 0;

                if (message[i][j]) { // if pixel is black means red component should be odd
                    if (isRedOdd) continue; // if true no change is needed
                    else r++; // make odd
                } else { // pixel not black means red component should be even
                    if (isRedOdd) r--; // if true make even
                }

                int g = GImage.getGreen(image[i][j]);
                int b = GImage.getBlue(image[i][j]);

                // reassign array element to a new pixel
                image[i][j] = GImage.createRGBPixel(r, g, b);
            }
        }

        return new GImage(image);
    }

//    private static int createRedComponent(int rgbPixel, boolean isBlackPixel) {
//        int red = GImage.getRed(rgbPixel);
//        boolean isRedOdd = red % 2 != 0;
//
//        if (isBlackPixel) { // black pixel means red component should be odd
//            return isRedOdd ? red : ++red;
//        } else { // pixel not black means red component should be even
//            return isRedOdd ? --red : red;
//        }
//    }
}

