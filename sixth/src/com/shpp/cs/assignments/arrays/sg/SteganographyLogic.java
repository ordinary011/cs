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
        final int[][] arr = source.getPixelArray();
        final boolean[][] res = new boolean[arr.length][arr[0].length];

        // black pixels are odd ints in red component and should be marked as true in res arr
        for (int i = 0; i < arr.length; i++) { // iterate through inner arrays
            for (int j = 0; j < arr[i].length; j++) { // iterate through elements of inner arr
                res[i][j] = (GImage.getRed(arr[i][j]) % 2) != 0; // true if num is odd
            }
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
        int[][] image = source.getPixelArray();

        // iterate through every row of pixels
        for (int i = 0; i < image.length; i++) {
            // black pixels are odd ints in red component and are defined as true in message
            for (int j = 0; j < image[i].length; j++) { // iterate through pixels within row
                int r = GImage.getRed(image[i][j]);
                boolean isRedOdd = r % 2 != 0;
                boolean isPixelBlack = message[i][j];

                if (isPixelBlack) { // if pixel is black means red component should be odd
                    if (isRedOdd) continue; // if odd no change is needed
                    else r++; // make odd
                } else { // pixel not black means red component should be even
                    if (!isRedOdd) continue; // if even no change is needed
                    else r--; // make even
                }

                int g = GImage.getGreen(image[i][j]);
                int b = GImage.getBlue(image[i][j]);

                // reassign array pixel to a new pixel with changed value of red
                image[i][j] = GImage.createRGBPixel(r, g, b);
            }
        }

        return new GImage(image);
    }
}

