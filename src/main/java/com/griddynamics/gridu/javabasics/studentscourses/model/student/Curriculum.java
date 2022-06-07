package com.griddynamics.gridu.javabasics.studentscourses.model.student;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Curriculum {

    String name;
    List<Course> courseList;
    StatusCurriculum status;
    String leftTime;
}
