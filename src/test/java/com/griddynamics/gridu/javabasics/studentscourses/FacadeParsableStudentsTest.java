package com.griddynamics.gridu.javabasics.studentscourses;

import com.griddynamics.gridu.javabasics.studentscourses.facade.FacadeParsableStudents;
import com.griddynamics.gridu.javabasics.studentscourses.model.CoursesSummaryInfo;
import com.griddynamics.gridu.javabasics.studentscourses.model.input.OutputDataType;
import com.griddynamics.gridu.javabasics.studentscourses.model.student.Curriculum;
import com.griddynamics.gridu.javabasics.studentscourses.model.student.Program;
import com.griddynamics.gridu.javabasics.studentscourses.model.student.Student;
import com.griddynamics.gridu.javabasics.studentscourses.model.student.Training;
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
import static org.mockito.Mockito.verify;
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
        String nameFile = "student-data-one.json";
        Instant time = Instant.parse("2022-04-05T10:00:00.00Z");
        Instant end = Instant.parse("2022-04-05T16:00:00.00Z");
        OutputDataType outputDataType = OutputDataType.FULL;
        Student student = getStudent(end);
        Training training = getTraining(student, 5);
        String dataStudent = "\nstudent name: Ivan Ivanov; working time: from 10:00 to 18:00; program name: Java;" +
                " program duration: PT9Hh.; start date: 2022-04-01T10:00:00Z; end date: 2022-04-05T16:00:00Z;" +
                " left time: 5 d. have passed since the end.";
        CoursesSummaryInfo expectedSummary = new CoursesSummaryInfo(getCoursesSummaryInfo(0, ""),
                getCoursesSummaryInfo(5, dataStudent), getCoursesSummaryInfo(0, ""));

        when(jsonConverter.converterJson(any(), any())).thenReturn(training);
        when(finishTimeCalculator.calculateFinishTime(any(), any())).thenReturn(end);
        when(enrichingStudent.enrichStudent(any(), any(), any())).thenReturn(student);

        CoursesSummaryInfo actualSummary = facade.getParsedStudentsData(nameFile, time, outputDataType);

        verify(jsonConverter).converterJson(any(), any());
        assertEquals(expectedSummary, actualSummary);
    }

    @Test
    public void checkShortParsableStudentIfWeHaveValidDataAndDate() {
        String nameFile = "student-data-one.json";
        Instant time = Instant.parse("2022-04-05T10:00:00.00Z");
        Instant end = Instant.parse("2022-04-05T16:00:00.00Z");
        OutputDataType typeOutputData = OutputDataType.SHORT;
        Student student = getStudent(end);
        Training training = getTraining(student, 5);
        String dataStudent = "\nIvan Ivanov ( Java ) - Training completed. 5 d. have passed since the end.";
        CoursesSummaryInfo expectedSummary = new CoursesSummaryInfo(getCoursesSummaryInfo(0, ""),
                getCoursesSummaryInfo(5, dataStudent), getCoursesSummaryInfo(0, ""));

        when(jsonConverter.converterJson(any(), any())).thenReturn(training);
        when(finishTimeCalculator.calculateFinishTime(any(), any())).thenReturn(end);
        when(enrichingStudent.enrichStudent(any(), any(), any())).thenReturn(student);

        CoursesSummaryInfo actualSummary = facade.getParsedStudentsData(nameFile, time, typeOutputData);

        verify(jsonConverter).converterJson(any(), any());
        assertEquals(expectedSummary, actualSummary);
    }

    private static Training getTraining(Student student, int countElement) {
        List<Student> studentList = new ArrayList<>();
        for (int i = 0; i < countElement; i++) {
            studentList.add(student);
        }
        return new Training(studentList);
    }

    private static List<String> getCoursesSummaryInfo(int countElement, String dataStudent) {
        List<String> summary = new ArrayList<>();
        for (int i = 0; i < countElement; i++) {
            summary.add(dataStudent);
        }
        return summary;
    }

    private static Student getStudent(Instant end) {
        Instant startTimeFr = Instant.parse("2022-04-01T10:00:00.00Z");
        Duration duration = Duration.of(9, ChronoUnit.HOURS);
        String statusCourse = "Training completed.";
        String leftTime = "5 d. have passed since the end.";
        Curriculum curriculum = new Curriculum("Java", startTimeFr, end, duration);
        Program program = new Program(curriculum, statusCourse, leftTime);
        return new Student("Ivan", "Ivanov", program);
    }
}
