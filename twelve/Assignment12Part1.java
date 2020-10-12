package com.shpp.p2p.cs.ldebryniuk.assignment12;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * The following class is a very simplified implementation of image analize.
 * It uses deep first-search in order to determine the amount of objects on the image
 */
public class Assignment12Part1 {

    // contains visited pixels numbers. e.g. 1, 2, 3, 44, 556678 etc...
    private static final HashSet<Integer> visitedPixs = new HashSet<>();
    // an image that is represented as an array of RGB values
    private static int[][] pixels;
    // width and height of an image
    private static int W;
    private static int H;
    // background color
    private static int BG_COLOR;

    public static void main(String[] args) {
        if (args.length == 0) go("test.jpg");
        else go(args[0]);
    }

    /**
     * The following method comprises all the necessary logic for implementation of dfs.
     */
    private static void go(String imgName) {
        try {
            BufferedImage img = ImageIO.read(new File(imgName));

            createArrOfPixs(img);

            findBGColor();

            countObjects();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * The following method searches for a background color of an img.
     */
    private static void findBGColor() {
        // <pixVal, pixCountOfCurrentPixValue>
        HashMap<Integer, Integer> pixPopularities = new HashMap<>();

        for (int[] pixRow : pixels) {
            for (int pixVal : pixRow) {
                if (!pixPopularities.containsKey(pixVal)) pixPopularities.put(pixVal, 1);
                else pixPopularities.compute(pixVal, (k, v) -> v + 1); // increment popularity
            }
        }

        int mostPopularPixVal = 0;
        int biggestPixCount = 0;
        for (Map.Entry<Integer, Integer> entry : pixPopularities.entrySet()) {
            int pixValCount = entry.getValue();

            if (pixValCount > biggestPixCount) {
                mostPopularPixVal = entry.getKey();
                biggestPixCount = pixValCount;
            }
        }

        BG_COLOR = mostPopularPixVal;
    }

    /**
     * The following method extracts rgb values of image
     */
    private static void createArrOfPixs(BufferedImage img) {
        W = img.getWidth();
        H = img.getHeight();

        // create arr of pixels
        pixels = new int[H][W];
        for (int i = 0; i < H; i++)
            for (int j = 0; j < W; j++)
                pixels[i][j] = img.getRGB(j, i);
    }

    /**
     * The following method counts objects
     */
    private static void countObjects() {
        int objCount = 0;

        for (int i = 0; i < pixels.length; i++) {
            for (int j = 0; j < pixels[0].length; j++) {
                if (pixels[i][j] != BG_COLOR && !visitedPixs.contains(W * i + j)) { // if not BG and was not visited
                    if (isRelated(i, j)) // if not single pix
                        objCount++;
                }
            }
        }

        System.out.println(objCount);
    }

    /**
     * The following method checks if the pixel has already been visited
     *
     * @return specifies that pix is not single and has relations to others pixels
     */
    private static boolean check(int i, int j) {
        if (!visitedPixs.contains(W * i + j)) { // if pix was not visited
            isRelated(i, j);
        }

        return true; // inside this method already means that pix is not single
    }

    /**
     * The following method determines if pixel is related to other pixels and if pixel is single
     */
    private static boolean isRelated(int i, int j) {
        visitedPixs.add(W * i + j);
        boolean isNotSinglePix = false;

        if (j < W - 1 && pixels[i][j + 1] != BG_COLOR) { // if right pixel is black
            isNotSinglePix = check(i, j + 1);
        }

        if (i < H - 1 && j < W - 1 && pixels[i + 1][j + 1] != BG_COLOR) { // if below right pix is black
            isNotSinglePix = check(i + 1, j + 1);
        }

        if (i < H - 1 && pixels[i + 1][j] != BG_COLOR) { // if pix below is black
            isNotSinglePix = check(i + 1, j);
        }

        if (i < H - 1 && j > 0 && pixels[i + 1][j - 1] != BG_COLOR) { // if below left pix is black
            isNotSinglePix = check(i + 1, j - 1);
        }

        if (j > 0 && pixels[i][j - 1] != BG_COLOR) { // if left pixel is black
            isNotSinglePix = check(i, j - 1);
        }

        if (i > 0 && j > 0 && pixels[i - 1][j - 1] != BG_COLOR) { // if above left pix is black
            isNotSinglePix = check(i - 1, j - 1);
        }

        if (i > 0 && pixels[i - 1][j] != BG_COLOR) { // if pix above is black
            isNotSinglePix = check(i - 1, j);
        }

        if (i > 0 && j < W - 1 && pixels[i - 1][j + 1] != BG_COLOR) { // if above right pix is black
            isNotSinglePix = check(i - 1, j + 1);
        }

        return isNotSinglePix;
    }

}
