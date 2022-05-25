package com.griddynamics.gridu.javabasics.studentscourses.unit;

import com.griddynamics.gridu.javabasics.studentscourses.EnrichingStudent;
import com.griddynamics.gridu.javabasics.studentscourses.EnrichingStudentImpl;
import com.griddynamics.gridu.javabasics.studentscourses.model.student.Curriculum;
import com.griddynamics.gridu.javabasics.studentscourses.model.student.Program;
import com.griddynamics.gridu.javabasics.studentscourses.model.student.StatusCourse;
import com.griddynamics.gridu.javabasics.studentscourses.model.student.Student;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

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
        Student student = getSpecificStudent();
        StatusCourse expectedStatus = StatusCourse.COMPLETED;

        Student studentWithStatus = statusCalculator.enrichStudent(nowTime, finishTime, student);
        StatusCourse actualStatus = studentWithStatus.getProgram().getStatusCourse();
        String actualLeftTime = studentWithStatus.getProgram().getLeftTime();

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
        Student student = getSpecificStudent();
        StatusCourse expectedStatus = StatusCourse.IN_PROCESS;

        Student studentWithStatus = statusCalculator.enrichStudent(nowTime, finishTime, student);
        StatusCourse actualStatus = studentWithStatus.getProgram().getStatusCourse();
        String actualLeftTime = studentWithStatus.getProgram().getLeftTime();

        assertEquals(expectedStatus, actualStatus);
        assertEquals(expectedLeftTime, actualLeftTime);
    }

    @ParameterizedTest
    @CsvSource({
            "2022-04-01T11:00:00.00Z , 2022-04-01T10:00:00.00Z"
    })
    public void checkNotHaveCourseIfWeNotHaveCourse(String endTime, String now) {
        Instant nowTime = Instant.parse(now);
        Instant finishTime = Instant.parse(endTime);
        Program program = new Program(null, null, null);
        Student student = new Student("Ivan", "Ivanov", program);
        StatusCourse expectedStatus = StatusCourse.NO_COURSE;

        Student studentWithStatus = statusCalculator.enrichStudent(nowTime, finishTime, student);
        StatusCourse actualStatus = studentWithStatus.getProgram().getStatusCourse();

        assertEquals(expectedStatus, actualStatus);
    }

    private Student getSpecificStudent() {
        Instant startTimeFr = Instant.parse("2022-04-01T10:00:00.00Z");
        Duration duration = Duration.of(9, ChronoUnit.HOURS);
        Curriculum curriculum = new Curriculum("Java Developer", startTimeFr, null, duration);
        Program program = new Program(curriculum, null, null);
        return new Student("Ivan", "Ivanov", program);
    }
}
