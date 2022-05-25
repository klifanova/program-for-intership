package com.griddynamics.gridu.javabasics.studentscourses.facade;

import com.griddynamics.gridu.javabasics.studentscourses.model.CoursesSummaryInfo;
import com.griddynamics.gridu.javabasics.studentscourses.model.SummaryStudentsInfo;
import com.griddynamics.gridu.javabasics.studentscourses.model.inputdata.ReportDataType;
import com.griddynamics.gridu.javabasics.studentscourses.model.student.*;
import com.griddynamics.gridu.javabasics.studentscourses.service.*;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Get info about students and put this info in three lists depending on the status course
 */

public class FacadeParsableStudents {

    private final FinishTimeCalculator finishTimeCalculator = new FinishTimeCalculatorImpl();
    private final EnrichingStudent enrichingStudent = new EnrichingStudentImpl();
    private final JsonConverter jsonConverter = new JsonConverter();

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
            Curriculum curriculum = student.getProgram().getCurriculum();

            if (curriculum == null) {
                student = enrichingStudent.enrichStudent(nowDate, null, student);
                noCourseStudentsList.add(student);
            } else {
                Instant startTime = curriculum.getStartTimeCourse();
                Duration duration = curriculum.getDuration();

                Instant endDate = finishTimeCalculator.calculateFinishTime(startTime, duration);
                student.getProgram().getCurriculum().setEndTimeCourse(endDate);
                student = enrichingStudent.enrichStudent(nowDate, endDate, student);

                StatusCourse status = student.getProgram().getStatusCourse();

                if (status == StatusCourse.IN_PROCESS) {
                    inProgressCourseStudentsList.add(student);
                } else if (status == StatusCourse.COMPLETED) {
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

        for (Student item : students) {
            Program program = item.getProgram();
            studentsList.add(item.getFullName() + " ( " + program.getCurriculum().getName() + " ) - "
                    + program.getStatusCourse().getStatus() + " " + program.getLeftTime());
        }
        return studentsList;
    }

    private List<String> getFullDataStudents(List<Student> students) {
        List<String> studentsList = new ArrayList<>();

        for (Student item : students) {
            Program program = item.getProgram();
            Curriculum curriculum = program.getCurriculum();
            studentsList.add("Student name: " + item.getFullName() + "; " + "working time: from 10:00 to 18:00; "
                    + "program name: " + curriculum.getName() + "; program duration: " +
                    curriculum.getDuration().toString() + "h.; start date: "
                    + curriculum.getStartTimeCourse().toString() + "; end date: "
                    + curriculum.getEndTimeCourse().toString() + "; left time: " + program.getLeftTime());
        }
        return studentsList;
    }

    private List<String> getInfoForStudentsNoHaveCourse(List<Student> students, Instant nowTime) {
        List<String> studentsList = new ArrayList<>();

        for (Student student : students) {
            studentsList.add(student.getFullName() + ". " + student.getProgram().getStatusCourse().getStatus() + "in "
                    + nowTime);
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
}