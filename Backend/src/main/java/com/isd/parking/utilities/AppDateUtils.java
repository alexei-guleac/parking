package com.isd.parking.utilities;

import com.ibm.icu.text.PluralRules;
import com.isd.parking.config.StaticContextAccessor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Locale;


/**
 * Date utilities
 */
@Slf4j
@Service
public class AppDateUtils {

    private static MessageSource messageSource;

    @Autowired(required = true)
    public void setMessageSource(@Qualifier("messageSource") MessageSource message) {
        messageSource = message;
    }

    public static MessageSource getMessageSource() {
        return messageSource;
    }

    /**
     * Assert that specified date is before present date
     *
     * @param subjectDate      - target date
     * @param clockSkewMinutes - guarantee clock skew in minutes
     * @return operation result
     */
    public static boolean isDateBeforeNow(@NotNull LocalDateTime subjectDate, int clockSkewMinutes) {
        return subjectDate.isBefore(LocalDateTime.now().minusMinutes(clockSkewMinutes));
    }

    /**
     * Assert that specified date is after present date
     *
     * @param subjectDate      - target date
     * @param clockSkewMinutes - guarantee clock skew in minutes
     * @return operation result
     */
    public static boolean isDateAfterNow(@NotNull LocalDateTime subjectDate, int clockSkewMinutes) {
        return subjectDate.isAfter(LocalDateTime.now().plusMinutes(clockSkewMinutes));
    }

    /**
     * Get string localized date ending with plural forms word based on days count
     *
     * @param daysNumber - target days count
     * @return specified localized date ending with plural forms
     */
    public static @NotNull String getPeriodEndingLocalized(int daysNumber, Locale userLocale) {

        ResourceBundleMessageSource messageSource =
            StaticContextAccessor.getBean(ResourceBundleMessageSource.class);

        int day = 1;
        int week = 7;
        int month = 30;

        PluralRules pluralRules = PluralRules.forLocale(userLocale);
        String daysResourceKey = "date.period.days.plural_form." + pluralRules.select(daysNumber);

        // localized messages from resource bundle
        @NotNull String once = messageSource.getMessage("date.period.once", null, userLocale);
        @NotNull String at = messageSource.getMessage("date.period.at", null, userLocale);
        @NotNull String days = messageSource.getMessage(daysResourceKey, null, userLocale);
        @NotNull String aDay = messageSource.getMessage("date.period.once-a-day", null, userLocale);
        @NotNull String aWeek = messageSource.getMessage("date.period.once-a-week", null, userLocale);
        @NotNull String aMonth = messageSource.getMessage("date.period.once-a-month", null, userLocale);

        @NotNull String atDays = once + " " + at + " " + daysNumber + " " + days;

        if (daysNumber == day) {
            return once + " " + aDay;
        }
        if (daysNumber < week) {
            return atDays;
        }
        if (daysNumber == week) {
            return once + " " + aWeek;
        } else {
            if (daysNumber % week == 0) {
                final int weeksNumber = daysNumber / week;
                String weeksResourceKey = "date.period.weeks.plural_form." + pluralRules.select(weeksNumber);
                @NotNull String weeks = messageSource.getMessage(weeksResourceKey, null, userLocale);
                return once + " " + at + " " + weeksNumber + " " + weeks;
            }
            if (daysNumber < month) {
                return atDays;
            }
            if (daysNumber == month) {
                return once + " " + aMonth;
            } else {
                if (daysNumber % month == 0) {
                    final int monthNumber = daysNumber / month;
                    String monthsResourceKey = "date.period.months.plural_form." + pluralRules.select(monthNumber);
                    @NotNull String months = messageSource.getMessage(monthsResourceKey, null, userLocale);
                    return once + " " + at + " " + monthNumber + " " + months;
                }
                return atDays;
            }
        }
    }

    /**
     * Get string date ending word based on days count
     *
     * @param days - target days count
     * @return specified date ending
     */
    public static @NotNull String getPeriodEnding(int days) {
        @NotNull String once = "once";
        int day = 1;
        int week = 7;
        int month = 30;
        @NotNull String atDays = once + " at " + days + " days";

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
