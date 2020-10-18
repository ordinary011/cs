package com.shpp.p2p.cs.ldebryniuk;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;

public class HS {

    private static final ArrayList<HashSet<Integer>> currentRow = new ArrayList<>();
    private static ArrayList<HashSet<Integer>> prevRow = new ArrayList<>();
    private static int objCount = 0;
    private static int[][] pixels;
    private static int BACKGROUND_COLOR = -1;

    public static void main(String[] args) {
        try {
            BufferedImage img = ImageIO.read(new File("test11.png"));

            int w = img.getWidth();
            int h = img.getHeight();

            // create arr of pixels
            pixels = new int[h][w];
            for (int i = 0; i < h; i++)
                for (int j = 0; j < w; j++)
                    pixels[i][j] = img.getRGB(j, i);


            countObjects();

//            // print pixels
//            for (int i = 0; i < pixels.length; i++) {
//                for (int j = 0; j < pixels[0].length; j++)
//                    System.out.print(pixels[i][j] + " ");
//
//                System.out.println();
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void countObjects() {
        // first pixel of first row
        if (pixels[0][0] != BACKGROUND_COLOR) { // if pixel is black
            HashSet<Integer> obj = new HashSet<>();
            obj.add(0); // address of pix
            prevRow.add(obj);
            objCount++;
        }
        // rest pixels of first row
        for (int j = 1; j < pixels[0].length; j++) {
            if (pixels[0][j] != BACKGROUND_COLOR) { // if pixel is black
                if (pixels[0][j - 1] == BACKGROUND_COLOR) { // prev is not black means separated object
                    HashSet<Integer> obj = new HashSet<>();
                    obj.add(j);
                    prevRow.add(obj);
                    objCount++;
                } else { // the same object
                    prevRow.get(prevRow.size() - 1).add(j); // add cell address to the last obj of current row
                }
            }
        }

        // rest of rows
        for (int i = 1; i < pixels.length; i++) { // each iteration new row
            // first pixel of current row
            if (pixels[i][0] != BACKGROUND_COLOR) { // if the first pixel is black
                HashSet<Integer> obj = new HashSet<>();
                obj.add(0);
                currentRow.add(obj);

                // if is new object
                if (prevRow.isEmpty()) {
                    objCount++;
                } else { // pix may be related to an object that exists already
                    HashSet<Integer> firstObjOfPrevRow = prevRow.get(0);
                    if (!firstObjOfPrevRow.contains(0) && !firstObjOfPrevRow.contains(1)) objCount++;
                }
            }

            // rest of pixels in current row
            for (int j = 1; j < pixels[0].length; j++) {
                if (pixels[i][j] != BACKGROUND_COLOR) { // if current pix is black
                    logic(i, j);
                    if (mergesTwoObjects(i, j)) objCount--;
                }
            }

            prevRow = currentRow;
            currentRow.clear();
        }

        System.out.println(objCount);
    }

    private static boolean mergesTwoObjects(int i, int j) {
        if (pixels[i-1][j] == BACKGROUND_COLOR) { // if pix above is not black
            HashSet<Integer> UpRightPixObj = findPixObjWithSpecificAddress(j + 1);
            if (UpRightPixObj == null) return false;

            if (pixels[i-1][j-1] != BACKGROUND_COLOR) { // if pix left above is black
                HashSet<Integer> UpLeftPixObj = findPixObjWithSpecificAddress(j - 1);
                if (UpLeftPixObj != null && UpLeftPixObj != UpRightPixObj) return true;
            }

            int pixAddress = j - 1;
            while (pixels[i][pixAddress] != BACKGROUND_COLOR) { // while pix before is black
                // todo inside i probably wrong
                HashSet<Integer> UpLeftPixObj = findPixObjWithSpecificAddress(j - 1);
                if (UpLeftPixObj != null) return false;
            }
        }

        return false;
    }

    private static void logic(int i, int j) {
        if (currentRow.isEmpty()) { // if pix is the first black pix in this row
            HashSet<Integer> obj = new HashSet<>();
            obj.add(j);
            currentRow.add(obj);
        }

        if (pixels[i][j - 1] == BACKGROUND_COLOR) { // if prev is not black
            int pixBeforeCurrent = findPixBeforeCurrent(i, j);
            HashSet<Integer> objOfPixBeforeCurrent = findPixObj(pixBeforeCurrent);
            HashSet<Integer> objOfCurrentPix = findPixObj(j);
            if (objOfCurrentPix != null && objOfCurrentPix == objOfPixBeforeCurrent) {// if pixels are from the same obj
                currentRow.get(currentRow.size() - 1).add(j); // add cell address to the last obj of current row
            } else { // new seperate object
                HashSet<Integer> obj = new HashSet<>();
                obj.add(j);
                currentRow.add(obj);
                objCount++;
            }
        } else { // if prev is black then pix is related to that obj of prev pix
            currentRow.get(currentRow.size() - 1).add(j); // add cell address to the last obj of current row
        }
    }


    private static int findPixBeforeCurrent(int i, int pixToSearchBefore) {
        for (int j = pixToSearchBefore - 1; j > -1; j--) {
            if (pixels[i][j] != BACKGROUND_COLOR) { // if pix is black
                return j;
            }
        }

        return -1; // if returns -1 there was a mistake somewhere above
    }

    private static HashSet<Integer> findPixObj(int j) {
        for (HashSet<Integer> obj : prevRow) {
            if (obj.contains(j - 1) ||
                    obj.contains(j) ||
                    obj.contains(j + 1)
            ) return obj;
        }

        return null;
    }


    private static HashSet<Integer> findPixObjWithSpecificAddress(int pixAdress) {
        for (HashSet<Integer> obj : prevRow) {
            if (obj.contains(pixAdress)) return obj;
        }

        return null;
    }

}

