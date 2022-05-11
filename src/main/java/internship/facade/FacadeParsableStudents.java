package internship.facade;

import internship.*;
import internship.exception.InvalidTypeOutputDataException;
import internship.model.CoursesSummaryInfo;
import internship.model.student.Curriculum;
import internship.model.student.Program;
import internship.model.student.Student;
import internship.model.student.Training;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FacadeParsableStudents {

    private static final String STATUS_IN_PROCESS = "Training is not finished.";
    private static final String STATUS_COMPLETED = "Training completed.";

    private FinishTimeCalculator finishTimeCalculator = new FinishTimeCalculatorImpl();
    private EnrichingStudent enrichingStudent = new EnrichingStudentImpl();
    private JsonConverter jsonConverter = new JsonConverter();

    public CoursesSummaryInfo parseDataStudents(String nameFile, Instant nowDate, String typeOutputData) {
        if (!Objects.equals(typeOutputData, "full") && !Objects.equals(typeOutputData, "short")) {
            throw new InvalidTypeOutputDataException(String.format("It's not valid %s type output data.",
                    typeOutputData));
        }

        List<Student> collectionStudentsInProcessCourse = new ArrayList<>();
        List<Student> collectionStudentsCompleteCourse = new ArrayList<>();
        List<Student> collectionStudentsNotHaveCourse = new ArrayList<>();
        Training training = jsonConverter.converterJson(nameFile, Training.class);
        List<Student> dataStudentFromFile = training.getStudentList();
        Instant endDate = null;

        for (Student item : dataStudentFromFile) {
            Curriculum curriculum = item.getProgram().getCurriculum();
            Instant startTime = curriculum.getStartTimeCourse();
            Duration duration = curriculum.getDuration();
            endDate = finishTimeCalculator.calculateFinishTime(startTime, duration);
            item.getProgram().getCurriculum().setEndTimeCourse(endDate);
            item = enrichingStudent.enrichStudent(nowDate, endDate, item);
            String status = item.getProgram().getStatusCourse();

            if (status.equals(STATUS_IN_PROCESS)) {
                collectionStudentsInProcessCourse.add(item);
            } else if (status.equals(STATUS_COMPLETED)) {
                collectionStudentsCompleteCourse.add(item);
            } else {
                collectionStudentsNotHaveCourse.add(item);
            }
        }

        List<String> collectionForPrintDataStudentsInProcessCourse = new ArrayList<>();
        List<String> collectionForPrintDataStudentsCompleteCourse = new ArrayList<>();
        List<String> collectionForPrintDataStudentsNotHaveCourse = new ArrayList<>();

        if (typeOutputData.equals("short")) {
            printShortDataFromCollectionStatus(collectionStudentsInProcessCourse,
                    collectionForPrintDataStudentsInProcessCourse);
            printShortDataFromCollectionStatus(collectionStudentsCompleteCourse,
                    collectionForPrintDataStudentsCompleteCourse);
            printShortDataFromCollectionStatus(collectionStudentsNotHaveCourse,
                    collectionForPrintDataStudentsNotHaveCourse);
        }
        if (typeOutputData.equals("full")) {
            printFullDataFromCollectionStatus(collectionStudentsInProcessCourse,
                    collectionForPrintDataStudentsInProcessCourse);
            printFullDataFromCollectionStatus(collectionStudentsCompleteCourse,
                    collectionForPrintDataStudentsCompleteCourse);
            printFullDataFromCollectionStatus(collectionStudentsNotHaveCourse,
                    collectionForPrintDataStudentsNotHaveCourse);
        }
        return new CoursesSummaryInfo(collectionForPrintDataStudentsInProcessCourse,
                collectionForPrintDataStudentsCompleteCourse, collectionForPrintDataStudentsNotHaveCourse);
    }

    private List<String> printShortDataFromCollectionStatus(List<Student> students, List<String> studentsList) {
        for (Student item : students) {
            Program program = item.getProgram();
            studentsList.add("\n" + item.getFullName() + " ( " + program.getCurriculum().getName() + " ) - "
                    + program.getStatusCourse() + " " + program.getLeftTime());
        }
        return studentsList;
    }

    private List<String> printFullDataFromCollectionStatus(List<Student> students, List<String> studentsList) {
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
}
