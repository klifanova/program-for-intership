package internship.unit;

import internship.EnrichingStudentImpl;
import internship.model.student.Curriculum;
import internship.model.student.Program;
import internship.model.student.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StatusCalculatorTest {

    private EnrichingStudentImpl statusCalculator;
    private Student student;
    private static final String STATUS_COMPLETED = "Training completed.";
    private static final String STATUS_IN_PROCESS = "Training is not finished.";

    @BeforeEach
    public void init() {
        statusCalculator = new EnrichingStudentImpl();
        Instant startTimeFr = Instant.parse("2022-04-01T10:00:00.00Z");
        Duration duration = Duration.of(9, ChronoUnit.HOURS);
        Curriculum curriculum = new Curriculum("Java", startTimeFr, null, duration);
        Program program = new Program(curriculum, null, null);
        student = new Student("Ivan", "Ivanov", program);
    }

    @ParameterizedTest
    @CsvSource({
            "2022-04-01T10:00:00.00Z , 2022-04-01T11:00:00.00Z, 1 h. have passed since the end.",
            "2022-04-04T10:00:00.00Z , 2022-04-08T18:00:00.00Z, 5 d. have passed since the end.",
            "2022-04-05T10:00:00.00Z , 2022-04-12T11:00:00.00Z, 5 d. 1 h. have passed since the end.",
            "2022-04-05T10:00:00.00Z , 2022-04-05T10:00:00.00Z, You just finished the course."
    })
    public void checkCompletedStatusCourseIfWeHaveValidParameters(String endTime, String now, String actualLeftTime) {
        Instant nowTime = Instant.parse(now);
        Instant finishTime = Instant.parse(endTime);

        Student studentWithStatus = statusCalculator.enrichStudent(nowTime, finishTime, student);
        String expectedStatus = studentWithStatus.getProgram().getStatusCourse();
        String expectedLeftTime = studentWithStatus.getProgram().getLeftTime();

        assertEquals(expectedStatus, STATUS_COMPLETED);
        assertEquals(expectedLeftTime, actualLeftTime);
    }

    @ParameterizedTest
    @CsvSource({
            "2022-04-01T11:00:00.00Z , 2022-04-01T10:00:00.00Z, 1 h. are left unit the end.",
            "2022-04-11T11:00:00.00Z , 2022-04-01T10:00:00.00Z, 6 d. 1 h. are left unit the end.",
            "2022-04-11T18:00:00.00Z , 2022-04-01T18:00:00.00Z, 6 d. are left unit the end."
    })
    public void checkInProcessStatusCourseIfWeHaveValidParameters(String endTime, String now, String actualLeftTime) {
        Instant nowTime = Instant.parse(now);
        Instant finishTime = Instant.parse(endTime);

        Student studentWithStatus = statusCalculator.enrichStudent(nowTime, finishTime, student);
        String expectedStatus = studentWithStatus.getProgram().getStatusCourse();
        String expectedLeftTime = studentWithStatus.getProgram().getLeftTime();

        assertEquals(expectedStatus, STATUS_IN_PROCESS);
        assertEquals(expectedLeftTime, actualLeftTime);
    }
}
