package com.griddynamics.gridu.javabasics.studentscourses.facade;

import com.griddynamics.gridu.javabasics.studentscourses.model.CoursesSummaryInfo;
import com.griddynamics.gridu.javabasics.studentscourses.model.SummaryStudentsInfo;
import com.griddynamics.gridu.javabasics.studentscourses.model.inputdata.ReportDataType;
import com.griddynamics.gridu.javabasics.studentscourses.model.student.*;
import com.griddynamics.gridu.javabasics.studentscourses.service.*;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * Get info about students and put this info in three lists depending on the status course
 */

public class FacadeParsableStudents {

    private FinishTimeCalculator finishTimeCalculator = new FinishTimeCalculatorImpl();
    private EnrichingStudent enrichingStudent = new EnrichingStudentImpl();
    private JsonConverter jsonConverter = new JsonConverter();

    /**
     * This method lists students based on their course status.
     *
     * @param fileName       - the path to the file
     * @param nowDate        - the specific date for which report will be generated
     * @param reportDataType - the form of report data
     * @return CoursesSummaryInfo - the info containing lists of students with special format
     */

    public CoursesSummaryInfo getParsedStudentsData(String fileName, Instant nowDate, ReportDataType reportDataType) {
        List<Student> inProgressCourseStudentsList = new ArrayList<>();
        List<Student> completeCourseStudentsList = new ArrayList<>();
        List<Student> noCourseStudentsList = new ArrayList<>();

        Training training = jsonConverter.converterJson(fileName, Training.class);
        List<Student> studentDataFromFile = training.getStudentList();

        for (Student student : studentDataFromFile) {
            Curriculum curriculum = student.getCurriculum();
            List<Course> courseList = curriculum.getCourseList();
            int sumDurations = 0;

            if (courseList.isEmpty() || courseList == null) {
                student = enrichingStudent.enrichStudent(nowDate, null, student);
                noCourseStudentsList.add(student);
            } else {

                for (int i = 0; i < courseList.size(); i++) {
                    Course course = courseList.get(i);

                    Instant startTime = course.getStartTimeCourse();
                    Duration duration = course.getDuration();

                    Instant endDate = finishTimeCalculator.calculateFinishTime(startTime, duration);
                    course.setEndTimeCourse(endDate);
                    long durationHours = duration.toHours();

                    sumDurations += durationHours;
                }

                Duration durations = Duration.of(sumDurations, ChronoUnit.HOURS);
                Instant startTime = courseList.get(0).getStartTimeCourse();
                Instant endDate = finishTimeCalculator.calculateFinishTime(startTime, durations);
                student = enrichingStudent.enrichStudent(nowDate, endDate, student);

                if (curriculum.getStatus() == StatusCurriculum.IN_PROCESS) {
                    inProgressCourseStudentsList.add(student);
                } else if (curriculum.getStatus() == StatusCurriculum.COMPLETED) {
                    completeCourseStudentsList.add(student);
                } else {
                    noCourseStudentsList.add(student);
                }
            }
        }

        SummaryStudentsInfo summaryStudentsInfo = new SummaryStudentsInfo(inProgressCourseStudentsList,
                completeCourseStudentsList, noCourseStudentsList);

        return getCoursesSummaryInfo(reportDataType, summaryStudentsInfo, nowDate);
    }

    private List<String> getShortDataStudents(List<Student> students) {
        List<String> studentsList = new ArrayList<>();

        for (Student student : students) {
            Curriculum curriculum = student.getCurriculum();

            studentsList.add(student.getFullName() + " ( " + curriculum.getName() + " ) - "
                    + curriculum.getStatus().getStatus() + " " + curriculum.getLeftTime());
        }
        return studentsList;
    }

    private List<String> getFullDataStudents(List<Student> students) {
        List<String> studentsList = new ArrayList<>();

        for (Student student : students) {
            StringBuilder sb = new StringBuilder();
            Curriculum curriculum = student.getCurriculum();
            List<Course> courseList = curriculum.getCourseList();
            String curriculumInfo = getListCurriculum(courseList);
            //change this add
            sb.append("STUDENT: ")
                    .append(student.getFullName())
                    .append(". Working time: from 10:00 to 18:00. CURRICULUM: ")
                    .append(curriculum.getName())
                    .append(";")
                    .append(curriculumInfo);

            studentsList.add(sb.toString());
        }
        return studentsList;
    }

    private List<String> getInfoForStudentsNoHaveCourse(List<Student> students, Instant nowTime) {
        List<String> studentsList = new ArrayList<>();

        for (Student student : students) {
            studentsList.add(student.getFullName() + ". " +
                    student.getCurriculum().getStatus().getStatus() + "in " + nowTime);
        }
        return studentsList;
    }

    /**
     * This method parse students data in specific report form(short/full) in CoursesSummaryInfo.
     *
     * @param reportDataType      - the form of report data
     * @param summaryStudentsInfo - the info containing lists of students
     * @return CoursesSummaryInfo - the info containing lists of students with special format
     */

    private CoursesSummaryInfo getCoursesSummaryInfo(ReportDataType reportDataType,
                                                     SummaryStudentsInfo summaryStudentsInfo, Instant nowTime) {
        List<String> listDataStudentsInProcessCourse = new ArrayList<>();
        List<String> listDataStudentsCompleteCourse = new ArrayList<>();
        List<String> listDataStudentsNotHaveCourse = new ArrayList<>();

        List<Student> inProgressCourseStudentsList = summaryStudentsInfo.getInProgressCoursesStudentsList();
        List<Student> completeCourseStudentsList = summaryStudentsInfo.getCompleteCoursesStudentList();
        List<Student> noCourseStudentsList = summaryStudentsInfo.getNoCourseStudentList();

        if (reportDataType == ReportDataType.SHORT) {
            listDataStudentsInProcessCourse = getShortDataStudents(inProgressCourseStudentsList);
            listDataStudentsCompleteCourse = getShortDataStudents(completeCourseStudentsList);
            listDataStudentsNotHaveCourse = getInfoForStudentsNoHaveCourse(noCourseStudentsList, nowTime);
        }
        if (reportDataType == ReportDataType.FULL) {
            listDataStudentsInProcessCourse = getFullDataStudents(inProgressCourseStudentsList);
            listDataStudentsCompleteCourse = getFullDataStudents(completeCourseStudentsList);
            listDataStudentsNotHaveCourse = getInfoForStudentsNoHaveCourse(noCourseStudentsList, nowTime);
        }
        return new CoursesSummaryInfo(listDataStudentsInProcessCourse,
                listDataStudentsCompleteCourse, listDataStudentsNotHaveCourse);
    }

    private String getListCurriculum(List<Course> courseList) {
        StringBuilder sb = new StringBuilder();

        for (Course course : courseList) {
            sb.append(" COURSE: ")
                    .append(course.getName())
                    .append(" Program duration: ")
                    .append(course.getDuration().toHours())
                    .append("h. START_DATE: ")
                    .append(course.getStartTimeCourse().toString())
                    .append("; END_DATE: ")
                    .append(course.getEndTimeCourse().toString())
                    .append(".");
        }

        return sb.toString();
    }
}