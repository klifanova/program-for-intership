package com.griddynamics.gridu.javabasics.studentscourses;

import com.griddynamics.gridu.javabasics.studentscourses.exception.InvalidDurationException;

import java.time.*;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;

/**
 * Calculate finish time, if we have duration and start time course.
 */

public class FinishTimeCalculatorImpl implements FinishTimeCalculator {

    private static final ZoneOffset ZONE_OFFSET = ZoneOffset.UTC;
    private static final int WORK_HOUR_START = 10;
    private static final int WORK_HOUR_END = 18;
    private static final int WORK_HOURS = WORK_HOUR_END - WORK_HOUR_START;
    private static final int WORK_HOURS_IN_WEEK = WORK_HOURS * 5;
    private static final int WORK_DAY_IN_WEEK = 5;

    /**
     * This method calculates end time considering only working hours and days.
     * @param startTimeForStudent
     * @param duration
     * @return Instant
     */

    @Override
    public Instant calculateFinishTime(Instant startTimeForStudent, Duration duration) {
        LocalDateTime startDate = LocalDateTime.ofInstant(startTimeForStudent, ZONE_OFFSET);
        int durationOfHours = (int) duration.toHours();
        if (durationOfHours <= 0) {
            throw new InvalidDurationException(String.format("We don't have course with %s duration." +
                    "It's not valid duration.", duration.toHours()));
        }
        int weeks = durationOfHours / WORK_HOURS_IN_WEEK;
        int days = (durationOfHours - weeks * WORK_HOURS_IN_WEEK) / WORK_HOURS;
        int hours = (durationOfHours % WORK_HOURS);

        LocalDateTime startDateWithoutWeekend = shiftStartDateFromWeekend(startDate);
        LocalDateTime startDateForWorkHours = shiftStartDateForWorkHours(startDateWithoutWeekend);
        LocalDateTime timeWithoutWeekend = getDaysSkippingWeekends(startDateForWorkHours,
                days + weeks * WORK_DAY_IN_WEEK);
        LocalDateTime finishTime = getHoursSkippingNotWorkHours(timeWithoutWeekend, hours);

        return shiftDayFromWeekend(finishTime).toInstant(ZoneOffset.UTC);
    }

    private static LocalDateTime shiftStartDateFromWeekend(LocalDateTime startDate) {
        if (startDate.getDayOfWeek() == DayOfWeek.SATURDAY) {
            return startDate.plusDays(2)
                    .with(ChronoField.HOUR_OF_DAY, WORK_HOUR_START);
        } else if (startDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
            return startDate.plusDays(1)
                    .with(ChronoField.HOUR_OF_DAY, WORK_HOUR_START);
        }
        return startDate;
    }

    private static LocalDateTime shiftStartDateForWorkHours(LocalDateTime startDate) {
        if (startDate.getHour() > WORK_HOUR_END) {
            return startDate.plusDays(1)
                    .with(ChronoField.HOUR_OF_DAY, WORK_HOUR_START);
        } else if (startDate.getHour() < WORK_HOUR_START) {
            return startDate.with(ChronoField.HOUR_OF_DAY, WORK_HOUR_START);
        } else {
            return startDate;
        }
    }

    private static LocalDateTime getDaysSkippingWeekends(LocalDateTime date, int days) {
        LocalDateTime result = date;
        int addedDays = 0;
        while (addedDays < days) {
            result = result.plusDays(1);
            if (!(result.getDayOfWeek() == DayOfWeek.SATURDAY || result.getDayOfWeek() == DayOfWeek.SUNDAY)) {
                ++addedDays;
            }
        }
        return result;
    }

    private static LocalDateTime getHoursSkippingNotWorkHours(LocalDateTime date, int hours) {
        if (date.getHour() + hours >= WORK_HOUR_END) {
            int leftHoursForNextDay = hours - (WORK_HOUR_END - date.getHour());
            return date.plus(1, ChronoUnit.DAYS)
                    .with(ChronoField.HOUR_OF_DAY, (leftHoursForNextDay + WORK_HOUR_START));
        } else if (date.getHour() < WORK_HOUR_START) {
            return date.with(ChronoField.HOUR_OF_DAY, (WORK_HOUR_START + hours));
        } else if (date.getHour() == WORK_HOUR_START && hours == 0) {
            return date.minusDays(1).with(ChronoField.HOUR_OF_DAY, WORK_HOUR_END);
        } else {
            return date.plusHours(hours);
        }
    }

    private static LocalDateTime shiftDayFromWeekend(LocalDateTime day) {
        if (day.get(ChronoField.DAY_OF_WEEK) == DayOfWeek.SATURDAY.getValue()) {
            return day.plus(2, ChronoUnit.DAYS);
        } else if (day.get(ChronoField.DAY_OF_WEEK) == DayOfWeek.SUNDAY.getValue()) {
            return day.plus(1, ChronoUnit.DAYS);
        }
        return day;
    }
}
