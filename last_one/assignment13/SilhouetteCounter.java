package com.shpp.p2p.cs.ldebryniuk.assignment13;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

/**
 * The following class is used solely to determine the number of silhouettes on the black and white image.
 * The underlying algorithm is breadth first search that is used when "moving" from one pixel to another.
 * it first searches for a background color that is taken from the top most left pixel
 * then it determines the minimum amount of pixels in a silhouette by multiplying total number of pixels and THRESHOLD.
 * Pixels that have been already explored are marked as true in the boolean array (visitedPixels)
 */
class SilhouetteCounter {

    private int BGColor;

    /**
     * queue that is used to store pixel coordinates that need to be explored
     */
    private Queue<Integer> BFSQueue = new LinkedList<>();

    /**
     * an array of boolean values, where explored pixels are marked as "true"
     */
    private boolean[][] exploredPixels;

    /**
     * the image that is represented as an array of RGB values
     */
    private int[][] imageRGBValues;

    /**
     * width of the image
     */
    private int W;

    /**
     * height of the image
     */
    private int H;

    /**
     * number of pixels in the silhouette that is being explored
     */
    private int pixelsNumInCurrentObj;

    /**
     * threshold that is used in order to determine the minimum object size (in percents)
     */
    private final double THRESHOLD = 0.0019;

    /**
     * The following method comprises all the necessary logic for silhouettes counting
     *
     * @param imgName path to the image that needs to be explored
     */
    void countSilhouettes(String imgName) {
        try {
            BufferedImage bufferedImage = ImageIO.read(new File(imgName));

            // take background color from top left corner
            BGColor = bufferedImage.getRGB(0, 0);

            int totalAmountOfPixels = createArraysFrom(bufferedImage);
            int minSilhouetteSize = (int) (totalAmountOfPixels * THRESHOLD);

            int silhouettesCount = findSilhouettes(minSilhouetteSize);

            System.out.println("There are " + silhouettesCount + " silhouettes on the image");
        } catch (IOException e) {
            System.err.println("Could not find an image with specified name: " + imgName);
        } catch (Exception e) {
            System.err.println("Some mistake occurred. Read the reason down below");
            System.err.println(e.toString());
        }
    }

    /**
     * The following method creates two arrays
     * the first one is used for storing rgb values of the image
     * and the second is used for storing pixels that are already explored
     *
     * @param bufferedImage buffered image that is read from the certain drive
     * @return total amount Of pixels in the image
     */
    private int createArraysFrom(BufferedImage bufferedImage) {
        W = bufferedImage.getWidth();
        H = bufferedImage.getHeight();

        imageRGBValues = new int[H][W];
        exploredPixels = new boolean[H][W];

        for (int i = 0; i < H; i++) {
            for (int j = 0; j < W; j++) {
                imageRGBValues[i][j] = bufferedImage.getRGB(j, i);
            }
        }

        return W * H;
    }

    /**
     * The following method searches for silhouettes on the image
     *
     * @param minSilhouetteSize minimum amount of pixels that the silhouette can contain
     * @return total amount of silhouettes on the image
     */
    private int findSilhouettes(int minSilhouetteSize) {
        int silhouettesCount = 0;

        for (int y = 0; y < imageRGBValues.length; y++) {
            for (int x = 0; x < imageRGBValues[0].length; x++) {
                // if pixel is not BG and was not visited
                if (!isBG(imageRGBValues[y][x]) && !exploredPixels[y][x]) {
                    rememberPixel(y, x);

                    while (!BFSQueue.isEmpty()) {
                        // BFSQueue: (i == y) and (i+1 == x)
                        findRelations(BFSQueue.poll(), BFSQueue.poll());
                    }

                    if (pixelsNumInCurrentObj > minSilhouetteSize) {
                        silhouettesCount++;
                    }

                    pixelsNumInCurrentObj = 0;
                }
            }
        }

        return silhouettesCount;
    }

    /**
     * The following method adds pixel coordinates to the queue and "marks" pixel as explored
     */
    private void rememberPixel(int y, int x) {
        BFSQueue.add(y);
        BFSQueue.add(x);
        exploredPixels[y][x] = true;
        pixelsNumInCurrentObj++;
    }

    /**
     * The following method determines if pixel is a background pixel
     *
     * @param pixValue value of the pixel that is being explored
     * @return true if pixel is a background pixel
     */
    private boolean isBG(int pixValue) {
        return pixValue == BGColor;
    }

    /**
     * The following method determines if pixel is related to other pixels
     *
     * @param y a row number in the pixel array
     * @param x a column number in the pixel array
     */
    private void findRelations(int y, int x) {
        // check right pixel
        if (x < (W - 1) && !isBG(imageRGBValues[y][x + 1]) && !exploredPixels[y][x + 1]) {
            rememberPixel(y, x + 1);
        }

        // check below right pixel
        if (y < H - 1 && x < W - 1 && !isBG(imageRGBValues[y + 1][x + 1]) && !exploredPixels[y + 1][x + 1]) {
            rememberPixel(y + 1, x + 1);
        }

        // check pixel below
        if (y < H - 1 && !isBG(imageRGBValues[y + 1][x]) && !exploredPixels[y + 1][x]) {
            rememberPixel(y + 1, x);
        }

        // check below left pixel
        if (y < H - 1 && x > 0 && !isBG(imageRGBValues[y + 1][x - 1]) && !exploredPixels[y + 1][x - 1]) {
            rememberPixel(y + 1, x - 1);
        }

        // check left pixel
        if (x > 0 && !isBG(imageRGBValues[y][x - 1]) && !exploredPixels[y][x - 1]) {
            rememberPixel(y, x - 1);
        }

        // check above left pixel
        if (y > 0 && x > 0 && !isBG(imageRGBValues[y - 1][x - 1]) && !exploredPixels[y - 1][x - 1]) {
            rememberPixel(y - 1, x - 1);
        }

        // check pixel above
        if (y > 0 && !isBG(imageRGBValues[y - 1][x]) && !exploredPixels[y - 1][x]) {
            rememberPixel(y - 1, x);
        }

        // check above right pixel
        if (y > 0 && x < W - 1 && !isBG(imageRGBValues[y - 1][x + 1]) && !exploredPixels[y - 1][x + 1]) {
            rememberPixel(y - 1, x + 1);
        }
    }
}
