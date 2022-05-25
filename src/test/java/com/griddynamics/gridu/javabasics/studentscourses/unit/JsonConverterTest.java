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
import static org.junit.jupiter.api.Assertions.assertThrows;

public class JsonConverterTest {

    private JsonConverter jsonConverter = new JsonConverter();

    @Test
    public void checkValidTrainingFormatIfWeConvertFromValidJsonFile() {
        String studentsDataFile = "src/test/resources/students-data.json";
        Curriculum curriculum = getSpecificCurriculum();
        Program program = getSpecificProgram(curriculum);
        Training expectedTraining = getSpecificTraining(program);

        Training actualTraining = jsonConverter.converterJson(studentsDataFile, Training.class);

        assertNotNull(expectedTraining);
        assertEquals(expectedTraining, actualTraining);
    }

    @Test
    public void checkInvalidFormatIfWeConvertFromValidJsonFile() {
        String studentsDataFile = "src/test/resources/students-data.json";

        assertThrows(IllegalArgumentException.class, () -> {
            jsonConverter.converterJson(studentsDataFile, Curriculum.class);
        });
    }

    @Test
    public void checkStudentNotHaveCourseIfWeConvertFromValidJsonFile() {
        String studentsDataFile = "src/test/resources/students-not-have-course.json";
        Program program = new Program(null, null, null);
        Training expectedTraining = getSpecificTraining(program);

        Training actualTraining = jsonConverter.converterJson(studentsDataFile, Training.class);

        assertNotNull(expectedTraining);
        assertEquals(expectedTraining, actualTraining);
    }

    private List<Student> addElementsInList(Student student, int countElement) {
        List<Student> studentList = new ArrayList<>();
        for (int i = 0; i < countElement; i++) {
            studentList.add(student);
        }
        return studentList;
    }

    private Program getSpecificProgram(Curriculum curriculum) {
        return new Program(curriculum, null, null);
    }

    private Training getSpecificTraining(Program program) {
        Student actualStudent = new Student("Ivan", "Ivanov", program);
        int COUNT_ELEMENT = 5;
        List<Student> actualStudentList = addElementsInList(actualStudent, COUNT_ELEMENT);
        return new Training(actualStudentList);
    }

    private Curriculum getSpecificCurriculum() {
        Instant startTimeFr = Instant.parse("2022-04-01T10:00:00.00Z");
        Duration duration = Duration.of(9, ChronoUnit.HOURS);
        return new Curriculum("Java Developer", startTimeFr, null, duration);
    }
}
