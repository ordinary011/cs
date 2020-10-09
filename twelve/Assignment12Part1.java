package com.shpp.p2p.cs.ldebryniuk.assignment12;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Assignment12Part1 {

    private static final HashSet<Integer> visitedPixs = new HashSet<>();
    private static int[][] pixels;
    private static int w;
    private static int h;
    private static int BG_COLOR;

    public static void main(String[] args) {
//        if (args.length == 0) go("test.jpg");
//        else go(args[0]);

//        go("testdrive.jpg");
        go("skater.jpg");
//        go("skater.png");
//        go("testColors_new.png");
//        go("test41.png");
    }

    private static void go(String imgName) {
        try {
            BufferedImage img = ImageIO.read(new File(imgName));

            createArrOfPixs(img);

            findBGColor();

//            countObjects();


//            isRelated(124, 122);

//            System.out.println(pixels[125][121]);
//            System.out.println(w * h + w);

//            // print pixels
//            for (int i = 0; i < pixels.length; i++) {
//                for (int j = 0; j < pixels[0].length; j++) {
////                    System.out.println(w * i + j);
//                    System.out.println(pixels[40][j] + " ");
//                }
//                System.out.println();
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void findBGColor() {
        // <pixVal, pixCountOfCurrentPixValue>
        HashMap<Integer, Integer> pixPopularity = new HashMap<>();

        for (int[] pixRow : pixels) {
            for (int pixVal : pixRow) {
                if (!pixPopularity.containsKey(pixVal)) pixPopularity.put(pixVal, 0);
                else pixPopularity.compute(pixVal, (k, v) -> v + 1); // increment popularity
            }
        }

        int mostPopularVal = 0;
        int biggestPixCount = 0;
        for (Map.Entry<Integer, Integer> entry : pixPopularity.entrySet()) {
            int pixCountOfPixVal = entry.getValue();
            if (pixCountOfPixVal > biggestPixCount) {
                mostPopularVal = entry.getKey();
                biggestPixCount = pixCountOfPixVal;
            }
        }

        BG_COLOR = mostPopularVal;
    }

    private static void createArrOfPixs(BufferedImage img) {
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
                if (pixels[i][j] != BG_COLOR && !visitedPixs.contains(w * i + j)) { // if pix was not visited
                    if (isRelated(i, j)) // if not single pix
                        objCount++;
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
        boolean isNotSinglePix = false;
//        System.out.println(w * i + j);
        visitedPixs.add(w * i + j);

        if (j < w - 1 && pixels[i][j + 1] != BG_COLOR) { // if right pixel is black
            check(i, j + 1);
            isNotSinglePix = true;
        }

        if (i < h - 1 && j < w - 1 && pixels[i + 1][j + 1] != BG_COLOR) { // if below right pix is black
            check(i + 1, j + 1);
            isNotSinglePix = true;
        }

        if (i < h - 1 && pixels[i + 1][j] != BG_COLOR) { // if pix below is black
            check(i + 1, j);
            isNotSinglePix = true;
        }

        if (i < h - 1 && j > 0 && pixels[i + 1][j - 1] != BG_COLOR) { // if below left pix is black
            check(i + 1, j - 1);
            isNotSinglePix = true;
        }

        if (j > 0 && pixels[i][j - 1] != BG_COLOR) { // if left pixel is black
            check(i, j - 1);
            isNotSinglePix = true;
        }

        if (i > 0 && j > 0 && pixels[i - 1][j - 1] != BG_COLOR) { // if above left pix is black
            check(i - 1, j - 1);
            isNotSinglePix = true;
        }

        if (i > 0 && pixels[i - 1][j] != BG_COLOR) { // if pix above is black
            check(i - 1, j);
            isNotSinglePix = true;
        }

        if (i > 0 && j < w - 1 && pixels[i - 1][j + 1] != BG_COLOR) { // if above right pix is black
            check(i - 1, j + 1);
            isNotSinglePix = true;
        }

        return isNotSinglePix;
    }


}

// testColors_new.png:
// i: 124, j: 122 subNum: 26906
// pix value = -65540