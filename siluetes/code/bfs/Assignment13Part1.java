package com.shpp.p2p.cs.ldebryniuk.assignment13;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.HashSet;

/**
 * The following class is a very simplified implementation of image analise.
 * It uses deep first-search in order to determine the amount of objects on the image
 */
public class Assignment13Part1 {

    // contains visited pixels numbers. e.g. 1, 2, 3, 44, 556678 etc...
    private static final HashSet<Integer> visitedPixs = new HashSet<>();
    // Queue that contains coordinates of pixels. e.g. (i == y) and (i+1 == x)
    private static final LinkedList<Integer> q = new LinkedList<>();
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

        runTests();
    }

    /**
     * The following method comprises all the necessary logic for implementation of dfs.
     */
    private static String findObjCount(String imgName) {
        try {
            BufferedImage img = ImageIO.read(new File(imgName));

            int minObjSize = createArrOfPixs(img);

            boolean onlyBGPresent = new BGHelper().findBGColor(pixels, minObjSize);
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
     * The following method determines if pix val is background
     */
    private static boolean isBG(int pixVal) {
        return pixVal > BG_MIN_VAL && pixVal < BG_MAX_VAL;
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
                    rememberPix(y, x, obj);

                    while (!q.isEmpty()) {
                        findRelations(q.poll(), q.poll(), obj); // q: (i == y) and (i+1 == x)
                    }

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
     * The following method adds pixel coordinates to the queue and adds its sequential number to the obj hashSet
     */
    private static void rememberPix(int y, int x, HashSet<Integer> obj) {
        q.add(y);
        q.add(x);
        obj.add(W * y + x);
    }

    /**
     * The following method determines if pixel is related to other pixels and if pixel is single
     */
    private static void findRelations(int y, int x, HashSet<Integer> obj) {
        if (x < W - 1 && !isBG(pixels[y][x + 1]) && !obj.contains(W * y + (x + 1))) { // right pixel
            rememberPix(y, x + 1, obj);
        }

        if (y < H - 1 && x < W - 1 && !isBG(pixels[y + 1][x + 1]) && !obj.contains(W * (y + 1) + (x + 1))) {//below right
            rememberPix(y + 1, x + 1, obj);
        }

        if (y < H - 1 && !isBG(pixels[y + 1][x]) && !obj.contains(W * (y + 1) + x)) { // pix below
            rememberPix(y + 1, x, obj);
        }

        if (y < H - 1 && x > 0 && !isBG(pixels[y + 1][x - 1]) && !obj.contains(W * (y + 1) + (x - 1))) { // below left
            rememberPix(y + 1, x - 1, obj);
        }

        if (x > 0 && !isBG(pixels[y][x - 1]) && !obj.contains(W * y + (x - 1))) { // left pixel
            rememberPix(y, x - 1, obj);
        }

        if (y > 0 && x > 0 && !isBG(pixels[y - 1][x - 1]) && !obj.contains(W * (y - 1) + (x - 1))) { // above left
            rememberPix(y - 1, x - 1, obj);
        }

        if (y > 0 && !isBG(pixels[y - 1][x]) && !obj.contains(W * (y - 1) + x)) { // pix above
            rememberPix(y - 1, x, obj);
        }

        if (y > 0 && x < W - 1 && !isBG(pixels[y - 1][x + 1]) && !obj.contains(W * (y - 1) + (x + 1))) { // above right
            rememberPix(y - 1, x + 1, obj);
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
            String res = findObjCount(tests[i][0]);
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
