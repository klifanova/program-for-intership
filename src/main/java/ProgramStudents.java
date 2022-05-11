import internship.console.ReadingDataFromConsole;
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
        ReadingDataFromConsole readingDataFromConsole = new ReadingDataFromConsole();
        InputData inputData = readingDataFromConsole.readDataFromConsole();
        String nowTime = inputData.getTime();
        Instant time = Instant.parse(nowTime);
        String nameFile = inputData.getNameFile();
        String typeOutputData = inputData.getTypeOutputData();

        CoursesSummaryInfo coursesSummaryInfo = facadeParsableStudents.parseDataStudents(nameFile, time,
                typeOutputData);
        printDataStudent("List students in process course :",
                coursesSummaryInfo.getCollectionForPrintInProcessCourse());
        printDataStudent("List students complete course :",
                coursesSummaryInfo.getCollectionForPrintCompleteCourse());
        printDataStudent("List students not have course :",
                coursesSummaryInfo.getCollectionForPrintINotHaveCourse());
    }

    private static void printDataStudent(String nameList, List<String> listDataStudents) {
        System.out.println(nameList + "\n" + listDataStudents + "\n" + SEPARATE_STRINGS + "\n");
    }
}
