package com.griddynamics.gridu.javabasics.studentscourses.unit;

import com.griddynamics.gridu.javabasics.studentscourses.model.student.Course;
import com.griddynamics.gridu.javabasics.studentscourses.model.student.Curriculum;
import com.griddynamics.gridu.javabasics.studentscourses.model.student.Student;
import com.griddynamics.gridu.javabasics.studentscourses.model.student.Training;
import com.griddynamics.gridu.javabasics.studentscourses.service.JsonConverter;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JsonConverterTest {

    private final JsonConverter jsonConverter = new JsonConverter();

    @Test
    public void checkValidTrainingFormatIfWeConvertFromValidJsonFile() {
        String studentsDataFile = "src/test/resources/students-data.json";
        Curriculum curriculum = getSpecificCurriculum(2);
        Training expectedTraining = getSpecificTraining(curriculum);

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
        Curriculum curriculum = new Curriculum(null, null, null, null);
        Training expectedTraining = getSpecificTraining(curriculum);

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

    private Training getSpecificTraining(Curriculum curriculum) {
        Student actualStudent = new Student("Ivan", "Ivanov", curriculum);
        int COUNT_ELEMENT = 5;
        List<Student> actualStudentList = addElementsInList(actualStudent, COUNT_ELEMENT);
        return new Training(actualStudentList);
    }

    private Curriculum getSpecificCurriculum(int numberOfCourse) {
        Instant startTimeFr = Instant.parse("2022-04-01T10:00:00.00Z");
        Duration duration = Duration.of(9, ChronoUnit.HOURS);
        List<Course> courseList = new ArrayList<>();
        Course course = new Course("Java.", startTimeFr, null, duration);
        Curriculum curriculum = new Curriculum("Java Developer", courseList, null, null);

        for (int i = 0; i < numberOfCourse; i++) {
            courseList.add(course);
        }

        return curriculum;
    }
}
