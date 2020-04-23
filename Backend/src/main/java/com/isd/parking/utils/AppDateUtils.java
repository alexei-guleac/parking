package com.isd.parking.utils;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;


/**
 * Date utilities
 */
@Slf4j
public class AppDateUtils {

    /**
     * Assert that specified date is before present date
     *
     * @param subjectDate      - target date
     * @param clockSkewMinutes - guarantee clock skew in minutes
     * @return operation result
     */
    public static boolean isDateBeforeNow(LocalDateTime subjectDate, int clockSkewMinutes) {
        return subjectDate.isBefore(LocalDateTime.now().minusMinutes(clockSkewMinutes));
    }

    /**
     * Assert that specified date is after present date
     *
     * @param subjectDate      - target date
     * @param clockSkewMinutes - guarantee clock skew in minutes
     * @return operation result
     */
    public static boolean isDateAfterNow(LocalDateTime subjectDate, int clockSkewMinutes) {
        return subjectDate.isAfter(LocalDateTime.now().plusMinutes(clockSkewMinutes));
    }

    /**
     * Get string date ending word based on days count
     *
     * @param days - target days count
     * @return specified date ending
     */
    public static String getPeriodEnding(int days) {
        String once = "once";
        int day = 1;
        int week = 7;
        int month = 30;
        String atDays = once + " at " + days + " days";

        if (days == day) {
            return once + " a day";
        }
        if (days < week) {
            return atDays;
        }
        if (days == week) {
            return once + " a week";
        } else {
            if (days % week == 0) {
                return once + " at " + days / week + " weeks";
            }
            if (days < month) {
                return atDays;
            }
            if (days == month) {
                return once + " a month";
            } else {
                if (days % month == 0) {
                    return once + " at " + days / month + " months";
                }
                return atDays;
            }
        }
    }
}
