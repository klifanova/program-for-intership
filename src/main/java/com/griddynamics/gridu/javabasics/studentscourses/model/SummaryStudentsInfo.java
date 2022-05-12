package com.griddynamics.gridu.javabasics.studentscourses.model;

import com.griddynamics.gridu.javabasics.studentscourses.model.student.Student;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SummaryStudentsInfo {

    List<Student> inProgressCoursesStudentsList;
    List<Student> completeCoursesStudentList;
    List<Student> noCourseStudentList;
}
