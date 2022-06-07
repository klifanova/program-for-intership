package com.griddynamics.gridu.javabasics.studentscourses.unit;

import com.griddynamics.gridu.javabasics.studentscourses.model.student.Course;
import com.griddynamics.gridu.javabasics.studentscourses.model.student.Curriculum;
import com.griddynamics.gridu.javabasics.studentscourses.model.student.StatusCurriculum;
import com.griddynamics.gridu.javabasics.studentscourses.model.student.Student;
import com.griddynamics.gridu.javabasics.studentscourses.service.EnrichingStudent;
import com.griddynamics.gridu.javabasics.studentscourses.service.EnrichingStudentImpl;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StatusCalculatorTest {

    private final EnrichingStudent statusCalculator = new EnrichingStudentImpl();

    @ParameterizedTest
    @CsvSource({
            "2022-04-01T10:00:00.00Z , 2022-04-01T11:00:00.00Z, 1 h. have passed since the end.",
            "2022-04-04T10:00:00.00Z , 2022-04-08T18:00:00.00Z, 5 d. have passed since the end.",
            "2022-04-05T10:00:00.00Z , 2022-04-12T11:00:00.00Z, 5 d. 1 h. have passed since the end.",
            "2022-04-05T10:00:00.00Z , 2022-04-05T10:00:00.00Z, You just finished the course."
    })
    public void checkCompletedStatusCourseIfWeHaveValidParameters(String endTime, String now, String expectedLeftTime) {
        Instant nowTime = Instant.parse(now);
        Instant finishTime = Instant.parse(endTime);
        Student student = getSpecificStudent(1);
        StatusCurriculum expectedStatus = StatusCurriculum.COMPLETED;

        Student studentWithStatus = statusCalculator.enrichStudent(nowTime, finishTime, student);
        Curriculum curriculum = studentWithStatus.getCurriculum();
        StatusCurriculum actualStatus = curriculum.getStatus();
        String actualLeftTime = curriculum.getLeftTime();

        assertEquals(expectedStatus, actualStatus);
        assertEquals(expectedLeftTime, actualLeftTime);
    }

    @ParameterizedTest
    @CsvSource({
            "2022-04-01T11:00:00.00Z , 2022-04-01T10:00:00.00Z, 1 h. are left unit the end.",
            "2022-04-11T11:00:00.00Z , 2022-04-01T10:00:00.00Z, 6 d. 1 h. are left unit the end.",
            "2022-04-11T18:00:00.00Z , 2022-04-01T18:00:00.00Z, 6 d. are left unit the end."
    })
    public void checkInProcessStatusCourseIfWeHaveValidParameters(String endTime, String now, String expectedLeftTime) {
        Instant nowTime = Instant.parse(now);
        Instant finishTime = Instant.parse(endTime);
        Student student = getSpecificStudent(1);
        StatusCurriculum expectedStatus = StatusCurriculum.IN_PROCESS;

        Student studentWithStatus = statusCalculator.enrichStudent(nowTime, finishTime, student);
        Curriculum curriculum = studentWithStatus.getCurriculum();
        StatusCurriculum actualStatus = curriculum.getStatus();
        String actualLeftTime = curriculum.getLeftTime();

        assertEquals(expectedStatus, actualStatus);
        assertEquals(expectedLeftTime, actualLeftTime);
    }

    @ParameterizedTest
    @CsvSource({
            "2022-04-01T11:00:00.00Z , 2022-04-01T10:00:00.00Z"
    })
    public void checkNotHaveCourseIfWeHaveListCoursesIsEmpty(String endTime, String now) {
        Instant nowTime = Instant.parse(now);
        Instant finishTime = Instant.parse(endTime);
        Student student = getSpecificStudent(0);
        StatusCurriculum expectedStatus = StatusCurriculum.NO_COURSE;

        Student studentWithStatus = statusCalculator.enrichStudent(nowTime, finishTime, student);
        StatusCurriculum actualStatus = studentWithStatus.getCurriculum().getStatus();

        assertEquals(expectedStatus, actualStatus);
    }

    @ParameterizedTest
    @CsvSource({
            "2022-04-01T10:00:00.00Z"
    })
    public void checkNotHaveCourseIfWeNotHaveEndTime(String now) {
        Instant nowTime = Instant.parse(now);
        Student student = getSpecificStudent(1);
        StatusCurriculum expectedStatus = StatusCurriculum.NO_COURSE;

        Student studentWithStatus = statusCalculator.enrichStudent(nowTime, null, student);
        StatusCurriculum actualStatus = studentWithStatus.getCurriculum().getStatus();

        assertEquals(expectedStatus, actualStatus);
    }

    private Student getSpecificStudent(int numberOfCourse) {
        Instant startTimeFr = Instant.parse("2022-04-01T10:00:00.00Z");
        Duration duration = Duration.of(9, ChronoUnit.HOURS);
        List<Course> courseList = new ArrayList<>();
        Course course = new Course("Java.", startTimeFr, null, duration);
        Curriculum curriculum = new Curriculum("Java Developer", courseList, null, null);

        for (int i = 0; i < numberOfCourse; i++) {
            courseList.add(course);
        }

        return new Student("Ivan", "Ivanov", curriculum);
    }
//    private List<Instant> getSpecificListEndTimes(int numberOfCourse, Instant endTime) {
//        List<Instant> endTimesList = new ArrayList<>();
//
//        for (int i = 0; i < numberOfCourse; i++) {
//            endTimesList.add(endTime);
//        }
//        return endTimesList;
//    }
}
