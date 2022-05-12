package com.griddynamics.gridu.javabasics.studentscourses.unit;

import com.griddynamics.gridu.javabasics.studentscourses.JsonConverter;
import com.griddynamics.gridu.javabasics.studentscourses.model.student.Curriculum;
import com.griddynamics.gridu.javabasics.studentscourses.model.student.Program;
import com.griddynamics.gridu.javabasics.studentscourses.model.student.Student;
import com.griddynamics.gridu.javabasics.studentscourses.model.student.Training;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class JsonConverterTest {

    private JsonConverter jsonConverter = new JsonConverter();

    @Test
    public void checkValidStudentIfWeConvertFromValidJsonFile() {
        String STUDENT_DATA_FILE = "src/test/resources/students-data.json";
        Training expectedTraining = jsonConverter.converterJson(STUDENT_DATA_FILE, Training.class);
        Training actualTraining = getTraining();

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

    private static Training getTraining() {
        Instant startTimeFr = Instant.parse("2022-04-01T10:00:00.00Z");
        Duration duration = Duration.of(9, ChronoUnit.HOURS);
        Curriculum curriculum = new Curriculum("Java Developer", startTimeFr, null, duration);
        Program program = new Program(curriculum, null, null);
        Student actualStudent = new Student("Ivan", "Ivanov", program);
        int COUNT_ELEMENT = 8;
        List<Student> actualStudentList = addElementsInList(actualStudent, COUNT_ELEMENT);
        return new Training(actualStudentList);
    }
}
