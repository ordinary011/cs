package com.shpp.p2p.cs.ldebryniuk.assignment12;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;

/**
 * The following class is a very simplified implementation of image analise.
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

    public static void main(String[] args) {
        if (args.length == 0) findObjCount("test.jpg");
        else System.out.println(findObjCount(args[0]));
    }

    /**
     * The following method comprises all the necessary logic for implementation of dfs.
     */
    private static String findObjCount(String imgName) {
        try {
            BufferedImage img = ImageIO.read(new File(imgName));

            int minObjSize = createArrOfPixs(img);

            boolean onlyBGPresent = new BGHandler().findBGColor(pixels, minObjSize);
            if (onlyBGPresent) return "0"; // if no objects at all

            int res = countObjects(minObjSize);
            visitedPixs.clear();

            return String.valueOf(res);
        } catch (IOException e) {
            System.err.println("Could not find an image with specified name: " + imgName);
        } catch (Exception e) {
            System.err.println("Some mistake occurred. Read the reason down below");
            e.printStackTrace();
        }

        return null;
    }

    /**
     * The following method extracts rgb values of image
     */
    private static int createArrOfPixs(BufferedImage img) {
        W = img.getWidth();
        H = img.getHeight();

        // create arr of pixels
        pixels = new int[H][W];
        for (int i = 0; i < H; i++)
            for (int j = 0; j < W; j++)
                pixels[i][j] = img.getRGB(j, i);

        int pixTotal = pixels.length * pixels[0].length;
        return (int) (pixTotal * 0.0019);
    }

    /**
     * The following method counts objects
     */
    private static int countObjects(int minObjSize) {
        int objCount = 0;

        for (int y = 0; y < pixels.length; y++) {
            for (int x = 0; x < pixels[0].length; x++) {
                if (!isBG(pixels[y][x]) && !visitedPixs.contains(W * y + x)) { // if not BG and was not visited
                    HashSet<Integer> obj = new HashSet<>();
                    findRelations(y, x, obj);

                    if (obj.size() > minObjSize) {
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
    private static void findRelations(int y, int x, HashSet<Integer> obj) {
        obj.add(W * y + x);

        if (x < W - 1 && !isBG(pixels[y][x + 1]) && !obj.contains(W * y + (x + 1))) { // right pixel
            findRelations(y, x + 1, obj);
        }

        if (y < H - 1 && x < W - 1 && !isBG(pixels[y + 1][x + 1]) && !obj.contains(W * (y + 1) + (x + 1))) {//below right
            findRelations(y + 1, x + 1, obj);
        }

        if (y < H - 1 && !isBG(pixels[y + 1][x]) && !obj.contains(W * (y + 1) + x)) { // pix below
            findRelations(y + 1, x, obj);
        }

        if (y < H - 1 && x > 0 && !isBG(pixels[y + 1][x - 1]) && !obj.contains(W * (y + 1) + (x - 1))) { // below left
            findRelations(y + 1, x - 1, obj);
        }

        if (x > 0 && !isBG(pixels[y][x - 1]) && !obj.contains(W * y + (x - 1))) { // left pixel
            findRelations(y, x - 1, obj);
        }

        if (y > 0 && x > 0 && !isBG(pixels[y - 1][x - 1]) && !obj.contains(W * (y - 1) + (x - 1))) { // above left
            findRelations(y - 1, x - 1, obj);
        }

        if (y > 0 && !isBG(pixels[y - 1][x]) && !obj.contains(W * (y - 1) + x)) { // pix above
            findRelations(y - 1, x, obj);
        }

        if (y > 0 && x < W - 1 && !isBG(pixels[y - 1][x + 1]) && !obj.contains(W * (y - 1) + (x + 1))) { // above right
            findRelations(y - 1, x + 1, obj);
        }
    }
}
