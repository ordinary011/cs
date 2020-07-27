package com.shpp.p2p.cs.ldebryniuk.assignment2;

import com.shpp.cs.a.console.TextProgram;

/**
 * This class works like a sport tracker. It notifies user about the necessity of trainings
 */
public class Assignment3Part1 extends TextProgram {

    // specifies amount of days when sport trainings were sufficient for health maintanance
    private int spentDaysOnCardiovacular = 0;
    private int spentDaysOnBloodPressure = 0;

    /**
     * This is the starting method of the program
     */
    public void run() {
        collectAndProcessUserData();

        getResults();
    }

    /**
     * this method gets input from user and confirms which days were satisfactory for health maintanance
     */
    private void collectAndProcessUserData() {
        // specifies amount of minutes per each day spent on trainings
        int spentMinutesPerDay;
        final int DAYS_IN_WEEK = 7;

        for (int i = 1; i <= DAYS_IN_WEEK; i++) {
            spentMinutesPerDay = readInt("How many minutes did you do on day " + i + "? ");

            // 40 minutes per day is enough for both health programs
            if (spentMinutesPerDay >= 40) {
                spentDaysOnBloodPressure++;
                spentDaysOnCardiovacular++;
                continue;
            }

            // 30 minutes per day is enough for cardio but not for blood pressure
            if (spentMinutesPerDay >= 30) {
                spentDaysOnCardiovacular++;
            }
        }
    }

    /**
     * this method collects all the data that is needed for the results. And uses other method to print those results
     */
    private void getResults() {
        // amount of days that are needed for certain health maintanance
        final int DAYS_NEEDED_FOR_CARDIOVACULAR = 5;
        final int DAYS_NEEDED_FOR_BLOOD_PRESSURE = 3;

        // prints results for Cardiovacular health
        printResults("Cardiovacular health:", spentDaysOnCardiovacular,
                DAYS_NEEDED_FOR_CARDIOVACULAR, "cardiovacular health");

        // prints results for Blood pressure
        printResults("Blood pressure:", spentDaysOnBloodPressure,
                DAYS_NEEDED_FOR_BLOOD_PRESSURE, "to keep a low blood pressure.");
    }


    /**
     * This method is used to print reslts of our trainings
     *
     * @param resultType    is the type of health maintanance (etiher Cardiovacular or Blood pressure)
     * @param daysSpent     amount of days that the user spent for this specific health maintanance
     * @param daysNeeded    amount of days that are needed for successful results
     * @param successString this is a part of a success message that is different in both health maintanances
     */
    private void printResults(String resultType, int daysSpent, int daysNeeded, String successString) {
        System.out.println(
                resultType +
                        ((daysSpent >= daysNeeded) ?
                                "  Great job! You've done enough exercise " + successString :
                                "  You needed to train hard for at least " +
                                        (daysNeeded - daysSpent) + " more day(s) a week!")
        );
    }
}
