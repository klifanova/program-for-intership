import internship.console.ConsoleData;
import internship.facade.FacadeParsableStudents;
import internship.model.CoursesSummaryInfo;
import internship.model.InputData;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

public class ProgramStudents {
    private static final String SEPARATE_STRINGS = "//////";

    public static void main(String[] args) throws IOException {
        FacadeParsableStudents facadeParsableStudents = new FacadeParsableStudents();
        ConsoleData readingDataFromConsole = new ConsoleData();
        InputData inputData = readingDataFromConsole.read();
        String nowTime = inputData.getTime();
        Instant time = Instant.parse(nowTime);
        String nameFile = inputData.getNameFile();
        String outputDataType = inputData.getOutputDataType();

        CoursesSummaryInfo coursesSummaryInfo = facadeParsableStudents.parseDataStudents(nameFile, time,
                outputDataType);
        printStudentsData("List students in process course :",
                coursesSummaryInfo.getInProgressCoursesStudentsList());
        printStudentsData("List students complete course :",
                coursesSummaryInfo.getCompleteCoursesStudentList());
        printStudentsData("List students not have course :",
                coursesSummaryInfo.getNotHaveCoursesStudentList());
    }

    private static void printStudentsData(String nameList, List<String> listDataStudents) {
        System.out.println(nameList + "\n" + listDataStudents + "\n" + SEPARATE_STRINGS + "\n");
    }
}
