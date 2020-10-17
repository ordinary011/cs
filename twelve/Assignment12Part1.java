package com.shpp.p2p.cs.ldebryniuk.assignment12;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;

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
    // background upper and lower bounds
    public static int BG_MAX_VAL;
    public static int BG_MIN_VAL;
    // minimum amount of pixels together that are considered as an object
    private static int pixCountFilter;

    public static void main(String[] args) {
        if (args.length == 0) getObjCount("test.jpg");
        else System.out.println(getObjCount(args[0]));
    }

    /**
     * The following method comprises all the necessary logic for implementation of dfs.
     */
    private static String getObjCount(String imgName) {
        try {
            BufferedImage img = ImageIO.read(new File(imgName));

            createArrOfPixs(img);

            boolean onlyBGPresent = new BGHandler().findBGColor(pixels, pixCountFilter);
            if (onlyBGPresent) return "0"; // no objects at all

            int res = countObjects();
            visitedPixs.clear();

            return String.valueOf(res);
        } catch (IOException e) {
            System.err.println("Could not find an image with specified name: " + imgName);
        } catch (Exception e) {
            System.err.println("Some mistake occured read down below the reason");
            e.printStackTrace();
        }

        return null;
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

        int pixTotal = pixels.length * pixels[0].length;
        pixCountFilter = (int) (pixTotal * 0.0019);
    }

    /**
     * The following method counts objects
     */
    private static int countObjects() {
        int objCount = 0;

        for (int i = 0; i < pixels.length; i++) {
            for (int j = 0; j < pixels[0].length; j++) {
                if (!isBG(pixels[i][j]) && !visitedPixs.contains(W * i + j)) { // if not BG and was not visited
                    HashSet<Integer> obj = new HashSet<>();
                    findRelations(i, j, obj);

                    if (obj.size() > pixCountFilter) {
                        visitedPixs.addAll(obj);
                        objCount++;
                    }
                }
            }
        }

        return objCount;
    }

    /**
     * The following method determines if pix val is background
     */
    private static boolean isBG(int pixVal) {
        return pixVal > BG_MIN_VAL && pixVal < BG_MAX_VAL;
    }

    /**
     * The following method determines if pixel is related to other pixels and if pixel is single
     */
    private static void findRelations(int i, int j, HashSet<Integer> obj) {
        obj.add(W * i + j);

        if (j < W - 1 && !isBG(pixels[i][j + 1]) && !obj.contains(W * i + (j + 1))) { // right pixel
            findRelations(i, j + 1, obj);
        }

        if (i < H - 1 && j < W - 1 && !isBG(pixels[i + 1][j + 1]) && !obj.contains(W * (i + 1) + (j + 1))) {//below right
            findRelations(i + 1, j + 1, obj);
        }

        if (i < H - 1 && !isBG(pixels[i + 1][j]) && !obj.contains(W * (i + 1) + j)) { // pix below
            findRelations(i + 1, j, obj);
        }

        if (i < H - 1 && j > 0 && !isBG(pixels[i + 1][j - 1]) && !obj.contains(W * (i + 1) + (j - 1))) { // below left
            findRelations(i + 1, j - 1, obj);
        }

        if (j > 0 && !isBG(pixels[i][j - 1]) && !obj.contains(W * i + (j - 1))) { // left pixel
            findRelations(i, j - 1, obj);
        }

        if (i > 0 && j > 0 && !isBG(pixels[i - 1][j - 1]) && !obj.contains(W * (i - 1) + (j - 1))) { // above left
            findRelations(i - 1, j - 1, obj);
        }

        if (i > 0 && !isBG(pixels[i - 1][j]) && !obj.contains(W * (i - 1) + j)) { // pix above
            findRelations(i - 1, j, obj);
        }

        if (i > 0 && j < W - 1 && !isBG(pixels[i - 1][j + 1]) && !obj.contains(W * (i - 1) + (j + 1))) { // above right
            findRelations(i - 1, j + 1, obj);
        }
    }
}
