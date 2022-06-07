package com.griddynamics.gridu.javabasics.studentscourses;

import com.griddynamics.gridu.javabasics.studentscourses.facade.FacadeParsableStudents;
import com.griddynamics.gridu.javabasics.studentscourses.model.CoursesSummaryInfo;
import com.griddynamics.gridu.javabasics.studentscourses.model.inputdata.ReportDataType;
import com.griddynamics.gridu.javabasics.studentscourses.model.student.*;
import com.griddynamics.gridu.javabasics.studentscourses.service.EnrichingStudent;
import com.griddynamics.gridu.javabasics.studentscourses.service.FinishTimeCalculator;
import com.griddynamics.gridu.javabasics.studentscourses.service.JsonConverter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class FacadeParsableStudentsTest {

    @Mock
    private FinishTimeCalculator finishTimeCalculator;
    @Mock
    private EnrichingStudent enrichingStudent;
    @Mock
    private JsonConverter jsonConverter;

    @InjectMocks
    private FacadeParsableStudents facade = new FacadeParsableStudents();

    @Test
    public void checkFullParsableStudentIfWeHaveValidDataAndDate() {
        String nameFile = "src/test/resources/students-data.json";
        Instant time = Instant.parse("2022-04-05T10:00:00.00Z");
        Instant end = Instant.parse("2022-04-05T16:00:00.00Z");
        ReportDataType reportDataType = ReportDataType.FULL;
        StatusCurriculum statusCurriculum = StatusCurriculum.COMPLETED;
        Student student = getSpecificStudent(2, statusCurriculum);
        Training training = getTraining(student, 5);
        String dataStudent = "STUDENT: Ivan Ivanov. Working time: from 10:00 to 18:00. CURRICULUM: Java Developer;" +
                " COURSE: Java. Program duration: 9h. START_DATE: 2022-04-01T10:00:00Z; END_DATE: 2022-04-05T16:00:00Z." +
                " COURSE: Java. Program duration: 9h. START_DATE: 2022-04-01T10:00:00Z; END_DATE: 2022-04-05T16:00:00Z.";
        CoursesSummaryInfo expectedSummary = new CoursesSummaryInfo(getCoursesSummaryInfo(0, ""),
                getCoursesSummaryInfo(5, dataStudent), getCoursesSummaryInfo(0, ""));

        when(jsonConverter.converterJson(any(), any())).thenReturn(training);
        when(finishTimeCalculator.calculateFinishTime(any(), any())).thenReturn(end);
        when(enrichingStudent.enrichStudent(any(), any(), any())).thenReturn(student);

        CoursesSummaryInfo actualSummary = facade.getParsedStudentsData(nameFile, time, reportDataType);

        assertEquals(expectedSummary, actualSummary);
    }

    @Test
    public void checkShortParsableStudentIfWeHaveValidDataAndDate() {
        String nameFile = "src/test/resources/students-data.json";
        Instant time = Instant.parse("2022-04-05T10:00:00.00Z");
        Instant end = Instant.parse("2022-04-05T16:00:00.00Z");
        ReportDataType reportDataType = ReportDataType.SHORT;
        StatusCurriculum statusCurriculum = StatusCurriculum.COMPLETED;
        Student student = getSpecificStudent(2, statusCurriculum);
        Training training = getTraining(student, 5);
        String dataStudent = "Ivan Ivanov ( Java Developer ) - Training completed. 5 h. have passed since the end.";
        CoursesSummaryInfo expectedSummary = new CoursesSummaryInfo(getCoursesSummaryInfo(0, ""),
                getCoursesSummaryInfo(5, dataStudent), getCoursesSummaryInfo(0, ""));

        when(jsonConverter.converterJson(any(), any())).thenReturn(training);
        when(finishTimeCalculator.calculateFinishTime(any(), any())).thenReturn(end);
        when(enrichingStudent.enrichStudent(any(), any(), any())).thenReturn(student);

        CoursesSummaryInfo actualSummary = facade.getParsedStudentsData(nameFile, time, reportDataType);

        assertEquals(expectedSummary, actualSummary);
    }

    @Test
    public void checkFullParsableStudentIfWeHaveValidDataAndDateWithOneCourse() {
        String nameFile = "src/test/resources/students-data-one-course.json";
        Instant time = Instant.parse("2022-04-05T10:00:00.00Z");
        Instant end = Instant.parse("2022-04-05T16:00:00.00Z");
        ReportDataType reportDataType = ReportDataType.FULL;
        StatusCurriculum statusCurriculum = StatusCurriculum.COMPLETED;
        Student student = getSpecificStudent(1, statusCurriculum);
        Training training = getTraining(student, 5);
        String dataStudent = "STUDENT: Ivan Ivanov. Working time: from 10:00 to 18:00. CURRICULUM: Java Developer;" +
                " COURSE: Java. Program duration: 9h. START_DATE: 2022-04-01T10:00:00Z; END_DATE: 2022-04-05T16:00:00Z.";
        CoursesSummaryInfo expectedSummary = new CoursesSummaryInfo(getCoursesSummaryInfo(0, ""),
                getCoursesSummaryInfo(5, dataStudent), getCoursesSummaryInfo(0, ""));

        when(jsonConverter.converterJson(any(), any())).thenReturn(training);
        when(finishTimeCalculator.calculateFinishTime(any(), any())).thenReturn(end);
        when(enrichingStudent.enrichStudent(any(), any(), any())).thenReturn(student);

        CoursesSummaryInfo actualSummary = facade.getParsedStudentsData(nameFile, time, reportDataType);

        assertEquals(expectedSummary, actualSummary);
    }

    @Test
    public void checkFullParsableStudentIfWeHaveValidDataAndDateWithoutCourses() {
        String nameFile = "src/test/resources/students-data-not-have-course.json";
        Instant time = Instant.parse("2022-04-05T10:00:00.00Z");
        ReportDataType reportDataType = ReportDataType.FULL;
        StatusCurriculum statusCurriculum = StatusCurriculum.NO_COURSE;
        Student student = getSpecificStudent(0, statusCurriculum);
        Training training = getTraining(student, 5);
        String dataStudent = "Ivan Ivanov. Training is not have in 2022-04-05T10:00:00Z";
        CoursesSummaryInfo expectedSummary = new CoursesSummaryInfo(getCoursesSummaryInfo(0, ""),
                getCoursesSummaryInfo(0, ""), getCoursesSummaryInfo(5, dataStudent));

        when(jsonConverter.converterJson(any(), any())).thenReturn(training);
        when(enrichingStudent.enrichStudent(any(), any(), any())).thenReturn(student);

        CoursesSummaryInfo actualSummary = facade.getParsedStudentsData(nameFile, time, reportDataType);

        assertEquals(expectedSummary, actualSummary);
    }

    private Training getTraining(Student student, int countStudents) {
        List<Student> studentList = new ArrayList<>();
        for (int i = 0; i < countStudents; i++) {
            studentList.add(student);
        }
        return new Training(studentList);
    }

    private List<String> getCoursesSummaryInfo(int countElement, String dataStudent) {
        List<String> summary = new ArrayList<>();
        for (int i = 0; i < countElement; i++) {
            summary.add(dataStudent);
        }
        return summary;
    }

    private Student getSpecificStudent(int numberOfCourse, StatusCurriculum statusCurriculum) {
        Instant startTimeFr = Instant.parse("2022-04-01T10:00:00.00Z");
        Duration duration = Duration.of(9, ChronoUnit.HOURS);
        List<Course> courseList = new ArrayList<>();
        Course course = new Course("Java.", startTimeFr, null, duration);
        Curriculum curriculum = new Curriculum("Java Developer", courseList, statusCurriculum,
                "5 h. have passed since the end.");

        for (int i = 0; i < numberOfCourse; i++) {
            courseList.add(course);
        }

        return new Student("Ivan", "Ivanov", curriculum);
    }
}
