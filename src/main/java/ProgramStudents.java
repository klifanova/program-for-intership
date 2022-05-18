import com.griddynamics.gridu.javabasics.studentscourses.exception.InvalidOutputDataTypeException;
import com.griddynamics.gridu.javabasics.studentscourses.facade.FacadeParsableStudents;
import com.griddynamics.gridu.javabasics.studentscourses.model.CoursesSummaryInfo;
import com.griddynamics.gridu.javabasics.studentscourses.model.input.RetortDataType;
import com.griddynamics.gridu.javabasics.studentscourses.model.input.InputData;

import java.time.Instant;
import java.util.List;

/**
 * Running the program and outputting data to the console.
 */

public class ProgramStudents {
    private static final String SEPARATE_STRINGS = "//////";

    /**
     * This method prints lists of students to the console.
     *
     * @param args - the input data includes fileName, date and reportDataType
     */

    public static void main(String[] args) {
        InputData inputData = new InputData();
        inputData.setFileName(args[0]);
        inputData.setTime(args[1]);
        Instant instant = Instant.parse(inputData.getTime());
        String reportData = args[2];
        reportData = reportData.toUpperCase();
        if (!(reportData.equals(RetortDataType.FULL.name()) || reportData.equals(RetortDataType.SHORT.name()))) {
            throw new InvalidOutputDataTypeException(String.format("It's not correct %s reportData."
                    , reportData));
        }
        RetortDataType retortDataType = RetortDataType.valueOf(reportData);
        inputData.setRetortDataType(retortDataType);
        FacadeParsableStudents facadeParsableStudents = new FacadeParsableStudents();

        CoursesSummaryInfo coursesSummaryInfo = facadeParsableStudents.getParsedStudentsData(inputData.getFileName(),
                instant, inputData.getRetortDataType());
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
