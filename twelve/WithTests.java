package com.shpp.p2p.cs.ldebryniuk.assignment12;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * The following class is a very simplified implementation of image analize.
 * It uses deep first-search in order to determine the amount of objects on the image
 */
public class WithTests {

    // contains visited pixels numbers. e.g. 1, 2, 3, 44, 556678 etc...
    private static final HashSet<Integer> visitedPixs = new HashSet<>();
    // an image that is represented as an array of RGB values
    private static int[][] pixels;
    // width and height of an image
    private static int W;
    private static int H;
    // background upper and lower bounds
    private static int BG_MAX_VAL;
    private static int BG_MIN_VAL;
    // minimum amount of pixels together that are considered as an object
    private static int pixCountFilter;

    public static void main(String[] args) {
//        args = new String[]{"test4.png"}; //, {"10"},
//        args = new String[]{"1.bmp"}; //, {"13"},
//        args = new String[]{"cats.png"}; //, {"9"},
//        args = new String[]{"test18.png"}; //, {"0"},
//        args = new String[]{"squares3.png"}; //, {"5"},
//        args = new String[]{"child.png"}; //, {"5"},
//
//        if (args.length == 0) ggo("test.jpg");
//        else System.out.println(ggo(args[0]));

        runTests();
    }

    /**
     * The following method comprises all the necessary logic for implementation of dfs.
     */
    private static String ggo(String imgName) {
        try {
            BufferedImage img = ImageIO.read(new File(imgName));

            createArrOfPixs(img);

            boolean onlyBGPresent = findBGColor();
            if (onlyBGPresent) return "0"; // no objects at all

////             print pixels
////            for (int i = 0; i < pixels.length; i++) {
//            for (int i = 79; i < 100; i++) {
//                for (int j = 225; j < pixels[0].length; j++) {
////                    System.out.println(W * i + j);
////                    System.out.print(pixels[i][j] + " ");
////                    System.out.print(pixels[i][j] == BG_COLOR ? "0 " : "1 ");
//                    System.out.print(!isBG(pixels[i][j]) ? "1 " : "0 ");
//                }
//                System.out.println();
//            }

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
     * The following method searches for a background color of an img.
     *
     * @return true only if there are no objects
     */
    private static boolean findBGColor() {
        // <pixVal, pixCountOfCurrentPixValue>
        HashMap<Integer, Integer> pixPopularities = new HashMap<>();

        findPixPopularities(pixPopularities);
        if (pixPopularities.size() == 1) return true; // only BG nothing else

        // distribute values
        int mostPopularPixVal = 0;
        int biggestPixCount = 0;
        int minVal = 0;
        int maxVal = 0;
        for (Map.Entry<Integer, Integer> entry : pixPopularities.entrySet()) {
            int pixValCount = entry.getValue();

            if (pixValCount > biggestPixCount) { // bg value
                mostPopularPixVal = entry.getKey();
                biggestPixCount = pixValCount;
            }

            if (pixValCount > pixCountFilter) { // bg max and min values
                int pixVal = entry.getKey();
                if (pixVal > maxVal) maxVal = pixVal;
                else if (pixVal < minVal) minVal = pixVal;
            }
        }

        findBGRange(mostPopularPixVal, maxVal, minVal);
        return false; // false means not only BG is present
    }

    /**
     * The following method searches for a BG max and min values
     */
    private static void findBGRange(int BG_COLOR, int maxVal, int minVal) {
        int diff1 = BG_COLOR - maxVal;
        int diff2 = BG_COLOR - minVal;

        int BGFilter = (int) (Math.max(diff1, diff2) * 0.001);

        BG_MAX_VAL = BG_COLOR + BGFilter;
        BG_MIN_VAL = BG_COLOR - BGFilter;
    }

    /**
     * The following method maps pix values with their amount
     */
    private static void findPixPopularities(HashMap<Integer, Integer> pixPopularities) {
        for (int[] pixRow : pixels) {
            for (int pixVal : pixRow) {
                if (!pixPopularities.containsKey(pixVal)) pixPopularities.put(pixVal, 1);
                else pixPopularities.compute(pixVal, (k, v) -> v + 1); // increment popularity
            }
        }
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

    /**
     * Tests for different cases of input
     */
    private static void runTests() {
        String[][] tests = {
                {"1.bmp"}, {"13"},

                {"balet.png"}, {"1"},
                {"balet1.png"}, {"1"},
                {"cats.png"}, {"9"},
                {"child.png"}, {"5"},
                {"handball.png"}, {"7"},
                {"mat1.png"}, {"1"},
                {"mater.png"}, {"2"},
                {"materials.png"}, {"4"},
                {"rect3.png"}, {"2"},
                {"test3.png"}, {"1"},
                {"test4.png"}, {"10"},
                {"test9.png"}, {"7"},
                {"test10.png"}, {"1"},
                {"test11.png"}, {"1"},
                {"test16.png"}, {"2"},
                {"test41.png"}, {"4"},
                {"testColors_new.png"}, {"6"},
                {"unknown.png"}, {"1"},
                {"test18.png"}, {"0"},
                {"fashion.png"}, {"10"},
                {"squares1.png"}, {"4"},
                {"squares3.png"}, {"4"},
                {"squares5.png"}, {"5"},
                {"ball_n.png"}, {"1"},
                {"snowman.png"}, {"1"},
                {"ball.png"}, {"1"},

                {"2cats.jpg"}, {"2"},
                {"3balls.jpg"}, {"3"},
                {"3balls.jpg"}, {"3"},
                {"6.jpg"}, {"6"},
                {"29.jpg"}, {"29"},
                {"balet.jpg"}, {"1"},
                {"cat.jpg"}, {"2"},
                {"children.jpg"}, {"7"},
                {"dance.jpg"}, {"6"},
                {"djin.jpg"}, {"1"},
                {"dust.jpg"}, {"1"},
                {"example.jpg"}, {"5"},
                {"example1.jpg"}, {"6"},
                {"new.jpg"}, {"6"},
                {"ten.jpg"}, {"10"},
                {"test.jpg"}, {"8"},
                {"test22.jpg"}, {"6"},
                {"test22i.jpg"}, {"6"},
                {"testdrive.jpg"}, {"1"},
                {"20.jpg"}, {"20"},
                {"5.jpg"}, {"5"},

                {"img_3.jpeg"}, {"3"},
                {"img_4.jpeg"}, {"3"},

                // advanced below
                {"skater.jpg"}, {"1"},
                {"skater.png"}, {"1"},
                {"dance1.png"}, {"1"},
                {"img_7.jpeg"}, {"7"},
                {"img_23.jpeg"}, {"3"},
                {"01.jpg"}, {"1"},
                {"1.1.jpg"}, {"10"},
                {"1.jpg"}, {"21"},
                {"danger2.jpg"}, {"1"},
                {"danger3.jpg"}, {"5"},
                {"photo4.jpg"}, {"4"},

//                // stuck together
//                {"balls2.jpg"}, {"13"},
//                {"vert_sl.png"}, {"2"},
//                {"vert_sl3.png"}, {"3"},
        };

        int passed = 0;
        for (int i = 0; i < tests.length; i += 2) {
            String res = ggo(tests[i][0]);
            if (res != null && res.equals(tests[i + 1][0])) {
                System.out.println("  Pass: " + Arrays.toString(tests[i]) + " Result: " + res);
                passed++;
            } else {
                System.out.println("! FAIL: " + Arrays.toString(tests[i]) +
                        " Expected " + tests[i + 1][0] + " Got: " + res);
            }
        }

        System.out.println("finished all tests, passed: " + passed + " out of " + tests.length / 2);
    }


}
