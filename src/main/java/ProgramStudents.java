import com.griddynamics.gridu.javabasics.studentscourses.exception.InvalidOutputDataTypeException;
import com.griddynamics.gridu.javabasics.studentscourses.facade.FacadeParsableStudents;
import com.griddynamics.gridu.javabasics.studentscourses.model.CoursesSummaryInfo;
import com.griddynamics.gridu.javabasics.studentscourses.model.input.OutputDataType;
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
     * @param args
     */

    public static void main(String[] args) {
        InputData inputData = new InputData();
        inputData.setFileName(args[0]);
        inputData.setTime(args[1]);
        Instant instant = Instant.parse(inputData.getTime());
        String outputDataType = args[2];
        outputDataType = outputDataType.toUpperCase();
        if (!(outputDataType.equals(OutputDataType.FULL.name()) || outputDataType.equals(OutputDataType.SHORT.name()))) {
            throw new InvalidOutputDataTypeException(String.format("It's not correct %s outputDataType."
                    , outputDataType));
        }
        OutputDataType outputData = OutputDataType.valueOf(outputDataType);
        inputData.setOutputDataType(outputData);
        FacadeParsableStudents facadeParsableStudents = new FacadeParsableStudents();

        CoursesSummaryInfo coursesSummaryInfo = facadeParsableStudents.getParsedStudentsData(inputData.getFileName(),
                instant, inputData.getOutputDataType());
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
