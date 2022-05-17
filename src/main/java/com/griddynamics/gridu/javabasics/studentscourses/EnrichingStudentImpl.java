package com.griddynamics.gridu.javabasics.studentscourses;

import com.griddynamics.gridu.javabasics.studentscourses.model.student.Program;
import com.griddynamics.gridu.javabasics.studentscourses.model.student.Student;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

/**
 * Updating the student's course status. Updating the left time student without weekend, not working hours.
 */

public class EnrichingStudentImpl implements EnrichingStudent {

    private static final ZoneOffset ZONE_OFFSET = ZoneOffset.UTC;
    private static final int HOURS_OF_DAY = 24;
    private static final int NOT_WORK_HOURS = 16;
    private static final int WORK_HOURS_OF_DAY = 8;
    private static final String STATUS_IN_PROCESS = "Training is not finished.";
    private static final String STATUS_COMPLETED = "Training completed.";

    /**
     * This method enriches data Student: updates leftTime and statusCourse only in working hours and days.
     *
     * @param nowTime
     * @param endTime
     * @param student
     * @return Student
     */

    public Student enrichStudent(Instant nowTime, Instant endTime, Student student) {
        long durationOfDays = Math.abs(nowTime.until(endTime, ChronoUnit.DAYS));
        long durationOfHours = Math.abs(nowTime.until(endTime, ChronoUnit.HOURS));
        Program program = student.getProgram();
        int leftDurationOfHours = (int) (durationOfHours - durationOfDays * HOURS_OF_DAY);
        int leftDaysFromNow = calculateDurationWithoutWeekend(nowTime, durationOfDays, leftDurationOfHours);
        int leftDaysToEnd = calculateDurationWithoutWeekend(endTime, durationOfDays, leftDurationOfHours);
        int leftHours = calculateDurationWithWorkHours(leftDurationOfHours);

        if (endTime.isAfter(nowTime)) {
            student.getProgram().setStatusCourse(STATUS_IN_PROCESS);
            if (leftDaysFromNow == 0) {
                program.setLeftTime(leftHours + " h. are left unit the end.");
            } else if (leftHours == 0 || leftHours == 8) {
                program.setLeftTime(leftDaysFromNow + " d. are left unit the end.");
            } else {
                program.setLeftTime(leftDaysFromNow + " d. " + leftHours + " h." + " are left unit the end.");
            }
            return student;
        } else if (endTime.isBefore(nowTime)) {
            program.setStatusCourse(STATUS_COMPLETED);
            if (leftDaysToEnd == 0) {
                program.setLeftTime(leftHours + " h. have passed since the end.");
            } else if (leftHours == 0 || leftHours == 8) {
                program.setLeftTime(leftDaysToEnd + " d. have passed since the end.");
            } else {
                program.setLeftTime(leftDaysToEnd + " d. " + leftHours + " h." + " have passed since the end.");
            }
        } else {
            program.setStatusCourse(STATUS_COMPLETED);
            program.setLeftTime("You just finished the course.");
            return student;
        }
        return student;
    }

    private static int calculateDurationWithoutWeekend(Instant startDate, long durationOfDays, int leftDurationOfHours) {
        int days = 0;
        LocalDateTime result = LocalDateTime.ofInstant(startDate, ZONE_OFFSET);
        int addedDays = 0;
        while (addedDays < durationOfDays) {
            result = result.plusDays(1);
            days += 1;
            addedDays += 1;
            if ((result.getDayOfWeek() == DayOfWeek.SATURDAY || result.getDayOfWeek() == DayOfWeek.SUNDAY)) {
                days -= 1;
            }
        }
        if (leftDurationOfHours == WORK_HOURS_OF_DAY) {
            days += 1;
        }
        return days;
    }

    private static int calculateDurationWithWorkHours(long leftDurationOfHours) {
        int leftTimeHours = (int) leftDurationOfHours - NOT_WORK_HOURS;
        if (leftTimeHours >= 0) {
            return leftTimeHours;
        }
        if (leftDurationOfHours == 8) {
            return 0;
        }
        return (int) leftDurationOfHours;
    }
}
