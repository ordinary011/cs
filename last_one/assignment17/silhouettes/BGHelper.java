package com.shpp.p2p.cs.ldebryniuk.assignment17.silhouettes;

import com.shpp.p2p.cs.ldebryniuk.assignment17.mycollection.map.MyHashMap;
import com.shpp.p2p.cs.ldebryniuk.assignment17.mycollection.map.MyMap;

/**
 * The following class comprises the logic for determining background and its max and min values
 */
public class BGHelper {

    /**
     * The following method searches for a background color of an img.
     *
     * @return true only if there are no objects
     */
    public boolean findBGColor(int[][] pixels, int minPixsInObj) {
        // <pixVal, pixCountOfCurrentPixValue>
        MyHashMap<Integer, Integer> pixPopularities = new MyHashMap<>();

        findPixPopularities(pixPopularities, pixels);
        if (pixPopularities.size() == 1) return true; // only BG nothing else

        // distribute values
        int mostPopularPixVal = 0;
        int biggestPixCount = 0;
        int minVal = 0;
        int maxVal = 0;
        for (MyMap.Entry<Integer, Integer> entry : pixPopularities.entryList()) {
            int pixValCount = entry.getValue();

            if (pixValCount > biggestPixCount) { // bg value
                mostPopularPixVal = entry.getKey();
                biggestPixCount = pixValCount;
            }

            if (pixValCount > minPixsInObj) { // bg max and min values
                int pixVal = entry.getKey();
                if (pixVal > maxVal) maxVal = pixVal;
                else if (pixVal < minVal) minVal = pixVal;
            }
        }

        findBGRange(mostPopularPixVal, maxVal, minVal);
        return false; // false means there is at least one obj
    }

    /**
     * The following method maps pix values with their amount
     */
    private void findPixPopularities(MyHashMap<Integer, Integer> pixPopularities, int[][] pixels) {
        for (int[] pixRow : pixels) {
            for (int pixVal : pixRow) {
                if (!pixPopularities.containsKey(pixVal)) pixPopularities.put(pixVal, 1);
                else pixPopularities.put(pixVal, pixPopularities.get(pixVal) + 1); // increment popularity
            }
        }
    }

    /**
     * The following method searches for a BG max and min values
     */
    private void findBGRange(int BG_COLOR, int maxVal, int minVal) {
        int diff1 = BG_COLOR - maxVal;
        int diff2 = BG_COLOR - minVal;

        int BGFilter = (int) (Math.max(diff1, diff2) * 0.001);

        Assignment13Part1.BG_MAX_VAL = BG_COLOR + BGFilter;
        Assignment13Part1.BG_MIN_VAL = BG_COLOR - BGFilter;
    }


}
