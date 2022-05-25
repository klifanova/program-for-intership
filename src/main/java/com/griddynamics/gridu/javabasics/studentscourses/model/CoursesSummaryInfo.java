package com.griddynamics.gridu.javabasics.studentscourses.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CoursesSummaryInfo {

    List<String> inProgressCoursesStudentsList;
    List<String> completeCoursesStudentList;
    List<String> noCourseStudentList;
}
