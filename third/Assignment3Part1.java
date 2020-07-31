package com.shpp.p2p.cs.ldebryniuk.assignment3;

import com.shpp.cs.a.console.TextProgram;

/**
 * This class works like a sport tracker. It notifies user about the necessity of trainings
 */
public class Assignment3Part1 extends TextProgram {

    // specifies amount of days when sport trainings were sufficient for health maintenance
    private int spentDaysOnCardiovascular = 0;
    private int spentDaysOnBloodPressure = 0;

    final int MINUTES_IN_ONE_DAY = 1440;

    /**
     * This is the starting method of the program
     */
    public void run() {
        collectAndProcessUserData();

        getResults();
    }

    /**
     * The following method gets input from user and confirms which days were satisfactory for health maintenance
     */
    private void collectAndProcessUserData() {
        final int DAYS_IN_WEEK = 7;

        for (int i = 1; i <= DAYS_IN_WEEK; i++) {
            double spentMinutesPerDay = getMinutesFromUser(i);

            // 40 minutes per day is enough for both health programs
            if (spentMinutesPerDay >= 40) {
                spentDaysOnBloodPressure++;
                spentDaysOnCardiovascular++;
                continue;
            }

            // 30 minutes per day is enough for cardio but not for blood pressure
            if (spentMinutesPerDay >= 30) {
                spentDaysOnCardiovascular++;
            }
        }
    }

    /**
     * The following method collects amount of minutes that user spent on trainings in current day
     *
     * @param dayNumber number of current day
     * @return amount of minutes that user spent on trainings in current day
     */
    private double getMinutesFromUser(int dayNumber) {
        double spentMinutesPerDay = 0;
        try {
            spentMinutesPerDay = readDouble("How many minutes did you do on day " + dayNumber + "? ");

            while (spentMinutesPerDay < 1 || spentMinutesPerDay > MINUTES_IN_ONE_DAY) {
                spentMinutesPerDay = readDouble("Sorry only numbers in range between 1 and 1440." +
                        " How many minutes did you do on day " + dayNumber + "? ");
            }
        } catch (Exception e) {
            System.out.println("sorry wrong input");
            e.printStackTrace();
        }

        return spentMinutesPerDay;
    }

    /**
     * The following method collects all the data that is needed for the results.
     * And uses other method to print those results
     */
    private void getResults() {
        // amount of days that are needed for certain health maintenance
        final int DAYS_NEEDED_FOR_CARDIOVASCULAR = 5;
        final int DAYS_NEEDED_FOR_BLOOD_PRESSURE = 3;

        System.out.println(); // prints an empty line before results

        // prints results for Cardiovascular health
        printResults("Cardiovascular health:", spentDaysOnCardiovascular,
                DAYS_NEEDED_FOR_CARDIOVASCULAR, "cardiovascular health");

        // prints results for Blood pressure
        printResults("Blood pressure:", spentDaysOnBloodPressure,
                DAYS_NEEDED_FOR_BLOOD_PRESSURE, "to keep a low blood pressure.");
    }


    /**
     * The following method is used to print results of our trainings
     *
     * @param resultType    is the type of health maintenance (either Cardiovascular or Blood pressure)
     * @param daysSpent     amount of days that the user spent for this specific health maintenance
     * @param daysNeeded    amount of days that are needed for successful results
     * @param successString this is a part of a success message that is different in both health maintenances
     */
    private void printResults(String resultType, int daysSpent, int daysNeeded, String successString) {
        System.out.println(
                resultType +
                        ((daysSpent >= daysNeeded) ?
                                "\n  Great job! You've done enough exercise " + successString :
                                "\n  You needed to train hard for at least " +
                                        (daysNeeded - daysSpent) + " more day(s) a week!")
        );
    }
}
