package com.shpp.p2p.cs.ldebryniuk.assignment12;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.HashSet;

public class Assignment12Part1 {

    private static final HashSet<Integer> visitedPixs = new HashSet<>();
    private static int[][] pixels;
    private static int w;
    private static int h;
    private static int BACKGROUND_COLOR = -1;
//    private static int BACKGROUND_COLOR = -16184566;

    public static void main(String[] args) {
//        if (args.length == 0) go("test.jpg");
//        else go(args[0]);

        go("testdrive.jp");
    }

    private static void go(String imgName) {
        try {
            BufferedImage img = ImageIO.read(new File(imgName));

            getPixValues(img);

            findBGColor();

            countObjects();


//            isRelated(124, 122);

//            System.out.println(pixels[125][121]);
//            System.out.println(w * h + w);

//            // print pixels
//            for (int i = 0; i < pixels.length; i++) {
//                for (int j = 0; j < pixels[0].length; j++) {
////                    System.out.println(w * i + j);
//                    System.out.print(pixels[124][j] + " ");
//                }
//                System.out.println();
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void findBGColor() {
        HashMap<Integer, Integer> pixPopularity = new HashMap<>();

        for (int i = 0; i < pixels.length; i++) {
            for (int j = 0; j < pixels[0].length; j++) {
                int pixVal = pixels[i][j];
                if (!pixPopularity.containsKey(pixVal)) {
                    pixPopularity.put(pixVal, 0);
                } else {
//                    pixPopularity.put(pixVal, pixPopularity.get(pixVal) + 1); // increment popularity
                    pixPopularity.compute(pixVal, (k, v) -> v + 1); // increment popularity
                }
            }
        }

//        int res = 0;
//        int pop = 0;
//        for (Integer pixVal : pixPopularity.) {
//            if ()
//        }
    }

    private static void getPixValues(BufferedImage img) {
        w = img.getWidth();
        h = img.getHeight();

        // create arr of pixels
        pixels = new int[h][w];
        for (int i = 0; i < h; i++)
            for (int j = 0; j < w; j++)
                pixels[i][j] = img.getRGB(j, i);
    }

    private static void countObjects() {
        int objCount = 0;

        for (int i = 0; i < pixels.length; i++) {
            for (int j = 0; j < pixels[0].length; j++) {
                if (pixels[i][j] != BACKGROUND_COLOR) {
                    if (!visitedPixs.contains(w * i + j)) { // if pix was not visited
                        if (isRelated(i, j)) objCount++;
                    }
                }
            }
        }

        System.out.println(objCount);
    }

    private static void check(int i, int j) {
        if (!visitedPixs.contains(w * i + j)) { // if pix was not visited
            isRelated(i, j);
        }
    }

    private static boolean isRelated(int i, int j) {
        boolean isRelated = false;
//        System.out.println(w * i + j);
        visitedPixs.add(w * i + j);

        if (j < w - 1 && pixels[i][j + 1] != BACKGROUND_COLOR) { // if right pixel is black
            check(i, j + 1);
            isRelated = true;
        }

        if (i < h - 1 && j < w - 1 && pixels[i + 1][j + 1] != BACKGROUND_COLOR) { // if below right pix is black
            check(i + 1, j + 1);
            isRelated = true;
        }

        if (i < h - 1 && pixels[i + 1][j] != BACKGROUND_COLOR) { // if pix below is black
            check(i + 1, j);
            isRelated = true;
        }

        if (i < h - 1 && j > 0 && pixels[i + 1][j - 1] != BACKGROUND_COLOR) { // if below left pix is black
            check(i + 1, j - 1);
            isRelated = true;
        }

        if (j > 0 && pixels[i][j - 1] != BACKGROUND_COLOR) { // if left pixel is black
            check(i, j - 1);
            isRelated = true;
        }

        if (i > 0 && j > 0 && pixels[i - 1][j - 1] != BACKGROUND_COLOR) { // if above left pix is black
            check(i - 1, j - 1);
            isRelated = true;
        }

        if (i > 0 && pixels[i - 1][j] != BACKGROUND_COLOR) { // if pix above is black
            check(i - 1, j);
            isRelated = true;
        }

        if (i > 0 && j < w - 1 && pixels[i - 1][j + 1] != BACKGROUND_COLOR) { // if above right pix is black
            check(i - 1, j + 1);
            isRelated = true;
        }

        return isRelated;
    }


}

// testColors_new.png:
// i: 124, j: 122
// 26906 pix value = -65540