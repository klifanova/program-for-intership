import com.griddynamics.gridu.javabasics.studentscourses.console.ConsoleData;
import com.griddynamics.gridu.javabasics.studentscourses.facade.FacadeParsableStudents;
import com.griddynamics.gridu.javabasics.studentscourses.model.CoursesSummaryInfo;
import com.griddynamics.gridu.javabasics.studentscourses.model.InputData;
import com.griddynamics.gridu.javabasics.studentscourses.model.OutputDataType;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

//Running the program and outputting data to the console.

public class ProgramStudents {
    private static final String SEPARATE_STRINGS = "//////";

    public static void main(String[] args) throws IOException {
        FacadeParsableStudents facadeParsableStudents = new FacadeParsableStudents();
        ConsoleData readingDataFromConsole = new ConsoleData();
        InputData inputData = readingDataFromConsole.read();
        String nowTime = inputData.getTime();
        Instant time = Instant.parse(nowTime);
        String fileName = inputData.getNameFile();
        OutputDataType outputDataType = inputData.getOutputDataType();

        CoursesSummaryInfo coursesSummaryInfo = facadeParsableStudents.getParsedStudentsData(fileName, time,
                outputDataType);
        printStudentsData("List students in process course :",
                coursesSummaryInfo.getInProgressCoursesStudentsList());
        printStudentsData("List students complete course :",
                coursesSummaryInfo.getCompleteCoursesStudentList());
        printStudentsData("List students not have course :",
                coursesSummaryInfo.getNoCourseStudentList());
    }

    private static void printStudentsData(String nameList, List<String> listDataStudents) {
        System.out.println(nameList + "\n" + listDataStudents + "\n" + SEPARATE_STRINGS + "\n");
    }
}
