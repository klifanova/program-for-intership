package internship.unit;

import internship.JsonConverter;
import internship.model.student.Curriculum;
import internship.model.student.Program;
import internship.model.student.Student;
import internship.model.student.Training;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class JsonConverterTest {

    private JsonConverter jsonConverter;
    private Student actualStudent;
    private List<Student> actualStudentList;
    private Training actualTraining;
    private static String STUDENT_DATA_FILE = "src/test/resources/students-data.json";
    private static int COUNT_ELEMENT = 8;

    @BeforeEach
    public void init() {
        jsonConverter = new JsonConverter();
        Instant startTimeFr = Instant.parse("2022-04-01T10:00:00.00Z");
        Duration duration = Duration.of(9, ChronoUnit.HOURS);
        Curriculum curriculum = new Curriculum("Java Developer", startTimeFr, null, duration);
        Program program = new Program(curriculum, null, null);
        actualStudent = new Student("Ivan", "Ivanov", program);
        actualStudentList = addElementsInList(actualStudent, COUNT_ELEMENT);
        actualTraining = new Training(actualStudentList);
    }

    @Test
    public void checkValidStudentIfWeConvertFromValidJsonFile() {
        Training expectedTraining = jsonConverter.converterJson(STUDENT_DATA_FILE, Training.class);

        assertNotNull(expectedTraining);
        assertEquals(expectedTraining, actualTraining);
    }

    private static List<Student> addElementsInList(Student student, int countElement) {
        List<Student> studentList = new ArrayList<>();
        for (int i = 0; i < countElement; i++) {
            studentList.add(student);
        }
        return studentList;
    }
}
