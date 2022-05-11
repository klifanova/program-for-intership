package internship.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CoursesSummaryInfo {

    List<String> collectionForPrintInProcessCourse;
    List<String> collectionForPrintCompleteCourse;
    List<String> collectionForPrintINotHaveCourse;

}
