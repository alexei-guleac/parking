package com.isd.parking.utils;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;


@Slf4j
public class AppDateUtils {

    public static boolean isBeforeNow(LocalDateTime subjectDate, int clockSkewMinutes) {
        // log.info(methodMsgStatic("subjectDate " + subjectDate));
        // log.info(methodMsgStatic("LocalDateTime.now().minusMinutes(clockSkewMinutes)" + LocalDateTime.now().minusMinutes(clockSkewMinutes)));
        return subjectDate.isBefore(LocalDateTime.now().minusMinutes(clockSkewMinutes));
    }

    public static boolean isAfterNow(LocalDateTime subjectDate, int clockSkewMinutes) {
        return subjectDate.isAfter(LocalDateTime.now().plusMinutes(clockSkewMinutes));
    }

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
