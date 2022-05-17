package com.griddynamics.gridu.javabasics.studentscourses.facade;

import com.griddynamics.gridu.javabasics.studentscourses.*;
import com.griddynamics.gridu.javabasics.studentscourses.model.input.OutputDataType;
import com.griddynamics.gridu.javabasics.studentscourses.model.SummaryStudentsInfo;
import com.griddynamics.gridu.javabasics.studentscourses.model.student.Program;
import com.griddynamics.gridu.javabasics.studentscourses.model.CoursesSummaryInfo;
import com.griddynamics.gridu.javabasics.studentscourses.model.student.Curriculum;
import com.griddynamics.gridu.javabasics.studentscourses.model.student.Student;
import com.griddynamics.gridu.javabasics.studentscourses.model.student.Training;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Distribution of students on three lists
 */

public class FacadeParsableStudents {

    private static final String STATUS_IN_PROCESS = "Training is not finished.";
    private static final String STATUS_COMPLETED = "Training completed.";

    private FinishTimeCalculator finishTimeCalculator = new FinishTimeCalculatorImpl();
    private EnrichingStudent enrichingStudent = new EnrichingStudentImpl();
    private JsonConverter jsonConverter = new JsonConverter();

    /**
     * Returns: CoursesSummaryInfo;
     * Params: fileName, nowDate and outputDataType;
     * This method lists students based on their course status.
     */

    public CoursesSummaryInfo getParsedStudentsData(String fileName, Instant nowDate, OutputDataType outputDataType) {
        List<Student> inProgressCourseStudentsList = new ArrayList<>();
        List<Student> completeCourseStudentsList = new ArrayList<>();
        List<Student> noCourseStudentsList = new ArrayList<>();
        Training training = jsonConverter.converterJson(fileName, Training.class);
        List<Student> dataStudentFromFile = training.getStudentList();

        for (Student student : dataStudentFromFile) {
            Curriculum curriculum = student.getProgram().getCurriculum();
            Instant startTime = curriculum.getStartTimeCourse();
            Duration duration = curriculum.getDuration();
            Instant endDate = finishTimeCalculator.calculateFinishTime(startTime, duration);
            student.getProgram().getCurriculum().setEndTimeCourse(endDate);
            student = enrichingStudent.enrichStudent(nowDate, endDate, student);

            String status = student.getProgram().getStatusCourse();

            if (status.equals(STATUS_IN_PROCESS)) {
                inProgressCourseStudentsList.add(student);
            } else if (status.equals(STATUS_COMPLETED)) {
                completeCourseStudentsList.add(student);
            } else {
                noCourseStudentsList.add(student);
            }
        }

        SummaryStudentsInfo summaryStudentsInfo = new SummaryStudentsInfo(inProgressCourseStudentsList,
                completeCourseStudentsList, noCourseStudentsList);

        return getCoursesSummaryInfo(outputDataType, summaryStudentsInfo);
    }

    private List<String> getShortDataStudents(List<Student> students) {
        List<String> studentsList = new ArrayList<>();

        for (Student item : students) {
            Program program = item.getProgram();
            studentsList.add("\n" + item.getFullName() + " ( " + program.getCurriculum().getName() + " ) - "
                    + program.getStatusCourse() + " " + program.getLeftTime());
        }
        return studentsList;
    }

    private List<String> getFullDataStudents(List<Student> students) {
        List<String> studentsList = new ArrayList<>();

        for (Student item : students) {
            Program program = item.getProgram();
            Curriculum curriculum = program.getCurriculum();
            studentsList.add("\n" + "student name: " + item.getFullName() + "; " + "working time: from 10:00 to 18:00; "
                    + "program name: " + curriculum.getName() + "; program duration: " +
                    curriculum.getDuration().toString() + "h.; start date: "
                    + curriculum.getStartTimeCourse().toString() + "; end date: "
                    + curriculum.getEndTimeCourse().toString() + "; left time: " + program.getLeftTime());
        }
        return studentsList;
    }

    /**
     * Returns: CoursesSummaryInfo;
     * Params: summaryStudentsInfo and outputDataType;
     * This method determines in what format to output CoursesSummaryInfo.
     */

    private CoursesSummaryInfo getCoursesSummaryInfo(OutputDataType outputDataType,
                                                     SummaryStudentsInfo summaryStudentsInfo) {
        List<String> listDataStudentsInProcessCourse = new ArrayList<>();
        List<String> listDataStudentsCompleteCourse = new ArrayList<>();
        List<String> listDataStudentsNotHaveCourse = new ArrayList<>();

        List<Student> inProgressCourseStudentsList = summaryStudentsInfo.getInProgressCoursesStudentsList();
        List<Student> completeCourseStudentsList = summaryStudentsInfo.getCompleteCoursesStudentList();
        List<Student> noCourseStudentsList = summaryStudentsInfo.getNoCourseStudentList();

        if (OutputDataType.SHORT == outputDataType) {
            listDataStudentsInProcessCourse = getShortDataStudents(inProgressCourseStudentsList);
            listDataStudentsCompleteCourse = getShortDataStudents(completeCourseStudentsList);
            listDataStudentsNotHaveCourse = getShortDataStudents(noCourseStudentsList);
        }
        if (OutputDataType.FULL == outputDataType) {
            listDataStudentsInProcessCourse = getFullDataStudents(inProgressCourseStudentsList);
            listDataStudentsCompleteCourse = getFullDataStudents(completeCourseStudentsList);
            listDataStudentsNotHaveCourse = getFullDataStudents(noCourseStudentsList);
        }
        return new CoursesSummaryInfo(listDataStudentsInProcessCourse,
                listDataStudentsCompleteCourse, listDataStudentsNotHaveCourse);
    }
}